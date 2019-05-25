package org.iMage.plugins;

/**
 * Knows all available plug-ins and is responsible for using the service loader API to detect them.
 *
 * @author Dominik Fuchss
 */
public final class PluginManagement {

  /**
   * No constructor for utility class.
   */
  private PluginManagement() {
    throw new IllegalAccessError();
  }

  /**
   * Return an {@link Iterable} Object with all available {@link PluginForJmjrst PluginForJmjrsts}
   * sorted according to the length of their class names (shortest first).
   *
   * @return an {@link Iterable} Object containing all available plug-ins alphabetically sorted
   * according to their class names.
   */
  public static Iterable<PluginForJmjrst> getPlugins() {
    //TODO: implement me!
    return null;
  }

}
