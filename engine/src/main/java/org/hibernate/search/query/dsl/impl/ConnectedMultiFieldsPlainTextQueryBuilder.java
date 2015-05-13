/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.search.query.dsl.impl;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.hibernate.search.exception.SearchException;
import org.hibernate.search.query.dsl.PlainTextTermination;
import org.hibernate.search.util.StringHelper;

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

		if ( StringHelper.isEmpty( searchText ) ) {
			throw new SearchException( "You cannot search with an empty text" );
		}

		Query query = queryParser.parse( searchText );

		if ( query == null ) {
			throw new SearchException( "Parsed query is empty" );
		}

		return queryCustomizer.setWrappedQuery( query ).createQuery();
	}

}
