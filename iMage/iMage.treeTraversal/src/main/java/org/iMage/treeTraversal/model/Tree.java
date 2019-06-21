package org.iMage.treeTraversal.model;

import java.io.File;
import java.util.Iterator;

import org.iMage.treeTraversal.traverser.Traversal;

/**
 * Defines an element of a tree.
 */
public interface Tree {
  void accept(IVisitor visitor);

  /**
   * Get associated file.
   *
   * @return the associated file
   */
  File getFile();

  /**
   * Get the parent Tree.
   *
   * @return the parent or {@code null} if none exists.
   */
  Tree getParent();

  /**
   * Indicates whether the parent exist.
   *
   * @return indicator
   */
  boolean hasParent();

  /**
   * Get iterator by traversal.
   * 
   * @param traversal
   *          the class of traversal
   * @return the iterator
   */
  Iterator<Tree> getIterator(Class<? extends Traversal> traversal);

  /**
   * Get iterable by traversal.
   * 
   * @param traversal
   *          the class of traversal
   * @return the iterator
   */
  Iterable<Tree> getIterable(Class<? extends Traversal> traversal);
}
