package flint.durzo.theguide;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, number, dob, address, country, password, password2;
    Spinner gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);
        dob = findViewById(R.id.dob);
        gender = findViewById(R.id.gender);
        address = findViewById(R.id.address);
        country = findViewById(R.id.country);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formIsValid()){
                    new RegisterUser().execute(name.getText().toString(), number.getText().toString(),
                            password.getText().toString(), email.getText().toString(), address.getText().toString(),
                            country.getText().toString(), gender.getSelectedItem().toString(), dob.getText().toString());
                }
            }
        });
    }

    private boolean checkNumber(EditText number){
        String num = number.getText().toString();
        if (num.length()!=10){
            Toast.makeText(this, "Enter a 10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!num.matches("[0-9]+")) {
            Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkEmail(EditText emailET){
        String email = emailET.getText().toString();
        boolean check = true;
        int emid=email.indexOf( '@' );
        int dotid=email.lastIndexOf('.');
        if(emid==-1 || dotid==-1) check =false;
        if(dotid-emid<1) check =false;
        if(dotid==email.length()-1) check =false;
        return check;
    }

    private boolean formIsValid() {
        if (name.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkEmail(email))
        {
            Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.getText().toString().trim().isEmpty() || password2.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.getText().toString().equals(password2.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (number.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Number cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidDate())
        {
            Toast.makeText(this, "Invalid Date. Please use DD/MM/YYYY format", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (address.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (country.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Country cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return checkNumber(number);
    }

    public boolean isValidDate() {
        String date = dob.getText().toString();
        try
        {
            String day = date.substring(0,2);
            if (day.length()!=2)
                throw new Exception();
            String month = date.substring(3,5);
            if (month.length()!=2)
                throw new Exception();
            String year = date.substring(6);
            if (year.length()!=4)
                throw new Exception();
            int dd = Integer.parseInt(day);
            if (dd < 1 || dd > 31)
                throw new Exception();
            int mm = Integer.parseInt(month);
            if (mm < 1 || mm > 12)
                throw new Exception();
            int yyyy = Integer.parseInt(year);
            if (yyyy < 1950 || yyyy > 2100)
                throw new Exception();
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    class RegisterUser extends AsyncTask<String,Void, Void>{
        String webPage="";
        String baseUrl="http://mobile.tornosindia.com/theguide/";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RegisterActivity.this, "Please Wait", "Registering...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            switch (webPage) {
                case "exists":
                    Toast.makeText(RegisterActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                    break;
                case "success":
                    Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    Toast.makeText(RegisterActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                String myURL = baseUrl+"register.php?name="+strings[0]+"&number="+strings[1]
                        +"&password="+strings[2]+"&email="+strings[3]+"&address="+strings[4]
                        +"&country="+strings[5]+"&gender="+strings[6]+"&dob="+strings[7];
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
    }
}
