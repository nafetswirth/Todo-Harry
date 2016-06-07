package me.hyph.todoharry.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.hyph.todoharry.R;
import me.hyph.todoharry.enums.IntentKey;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        final Intent intent = getIntent();
        final String itemName = intent.getStringExtra(IntentKey.ItemName.toString());
        final TextView textView = (TextView) findViewById(R.id.editTextView);
        textView.setText(itemName);
        final Button button = (Button) findViewById(R.id.saveBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSavePressed(v);
            }
        });
    }

    public void onSavePressed(final View v) {
        final TextView textView = (TextView) findViewById(R.id.editTextView);
        final String newItemName = textView.getText().toString();
        final Intent data = new Intent();
        data.putExtra(IntentKey.ItemName.toString(), newItemName);
        setResult(RESULT_OK, data);
        finish();
    }
}
