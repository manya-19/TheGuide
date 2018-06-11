package flint.durzo.theguide;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    String TAG = "Abhinav";

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.ENGLISH);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.d(TAG, "This Language is not supported");
                    }
                }
                else
                    Log.d(TAG, "Initilization Failed!");
            }
        });

        Button speak = findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertTextToSpeech();
            }
        });
    }

    @Override
    protected void onPause() {
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    private void convertTextToSpeech() {
        tts.setPitch(0.9f);

        String text = "This is my voice. How does it sound?";
        if(!text.isEmpty())
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        else{
            Log.d(TAG, "Content not available");
        }
    }
}