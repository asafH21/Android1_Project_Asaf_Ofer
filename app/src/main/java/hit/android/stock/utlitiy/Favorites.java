package hit.android.stock.utlitiy;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hit.android.stock.R;
import hit.android.stock.db.SharedPreferencesJSON;
import hit.android.stock.adapters.FavoritesAdapter;
import hit.android.stock.models.FavoriteModel;

public class Favorites implements FavoritesAdapter.DeleteListener {

    // Context variables
    private Context context;
    private Activity mainActivity;
    private SharedPreferencesJSON favJSON;
    // RecyclerView variables
    private RecyclerView recyclerView;
    private FavoritesAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;

    // Layout variables
//    private ProgressBar favProgBar;
    private ImageView refreshBtn;


    public Favorites(Context context, Activity activity) {
        this.context = context;
        this.mainActivity = activity;
        this.favJSON = new SharedPreferencesJSON(context);
    }

    public void initialize() {
        recyclerView = (RecyclerView) mainActivity.findViewById(R.id.favRecyclerView);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        refreshBtn = (ImageView) mainActivity.findViewById(R.id.refreshBtn);
        progressBar = (ProgressBar) mainActivity.findViewById(R.id.progressBar);
        mAdapter = new FavoritesAdapter(this);
        recyclerView.setAdapter(mAdapter);
        refreshContent();
        refreshBtn.setOnClickListener(v -> refreshContent());
    }

    private void refreshContent() {
        mAdapter.clearList();
        new CacheStockExecuteForDataTask().execute();
    }


    public double getDelta(double prev, double curr) {
        return (curr - prev) / prev * 100;
    }

    @Override
    public void onFavRequestedDelete(String stock) {
        favJSON.removeFromSharedPreferencesJSON(stock);
        Toast.makeText(context, stock + " has been removed from favorites.", Toast.LENGTH_SHORT).show();
    }

    // Inner class for querying JSON in background thread.
    private class CacheStockExecuteForDataTask extends AsyncTask<Void, Long, Void> {
        private final List<FavoriteModel> list = new ArrayList<>();

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Void doInBackground(Void... params) {
            String token = context.getResources().getString(R.string.token);
            JSONObject jsonObjPref = favJSON.getSharedPreferencesJSON();

            // Iterate through the JSONObject and query every stock name within in Tiingo.
            Iterator<String> keys = jsonObjPref.keys();
            int i = 0;
            while (keys.hasNext()) {
                String stockName = keys.next();

                // Query Tiingo.
                JSONObject jsonObj = null;
                try {
                    URL url = new URL("https://api.tiingo.com/iex/?tickers=" + stockName + "&token=" + token);
                    jsonObj = JSONParser.getJsonObjectFromUrl(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                // Retrieve the json info we need for favorites.
                try {
                    String prevClosePrice = jsonObj.getString("prevClose");
                    String latestPrice = jsonObj.getString("last");
                    double deltaPrice = getDelta(Double.parseDouble(prevClosePrice), Double.parseDouble(latestPrice));
                    String stockChange = deltaPrice + "%";
                    if (deltaPrice > 0) {
                        stockChange = "+" + stockChange;
                    }

                    // Add it to the list of favorite stocks as an array.
                    list.add(new FavoriteModel(stockName, latestPrice, stockChange));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                i++;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... value) {
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            mAdapter.addList(list);
        }
    }
}
