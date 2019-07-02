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
public class BreathTraversal extends Traversal {
    private Tree cur;
    private List<Node> nodes;
    private int nodei;

    private IVisitor breathrootVisitor = new IVisitor() {
        @Override
        public void visit(Node node) {
            if (cur == null || cur == node || node == cur.getParent()) {
                // no child of this folder set cur to null and find first item
                if (cur == node) {
                    cur = null;
                }
                for (var tree : node.getChildren()) {
                    if (cur == null) {
                        cur = tree;
                        // Found no need for more loops
                        break;
                    } else if (cur == tree) {
                        // Found old, now search next new node
                        cur = null;
                    }
                }
            }
        }
    
        @Override
        public void visit(Leaf leaf) {
        }
    };
    // Collect all subfolders
    private IVisitor breathcollector = new IVisitor() {
        @Override
        public void visit(Leaf leaf) {
        }
    
        @Override
        public void visit(Node node) {
            // Add this folder to the list
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
                    // Found exit
                    return ret;
                }
                nodei++;
            }
            // Not found go one depth deeper
            nodei = 0;
            var nodes = this.nodes;
            this.nodes = new ArrayList<>();
            // Iterate over old folders (local nodes)
            for (var node : nodes) {
                for (var tree : node.getChildren()) {
                    // populate new folderlist (this.nodes)
                    tree.accept(breathcollector);
                }
            }
            // no subfolders exit no next item found (cur == null)
            if (this.nodes.isEmpty()) {
                return ret;
            }
        }
    }
}