/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.search.query.dsl;

/**
 * @author Guillaume Smet
 */
public interface PlainTextContext extends QueryCustomization<PlainTextContext> {
	/**
	 * @param field The field name the plain text query is executed on
	 *
	 * @return {@code PlainTextMatchingContext} to continue the term query
	 */
	PlainTextMatchingContext onField(String field);

	/**
	 * @param field The field names the term query is executed on. The underlying properties for the specified
	 * fields need to be of the same type. For example, it is not possible to use this method with a mixture of
	 * string and date properties. In the mixed case an alternative is to build multiple term queries and combine them
	 * via {@link org.hibernate.search.query.dsl.QueryBuilder#bool()}
	 * @return {@code PlainTextMatchingContext} to continue the plain text query
	 */
	PlainTextMatchingContext onFields(String... field);

}
