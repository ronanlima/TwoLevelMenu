package br.com.ronanlima.twolevelmenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    private TextView tvItemSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tvItemSelected = findViewById(R.id.tv_item_selected);
        tvItemSelected.setText(getIntent().getStringExtra("item_selected"));
    }
}
