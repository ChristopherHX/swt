package org.jis.generator;

import static org.junit.Assert.assertNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * GeneratorTest Test the Generator for working correctly
 */
public class GeneratorTest {
    private static Generator generator;
    private static BufferedImage image;

    /**
     * Prepares Generator and BufferedImage objects for all Tests
     * @throws IOException image.png not found
     */
    @BeforeClass
    public static void setUp() throws IOException {
        generator = new Generator(null, 0);
        image = ImageIO.read(new File(GeneratorTest.class.getResource("/image.jpg").getFile()));
    }

    /**
     * Tests rotating null Image without rotate
     */
    @Test
    public void rotateImageTestImageIsNullNoRotate() {
        assertNull(generator.rotateImage(null, 0.0));
    }
}