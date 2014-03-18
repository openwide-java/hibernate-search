/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2014, Red Hat, Inc. and/or its affiliates or third-party contributors as
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
