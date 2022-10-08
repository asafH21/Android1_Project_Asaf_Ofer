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

    //Delete the list, add back all the new list, notify UI
    public void addList(List<FavoriteModel> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    //Delete the list
    public void clearList() {
        list.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView stockName;
        public TextView stockPrice;
        public TextView stockChange;
        public View layout;
        public ImageView deleteFav;


        //The layout of single stock in Favorites recycler
        //Also here we "connect" between components (ex: ImageView to deleteFav)
        public ViewHolder(View v) {
            super(v);
            layout = v;
            stockName = (TextView) v.findViewById(R.id.favStockSymbol);
            stockPrice = (TextView) v.findViewById(R.id.favStockPrice);
            stockChange = (TextView) v.findViewById(R.id.favMarketChange);
            deleteFav = (ImageView) v.findViewById(R.id.deleteFav);
        }
    }


    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewTop) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fav_row_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //Takes the values received and put them in their correct place (stock name, price, percentage)
    public void onBindViewHolder(ViewHolder holder, final int position) {
        FavoriteModel favoriteModel = list.get(position);
        holder.stockName.setText(favoriteModel.getName());
        holder.stockPrice.setText(favoriteModel.getLastValue());
        String percentage = favoriteModel.getPrecentage();
        holder.stockChange.setText(getShortPrice(percentage));
        //Choose the colors that will show percentage of change (green/red)
        holder.stockChange.setTextColor(favoriteModel.getPrecentage().contains("+") ? Color.GREEN : Color.RED);
        //Trash can operation
        holder.deleteFav.setOnClickListener(view -> {
            list.remove(holder.getAdapterPosition());
            notifyDataSetChanged();
            listener.onFavRequestedDelete(favoriteModel.getName());
        });
    }
    public interface DeleteListener {
        public void onFavRequestedDelete(String stock);
    }

    //Show only 5 digits of the change of a stock
    private String getShortPrice(String orignal) {
        try {
            String result = orignal.substring(0, 5);
            return result + "%";
        } catch (Exception e) {

        }
        return orignal;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



}