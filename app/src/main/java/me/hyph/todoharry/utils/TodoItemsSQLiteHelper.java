package me.hyph.todoharry.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.hyph.todoharry.interfaces.ITodoItemsHelper;
import me.hyph.todoharry.models.Item;

/**
 * Created by herby on 08/06/16.
 */
public class TodoItemsSQLiteHelper extends SQLiteOpenHelper implements ITodoItemsHelper {

    private static TodoItemsSQLiteHelper instance;

    private static final String DB_NAME = "todoItemsDb";
    private static final int DB_VERSION = 3;

    private static final String TABLE_ITEMS = "items";

    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_ITEM_DUE_DATE = "due_to";

    public static synchronized TodoItemsSQLiteHelper getInstance(final Context context) {
        if(instance == null) {
            instance = new TodoItemsSQLiteHelper(context.getApplicationContext());
        }
        return instance;
    }

    private static final int ID_COLUMN_INDEX = 0;
    private static final int NAME_COLUMN_INDEX = 1;
    private static final int DUE_DATE_COLUMN_INDEX = 2;

    private TodoItemsSQLiteHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(final SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        final String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                    KEY_ITEM_ID + " INTEGER PRIMARY KEY," +
                    KEY_ITEM_NAME + " TEXT," +
                    KEY_ITEM_DUE_DATE + " INTEGER" +
                ")";
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    @Override
    public List<Item> readItems() throws IOException {
        final String selectItems = String.format("SELECT %s from %s", "*", TABLE_ITEMS);
        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectItems, null);
        try {
            //go through lines and return items
            if(!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            final List<Item> items = new ArrayList<>();
            while(!cursor.isAfterLast()) {
                final int id = cursor.getInt(ID_COLUMN_INDEX);
                final String name = cursor.getString(NAME_COLUMN_INDEX);
                final long date = cursor.getLong(DUE_DATE_COLUMN_INDEX);

                items.add(new Item(id, name, new Date(date)));
                cursor.moveToNext();
            }
            return items;
        } finally {
            if(cursor !=  null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    @Override
    public List<Item> writeItems(final List<Item> items) throws IOException {
        final SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            final List<Item> savedItems = new ArrayList<>();
            for(final Item item: items) {
                final long itemId = insertOrUpdate(db, item);
                savedItems.add(new Item(itemId, item.getName(), item.getDueTo()));
            }
            db.setTransactionSuccessful();
            return savedItems;
        } catch (final SQLException e) {
            e.printStackTrace();
            throw new IOException("Error writing items to the database");
        } finally {
            db.endTransaction();
        }
    }

    private long insertOrUpdate(final SQLiteDatabase db, final Item item) {
        final String selectItem = String.format("SELECT %s from %s where %s = ?",
                "*",
                TABLE_ITEMS,
                KEY_ITEM_ID
        );

        final String[] idQuery = new String[]{String.valueOf(item.getId())};
        final Cursor cursor = db.rawQuery(selectItem, idQuery);
        int newItemId = -1;

        try {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_ITEM_NAME, item.getName());
            contentValues.put(KEY_ITEM_DUE_DATE, item.getDueTo().getTime());
            if(!cursor.moveToFirst()) {
                return db.insertOrThrow(TABLE_ITEMS, null, contentValues);
            }
            db.update(TABLE_ITEMS, contentValues, KEY_ITEM_ID + "= ?", idQuery);
        } finally {
            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return newItemId;
    }

    @Override
    public void deleteItem(final Item item) throws IOException {
        final SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_ITEMS, KEY_ITEM_ID + "= ?", new String[]{String.valueOf(item.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
