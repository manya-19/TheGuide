package flint.durzo.theguide;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class InstructionActivity extends AppCompatActivity {
TextView txt;ImageButton b1;ImageButton btn;
    TextToSpeech t1;int c=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_instruction );
        txt=findViewById( R.id.textView );
        txt.setMovementMethod(new ScrollingMovementMethod());
        b1=findViewById( R.id.imageButton2);
        btn=findViewById( R.id.imageButton );
        final String toSpeak = txt.getText().toString();
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage( Locale.UK);
                }
            }
        });
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( InstructionActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        } );

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(c==0) {
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null,null);c++;
                }
                else {
                    onPause();c=0;
                }

            }
        });
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();

        }
        super.onPause();
    }


    }


