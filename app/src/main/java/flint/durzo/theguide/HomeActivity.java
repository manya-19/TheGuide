package flint.durzo.theguide;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    String TAG = "Abhinav";
    /*Location userLocation = null;
    private FusedLocationProviderClient mFusedLocationClient;
    int LOCATION_REQUEST_CODE = 1;*/

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
                        Log.e(TAG, "This Language is not supported");
                    }
                    else{
                        convertTextToSpeech();
                    }
                }
                else
                    Log.e(TAG, "Initilization Failed!");
            }
        });

        Button speak = findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*EditText text = findViewById(R.id.text);

                Speakerbox speakerbox = new Speakerbox(getApplication());
                speakerbox.play(text.getText().toString());*/

                convertTextToSpeech();
            }
        });

        /*mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocation();

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = findViewById(R.id.name);
                EditText desc = findViewById(R.id.desc);
                new RecordLocation().execute(userLocation.getLatitude()+"", userLocation.getLongitude()+"", name.getText().toString(), desc.getText().toString());
            }
        });*/
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
        TextView et = findViewById(R.id.text);

        String text = et.getText().toString();
        text = "J'espère que tu as eu mon email";
        if(!text.isEmpty())
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        else{
            Log.d(TAG, "Content not available");
        }
    }

    /*void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
        else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Toast.makeText(HomeActivity.this, "Location aa gayi hai", Toast.LENGTH_SHORT).show();
                                userLocation = location;
                                new UserAddress().execute(location);
                            }
                        }
                    });
        }
    }

    class RecordLocation extends AsyncTask<String, Void, Void>{
        String baseUrl = "http://mobile.tornosindia.com/theguide/";
        String webPage = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(HomeActivity.this, "Please Wait", "Adding Location");
        }

        @Override
        protected Void doInBackground(String... strings) {
            String lati = strings[0] + "";
            String longi = strings[1] + "";
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                String myURL = baseUrl+"addlocation.php?&name="+strings[2]+"&lati="+lati+"&longi="+longi+"&info="+strings[3];
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
            progressDialog.dismiss();
            if (webPage.equals("success"))
                Toast.makeText(HomeActivity.this, "Success", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(HomeActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            getLocation();
        }
    }

    class UserAddress extends AsyncTask<Location, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Location... locations) {
            Location location = locations[0];
            String errorMessage = "";

            Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        // In this sample, get just a single address.
                        1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                errorMessage = "Service not available";
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                errorMessage = "Invalid Cordinates";
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " +
                        location.getLongitude(), illegalArgumentException);
            }

            // Handle case where no address was found.
            if (addresses == null || addresses.size() == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "No Address Found";
                    Log.e(TAG, errorMessage);
                }
                //deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                Log.i(TAG, "Address Found");

                String userAddress = TextUtils.join(System.getProperty("line.separator"),
                        addressFragments);

                Log.d(TAG, address.getLocality());

                //Log.d(TAG, userAddress);

                *//*deliverResultToReceiver(Constants.SUCCESS_RESULT,
                        TextUtils.join(System.getProperty("line.separator"),
                                addressFragments));*//*
            }
                return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }*/

}