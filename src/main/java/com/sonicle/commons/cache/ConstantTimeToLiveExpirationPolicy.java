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
public class ConstantTimeToLiveExpirationPolicy implements ExpirationPolicy {
	private static final long serialVersionUID = 1L;
	private final long timeToLiveMillis;
	
	public static long validateAndConvertToMillis(final long timeToLive, final TimeUnit timeUnit) {
		return TimeUnit.MILLISECONDS.convert(timeToLive, timeUnit);
	}

	public ConstantTimeToLiveExpirationPolicy() {
		this(-1L);
	}

	public ConstantTimeToLiveExpirationPolicy(final long timeToLiveMillis) {
		super();
		this.timeToLiveMillis = timeToLiveMillis;
	}

	public ConstantTimeToLiveExpirationPolicy(final long timeToLive, final TimeUnit timeUnit) {
		this(validateAndConvertToMillis(timeToLive, timeUnit));
	}

	@Override
	public long expirationTime() {
		if (timeToLiveMillis >= 0L) {
			// avoid numerical overflow
			final long now = System.currentTimeMillis();
			if (now > Long.MAX_VALUE - timeToLiveMillis) {
				// expiration would be greater than Long.MAX_VALUE
				return -1; // never expire
			}

			return now + timeToLiveMillis; // timeToLiveMillis in the future
		}
		return -1L; // never expire
	}
}
