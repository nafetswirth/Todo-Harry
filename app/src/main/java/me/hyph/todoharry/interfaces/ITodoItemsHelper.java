package me.hyph.todoharry.interfaces;

import java.io.IOException;
import java.util.List;

/**
 * Created by herby on 07/06/16.
 */
public interface ITodoItemsHelper {
    List<String> readItems() throws IOException;
    void writeItems(final List<String> items) throws IOException;
}
