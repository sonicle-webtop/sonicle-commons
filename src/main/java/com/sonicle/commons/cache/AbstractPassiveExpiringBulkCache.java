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

import java.util.concurrent.TimeUnit;

/**
 *
 * @author malbinola
 */
public abstract class AbstractPassiveExpiringBulkCache extends AbstractBulkCache {
	private static final long serialVersionUID = 1L;
	protected final ExpirationPolicy expiringPolicy;
	protected Long expirationTime;
	
	public AbstractPassiveExpiringBulkCache() {
		this(-1L);
	}
	
	public AbstractPassiveExpiringBulkCache(final ExpirationPolicy expiringPolicy) {
		this.expiringPolicy = expiringPolicy;
	}
	
	public AbstractPassiveExpiringBulkCache(final long timeToLiveMillis) {
		this(new ConstantTimeToLiveExpirationPolicy(timeToLiveMillis));
	}
	
	public AbstractPassiveExpiringBulkCache(final long timeToLive, final TimeUnit timeUnit) {
		this(ConstantTimeToLiveExpirationPolicy.validateAndConvertToMillis(timeToLive, timeUnit));
	}
	
	@Override
	protected void internalBuild() {
		super.internalBuild();
		expirationTime = expiringPolicy.expirationTime();
	}
	
	@Override
	protected boolean internalShouldBuild() {
		return super.internalShouldBuild() || checkExpired(now(), expirationTime);
	}
	
	public void forceExpire() {
		long stamp = lock.writeLock();
		try {
			expirationTime = now();
		} finally {
			lock.unlockWrite(stamp);
		}
	}
	
	protected boolean checkExpired(final long now, final Long expirationTime) {
		if (expirationTime != null) {
			return expirationTime >= 0 && now >= expirationTime;
		}
		return false;
	}
	
	protected long now() {
		return System.currentTimeMillis();
	}
	
	
	
	
		/*
	public T getValue() {
		nullIfExpired(now());
		return value;
	}
	
	public T setValue(T value) {
		nullIfExpired(now());
		expirationTime = expiringPolicy.expirationTime();
		this.value = value;
		return value;
	}
	
	private void nullIfExpired(final long now) {
		if (isExpired(now, expirationTime)) {
			value = null;
		}
	}
	*/
}
