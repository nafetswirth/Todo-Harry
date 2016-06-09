package me.hyph.todoharry.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.hyph.todoharry.R;
import me.hyph.todoharry.adapters.ItemsAdapter;
import me.hyph.todoharry.enums.IntentKey;
import me.hyph.todoharry.interfaces.ITodoItemsHelper;
import me.hyph.todoharry.models.Item;
import me.hyph.todoharry.utils.TodoItemsSQLiteHelper;

public class MainActivity extends AppCompatActivity {

    private final int EDIT_ACTIVITY_REQUEST_CODE = 1;

    private List<Item> items;
    private ArrayAdapter<Item> itemsAdapter;
    private ListView lvItems;
    private ITodoItemsHelper datasourceHelper;

    private int lastEditedItemIndex;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        datasourceHelper = TodoItemsSQLiteHelper.getInstance(this);
        readItems();
        itemsAdapter = new ItemsAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setUpListViewListener();
    }

    public void onAddItem(final View v) {
        final EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        final String text = etNewItem.getText().toString();
        final Item savedItem = writeItem(new Item(text));
        itemsAdapter.add(savedItem);
        etNewItem.setText("");
    }

    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final Item removedItem = items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                deleteItem(removedItem);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                lastEditedItemIndex = position;
                final Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                final Item item = items.get(position);
                intent.putExtra(IntentKey.Item.toString(), item);
                startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private Item writeItem(final Item item) {
        try {
            //this is an insert
            final List<Item> items = new ArrayList<>();
            items.add(item);
            return datasourceHelper.writeItems(items).get(0);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void readItems() {
        try {
            items = datasourceHelper.readItems();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteItem(final Item item) {
        try {
            datasourceHelper.deleteItem(item);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK || requestCode != EDIT_ACTIVITY_REQUEST_CODE) {
            return;
        }

        final Item newItem = (Item) data.getExtras().getSerializable(IntentKey.Item.toString());
        Log.d("Before Save", newItem.getDueTo().toString());
        final Item savedItem = writeItem(newItem);
        items.set(lastEditedItemIndex, savedItem);
        Log.d("Before Save", newItem.getDueTo().toString());
        itemsAdapter.notifyDataSetChanged();
    }
}