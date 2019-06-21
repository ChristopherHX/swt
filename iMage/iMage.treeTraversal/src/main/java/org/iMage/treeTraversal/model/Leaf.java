package org.iMage.treeTraversal.model;

import java.io.File;


/**
 * Defines a leaf of the tree (single file).
 */
public class Leaf extends AbstractTree {
  /**
   * Create by file and parent.
   *
   * @param file
   *          the file
   * @param parent
   *          the parent
   */
  public Leaf(File file, Tree parent) {
    super(file, parent);
  }
}
