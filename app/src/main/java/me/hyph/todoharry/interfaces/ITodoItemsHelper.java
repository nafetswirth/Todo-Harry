package me.hyph.todoharry.interfaces;

import java.io.IOException;
import java.util.List;

import me.hyph.todoharry.models.Item;

/**
 * Created by herby on 07/06/16.
 */
public interface ITodoItemsHelper {
    List<Item> readItems() throws IOException;
    List<Item> writeItems(final List<Item> items) throws IOException;
    void deleteItem(final Item item) throws IOException;
}
