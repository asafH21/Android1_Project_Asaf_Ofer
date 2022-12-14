package hit.android.stock.adapters;


import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hit.android.stock.R;

public class StockDetailsAdapter extends RecyclerView.Adapter<StockDetailsAdapter.ViewHolder> {

    private List<Pair> values;

    public StockDetailsAdapter(List<Pair> myDataset) {
        values = myDataset;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textContent;

        public ViewHolder(View v) {
            super(v);
            textContent = (TextView) v.findViewById(R.id.content);
        }
    }


    @Override
    public StockDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewTop) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        Pair pair = values.get(position);
        holder.textContent.setText(String.format("%S - %s", (String) pair.first, (String) pair.second));
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
