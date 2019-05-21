package org.iMage.HDrize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.imaging.ImageReadException;
import org.iMage.HDrize.base.images.EnhancedImage;
import org.iMage.HDrize.matrix.MatrixCalculator;

/**
 * This class parses all command line parameters and creates an HDRImage.
 *
 */
public final class App {
  private App() {
    throw new IllegalAccessError();
  }

  private static final String CMD_OPTION_INPUT_IMAGES = "i";
  private static final String CMD_OPTION_OUTPUT_IMAGE = "o";
  private static final String CMD_OPTION_LAMBDA = "l";
  private static final String CMD_OPTION_SAMPLES = "s";

  private static boolean minPrefix(File[] strings, int min) {
      if (strings.length == 0 || strings[0].getName().length() <= min) {
          return false;
      }
      for (int prefixLen = 0; prefixLen < min; prefixLen++) {
          char c = strings[0].getName().charAt(prefixLen);
          for (int i = 1; i < strings.length; i++) {
              if (prefixLen >= strings[i].getName().length()
                  || strings[i].getName().charAt(prefixLen) != c) {
                  return  false;
              }
          }
      }
      return true;
  }

  /**
   * Handle commandline option and execute them
   * @param args commandline option as String array
   * @throws IOException
   * @throws FileNotFoundException
   * @throws ImageReadException
   */
  public static void main(String[] args) {
    // Don't touch...
    CommandLine cmd = null;
    try {
      cmd = App.doCommandLineParsing(args);
    } catch (ParseException e) {
      System.err.println("Wrong command line arguments given: " + e.getMessage());
      System.exit(1);
    }

    try {
      var imagess = cmd.getOptionValue(App.CMD_OPTION_INPUT_IMAGES);
      var oimages = cmd.getOptionValue(App.CMD_OPTION_OUTPUT_IMAGE);
      var lambdas = cmd.getOptionValue(App.CMD_OPTION_LAMBDA);
      double lambda = 30;
      if (lambdas != null) {
        lambda = Double.parseDouble(lambdas);
        if (lambda < 1 && lambda > 100) {
          System.err.println("lambda out of range");
          System.exit(1);
        }
      }
      var sampless = cmd.getOptionValue(App.CMD_OPTION_SAMPLES);
      short samples = 142;
      if (sampless != null) {
        samples = Short.parseShort(lambdas);
        if (samples < 1 && samples > 1000) {
          System.err.println("samples out of range");
          System.exit(1);
        }
      }
      var files = new File(imagess).listFiles(new FilenameFilter() {
      
        @Override
        public boolean accept(File dir, String name) {
          return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".tiff");
        }
      });
      if ((files.length % 2) == 0) {
        System.err.println("Invalid number of Input Images must be odd");
        System.exit(1);
      }
      if (!minPrefix(files, 3)) {
        System.err.println("To short common Input Images prefix");
         System.exit(1);
      }
      var images = new EnhancedImage[files.length];
      for (int i = 0; i < files.length; i++) {
        images[i] = new EnhancedImage(new FileInputStream(files[i]));
      }
      ImageIO.write(new HDrize().createRGB(images, samples, lambda, new MatrixCalculator()), "png", new File(oimages));
    } catch (ImageReadException e) {
      System.err.println("Invalid image Input");
      System.exit(1);
    } catch (FileNotFoundException e) {
      System.err.println("Invalid image Input");
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Invalid image Input");
      System.exit(1);
    }
  }

  /**
   * Parse and check command line arguments
   *
   * @param args
   *          command line arguments given by the user
   * @return CommandLine object encapsulating all options
   * @throws ParseException
   *           if wrong command line parameters or arguments are given
   */
  private static CommandLine doCommandLineParsing(String[] args) throws ParseException {
    Options options = new Options();
    Option opt;

    /*
     * Define command line options and arguments
     */
    opt = new Option(//
        App.CMD_OPTION_INPUT_IMAGES, "input-images", true, "path to folder with input images");
    opt.setRequired(true);
    opt.setType(String.class);
    options.addOption(opt);

    opt = new Option(App.CMD_OPTION_OUTPUT_IMAGE, "image-output", true, "path to output image");
    opt.setRequired(true);
    opt.setType(String.class);
    options.addOption(opt);

    opt = new Option(App.CMD_OPTION_LAMBDA, "lambda", true, "the lambda value of algorithm");
    opt.setRequired(false);
    opt.setType(Double.class);
    options.addOption(opt);

    opt = new Option(App.CMD_OPTION_SAMPLES, "samples", true, "the number of samples");
    opt.setRequired(false);
    opt.setType(Integer.class);
    options.addOption(opt);

    CommandLineParser parser = new DefaultParser();
    return parser.parse(options, args);
  }
}