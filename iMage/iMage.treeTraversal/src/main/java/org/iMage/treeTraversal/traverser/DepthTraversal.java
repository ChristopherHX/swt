package org.iMage.treeTraversal.traverser;

import org.iMage.treeTraversal.model.IVisitor;
import org.iMage.treeTraversal.model.Leaf;
import org.iMage.treeTraversal.model.Node;
import org.iMage.treeTraversal.model.Tree;

/**
 * DepthTranversal
 * Implements deep Traverse the folder stucture
 */
public class DepthTraversal extends Traversal implements IVisitor {
    private Tree cur;

    private IVisitor itemVisitor = new IVisitor() {
    
        @Override
        public void visit(Node node) {
            boolean isparentnode = cur != null && node == cur.getParent();
            if (cur == node) {
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
    
        @Override
        public void visit(Leaf leaf) {
            if (cur == null) {
                cur = leaf;
            } else if (cur == leaf) {
                cur = null;
            }
        }
    };

    /**
     * Traverse deeply beginning at
     * @param startItem root item
     */
    public DepthTraversal(Tree startItem) {
        super(startItem);
        cur = startItem;
    }

    @Override
    public boolean hasNext() {
        return cur != null;
    }

    @Override
    public Tree next() {
        var ret = cur;
        cur.accept(this);
        return ret;
    }

    @Override
    public void visit(Leaf leaf) {
        leaf.getParent().accept(itemVisitor);
    }

    @Override
    public void visit(Node node) {
        cur = null;
        itemVisitor.visit(node);
    }
}