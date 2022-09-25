package hit.android.stock.adapters;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hit.android.stock.R;
import hit.android.stock.models.FavoriteModel;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private final List<FavoriteModel> list = new ArrayList<>();

    private DeleteListener listener;


    public FavoritesAdapter(DeleteListener listener) {
        this.listener = listener;
    }

    public void addList(List<FavoriteModel> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void clearList() {
        list.clear();
        notifyDataSetChanged();
    }

    // Provides a reference to the views for each provided data item.
    // ViewHolder will contain all the views of the data items.
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView stockName;
        public TextView stockPrice;
        public TextView stockChange;
        public View layout;
        public ImageView deleteFav;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            stockName = (TextView) v.findViewById(R.id.favStockSymbol);
            stockPrice = (TextView) v.findViewById(R.id.favStockPrice);
            stockChange = (TextView) v.findViewById(R.id.favMarketChange);
            deleteFav = (ImageView) v.findViewById(R.id.deleteFav);
        }
    }


    // Inflate your views. (invoked by the layout manager)
    // This sets each data item's view in accordance with row_layout.xml.
    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewTop) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fav_row_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view. (invoked by the layout manager)
    public void onBindViewHolder(ViewHolder holder, final int position) {
        FavoriteModel favoriteModel = list.get(position);
        holder.stockName.setText(favoriteModel.getName());
        holder.stockPrice.setText(favoriteModel.getLastValue());
        String precentate = favoriteModel.getPrecentage();
        holder.stockChange.setText(getShortPrice(precentate));
        holder.stockChange.setTextColor(favoriteModel.getPrecentage().contains("+") ? Color.GREEN : Color.RED);
        holder.deleteFav.setOnClickListener(view -> {
            list.remove(holder.getAdapterPosition());
            notifyDataSetChanged();
            listener.onFavRequestedDelete(favoriteModel.getName());
        });
    }

    private String getShortPrice(String orignal) {
        try {
            String result = orignal.substring(0, 5);
            return result + "%";
        } catch (Exception e) {

        }
        return orignal;
    }

    // Return the size of your data set. (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface DeleteListener {
        public void onFavRequestedDelete(String stock);
    }
}