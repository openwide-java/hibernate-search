/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.search.query.dsl;

/**
* @author Emmanuel Bernard
* @author Guillaume Smet
*/
public interface PlainTextMatchingContext extends PlainTextQueryDefinitionTermination {
	/**
	 * Boost the field to a given value
	 * Most of the time positive float:
	 *  - lower than 1 to diminish the weight
	 *  - higher than 1 to increase the weight
	 *
	 * Could be negative but not unless you understand what is going on (advanced)
	 */
	PlainTextMatchingContext boostedTo(float boost);

	/**
	 * field / property the term query is executed on
	 */
	PlainTextMatchingContext andField(String field);

	/**
	 * Search for all the terms specified in matching().
	 */
	PlainTextQueryDefinitionTermination defaultOperatorIsAnd();

}
