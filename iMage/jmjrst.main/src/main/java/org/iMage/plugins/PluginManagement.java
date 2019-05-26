package org.iMage.plugins;

import java.util.ServiceLoader;
import java.util.TreeSet;
import java.util.ServiceLoader.Provider;
import java.util.stream.Collectors;

/**
 * Knows all available plug-ins and is responsible for using the service loader API to detect them.
 *
 * @author Dominik Fuchss
 * @author Christopher Homberger
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
    var sl = ServiceLoader.load(PluginForJmjrst.class);
    return sl.stream().map(Provider::get).collect(Collectors.toCollection(TreeSet::new));
  }

}
