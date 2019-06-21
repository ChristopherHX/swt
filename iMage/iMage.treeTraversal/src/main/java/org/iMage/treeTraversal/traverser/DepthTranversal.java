package org.iMage.treeTraversal.traverser;

import org.iMage.treeTraversal.model.IVisitor;
import org.iMage.treeTraversal.model.Leaf;
import org.iMage.treeTraversal.model.Node;
import org.iMage.treeTraversal.model.Tree;

/**
 * DepthTransversal
 */
public class DepthTranversal extends Traversal implements IVisitor {
    private Tree cur;

    public DepthTranversal(Tree startItem) {
        super(startItem);
        startItem.accept(this);
    }

    @Override
    public boolean hasNext() {
        return cur != null;
    }

    @Override
    public Tree next() {
        var ret = cur;
        cur.getParent().accept(this);
        return ret;
    }

    @Override
    public void visit(Leaf leaf) {
        if (cur == null) {
            cur = leaf;
        } else if (cur == leaf) {
            cur = null;
        }
    }

    @Override
    public void visit(Node node) {
        boolean isparentnode = cur != null && node == cur.getParent();
        if(cur == node) {
            cur = null;
        } else if (cur == null || isparentnode) {
            for (var tree : node.getChildren()) {
                tree.accept(this);
            }
            if (cur == null && isparentnode && node.getParent() != null) {
                cur = node;
                cur.getParent().accept(this);
            }
        }
    }
}