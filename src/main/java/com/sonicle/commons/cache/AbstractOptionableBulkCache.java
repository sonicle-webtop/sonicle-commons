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
 *
 * @author malbinola
 * @param <O>
 */
public abstract class AbstractOptionableBulkCache <O> {
	private static final long serialVersionUID = 1L;
	protected final StampedLock lock = new StampedLock();
	private int initCount = 0;
	
	protected abstract void internalBuildCache(final O options);
	protected abstract void internalCleanupCache(final O options);
	
	public void init() {
		init(null);
	}
	
	public final void init(final O options) {
		long stamp = lock.writeLock();
		try {
			internalInit(options);
		} finally {
			lock.unlockWrite(stamp);
		}
	}
	
	public final void clear() {
		clear(null);
	}
	
	public final void clear(final O options) {
		long stamp = lock.writeLock();
		try {
			internalClear(options);
		} finally {
			lock.unlockWrite(stamp);
		}
	}
	
	public boolean isInited() {
		return initCount > 0;
	}
	
	public int getInitCount() {
		return initCount;
	}
	
	public final long readLock() {
		return lock.readLock();
	}
	
	public final void unlockRead(final long stamp) {
		lock.unlockRead(stamp);
	}
	
	public final long writeLock() {
		return lock.writeLock();
	}
	
	public final void unlockWrite(final long stamp) {
		lock.unlockWrite(stamp);
	}
	
	protected void internalInit(final O options) {
		internalBuildCache(options);
		initCount++;
	}
	
	protected void internalClear(final O options) {
		internalCleanupCache(options);
	}
	
	protected void internalCheckBeforeGetDoNotLockThis() {
		// This may be useful for subclasses, do nothing for now...
	}
}
