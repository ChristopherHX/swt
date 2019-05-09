package org.iMage.HDrize;

import org.apache.commons.cli.*;

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

  public static void main(String[] args) {
    // Don't touch...
    CommandLine cmd = null;
    try {
      cmd = App.doCommandLineParsing(args);
    } catch (ParseException e) {
      System.err.println("Wrong command line arguments given: " + e.getMessage());
      System.exit(1);
    }
    // ...this!

    /**
     * Implement me! Remove exception when done!
     *
     * HINT: You have to convert the files from the image folder to InputStreams and then create
     * Objects of class org.iMage.HDrize.base.images.EnhancedImage before you can use HDrize.
     */
    try {
      throw new IllegalAccessException("Implement me first!");
    } catch (IllegalAccessException e) {
      System.err.println(e.getMessage());
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
    opt = new Option( //
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