package flint.durzo.theguide;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class PlayerActivity extends AppCompatActivity {
    TextToSpeech tts;
    File destinationFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_player );
        tts = new TextToSpeech( this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage( Locale.ENGLISH );
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(PlayerActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    }
                    new FetchTextToSpeak().execute("1");
                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {;
                        }
                        @Override
                        public void onDone(String utteranceId) {
                        }
                        @Override
                        public void onError(String utteranceId) {
                            Toast.makeText(PlayerActivity.this, "Some Error Occurred in TTS File", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else
                    Toast.makeText(PlayerActivity.this, "Speech Initialisation Failed", Toast.LENGTH_SHORT).show();
            }
        } );
        ImageButton speak = findViewById( R.id.speak );
        speak.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //convertTextToSpeech("1", "Sample Title", "Hello");
            }
        } );
    }

    @Override
    protected void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    private void convertTextToSpeech(String type, String title, String text) {
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, title);

        File location = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        destinationFile = new File(location,title+".wav");

        Log.d("Abhinav", destinationFile.getAbsolutePath());

        if (type.equals("Instruction"))
            tts.setPitch(0.75f);
        else
            tts.setPitch(1.25f);

        int sr = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            sr = tts.synthesizeToFile(text, params, destinationFile, title);
        }
        Log.d("Abhinav", "synthesize returns = " + sr);
    }

    class FetchTextToSpeak extends AsyncTask<String, Void, Void> {
        String webPage = "", baseUrl = "http://mobile.tornosindia.com/theguide/";
        //ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(PlayerActivity.this, "Please Wait", "Fetching Data...");
        }

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                String myURL = baseUrl+"fetchinfobymonument.php?id="+strings[0];
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
            super.onPostExecute(aVoid);
            //progressDialog.dismiss();
            if (webPage.isEmpty())
                Toast.makeText(PlayerActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
            else
            {
                while (webPage.contains("<br>")){
                    int brI = webPage.indexOf("<br>");
                    String type = webPage.substring(0, brI);
                    webPage = webPage.substring(brI+4);
                    brI = webPage.indexOf("<br>");
                    String title = webPage.substring(0, brI);
                    webPage = webPage.substring(brI+4);
                    brI = webPage.indexOf("<br>");
                    String desc = webPage.substring(0, brI);
                    webPage = webPage.substring(brI+4);

                    convertTextToSpeech(type, title, desc);
                }
            }
        }
    }
}