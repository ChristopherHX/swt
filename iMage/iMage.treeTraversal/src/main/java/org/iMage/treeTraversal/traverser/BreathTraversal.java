package org.iMage.treeTraversal.traverser;

import java.util.ArrayList;
import java.util.List;

import org.iMage.treeTraversal.model.IVisitor;
import org.iMage.treeTraversal.model.Leaf;
import org.iMage.treeTraversal.model.Node;
import org.iMage.treeTraversal.model.Tree;

/**
 * BreathTranversal
 */
public class BreathTraversal extends Traversal implements IVisitor {
    private Tree cur;
    private List<Node> nodes;
    private int nodei;

    private IVisitor breathrootVisitor = new IVisitor() {
        @Override
        public void visit(Node node) {
            if (cur == null) {
                cur = node;
            } else if (cur == node || node == cur.getParent()) {
                if (cur == node) {
                    cur = null;
                }
                for (var tree : node.getChildren()) {
                    if (cur == null) {
                        cur = tree;
                        // Found no need for more loops
                        break;
                    } else if (cur == tree) {
                        cur = null;
                    }
                }
            }
        }
    
        @Override
        public void visit(Leaf leaf) {
            if (cur == null) {
                cur = leaf;
            } else if (cur == leaf) {
                cur = null;
            }
        }
    };
    private IVisitor breathcollector = new IVisitor() {
        @Override
        public void visit(Leaf leaf) {
        }
    
        @Override
        public void visit(Node node) {
            nodes.add(node);
        }
    };

    /**
     * Traverse breath beginning at
     * @param startItem root item
     */
    public BreathTraversal(Tree startItem) {
        super(startItem);
        nodes = new ArrayList<>();
        startItem.accept(breathcollector);
        cur = startItem;
    }

    @Override
    public boolean hasNext() {
        return cur != null;
    }

    @Override
    public Tree next() {
        var ret = cur;
        while (true) {
            while (nodei < nodes.size()) {
                nodes.get(nodei).accept(breathrootVisitor);
                if (cur != ret && cur != null) {
                    return ret;
                }
                nodei++;
            }
            nodei = 0;
            var nodes = this.nodes;
            this.nodes = new ArrayList<>();
            for (var node : nodes) {
                node.accept(this);
            }
            if (this.nodes.isEmpty()) {
                return ret;
            }
        }
    }

    @Override
    public void visit(Leaf leaf) {
    }

    @Override
    public void visit(Node node) {
        for (var tree : node.getChildren()) {
            tree.accept(breathcollector);
        }
    }
}