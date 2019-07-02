package org.iMage.traverser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;

import org.iMage.treeTraversal.model.Leaf;
import org.iMage.treeTraversal.model.Node;
import org.iMage.treeTraversal.traverser.DepthTraversal;
import org.junit.Before;
import org.junit.Test;

/**
 * DepthTraverseTest
 * (There's no BreathTraverse Test,
 * because it was a bonus previous)
 */
public class DepthTraverseTest {
    private Node rootnode;
    private File[] depthorder = new File[] {
        new File("12wfcsusdsk"),
        new File("Hello.png"),
        new File("Hel4lo2.png"),
        new File("???"),
        new File("##543He2llo.png"),
        new File("5#43He2tello.png"),
        new File("Hewl##4lo2.png"),
        new File("543He2llo.png"),
        new File("Hewl4lo2.png"),
        new File("543He2tello.png")
    };
    private Node node;

    /**
     * Initialize static pseudotree
     */
    @Before
    public void populatepseudotree() {
        rootnode = new Node(new File("12wfcsusdsk"));
        rootnode.addChild(new Leaf(new File("Hello.png"), rootnode));
        rootnode.addChild(new Leaf(new File("Hel4lo2.png"), rootnode));
        node = new Node(new File("???"), rootnode);
        node.addChild(new Leaf(new File("##543He2llo.png"), node));
        node.addChild(new Leaf(new File("5#43He2tello.png"), node));
        node.addChild(new Leaf(new File("Hewl##4lo2.png"), node));
        rootnode.addChild(node);
        rootnode.addChild(new Leaf(new File("543He2llo.png"), rootnode));
        rootnode.addChild(new Leaf(new File("Hewl4lo2.png"), rootnode));
        rootnode.addChild(new Leaf(new File("543He2tello.png"), rootnode));
    }

    /**
     * Test Trivial things
     * Normal like runner uses it
     */
    @Test
    public void testDepthfolder() {
        var i = 0;
        for (var node : rootnode.getIterable(DepthTraversal.class)) {
            assertEquals(depthorder[i++], node.getFile());
        }
    }

    /**
     * Test Non Trivial things
     * Tests if working starting in subfolder 
     */
    @Test
    public void testDepthfolderinfolder() {
        var i = 3;
        for (var node : node.getIterable(DepthTraversal.class)) {
            assertFalse("i must be less equal than 6", i > 6);
            assertEquals(depthorder[i++], node.getFile());
        }
    }
}