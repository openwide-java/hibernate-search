/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat, Inc. and/or its affiliates or third-party contributors as
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
package org.hibernate.search.engine.spi;

import java.util.Set;

/**
 * Used to check the constraints of depth when using {@link org.hibernate.search.annotations.IndexedEmbedded}
 * or {@link org.hibernate.search.annotations.ContainedIn} annotations.
 *
 * @author Davide D'Alto
 * @author Yoann Rodiere
 */
public class ContainedInRecursionContext {

	private int maxDepth;
	private int depth;

	private Set<String> comprehensivePaths;

	public ContainedInRecursionContext(int maxDepth, int depth, Set<String> comprehensivePaths) {
		this.maxDepth = maxDepth;
		this.depth = depth;
		this.comprehensivePaths = comprehensivePaths;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public int getDepth() {
		return depth;
	}

	public Set<String> getComprehensivePaths() {
		return comprehensivePaths;
	}

	public boolean isTerminal() {
		return depth > maxDepth || comprehensivePaths != null && comprehensivePaths.isEmpty();
	}

	@Override
	public String toString() {
		return "[maxDepth=" + maxDepth + ", level=" + depth + ", comprehensivePaths=" + comprehensivePaths + "]";
	}
}
