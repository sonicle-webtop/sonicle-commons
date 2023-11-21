/*
 * Copyright (C) 2023 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2023 Sonicle S.r.l.".
 */
package com.sonicle.commons.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import net.sf.qualitycheck.Check;

/**
 *
 * @author malbinola
 * @param <E>
 * @param <K>
 */
public class ActivePollerQueue<E extends Delayed & KeyAware<K>, K> {
	private ScheduledExecutorService executor;
	private volatile boolean running;
	private volatile boolean die = false;
	private final UniqueDelayQueue<E, K> queue = new UniqueDelayQueue<>();
	private final PollCallback<E> pollCallback;
	
	public ActivePollerQueue(long pollInterval, TimeUnit pollIntervalUnit, PollCallback<E> pollCallback) {
		this(Executors.newSingleThreadScheduledExecutor(), pollInterval, pollIntervalUnit, pollCallback);
	}
	
	public ActivePollerQueue(ThreadFactory threadFactory, long pollInterval, TimeUnit pollIntervalUnit, PollCallback<E> pollCallback) {
		this(Executors.newSingleThreadScheduledExecutor(Check.notNull(threadFactory, "threadFactory")), pollInterval, pollIntervalUnit, pollCallback);
	}
	
	public ActivePollerQueue(ScheduledExecutorService executor, long pollInterval, TimeUnit pollIntervalUnit, PollCallback<E> pollCallback) {
		this.executor = Check.notNull(executor, "executor");
		this.pollCallback = Check.notNull(pollCallback, "pollCallback");
		Check.notNull(pollIntervalUnit, "pollIntervalUnit");
		
		executor.scheduleAtFixedRate(() -> {
			try {
				running = true;
				ArrayList<E> items = new ArrayList<>();
				queue.drainTo(items);
				if (!items.isEmpty()) {
					try {
						pollCallback.poll(items);
					} catch (Exception ex) { /* Do nothing... */ }
				}
			} finally {
				running = false;
			}
		}, 0, pollInterval, pollIntervalUnit);
	}
	
	public synchronized void stop() {
		die = true;
		executor.shutdown();
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public boolean push(E e) {
		if (die) throw new IllegalStateException("Executor is not running");
		return queue.offer(e);
	}
	
	public interface PollCallback<E> {
		void poll(Collection<E> items);
	}
}
