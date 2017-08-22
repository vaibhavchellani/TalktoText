package com.example.vaibhavchellani.talktotext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageButton micButton;
    private TextView resultTextView;
    private TextToSpeech mTts;
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

        mTts= new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS)
                {
                    mTts.setLanguage(Locale.US);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "feature not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void textToTalk(String text) {
        mTts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    private void promptspeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something ");
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
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultTextView.setText(result.get(0));
                addtofirebase(result.get(0));
            }
            break;
        }


    }

    private void addtofirebase(String s) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("status");
        int result=findWords(s);
  //      Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        myRef.setValue(result);
    }

    private int findWords(String s) {


        if ( s.toLowerCase().indexOf("Lights".toLowerCase()) != -1 && s.toLowerCase().indexOf("off".toLowerCase()) != -1 ) {
            textToTalk(" Turning Lights Off Sir  ");
            return 1;
        }
        else if ( s.toLowerCase().indexOf("Romantic".toLowerCase()) != -1 && s.toLowerCase().indexOf("Lights".toLowerCase()) != -1 ){
            textToTalk(" Have Fun sir , WINK WINK  ");
            return 2;
        }
        else if ( s.toLowerCase().indexOf("Wine".toLowerCase()) != -1){
            textToTalk(" Bringing you Special Wine Sir , Relax  ");
            return 4;
        }
        else if ( s.toLowerCase().indexOf("Music".toLowerCase()) != -1){
            textToTalk("  ROGER THAT !!  ");
            return 3;
        }
        else if ( s.toLowerCase().indexOf("enough".toLowerCase()) != -1){
            textToTalk("  okay sir  ");
            return 0;
        }
        else if ( s.toLowerCase().indexOf("who".toLowerCase()) != -1 && s.toLowerCase().indexOf("best".toLowerCase()) != -1) {
            textToTalk("  WE ALL KNOW ITS , G D G   ");
            return 0;
        }
        else {
            textToTalk(" I could not get you can sir , can you repeat please !!");
            return 0;

        }
    }
}
