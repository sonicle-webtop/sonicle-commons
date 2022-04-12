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
package com.sonicle.commons.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://www.baeldung.com/java-acquire-lock-by-key
 * https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-concurrency-advanced-4/src/main/java/com/baeldung/lockbykey/LockByKey.java
 * @author malbinola
 * @param <K> The Key type
 */
public class KeyedReentrantLocks<K> {
	private final ConcurrentHashMap<K, LockWrapper> locks = new ConcurrentHashMap<>();
	
	/**
	 * Acquires the lock for the specified Key.
	 * If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until the
     * lock has been acquired.
	 * @param key The Key on which lock.
	 */
	public void lock(final K key) {
		LockWrapper lockWrapper = getLock(key);
		lockWrapper.lock.lock();
	}
	
	/**
	 * Acquires the lock for the specified Key unless the current thread is interrupted.
	 * Acquires the lock if it is available and returns immediately.
	 * If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until
	 * one of two things happens:
	 *  - The lock is acquired by the current thread
	 *  - Some other thread interrupts the current thread
	 * @param key The Key on which lock.
	 * @throws InterruptedException InterruptedException if the current thread is interrupted
	 *         while acquiring the lock (and interruption of lock acquisition is supported)
	 */
	public void lockInterruptibly(final K key) throws InterruptedException {
		LockWrapper lockWrapper = getLock(key);
		lockWrapper.lock.lockInterruptibly();
	}
	
	/**
	 * Acquires the lock for the specified Key only if it is free at the time of invocation.
	 * Acquires the lock if it is available and returns immediately
     * with the value {@code true}.
     * If the lock is not available then this method will return
     * immediately with the value {@code false}.
	 * @param key The Key on which lock.
	 * @return {@code true} if the lock was acquired and {@code false} otherwise
	 */
	public boolean tryLock(final K key) {
		LockWrapper lockWrapper = getLock(key);
		return lockWrapper.lock.tryLock();
	}
	
	/**
	 * Acquires the lock for the specified Key if it is free within the given 
	 * waiting time and the current thread has not been interrupted.
	 * If the lock is available this method returns immediately
     * with the value {@code true}. If the lock is not available then
	 * the current thread becomes disabled for thread scheduling
	 * purposes and lies dormant until one of three things happens:
	 *  - The lock is acquired by the current thread
	 *  - Some other thread interrupts the current thread
	 *  - The specified waiting time elapses
	 * 
	 * @param key The Key on which lock.
	 * @param time The maximum time to wait for the lock.
	 * @param timeUnit Unit the time unit of the {@code time} argument.
	 * @return {@code true} if the lock was acquired and {@code false} otherwise
	 * @throws InterruptedException InterruptedException if the current thread is interrupted
	 *         while acquiring the lock (and interruption of lock acquisition is supported)
	 */
	public boolean tryLock(final K key, final long time, final TimeUnit timeUnit) throws InterruptedException {
		LockWrapper lockWrapper = getLock(key);
		return lockWrapper.lock.tryLock(time, timeUnit);
	}
	
	/**
	 * Releases the lock for the specified Key.
	 * @param key The Key on which unlock.
	 * @return {@code false} if the lock was NOT found, {@code true} otherwise
	 */
	public boolean unlock(final K key) {
		LockWrapper lockWrapper = locks.get(key);
		if (lockWrapper != null) {
			lockWrapper.lock.unlock();
			if (lockWrapper.removeThreadFromQueue() == 0) {
				// NB : We pass in the specific value to remove to handle the case where another thread would queue right before the removal
				locks.remove(key, lockWrapper);
			}
			return true;
		}
		return false;
	}
	
	private LockWrapper getLock(K key) {
		return locks.compute(key, (k, v) -> v == null ? new LockWrapper() : v.addThreadInQueue());
	}
	
	private static class LockWrapper {
		private final Lock lock = new ReentrantLock();
		private final AtomicInteger numberOfThreadsInQueue = new AtomicInteger(1);
		
		private LockWrapper addThreadInQueue() {
			numberOfThreadsInQueue.incrementAndGet();
			return this;
		}
		
		private int removeThreadFromQueue() {
			return numberOfThreadsInQueue.decrementAndGet(); 
		}
	}
}
