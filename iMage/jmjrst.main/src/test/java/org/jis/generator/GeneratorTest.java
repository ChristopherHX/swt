package org.jis.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
    
    /**
     * Tests rotating sample Image without rotate
     */
    @Test
    public void rotateImageTestImageNoRotate() {
        assertSame(image, generator.rotateImage(image, 0.0));
    }
    
    /**
     * Tests for Illegal Argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void rotateImageTestIllegalArgument() {
        generator.rotateImage(image, 0.42);
    }

    /**
     * Tests rotate 90°
     */
    @Test
    public void rotateImageTestRotate90() {
        var nimage = generator.rotateImage(image, Math.PI / 2);
        assertNotNull(nimage);
        assertNotSame(nimage, image);
        assertEquals(image.getHeight(), nimage.getWidth());
        assertEquals(image.getWidth(), nimage.getHeight());
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                assertEquals("False Image i=" + i + " j=" + j, image.getRGB(i, j),
                nimage.getRGB(nimage.getWidth() - 1 - j, i));
            }
        }
    }

    /**
     * Tests rotate 270
     */
    @Test
    public void rotateImageTestRotate270() {
        var nimage = generator.rotateImage(image, 3 * Math.PI / 2);
        assertNotNull(nimage);
        assertNotSame(nimage, image);
        assertEquals(image.getHeight(), nimage.getWidth());
        assertEquals(image.getWidth(), nimage.getHeight());
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                assertEquals("False Image i=" + i + " j=" + j, image.getRGB(i, j),
                nimage.getRGB(j, nimage.getHeight() - 1 - i));
            }
        }
    }

    /**
     * Tests rotate -270°
     */
    @Test
    public void rotateImageTestRotateM270() {
        var nimage = generator.rotateImage(image, -Math.PI / 2);
        assertNotNull(nimage);
        assertNotSame(nimage, image);
        assertEquals(image.getHeight(), nimage.getWidth());
        assertEquals(image.getWidth(), nimage.getHeight());
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                assertEquals("False Image i=" + i + " j=" + j, image.getRGB(i, j),
                nimage.getRGB(nimage.getWidth() - 1 - j, i));
            }
        }
    }
}