/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2013, Red Hat, Inc. and/or its affiliates or third-party contributors as
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
package org.hibernate.search.engine.metadata.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.common.reflection.XMember;
import org.hibernate.search.util.impl.ReflectionHelper;

/**
 * @author Hardy Ferentschik
 * @author Yoann Rodiere
 */
public class ContainedInMetadata {

	private final XMember containedInMember;
	private final Integer maxDepth;
	private final String prefix;
	private final Set<String> includePaths;

	public ContainedInMetadata(XMember containedInMember, Integer maxDepth, String prefix, String[] includePaths) {
		this.containedInMember = containedInMember;
		ReflectionHelper.setAccessible( this.containedInMember );
		this.maxDepth = maxDepth;
		this.prefix = prefix;
		this.includePaths = includePaths != null ? new HashSet<String>( Arrays.asList( includePaths ) ) : Collections.<String>emptySet();
	}

	public XMember getContainedInMember() {
		return containedInMember;
	}

	public Integer getMaxDepth() {
		return maxDepth;
	}

	public String getPrefix() {
		return prefix;
	}

	public Set<String> getIncludePaths() {
		return includePaths;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder( "ContainedInMetadata{" );
		sb.append( "containedInMember=" ).append( containedInMember );
		sb.append( ", maxDepth=" ).append( maxDepth );
		sb.append( '}' );
		return sb.toString();
	}
}


