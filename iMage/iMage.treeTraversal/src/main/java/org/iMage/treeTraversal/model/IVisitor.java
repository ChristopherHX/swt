package org.iMage.treeTraversal.model;

/**
 * IVisitor
 */
public interface IVisitor {
    void visit(Leaf leaf);
    void visit(Node node);
}