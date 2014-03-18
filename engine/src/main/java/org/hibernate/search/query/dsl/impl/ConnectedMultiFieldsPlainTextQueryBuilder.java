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

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.search.SearchException;
import org.hibernate.search.query.dsl.PlainTextTermination;

/**
 * @author Emmanuel Bernard
 * @author Guillaume Smet
 */
public class ConnectedMultiFieldsPlainTextQueryBuilder implements PlainTextTermination {

	private final String searchText;
	private final QueryCustomizer queryCustomizer;
	private final FieldsContext fieldsContext;
	private final QueryBuildingContext queryContext;
	
	private final boolean withAllTerms;

	public ConnectedMultiFieldsPlainTextQueryBuilder(String searchText,
												FieldsContext fieldsContext,
												QueryCustomizer queryCustomizer,
												QueryBuildingContext queryContext,
												boolean withAllTerms) {
		this.searchText = searchText;
		this.queryContext = queryContext;
		this.queryCustomizer = queryCustomizer;
		this.fieldsContext = fieldsContext;

		this.withAllTerms = withAllTerms;
	}

	@Override
	public Query createQuery() {
		ConnectedSimpleQueryParser queryParser = new ConnectedSimpleQueryParser( queryContext.getQueryAnalyzer(), fieldsContext );
		queryParser.setDefaultOperator( withAllTerms ? Occur.MUST : Occur.SHOULD );

		if (StringHelper.isEmpty( searchText )) {
			throw new SearchException( "You cannot search with an empty text" );
		}

		Query query = queryParser.parse( searchText );

		if ( query == null ) {
			throw new SearchException( "Parsed query is empty" );
		}

		return queryCustomizer.setWrappedQuery( query ).createQuery();
	}

}
