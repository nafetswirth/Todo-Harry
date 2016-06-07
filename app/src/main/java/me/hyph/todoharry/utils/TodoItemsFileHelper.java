package me.hyph.todoharry.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.hyph.todoharry.interfaces.ITodoItemsHelper;

/**
 * Created by Stefan Wirth on 07/06/16.
 * Encapsulates the file handling
 */
public class TodoItemsFileHelper implements ITodoItemsHelper {
    private final File filesDir;
    private final String fileName;

    public TodoItemsFileHelper(final File filesDir, final String fileName) {
        this.filesDir = filesDir;
        this.fileName = fileName;
    }

    public List<String> readItems() throws IOException {
        final File todoFile = new File(filesDir, fileName);
        return new ArrayList<>(FileUtils.readLines(todoFile));
    }

    public void writeItems(final List<String> items) throws IOException {
        final File todoFile = new File(filesDir, fileName);
        FileUtils.writeLines(todoFile, items);
    }
}
