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
        // if cur is null no next item found after next
        return cur != null;
    }

    @Override
    public Tree next() {
        var ret = cur;
        // Run the visitor to find next item
        cur.accept(this);
        return ret;
    }

    private void onItem(Tree item) {
        if (cur == null) {
            // New item found set it
            cur = item;
        } else if (cur == item) {
            // Old item found null it
            cur = null;
        }
    }

    @Override
    public void visit(Leaf leaf) {
        // Lookup in parent folder
        leaf.getParent().accept(this);
    }

    @Override
    public void visit(Node node) {
        if (cur == node) {
            // cur is this its not a child of themself null it
            cur = null;
        }
        var old = cur;
        for (var tree : node.getChildren()) {
            onItem(tree);
            if (old != cur && cur != null) {
                // next found exit
                return;
            }
        }
        if (cur == null && node != startItem) {
            cur = node;
            // Lookup in parent folder
            node.getParent().accept(this);
        }
    }
}