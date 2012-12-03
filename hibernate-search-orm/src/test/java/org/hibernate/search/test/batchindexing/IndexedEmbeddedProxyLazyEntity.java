package org.hibernate.search.test.batchindexing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.search.annotations.Field;

@Entity
public class IndexedEmbeddedProxyLazyEntity {

	@Id
	@GeneratedValue
	private Integer id;

	@Field
	@Column
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
