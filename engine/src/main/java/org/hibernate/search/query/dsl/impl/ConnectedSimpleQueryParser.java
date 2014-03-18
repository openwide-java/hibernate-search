package org.hibernate.search.query.dsl.impl;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;

/**
 * A FieldsContext aware implementation of the SimpleQueryParser of Lucene 4.7
 */
public class ConnectedSimpleQueryParser extends SimpleQueryParser {

	/** Fields context as defined by the DSL */
	protected final FieldsContext fieldsContext;

	/** Creates a new parser searching over multiple fields with different weights. */
	public ConnectedSimpleQueryParser(Analyzer analyzer, FieldsContext fieldsContext) {
		this( analyzer, fieldsContext, -1 );
	}

	/** Creates a new parser with custom flags used to enable/disable certain features. */
	public ConnectedSimpleQueryParser(Analyzer analyzer, FieldsContext fieldsContext, int flags) {
		super( analyzer, new HashMap<String, Float>( 0 ), flags );
		this.fieldsContext = fieldsContext;
	}

	/**
	 * Factory method to generate a standard query (no phrase or prefix operators).
	 */
	@Override
	protected Query newDefaultQuery(String text) {
		BooleanQuery bq = new BooleanQuery( true );
		for ( FieldContext fieldContext : fieldsContext ) {
			Query q = createBooleanQuery( fieldContext.getField(), text, getDefaultOperator() );
			if ( q != null ) {
				bq.add( fieldContext.getFieldCustomizer().setWrappedQuery( q ).createQuery(), BooleanClause.Occur.SHOULD );
			}
		}
		return simplify( bq );
	}

	/**
	 * Factory method to generate a fuzzy query.
	 */
	@Override
	protected Query newFuzzyQuery(String text, int fuzziness) {
		BooleanQuery bq = new BooleanQuery( true );
		for ( FieldContext fieldContext : fieldsContext ) {
			Query q = new FuzzyQuery( new Term( fieldContext.getField(), text ), fuzziness );
			if ( q != null ) {
				bq.add( fieldContext.getFieldCustomizer().setWrappedQuery( q ).createQuery(), BooleanClause.Occur.SHOULD );
			}
		}
		return simplify( bq );
	}

	/**
	 * Factory method to generate a phrase query with slop.
	 */
	@Override
	protected Query newPhraseQuery(String text, int slop) {
		BooleanQuery bq = new BooleanQuery( true );
		for ( FieldContext fieldContext : fieldsContext ) {
			Query q = createPhraseQuery( fieldContext.getField(), text, slop );
			if ( q != null ) {
				bq.add( fieldContext.getFieldCustomizer().setWrappedQuery( q ).createQuery(), BooleanClause.Occur.SHOULD );
			}
		}
		return simplify( bq );
	}

	/**
	 * Factory method to generate a prefix query.
	 */
	@Override
	protected Query newPrefixQuery(String text) {
		BooleanQuery bq = new BooleanQuery( true );
		for ( FieldContext fieldContext : fieldsContext ) {
			PrefixQuery prefix = new PrefixQuery( new Term( fieldContext.getField(), text ) );
			bq.add( fieldContext.getFieldCustomizer().setWrappedQuery( prefix ).createQuery(), BooleanClause.Occur.SHOULD );
		}
		return simplify( bq );
	}

}
