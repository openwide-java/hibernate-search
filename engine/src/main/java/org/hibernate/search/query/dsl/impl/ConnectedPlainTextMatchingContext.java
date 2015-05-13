/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.search.query.dsl.impl;

import org.hibernate.search.query.dsl.PlainTextMatchingContext;
import org.hibernate.search.query.dsl.PlainTextTermination;

/**
 * @author Emmanuel Bernard
 * @author Guillaume Smet
 */
public class ConnectedPlainTextMatchingContext implements PlainTextMatchingContext {

	private final QueryBuildingContext queryContext;
	private final QueryCustomizer queryCustomizer;
	private final FieldsContext fieldsContext;

	private boolean defaultOperatorIsAnd = false;

	public ConnectedPlainTextMatchingContext(String field, QueryCustomizer queryCustomizer, QueryBuildingContext queryContext) {
		this.queryContext = queryContext;
		this.queryCustomizer = queryCustomizer;
		this.fieldsContext = new FieldsContext( new String[] { field }, queryContext );
	}

	public ConnectedPlainTextMatchingContext(String[] fields, QueryCustomizer queryCustomizer, QueryBuildingContext queryContext) {
		this.queryContext = queryContext;
		this.queryCustomizer = queryCustomizer;
		this.fieldsContext = new FieldsContext( fields, queryContext );
	}

	@Override
	public PlainTextTermination matching(String value) {
		return new ConnectedMultiFieldsPlainTextQueryBuilder( value, fieldsContext, queryCustomizer, queryContext, defaultOperatorIsAnd );
	}

	@Override
	public PlainTextMatchingContext defaultOperatorIsAnd() {
		defaultOperatorIsAnd = true;
		return this;
	}

	@Override
	public PlainTextMatchingContext andField(String field) {
		fieldsContext.add( field );
		return this;
	}

	@Override
	public PlainTextMatchingContext boostedTo(float boost) {
		fieldsContext.boostedTo( boost );
		return this;
	}

	@Override
	public PlainTextMatchingContext ignoreAnalyzer() {
		// XXX gsmet: see if we allow to ignore the analyzer or not. It doesn't seem wise for plain text search.
		throw new UnsupportedOperationException( "Not supported for now" );
	}

	@Override
	public PlainTextMatchingContext ignoreFieldBridge() {
		// XXX gsmet: can we find a way to support field bridge? I'm not so sure we can get something really working.
		throw new UnsupportedOperationException( "Not supported for now" );
	}

}
