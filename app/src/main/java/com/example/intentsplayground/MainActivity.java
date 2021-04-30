package com.example.intentsplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.intentsplayground.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    int minValue,maxValue;
    int qty = 0;
    ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setUpEventHandlers();

        getInitialCount();
    }

    private void getInitialCount() {

        Bundle bundle = getIntent().getExtras();
        qty = bundle.getInt(Constants.INITIAL_COUNT_KEY,0);
        minValue = bundle.getInt(Constants.MIN_VALUE, Integer.MIN_VALUE);
        maxValue = bundle.getInt(Constants.MAX_VALUE, Integer.MAX_VALUE);
        b.textViewMA.setText(String.valueOf(qty));

        if(qty != 0){
            b.sendBackBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setUpEventHandlers() {

        b.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incQty();
            }
        });

        b.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decQty();
            }
        });
    }

    private void decQty() {
        qty--;
        b.textViewMA.setText(String.valueOf(qty));
    }

    private void incQty() {
        qty++;
        b.textViewMA.setText(String.valueOf(qty));
    }


    public void sendBack(View view) {

        if(qty >= minValue && qty <= maxValue){
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_COUNT, qty);
            setResult(RESULT_OK, intent);

            finish();
        }
        else {
            Toast.makeText(this, "Max range is 100", Toast.LENGTH_SHORT).show();
        }
    }
}