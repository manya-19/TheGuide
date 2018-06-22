package flint.durzo.theguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import flint.durzo.theguide.Adapters.CustomRVItemTouchListener;
import flint.durzo.theguide.Adapters.Data;
import flint.durzo.theguide.Adapters.RecyclerViewItemClickListener;
import flint.durzo.theguide.Adapters.Recycler_View_Adapter;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        List<Data> data = fill_with_data();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(this, recyclerView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                String city = "";
                switch (position){
                    case 0: city = "Lucknow";
                    break;


                }
                if(position==0) {


                    Intent intent = new Intent( HomeActivity.this, PlayerActivity.class );
                    intent.putExtra( "source", "Home" );
                    intent.putExtra( "title", city );
                    startActivity( intent );
                }
                else
                    Toast.makeText( HomeActivity.this, "Coming Soon!", Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data("Lucknow", "City of Nawabs", R.drawable.lko));
        data.add(new Data("Agra", "Agra is a city in northern India’s Uttar Pradesh state. It's home to the iconic Taj Mahal, a mausoleum built for the Mughal ruler Shah Jahan’s wife, Mumtaz Mahal (who died in childbirth in 1631). ", R.drawable.ic_launcher_background));
        data.add(new Data("Varanasi", "Varanasi is a city in the northern Indian state of Uttar Pradesh dating to the 11th century B.C. ", R.drawable.ic_launcher_background));
        data.add(new Data("Allahabad", "Allahabad is a city in Uttar Pradesh state, north India. ", R.drawable.ic_launcher_background));
        data.add(new Data("Delhi", "Delhi, India’s capital territory, is a massive metropolitan area in the country’s north ", R.drawable.ic_launcher_background));
        data.add(new Data("Mumbai", "Mumbai (formerly called Bombay) is a densely populated city on India’s west coast. ", R.drawable.ic_launcher_background));
        data.add(new Data("Srinagar", "Srinagar is the largest city and the summer capital of the Indian state of Jammu and Kashmir.  ", R.drawable.ic_launcher_background));


        return data;
    }

}