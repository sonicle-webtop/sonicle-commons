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

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import net.sf.qualitycheck.Check;

/**
 *
 * @author malbinola
 */
public class ThreadFactoryBuilder {
	private String namePrefix = null;
	private boolean daemon = false;
	private int priority = Thread.NORM_PRIORITY;
	private ThreadFactory backingThreadFactory = null;
	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
	
	public ThreadFactoryBuilder withNamePrefix(String namePrefix) {
		this.namePrefix = Check.notNull(namePrefix, "namePrefix");
		return this;
	}
	
	public ThreadFactoryBuilder setDaemon(boolean daemon) {
		this.daemon = daemon;
		return this;
	}
	
	public ThreadFactoryBuilder setPriority(int priority) {
		Check.lesserThan(Thread.MIN_PRIORITY, priority);
		Check.greaterThan(Thread.MAX_PRIORITY, priority);
		this.priority = priority;
		return this;
	}
	
	public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = Check.notNull(uncaughtExceptionHandler, "uncaughtExceptionHandler");
		return this;
	}
	
	public ThreadFactoryBuilder setThreadFactory(ThreadFactory  backingThreadFactory) {
		this.backingThreadFactory = Check.notNull(backingThreadFactory, "backingThreadFactory");
		return this;
	}
	
	public ThreadFactory build() {
		return build(this);
	}
	
	private static ThreadFactory build(ThreadFactoryBuilder builder) {
		final String namePrefix = builder.namePrefix;
		final Boolean daemon = builder.daemon;
		final Integer priority = builder.priority;
		final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
		final ThreadFactory backingThreadFactory = (builder.backingThreadFactory != null) ? builder.backingThreadFactory : Executors.defaultThreadFactory();
		
		final AtomicLong count = new AtomicLong(1);
		
		return (Runnable runnable) -> {
			Thread thread = backingThreadFactory.newThread(runnable);
			if (namePrefix != null) thread.setName(namePrefix + "-" + count.getAndIncrement());
			thread.setDaemon(daemon);
			thread.setPriority(priority);
			if (uncaughtExceptionHandler != null) thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
			return thread;
		};
	}
}
