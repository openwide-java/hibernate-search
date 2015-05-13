/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.search.query.dsl.impl;

import org.apache.lucene.search.Filter;
import org.hibernate.search.query.dsl.PlainTextContext;
import org.hibernate.search.query.dsl.PlainTextMatchingContext;

/**
 * @author Guillaume Smet
 */
class ConnectedPlainTextContext implements PlainTextContext {
	private final QueryBuildingContext queryContext;
	private final QueryCustomizer queryCustomizer;

	public ConnectedPlainTextContext(QueryBuildingContext queryContext) {
		this.queryContext = queryContext;
		this.queryCustomizer = new QueryCustomizer();
	}

	@Override
	public PlainTextMatchingContext onField(String field) {
		return new ConnectedPlainTextMatchingContext( field, queryCustomizer, queryContext );
	}

	@Override
	public PlainTextMatchingContext onFields(String... fields) {
		return new ConnectedPlainTextMatchingContext( fields, queryCustomizer, queryContext );
	}

	@Override
	public ConnectedPlainTextContext boostedTo(float boost) {
		queryCustomizer.boostedTo( boost );
		return this;
	}

	@Override
	public ConnectedPlainTextContext withConstantScore() {
		queryCustomizer.withConstantScore();
		return this;
	}

	@Override
	public ConnectedPlainTextContext filteredBy(Filter filter) {
		queryCustomizer.filteredBy( filter );
		return this;
	}
}
