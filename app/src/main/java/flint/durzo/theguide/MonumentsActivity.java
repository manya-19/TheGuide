package flint.durzo.theguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import flint.durzo.theguide.Adapters.CustomRVItemTouchListener;
import flint.durzo.theguide.Adapters.Info;
import flint.durzo.theguide.Adapters.RecyclerViewItemClickListener;
import flint.durzo.theguide.Adapters.Recycler_View_Adapter1;

public class MonumentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Recycler_View_Adapter1 adapter;
    String city;
    ArrayList<String> monumentsOfCity;
    List<Info> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monuments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        city = intent.getStringExtra("id");
        setTitle(city + " : Places of Interests");
        info = new ArrayList<>();
        monumentsOfCity = new ArrayList<>();
        new GetMonuments().execute(city);
    }

    class GetMonuments extends AsyncTask<String, Void, Void> {
        String webPage = "", baseUrl = "http://mobile.tornosindia.com/theguide/";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MonumentsActivity.this, "Please Wait", "Fetching Data...");
        }

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                String myURL = baseUrl+"fetchmonumentsbycity.php?city="+strings[0];
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
            progressDialog.dismiss();
            if (webPage.isEmpty())
                Toast.makeText(MonumentsActivity.this, "No Monuments Found", Toast.LENGTH_SHORT).show();
            else
            {
                while (webPage.contains("<br>")){
                    int brI = webPage.indexOf("<br>");
                    String name = webPage.substring(0, brI);
                    webPage = webPage.substring(brI+4);
                    brI = webPage.indexOf("<br>");
                    String desc = webPage.substring(0, brI);
                    webPage = webPage.substring(brI+4);
                    brI = webPage.indexOf("<br>");
                    String imageURL = webPage.substring(0, brI);
                    webPage = webPage.substring(brI+4);
                    monumentsOfCity.add(name);
                    info.add(new Info(name, desc, imageURL));
                }
            }
            recyclerView = findViewById(R.id.recyclerview);
            adapter = new Recycler_View_Adapter1(info, getApplication());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(MonumentsActivity.this));
            recyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(MonumentsActivity.this, recyclerView, new RecyclerViewItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(MonumentsActivity.this, PlayerActivity.class);
                    intent.putExtra("source", "Monuments");
                    intent.putExtra("title", monumentsOfCity.get(position));
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}