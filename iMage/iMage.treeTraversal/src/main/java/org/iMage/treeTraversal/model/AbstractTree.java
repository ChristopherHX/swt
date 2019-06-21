package org.iMage.treeTraversal.model;

import java.io.File;
import java.util.Iterator;
import java.util.Objects;

import org.iMage.treeTraversal.traverser.Traversal;

/**
 * Defines an abstract tree element.
 */
public abstract class AbstractTree implements Tree {

	private final File file;
	private final Tree parent;

	/**
	 * Create by file and parent.
	 *
	 * @param file
	 *          the file
	 * @param parent
	 *          the parent
	 */
	protected AbstractTree(File file, Tree parent) {
		this.file = Objects.requireNonNull(file, "file cannot be null");
		this.parent = parent;
	}

	/**
	 * Create by file (without parent).
	 *
	 * @param file
	 *          the file
	 */
	protected AbstractTree(File file) {
		this(file, null);
	}

	@Override
	public final File getFile() {
		return this.file;
	}

	@Override
	public final Tree getParent() {
		return this.parent;
	}

	@Override
	public final boolean hasParent() {
		return !Objects.isNull(this.parent);
	}

	@Override
	public final Iterator<Tree> getIterator(Class<? extends Traversal> traversal) {
		throw new UnsupportedOperationException("Implement me!");
	}
}
