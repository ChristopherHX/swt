package org.jis.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * GeneratorTest Test the Generator for working correctly
 */
public class GeneratorTest {
    private static Generator generator;
    private static File imagefile;
    private static BufferedImage image;

    private BufferedImage nimage = null;

    /**
     * Prepares Generator and BufferedImage objects for all Tests
     * @throws IOException image.png not found
     */
    @BeforeClass
    public static void setUp() throws IOException {
        var dir = new File("target/test");
        if (!dir.exists()) {
            dir.mkdir();
        }
        generator = new Generator(null, 0);
        imagefile = new File(GeneratorTest.class.getResource("/image.jpg").getFile());
        image = ImageIO.read(imagefile);
    }

    /**
     * Saves all rotated Images
     * @throws IOException rotated image couldn't be saved
     */
    @After
    public void tearDown()  throws IOException  {
        if (nimage != null) {
            ImageIO.write(nimage, "jpg", new File(String.format("target/test/image_rotated_%s.jpg", 
            new SimpleDateFormat("MM-dd_HH.mm.ss.SSS").format(new Date()))));
        }
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
        nimage = generator.rotateImage(image, Math.PI / 2);
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
        nimage = generator.rotateImage(image, 3 * Math.PI / 2);
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
        nimage = generator.rotateImage(image, -3 * Math.PI / 2);
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
     * Tests rotate -90°
     */
    @Test
    public void rotateImageTestRotateM90() {
        nimage = generator.rotateImage(image, -Math.PI / 2);
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
     * Tests racing conditions of generateText
     * from null
     */
    @Test(expected = NullPointerException.class)
    public void generateTextTestfromIsNull() {
        generator.generateText(null,
        new File("target/test"), 0, 0);
    }

    /**
     * Tests racing conditions of generateText
     * to null
     */
    @Test(expected = NullPointerException.class)
    public void generateTextTesttoIsNull() {
        generator.generateText(
        new File("target/test"), null, 0, 0);
    }

    /**
     * Tests resizing of generateText image
     * @throws IOException output image couldn't be written / read / no outputfile
     * Ignored for now missing Outputfile
     */
    @Test
    @Ignore("Need Main Object!")
    public void generateTextTestResize() throws IOException {
        var to = new File("target/test/image_resized_" + System.currentTimeMillis() + ".jpg");
        generator.generateText(imagefile, to, image.getWidth() / 2, image.getHeight() / 2);
        nimage = ImageIO.read(to); 
        assertEquals("mismatched width", image.getWidth() / 2, nimage.getWidth());
        assertEquals("mismatched height", image.getHeight() / 2, nimage.getHeight());
    }

    /**
     * Tests resizing of generateText image
     * @throws IOException output image couldn't be written / read / no outputfile
     */
    @Test
    public void generateImageTestResize() throws IOException {
        var to = new File("target/test/image_resized_" + System.currentTimeMillis() + ".jpg");
        generator.generateImage(imagefile, to, false, image.getWidth() / 2, image.getHeight() / 2, "");
        nimage = ImageIO.read(to);
        assertEquals("mismatched width", image.getWidth() / 2, nimage.getWidth());
        assertEquals("mismatched height", image.getHeight() / 2, nimage.getHeight());
    }

    /**
     * Tests creating zips
     * Test will Fail on all Systems with / as default Fileseperator (*nix) see Generator.java:113!!!
     * @throws IOException output zip couldn't be written / read / no outputfile
     */
    @Test
    public void createZipWithAbsolutePathTest() throws IOException {
        var zipfile = new File("target/test/test_zip" + System.currentTimeMillis() + ".zip");
        var files = new Vector<File>();
        var imfile = new File("target/test/test_image" + System.currentTimeMillis() + ".jpg");
        Files.copy(imagefile.toPath(), imfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        files.add(imfile);
        generator.createZip(zipfile, files);
        var izip = new ZipInputStream(new FileInputStream(zipfile));
        var entry = izip.getNextEntry();
        assertEquals(imfile.getName(), entry.getName());
        nimage = ImageIO.read(izip);
        assertEquals(image.getWidth(), nimage.getWidth());
        assertEquals(image.getHeight(), nimage.getHeight());
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                assertEquals("False Image i=" + i + " j=" + j, image.getRGB(i, j),
                nimage.getRGB(i, j));
            }
        }
        izip.closeEntry();
        izip.close();
    }

    /**
     * Tests rotate 90°
     * @throws IOException failed to copy / modify temp file
     * @throws NullPointerException Implementation Error handling not working without Main class
     */
    @Test
    @Ignore("1. Not a double as Parameter"
            + "2. Killswitch at Generator.java:690 have to throw an Error 90 radians isn't a valid rotation")
    public void rotateImageTestRotateFile90() throws IOException, NullPointerException {
        var imfile = new File("target/test/test_image" + System.currentTimeMillis() + ".zip");
        Files.copy(imagefile.toPath(), imfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        // redundant int cast, because deprecated Function signature
        generator.rotate(imfile, (int)(Math.PI / 2));
        nimage = ImageIO.read(imfile);
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