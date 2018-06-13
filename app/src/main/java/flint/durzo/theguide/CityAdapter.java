package flint.durzo.theguide;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {


    private List<Adapter> citiesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView city;

        public MyViewHolder(View view) {
            super(view);
            city = (TextView) view.findViewById(R.id.city);

        }
    }


    public CityAdapter(List<Adapter> citiesList) {
        this.citiesList = citiesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Adapter city = citiesList.get(position);
        holder.city.setText(Adapter.getTitle());
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }
}

