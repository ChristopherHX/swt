package org.iMage.treeTraversal.traverser;

import java.util.Iterator;

import org.iMage.treeTraversal.model.Tree;

/**
 * Defines an iterator over the elements of a {@link Tree}.
 */
public abstract class Traversal implements Iterator<Tree> {

  /**
   * Create by starting item.
   *
   * @param startItem
   *          the start item
   *
   */
  protected Traversal(Tree startItem) {
	  throw new UnsupportedOperationException("Implement me!");
  }

}
