package org.iMage.treeTraversal.runners;

import java.io.File;
import java.util.List;

import org.iMage.treeTraversal.model.Tree;
import org.iMage.treeTraversal.traverser.Traversal;

/**
 * A runner extracts files by using {@link Tree Trees}.
 */
public abstract class Runner {
  /**
   * Execute the runner.
   *
   * @param startFolder
   *          the start folder
   * @param traversalClass
   *          the traversal class
   */
  public final void run(File startFolder, Class<? extends Traversal> traversalClass) {
    Tree tree = this.buildFolderStructure(startFolder);
    List<File> files = this.getFiles(tree, traversalClass);
    List<File> selectedFiles = this.selectFiles(files);
    this.printResults(selectedFiles);
  }

  /**
   * Builds a {@link Tree} starting with a start folder.
   *
   * @param startFolder
   *          the start folder
   * @return the {@link Tree}
   */
  private Tree buildFolderStructure(File startFolder) {
	  throw new UnsupportedOperationException("Implement me!");
  }

  /**
   * Get a list of all files using the iterator defined by {@link Traversal}.
   *
   * @param tree
   *          the tree
   * @param traversalClass
   *          the traversal
   * @return the list of traversed files (in order of traversal)
   */
  private List<File> getFiles(Tree tree, Class<? extends Traversal> traversalClass) {
	  throw new UnsupportedOperationException("Implement me!");  
  }

  /**
   * Filter files according to strategy.
   *
   * @param files
   *          the files
   * @return the filtered list of files
   */
  protected abstract List<File> selectFiles(List<File> files);

  /**
   * Print a list of files to stdout.
   * 
   * @param selectedFiles
   *          the list of files
   */
  private void printResults(List<File> selectedFiles) {
	  throw new UnsupportedOperationException("Implement me!");
  }

}
