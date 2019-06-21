package org.iMage.treeTraversal.runners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * PNGRunner
 */
public class SingleFileExtensionRunner extends Runner {
    private String extension;

    public SingleFileExtensionRunner(String extension) {
        this.extension = extension;
    }

    @Override
    protected List<File> selectFiles(List<File> files) {
        var list = new ArrayList<File>();
        for (var file : files) {
            if (file.getName().endsWith(extension)) {
                list.add(file);
            }
        }
        return list;
    }  
}