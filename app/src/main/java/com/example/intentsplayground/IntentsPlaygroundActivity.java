package com.example.intentsplayground;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intentsplayground.databinding.ActivityIntentsPlaygroundBinding;

public class IntentsPlaygroundActivity extends AppCompatActivity {

    private static final int REQUEST_COUNT = 0;
    ActivityIntentsPlaygroundBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityIntentsPlaygroundBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Intents Playground");

        setupHideErrorForEditText();
    }

    private void setupHideErrorForEditText() {

        TextWatcher myTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        b.TIL1.getEditText().addTextChangedListener(myTextWatcher);
        b.TIL2.getEditText().addTextChangedListener(myTextWatcher);
    }

    /**
     * Event Handlers
     * @param view
     */

    public void openMainActivity(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void sendImplicitIntent(View view) {

        String input = b.TIL1.getEditText().getText().toString().trim();

        if(input.isEmpty()){
            b.TIL1.setError("Please enter something");
            return;
        }

        int type = b.ImplicitRadioBtnGrp.getCheckedRadioButtonId();
        
        if(type == R.id.openWebPageRBTn){
            openWebPage(input);
        }
        else if(type == R.id.dialNoRadioBTn){
            dialNumber(input);
        }
        else if(type == R.id.shareTextRadioBTn){
            shareText(input);
        }
        else Toast.makeText(this,"Choose type",Toast.LENGTH_LONG).show();

    }

    public void sendData(View view) {
        String input = b.TIL2.getEditText().getText().toString().trim();

        if(input.isEmpty()){
            b.TIL2.setError("Please enter something");
            return;
        }

        int initialCounter = Integer.parseInt(input);
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.INITIAL_COUNT_KEY,initialCounter);
        bundle.putInt(Constants.MIN_VALUE,-100);
        bundle.putInt(Constants.MAX_VALUE,100);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_COUNT);
        b.TIL2.setError(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_COUNT && resultCode == RESULT_OK){
            int count = data.getIntExtra(Constants.FINAL_COUNT, Integer.MIN_VALUE);
            b.result.setText("Final Count received is : " + count);
            b.result.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Implicit Intent Functions
     * @param input
     */

    private void shareText(String input) {
        Intent intent = new Intent(); intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, input);
        startActivity(Intent.createChooser(intent, "Share text using"));
    }

    private void dialNumber(String input) {

        if(!input.matches("^\\d{10}$")){
            b.TIL1.setError("Please enter a 10 digit number");
            return;
        }

        Uri uri = Uri.parse("tel:" +  input);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
        hideError();
    }

    private void openWebPage(String url) {

        if(!url.matches("[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")){
            b.TIL1.setError("Invalid URL");
            return;
        }

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        hideError();
    }


    /**
     * Utils
     */

    private void hideError(){
        b.TIL1.setError(null);
    }


}