/*
 * Copyright (C) 2020 Sonicle S.r.l.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY SONICLE, SONICLE DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle[at]sonicle[dot]com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * Sonicle logo and Sonicle copyright notice. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Copyright (C) 2020 Sonicle S.r.l.".
 */
package com.sonicle.commons.cache;

import java.util.concurrent.locks.StampedLock;

/**
 * Abstract class suitable for managing cache structures that are populated in bulk-mode.
 * Data structure management is completely delegated to implementing classes
 * throught hook-points (see below).
 * @author malbinola
 */
public abstract class AbstractBulkCache {
	private static final long serialVersionUID = 1L;
	protected final StampedLock lock = new StampedLock();
	//volatile: getBuildsCount()/isInitialized() read it without holding the lock
	private volatile int buildsCount = 0;
	
	/**
	 * Hook-point: load you data. Implement you custom logic here!
	 */
	protected abstract void internalBuildCache();
	
	/**
	 * Hook-point: clear you data. Implement you custom logic here!
	 */
	protected abstract void internalCleanupCache();
	
	/**
	 * @deprecated use getBuildCount instead.
	 */
	@Deprecated
	public int getInitCount() {
		return buildsCount;
	}
	
	/**
	 * Forcibly issue an initialization: this will call {@link #internalBuildCache() internalBuildCache}.
	 */
	public final void init() {
		long stamp = lock.writeLock();
		try {
			internalBuild();
		} finally {
			lock.unlockWrite(stamp);
		}
	}
	
	/**
	 * Clears data: this will call {@link #internalCleanupCache() internalCleanupCache}.
	 */
	public final void clear() {
		long stamp = lock.writeLock();
		try {
			internalClear();
		} finally {
			lock.unlockWrite(stamp);
		}
	}
	
	/**
	 * Returns the number of rebuild counts.
	 * @return 
	 */
	public final int getBuildsCount() {
		return buildsCount;
	}
	
	/**
	 * Returns if this cache needs an initialization, typically when builds-count is less than 1.
	 * @return 
	 */
	public final boolean isInitialized() {
		return buildsCount > 0;
	}
	
	/**
	 * Hook-point: whether {@link #internalBuild()} must clear current data (via
	 * {@link #internalCleanupCache()}) before re-building. Defaults to false to
	 * preserve legacy behavior for existing subclasses — several of them (e.g.
	 * swap-style builds, error-swallowing builds) deliberately keep the previous
	 * data when a rebuild fails. Subclasses whose {@link #internalBuildCache()}
	 * populates its structures assuming they are EMPTY (additive builds) must
	 * override this returning true, or a re-issued {@link #init()} accumulates
	 * duplicates.
	 * @return
	 */
	protected boolean cleanupBeforeBuild() {
		return false;
	}

	/**
	 * Internal method that performs cache building.
	 */
	protected void internalBuild() {
		if (cleanupBeforeBuild()) internalCleanupCache();
		internalBuildCache();
		buildsCount++;
	}

	/**
	 * Internal method that performs cache clearing.
	 */
	protected void internalClear() {
		internalCleanupCache();
		//a cleared cache must count as UNinitialized: without this reset a
		//cleared instance kept alive by a straggler reference would serve empty
		//data forever instead of lazily rebuilding on next access
		buildsCount = 0;
	}
	
	/**
	 * Internal method used to determine if a rebuild is necessary.
	 * Can be overrided by implementing classes to support their own logic.
	 * @return 
	 */
	protected boolean internalShouldBuild() {
		return buildsCount <= 0;
	}
	
	/**
	 * Method that needs to be called by implementing classes before any cache access operation.
	 * This method takes care of checking if the cache is already being built, 
	 * otherwise the initial lock (read) will be upgraded to write and the 
	 * initialization will be performed.
	 * As alse stated by method name, do NOT call this method in a synchronized/mutualexclusion section; it can cause deadlocks.
	 */
	protected void internalCheckBeforeGetDoNotLockThis() {
		long stamp = lock.readLock();
		try {
			if (internalShouldBuild()) {
				stamp = upgradeToWriteLock(stamp);
				//re-check: upgradeToWriteLock may release the read lock before
				//re-acquiring write, letting another thread build meanwhile —
				//without this, two concurrent first readers both built
				if (internalShouldBuild()) internalBuild();
			}
		} finally {
			lock.unlock(stamp);
		}
	}
	
	protected final long readLock() {
		return lock.readLock();
	}
	
	protected final void unlockRead(long stamp) {
		lock.unlockRead(stamp);
	}
	
	protected final long writeLock() {
		return lock.writeLock();
	}
	
	protected final void unlockWrite(long stamp) {
		lock.unlockWrite(stamp);
	}
	
	protected long upgradeToWriteLock(long rstamp) {
		long wstamp = lock.tryConvertToWriteLock(rstamp);
		if (wstamp == 0L) {
			lock.unlockRead(rstamp);
			return lock.writeLock();
		} else {
			return wstamp;
		}
	}
}
