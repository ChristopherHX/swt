package org.iMage.treeTraversal;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * The main class of this project.
 *
 * @author Dominik Fuchss
 *
 */
public final class App {
  /**
   * Defines the option for the start directory (parameter: string).
   */
  private static final String DIRECTORY_OPT = "d";
  /**
   * Defines the option to use JPGs (no parameter).<br>
   * This is not compatible with {@value #PNG_OPT}.
   */
  private static final String JPG_OPT = "j";
  /**
   * Defines the option to use PNGs (no parameter).<br>
   * This is not compatible with {@value #JPG_OPT}.
   */
  private static final String PNG_OPT = "p";
  /**
   * Defines the option to use BreadthTraversal (no parameter).<br>
   * Iff not set use DepthTraversal.
   */
  private static final String BFS_OPT = "b";

  private App() {
    throw new IllegalAccessError();
  }

  /**
   * The main method of this project.
   *
   * @param args
   *          the command line arguments
   */

  public static void main(String[] args) {
    CommandLine cmd = null;
    try {
      cmd = App.doCommandLineParsing(args);
    } catch (ParseException e) {
      System.err.println("Wrong command line arguments given: " + e.getMessage());
      System.exit(1);
    }
    throw new UnsupportedOperationException("Implement me!");

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
    opt = new Option(App.DIRECTORY_OPT, "dir", true, "path to start directory");
    opt.setRequired(true);
    opt.setType(String.class);
    options.addOption(opt);

    opt = new Option(App.JPG_OPT, "jpg", false, "show jpgs");
    opt.setRequired(false);
    options.addOption(opt);

    opt = new Option(App.PNG_OPT, "png", false, "show pngs");
    opt.setRequired(false);
    options.addOption(opt);

    opt = new Option(App.BFS_OPT, "bfs", false, "use bfs");
    opt.setRequired(false);
    options.addOption(opt);

    CommandLineParser parser = new DefaultParser();
    return parser.parse(options, args);
  }

}
