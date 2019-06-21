package org.iMage.treeTraversal.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Defines a node of the tree (folder).
 */
public class Node extends AbstractTree {
  private List<Tree> children = new ArrayList<>();

  /**
   * Create by folder and parent.
   *
   * @param folder
   *          the folder
   * @param parent
   *          the parent
   */
  public Node(File folder, Tree parent) {
    super(folder, parent);
  }

  /**
   * Create by folder (no parent)
   *
   * @param folder
   *          the folder
   */
  public Node(File folder) {
    super(folder);
  }

  /**
   * Add a child to the node.
   *
   * @param child
   *          the child
   */
  public void addChild(Tree child) {
    this.children.add(child);
  }

  /**
   * Remove a child from the node.
   *
   * @param child
   *          the child
   */
  public void removeChild(Tree child) {
    this.children.remove(child);
  }

  /**
   * Get a collection of all children (unmodifiable).
   *
   * @return a collection of all children
   */
  public Collection<Tree> getChildren() {
    return Collections.unmodifiableCollection(this.children);
  }

  @Override
  public void accept(IVisitor visitor) {
    visitor.visit(this);
  }

}
