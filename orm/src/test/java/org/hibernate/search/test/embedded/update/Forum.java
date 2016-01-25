/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.test.embedded.update;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class Forum {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@Field
	private boolean hidden;

	@OneToMany
	@ContainedIn
	private Set<ForumPost> posts = new HashSet<ForumPost>();

	protected Forum() {
	}

	public Forum(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ForumPost> getPosts() {
		return posts;
	}

	public void setPosts(Set<ForumPost> posts) {
		this.posts = posts;
	}
	
	public void addPost(ForumPost post) {
		posts.add( post );
		post.setForum( this );
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
