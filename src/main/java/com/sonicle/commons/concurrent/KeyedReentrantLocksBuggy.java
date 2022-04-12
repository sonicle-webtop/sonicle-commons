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
package com.sonicle.commons.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * https://stackoverflow.com/a/36591851
 * https://github.com/mojgh/JKeyLockManager
 * https://bitbucket.org/kilaka/kuava/src/master/src/main/java/kilaka/kuava/DynamicKeyLock.java
 * https://bitbucket.org/kilaka/kuava/src/master/src/main/java/kilaka/kuava/
 * @author malbinola
 * @param <K>
 * @deprecated
 */
@Deprecated
public class KeyedReentrantLocksBuggy<K> {
	private final ConcurrentMap<K, KeyedLock> key2Lock = new ConcurrentHashMap<>();
	
	public KeyedLock acquire(K key) throws InterruptedException {
		return internalAcquire(key, null);
	}
	
	public KeyedLock tryAcquire(K key, long millis) {
		try {
			return internalAcquire(key, millis);
		} catch(InterruptedException ex) {
			return null;
		}
	}
	
	protected KeyedLock internalAcquire(K key, Long millis) throws InterruptedException {
		final KeyedLock ourLock = new KeyedLock() {
			@Override
			public void close() {
				if (Thread.currentThread().getId() != threadId) {
					throw new IllegalStateException("Thread mismatch");
				}
				if (--lockedCount == 0) {
					key2Lock.remove(key);
					mutex.countDown();
				}
			}
		};
		for (;;) {
			KeyedLock theirLock = key2Lock.putIfAbsent(key, ourLock);
			if (theirLock == null) {
				return ourLock;
			}
			if (theirLock.threadId == Thread.currentThread().getId()) {
				theirLock.lockedCount++;
				return theirLock;
			}
			if (millis != null) {
				if (!theirLock.mutex.await(millis, TimeUnit.MILLISECONDS)) return null;
			} else {
				theirLock.mutex.await();
			}
		}
	}
	
	/*
	public KeyedLock acquire(K key) throws InterruptedException {
		final KeyedLock ourLock = new KeyedLock() {
			@Override
			public void close() {
				if (Thread.currentThread().getId() != threadId) {
					throw new IllegalStateException("Thread mismatch");
				}
				if (--lockedCount == 0) {
					key2Lock.remove(key);
					mutex.countDown();
				}
			}
		};
		for (;;) {
			KeyedLock theirLock = key2Lock.putIfAbsent(key, ourLock);
			if (theirLock == null) {
				return ourLock;
			}
			if (theirLock.threadId == Thread.currentThread().getId()) {
				theirLock.lockedCount++;
				return theirLock;
			}
			theirLock.mutex.await();
		}
	}
	*/
	
	public static abstract class KeyedLock implements AutoCloseable {
		protected final CountDownLatch mutex = new CountDownLatch(1);
		protected final long threadId = Thread.currentThread().getId();
		protected int lockedCount = 1;
		
		@Override
		public abstract void close();
	}
}
