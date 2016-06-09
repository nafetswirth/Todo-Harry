package me.hyph.todoharry.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import me.hyph.todoharry.R;
import me.hyph.todoharry.enums.IntentKey;
import me.hyph.todoharry.models.Item;

public class EditItemActivity extends AppCompatActivity {
    //This must be either now or when task edit -> an old date
    private Date datePickerDate = new Date();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        final Intent intent = getIntent();
        final Item item = (Item) intent.getSerializableExtra(IntentKey.Item.toString());
        final TextView textView = (TextView) findViewById(R.id.editTextView);
        textView.setText(item.getName());
        final Button button = (Button) findViewById(R.id.saveBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSavePressed(v);
            }
        });

        prepareDatePickerForCurrentDate();
    }

    private void prepareDatePickerForCurrentDate() {
        final Calendar c = Calendar.getInstance();
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);

        final DatePicker datePicker = (DatePicker) findViewById(R.id.dpResult);
        datePicker.init(year, month, day, null);
    }

    public void onSavePressed(final View v) {
        final TextView textView = (TextView) findViewById(R.id.editTextView);
        final Item item = (Item) getIntent().getSerializableExtra(IntentKey.Item.toString());
        final String newItemName = textView.getText().toString();
        final Intent data = new Intent();
        final DatePicker datePicker = (DatePicker) findViewById(R.id.dpResult);
        Log.d("Date", "Year=" + datePicker.getYear() + " Month=" + (datePicker.getMonth() + 1) + " day=" + datePicker.getDayOfMonth());
        final Date newTaskDate = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()).getTime();
        Log.d("WAT", newTaskDate.toString());
        data.putExtra(IntentKey.Item.toString(), new Item(item.getId(), newItemName, newTaskDate));
        setResult(RESULT_OK, data);
        finish();
    }
}
