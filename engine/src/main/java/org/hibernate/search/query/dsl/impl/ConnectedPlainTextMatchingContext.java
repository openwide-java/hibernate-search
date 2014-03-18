/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010-2014, Red Hat, Inc. and/or its affiliates or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat, Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
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

	private boolean withAllTerms = false;

	public ConnectedPlainTextMatchingContext(String field, QueryCustomizer queryCustomizer, QueryBuildingContext queryContext) {
		this.queryContext = queryContext;
		this.queryCustomizer = queryCustomizer;
		this.fieldsContext = new FieldsContext( new String[] { field } );
	}

	public ConnectedPlainTextMatchingContext(String[] fields, QueryCustomizer queryCustomizer, QueryBuildingContext queryContext) {
		this.queryContext = queryContext;
		this.queryCustomizer = queryCustomizer;
		this.fieldsContext = new FieldsContext( fields );
	}

	@Override
	public PlainTextTermination matching(String value) {
		return new ConnectedMultiFieldsPlainTextQueryBuilder( value, fieldsContext, queryCustomizer, queryContext, withAllTerms );
	}

	@Override
	// XXX gsmet: not sure it's the best wording. It comes from the original patch but the real meaning here is that
	// the default operator is MUST. Moreover, I think withAllTerms() should be the default behavior for a plain text
	// search.
	public PlainTextMatchingContext withAllTerms() {
		withAllTerms = true;
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
