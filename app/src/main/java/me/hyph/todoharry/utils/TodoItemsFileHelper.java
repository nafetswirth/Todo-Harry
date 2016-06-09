package me.hyph.todoharry.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.hyph.todoharry.interfaces.ITodoItemsHelper;
import me.hyph.todoharry.models.Item;

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

    @Override
    public List<Item> readItems() throws IOException {
        final File todoFile = new File(filesDir, fileName);
        final List<Item> items = new ArrayList<>();
        final List<String> itemsInFile = FileUtils.readLines(todoFile);
        for (int i = 0, n = itemsInFile.size(); i < n; i++) {
            items.add(new Item(i, itemsInFile.get(i)));
        }
        return items;
    }

    @Override
    public List<Item> writeItems(final List<Item> items) throws IOException {
        final File todoFile = new File(filesDir, fileName);
        final List<String> itemsToSave = new ArrayList<>();
        for (final Item item : items) {
            itemsToSave.add(item.getName());
        }
        FileUtils.writeLines(todoFile, itemsToSave);
        return items;
    }

    @Override
    public void deleteItem(final Item item) throws IOException {
        final List<Item> items = readItems();
        final List<Item> itemsWithoutItemToDelete = new ArrayList<>();
        for(final Item persistedItem: items) {
            if(item.getId() == persistedItem.getId()) {
                continue;
            }
            itemsWithoutItemToDelete.add(persistedItem);
        }
        writeItems(itemsWithoutItemToDelete);
    }
}
