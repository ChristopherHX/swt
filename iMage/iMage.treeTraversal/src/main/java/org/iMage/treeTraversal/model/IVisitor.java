package org.iMage.treeTraversal.model;

/**
 * IVisitor
 * Represents the Visitor interface of the Visitor template
 */
public interface IVisitor {
    /**
     * Action for leafes
     * @param leaf to manipulate
     */
    void visit(Leaf leaf);
    /**
     * Action for node
     * @param node to manipulate
     */
    void visit(Node node);
}