package me.hyph.todoharry.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by herby on 08/06/16.
 */
public class Item implements Serializable {
    private final long id;
    private final String name;
    private final Date dueTo;

    public Item(final String name) {
        this(-1, name);
    }

    public Item(final long id, final String name) {
        this(id, name, new Date());
    }

    public Item(final long id, final String name, final Date dueTo) {
        this.id = id;
        this.name = name;
        this.dueTo = dueTo;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDueTo() {
        return dueTo;
    }
}
