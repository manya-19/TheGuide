package flint.durzo.theguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VerifyEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        Intent intent = getIntent();
        final String email = intent.getStringExtra("email");

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText code = findViewById(R.id.code);
                String usercode = code.getText().toString();
                new VerifyEmail().execute(email, usercode);
            }
        });
    }

    class VerifyEmail extends AsyncTask<String, Void, Void> {
        ProgressDialog proc;
        String webPage = "";
        String baseUrl = "http://mobile.tornosindia.com/theguide/";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            proc = ProgressDialog.show(VerifyEmailActivity.this, "Please Wait", "Verifying...");
        }

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "verifyemail.php?email=" + strings[0] + "&code=" + strings[1];
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
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String data;
                while ((data = br.readLine()) != null)
                    webPage = webPage + data;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            proc.dismiss();
            super.onPostExecute(aVoid);
            if (!webPage.equals("success"))
                Toast.makeText(VerifyEmailActivity.this, "Code is incorrect. Please try again ", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(VerifyEmailActivity.this, "Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(VerifyEmailActivity.this, LoginActivity.class));
            }
        }
    }
}