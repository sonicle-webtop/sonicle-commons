/*
 * Copyright (C) 2025 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2025 Sonicle S.r.l.".
 */
package com.sonicle.commons.rsql.parser;

import com.sonicle.commons.rsql.parser.ast.ComparisonOperator;
import com.sonicle.commons.rsql.parser.ast.RSQLOperators;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author malbinola
 */
public enum Operator {
	EQ(RSQLOperators.EQUAL),
	NEQ(RSQLOperators.NOT_EQUAL),
	LIKE(new ComparisonOperator("=like=", false)),
	NLIKE(new ComparisonOperator("=nlike=", false)),
	GT(RSQLOperators.GREATER_THAN),
	GTE(RSQLOperators.GREATER_THAN_OR_EQUAL),
	LT(RSQLOperators.LESS_THAN),
	LTE(RSQLOperators.LESS_THAN_OR_EQUAL),
	BTW(new ComparisonOperator("=btw=", true)),
	NBTW(new ComparisonOperator("=nbtw=", true)),
	IN(RSQLOperators.IN),
	NIN(RSQLOperators.NOT_IN),
	RE(new ComparisonOperator("=re=", false));
	//EX(new ComparisonOperator("=ex=", false));
	
	private final ComparisonOperator operator;
	
	Operator(ComparisonOperator operator) {
		this.operator = operator;
	}
	
	public ComparisonOperator getComparisonOperator() {
		return this.operator;
	}
	
	public static Operator toOperator(ComparisonOperator operator) {
		return Arrays.stream(values()).filter(value -> value.operator.equals(operator)).findFirst().orElse(null);
	}
	
	public static Set<ComparisonOperator> extendedOperators() {
		return new HashSet<>(Arrays.asList(
			Operator.LIKE.getComparisonOperator(),
			Operator.NLIKE.getComparisonOperator(),
			Operator.BTW.getComparisonOperator(),
			Operator.NBTW.getComparisonOperator(),
			Operator.RE.getComparisonOperator()
		));
		
		/*
		ops.add(new cz.jirutka.rsql.parser.ast.ComparisonOperator("=q=", false));
		ops.add(new cz.jirutka.rsql.parser.ast.ComparisonOperator("=ex=", false));
		ops.add(new cz.jirutka.rsql.parser.ast.ComparisonOperator("=re=", false));
		*/
	}
}
