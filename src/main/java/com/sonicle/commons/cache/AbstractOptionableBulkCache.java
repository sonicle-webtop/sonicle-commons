/*
 * Copyright (C) 2022 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2022 Sonicle S.r.l.".
 */
package com.sonicle.commons.cache;

import java.util.concurrent.locks.StampedLock;

/**
 * Abstract class suitable for managing cache structures that are populated in bulk-mode.
 * Data structure management is completely delegated to implementing classes
 * throught hook-points (see below).
 * Note that this implementation does NOT support initialization upon first use.
 * @author malbinola
 * @param <O>
 */
public abstract class AbstractOptionableBulkCache<O> {
	private static final long serialVersionUID = 1L;
	protected final StampedLock lock = new StampedLock();
	private int buildsCount = 0;
	
	/**
	 * Hook-point: load you data.Implement you custom logic here!
	 * @param options
	 */
	protected abstract void internalBuildCache(final O options);
	
	/**
	 * Hook-point: clear you data.Implement you custom logic here!
	 * @param options
	 */
	protected abstract void internalCleanupCache(final O options);
	
	/**
	 * Forcibly issue an initialization (with no options): this will call {@link #internalBuildCache() internalBuildCache}.
	 */
	public void init() {
		init(null);
	}
	
	/**
	 * Forcibly issue an initialization (with options): this will call {@link #internalBuildCache() internalBuildCache}.
	 * @param options 
	 */
	public final void init(final O options) {
		long stamp = lock.writeLock();
		try {
			internalBuild(options);
		} finally {
			lock.unlockWrite(stamp);
		}
	}
	
	/**
	 * Clears data (with no options): this will call {@link #internalCleanupCache() internalCleanupCache}.
	 */
	public final void clear() {
		clear(null);
	}
	
	/**
	 * Clears data (with options): this will call {@link #internalCleanupCache() internalCleanupCache}.
	 * @param options
	 */
	public final void clear(final O options) {
		long stamp = lock.writeLock();
		try {
			internalClear(options);
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
	 * Internal method that performs cache building.
	 * @param options
	 */
	protected void internalBuild(final O options) {
		internalBuildCache(options);
		buildsCount++;
	}
	
	/**
	 * Internal method that performs cache clearing.
	 * @param options
	 */
	protected void internalClear(final O options) {
		internalCleanupCache(options);
	}
	
	/**
	 * Internal method used to determine if a rebuild is necessary.
	 * Can be overrided by implementing classes to support their own logic.
	 * @param options
	 * @return 
	 */
	protected boolean internalShouldBuild(final O options) {
		return buildsCount <= 0;
	}
	
	/**
	 * Method that needs to be called by implementing classes before any cache access operation.
	 */
	protected void internalCheckBeforeGetDoNotLockThis() {
		// This may be useful for subclasses, do nothing for now...
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
