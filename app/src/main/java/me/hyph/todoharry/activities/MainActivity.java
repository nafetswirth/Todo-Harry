package me.hyph.todoharry.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.hyph.todoharry.R;
import me.hyph.todoharry.enums.IntentKey;
import me.hyph.todoharry.utils.TodoItemsFileHelper;

public class MainActivity extends AppCompatActivity {

    private final int EDIT_ACTIVITY_REQUEST_CODE = 1;

    private List<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private TodoItemsFileHelper fileHelper;

    private int lastEditedItemIndex;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        fileHelper = new TodoItemsFileHelper(getFilesDir(), "todo.txt");
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                items
        );
        lvItems.setAdapter(itemsAdapter);
        setUpListViewListener();
    }

    public void onAddItem(final View v) {
        final EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        final String text = etNewItem.getText().toString();
        itemsAdapter.add(text);
        etNewItem.setText("");
        writeItems();
    }

    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                lastEditedItemIndex = position;
                final Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                final String item = items.get(position);
                intent.putExtra(IntentKey.ItemName.toString(), item);
                startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void writeItems() {
        try {
            fileHelper.writeItems(items);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void readItems() {
        try {
            items = fileHelper.readItems();
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

        final String newTaskName = data.getExtras().getString(IntentKey.ItemName.toString());
        items.set(lastEditedItemIndex, newTaskName);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }
}
