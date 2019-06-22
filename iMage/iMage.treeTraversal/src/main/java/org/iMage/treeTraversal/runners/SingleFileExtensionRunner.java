package org.iMage.treeTraversal.runners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * SingleFileExtensionRunner filter exactly one fileextension
 */
public class SingleFileExtensionRunner extends Runner {
    private String extension;

    /**
     * Creates an Runner with prints files with only specified extensions
     * @param extension extension to allow
     */
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