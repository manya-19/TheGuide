package flint.durzo.theguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    boolean savelogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailET = findViewById(R.id.email);
        final EditText passET = findViewById(R.id.password);
        final CheckBox rememberpasswordbox = findViewById(R.id.checkBox);

        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
        savelogin = loginPreferences.getBoolean("savelogin", false);
        if(savelogin)
        {
            emailET.setText(loginPreferences.getString("id",""));
            passET.setText(loginPreferences.getString("password",""));
            rememberpasswordbox.setChecked(true);
        }

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
                    if (rememberpasswordbox.isChecked()) {
                        loginPrefsEditor.putBoolean("savelogin", true);
                        loginPrefsEditor.putString("id", email);
                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.apply();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }
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
    }

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