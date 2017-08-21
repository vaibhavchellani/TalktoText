package com.example.vaibhavchellani.talktotext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageButton micButton;
    private TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView=(TextView)findViewById(R.id.result);
        micButton =(ImageButton)findViewById(R.id.micButton);
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptspeechInput();
            }
        });

    }

    private void promptspeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say soemthing vaibhav");
        try {
            startActivityForResult(intent, 100);
        }
        catch(ActivityNotFoundException a)
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 100 : if(resultCode==RESULT_OK && data !=null)
            {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);;
                resultTextView.setText(result.get(0));
            }
            break;
        }


    }
}
