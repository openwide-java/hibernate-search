/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * JBoss, Home of Professional Open Source
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.hibernate.search.test.embedded.polymorphism.uninitializedproxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.test.SearchTestCaseJUnit4;
import org.hibernate.search.test.util.TestForIssue;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestForIssue(jiraKey = "HSEARCH-1448")
public class SearchAfterUninitializedProxyEntityLoadingTest extends SearchTestCaseJUnit4 {

	private static final Logger LOGGER = LoggerFactory.getLogger( SearchAfterUninitializedProxyEntityLoadingTest.class );

	private Integer entityId;
	private Integer entityReferenceId;

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { AbstractEntity.class, ConcreteEntity.class, LazyAbstractEntityReference.class };
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		LOGGER.info( "Populating database..." );
		populateDatabase();
	}

	private void populateDatabase() {
		Session session = openSession();
		
		try {
			Transaction t = session.beginTransaction();
			
			ConcreteEntity entity = new ConcreteEntity();
			session.save( entity );
			entityId = entity.getId();
			
			LazyAbstractEntityReference reference = new LazyAbstractEntityReference( entity );
			session.save( reference );
			entityReferenceId = reference.getId();
			
			session.flush();
			t.commit();
		}
		finally {
			session.close();
		}
	}

	/**
	 * In HSEARCH-1448 : works fine.
	 */
	@Test
	public void testSearchConcreteEntityWithoutPreLoadedProxy() {
		LOGGER.info( "Testing search on class ConcreteEntity *without* prior abstract proxy loading..." );
		doTest( ConcreteEntity.class, false );
	}

	/**
	 * In HSEARCH-1448 : works fine.
	 */
	@Test
	public void testSearchAbstractEntityWithoutPreLoadedProxy() {
		LOGGER.info( "Testing search on class AbstractEntity *without* prior abstract proxy loading..." );
		doTest( AbstractEntity.class, false );
	}

	/**
	 * In HSEARCH-1448 : works fine.
	 */
	@Test
	public void testSearchConcreteEntityWithPreLoadedProxy() {
		LOGGER.info( "Testing search on class ConcreteEntity *with* prior abstract proxy loading..." );
		doTest( ConcreteEntity.class, true );
	}

	/**
	 * In HSEARCH-1448 : fails.
	 */
	@Test
	public void testSearchAbstractEntityWithPreLoadedProxy() {
		LOGGER.info( "Testing search on class AbstractEntity *with* prior abstract proxy loading..." );
		doTest( AbstractEntity.class, true );
	}

	private void doTest(Class<? extends AbstractEntity> clazz, boolean loadAbstractProxyBeforeSearch) {
		Session session = openSession();
		
		try {
			if ( loadAbstractProxyBeforeSearch ) {
				// Load a proxified version of the entity into the session
				LazyAbstractEntityReference reference = (LazyAbstractEntityReference) session.get(
						LazyAbstractEntityReference.class, entityReferenceId );
				assertTrue( reference != null && !Hibernate.isInitialized( reference.getEntity() ) );
			}
			
			// Search for the created entity
			assertEquals( 1, doSearch( session, clazz, entityId ).size() );
		}
		finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> List<T> doSearch(Session session, Class<T> clazz, Integer entityId) {
		FullTextSession fullTextSession = Search.getFullTextSession( session );
		
		FullTextQuery query = fullTextSession.createFullTextQuery(
				new TermQuery( new Term( "id", entityId.toString() ) ), clazz );
		
		return query.list();
	}
}
