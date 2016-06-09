package me.hyph.todoharry.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import me.hyph.todoharry.R;
import me.hyph.todoharry.models.Item;

/**
 * Created by herby on 08/06/16.
 */
public class ItemsAdapter extends ArrayAdapter<Item> {

    public ItemsAdapter(final Context context, final List<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final Item item = getItem(position);
        View view = convertView;
        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        //setting up all the views
        final TextView todoNameTextView = (TextView) view.findViewById(R.id.todoName);
        todoNameTextView.setText(item.getName());
        final TextView todoDueDateTextView = (TextView) view.findViewById(R.id.todoDueTo);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
        final String formattedDate = dateFormat.format(item.getDueTo());
        todoDueDateTextView.setText(formattedDate);

        return view;
    }
}
