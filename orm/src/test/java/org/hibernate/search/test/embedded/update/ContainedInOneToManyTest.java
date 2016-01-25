/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.test.embedded.update;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.test.SearchTestBase;
import org.junit.Test;

/**
 * @author Guillaume Smet
 */
public class ContainedInOneToManyTest extends SearchTestBase {

	@Test
	public void testUpdatingContainedInEntityPropagatesToAllEntities() throws Exception {
		Forum forum1 = new Forum( "forum 1" );
		forum1.setHidden( false );

		ForumPost post1 = new ForumPost( "post 1" );
		forum1.addPost( post1 );
		ForumPost post2 = new ForumPost( "post 2" );
		forum1.addPost( post2 );

		// first operation -> save
		FullTextSession session = Search.getFullTextSession( openSession() );
		Transaction tx = session.beginTransaction();
		session.save( forum1 );
		session.save( post1 );
		session.save( post2 );
		tx.commit();
		session.close();

		// assert that everything got saved correctly
		session = Search.getFullTextSession( openSession() );
		tx = session.beginTransaction();

		// everything gets indexed correctly
		assertEquals( 2, getForumPostIdsFromIndex( session, false ).size() );
		assertEquals( 0, getForumPostIdsFromIndex( session, true ).size() );

		tx.commit();
		session.close();

		// let's move dad's to a new grandpa!
		forum1.setHidden(true);

		session = Search.getFullTextSession( openSession() );
		tx = session.beginTransaction();

		session.update( forum1 );

		tx.commit();
		session.close();

		// all right, let's assert that indexes got updated correctly
		session = Search.getFullTextSession( openSession() );
		tx = session.beginTransaction();

		// everything gets indexed correctly
		assertEquals( 0, getForumPostIdsFromIndex( session, false ).size() );
		assertEquals( 2, getForumPostIdsFromIndex( session, true ).size() );

		tx.commit();
		session.close();
	}

	private List<Long> getForumPostIdsFromIndex(FullTextSession session, boolean hidden) {
		final QueryBuilder b = session.getSearchFactory().buildQueryBuilder().forEntity( ForumPost.class ).get();

		
		FullTextQuery q = session.createFullTextQuery( b.keyword().onField( "forum.hidden" ).matching( hidden).createQuery());
		q.setProjection( "id" );
		@SuppressWarnings("unchecked")
		List<Object[]> results = q.list();
		List<Long> ids = new ArrayList<Long>();
		for ( Object[] result : results ) {
			ids.add((Long) result[0]);
		}
		return ids;
	}

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class[] { Forum.class, ForumPost.class };
	}
}
