package flint.durzo.theguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    String TAG = "Abhinav";

    //TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailET = findViewById(R.id.email);
        final EditText passET = findViewById(R.id.password);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passET.getText().toString();
                boolean check = true;
                int emid=email.indexOf( '@' );
                int dotid=email.lastIndexOf('.');
                if(emid==-1 || dotid==-1) check =false;
                if(dotid-emid<1) check =false;
                if(dotid==email.length()-1) check =false;
                if(!check)
                    Toast.makeText( LoginActivity.this, "Invalid Email ", Toast.LENGTH_SHORT ).show();
                else
                {
                    new Login().execute(email, password);
                }
            }
        });

        TextView reg = findViewById(R.id.register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        /*tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {

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
        });*/
/*
        Button speak = findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertTextToSpeech();
            }
        });*/
    }
/*
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
    }*/
    class Login extends AsyncTask<String,Void,Void> {

        ProgressDialog proc;
        String webPage="";
        String baseUrl="http://mobile.tornosindia.com/theguide/";
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            proc=ProgressDialog.show(LoginActivity.this,"Please Wait","Logging in...");
        }

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                String myURL = baseUrl+"login.php?user="+strings[0]+"&pass="+strings[1];
                myURL = myURL.replaceAll(" ", "%20");
                myURL = myURL.replaceAll("\'", "%27");
                myURL = myURL.replaceAll("\'", "%22");
                myURL = myURL.replaceAll("\\(", "%28");
                myURL = myURL.replaceAll("\\)", "%29");
                myURL = myURL.replaceAll("\\{", "%7B");
                myURL = myURL.replaceAll("\\}", "%7B");
                myURL = myURL.replaceAll("\\]", "%22");
                myURL = myURL.replaceAll("\\[", "%22");
                url = new URL(myURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String data;
                while ((data=br.readLine()) != null)
                    webPage=webPage+data;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            proc.dismiss();
            super.onPostExecute( aVoid );
            if(!webPage.equals( "success" ))
                Toast.makeText( LoginActivity.this, "Email/password is incorrect", Toast.LENGTH_SHORT ).show();
            else
            {
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        }
    }
}