package hit.android.stock.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hit.android.stock.utlitiy.JSONParser;
import hit.android.stock.R;
import hit.android.stock.adapters.StockDetailsAdapter;


public class CurrentStockInfoFragment extends Fragment {

    // Data variables
    private String stockName;

    // RecyclerView variables
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Pair> pairs = new ArrayList<>();

    // Context variables
    private Context context;

    public static CurrentStockInfoFragment newInstance(String stockName) {
        CurrentStockInfoFragment fragment = new CurrentStockInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stockName", stockName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view.
        View root = inflater.inflate(R.layout.recycler_view_current, container, false);

        // Create the recycler view.
        recyclerView = (RecyclerView) root.findViewById(R.id.currRecyclerView);
        recyclerView.setHasFixedSize(true);

        // LinearLayout manager
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        // Grab Tiingo info place it into UI in another thread.
        queryJSON();

        // Return.
        return root;
    }


    //Queries Tiingo's  JSON stock database for stock info.
    public void queryJSON() {
        String token = context.getResources().getString(R.string.token);
        Bundle bundle = this.getArguments();
        stockName = bundle.getString("stockName");
        String url = "https://api.tiingo.com/iex/?tickers=" + stockName + "&token=" + token;
        new JsonTask().execute(url);
    }

    // Inner class for querying JSON in background thread.
    private class JsonTask extends AsyncTask<String, Long, Pair[]> {
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected Pair[] doInBackground(String... params) {
            String jsonStr = null;
            try {
                URL url = new URL(params[0]);
                jsonStr = JSONParser.getJsonFromUrl(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // Parse json info and return it within an array.
            return JSONParser.getKeyValuePairArray(jsonStr);
        }

        @Override
        protected void onProgressUpdate(Long... value) {
        }

        @Override
        protected void onPostExecute(Pair[] result) {
            super.onPostExecute(result);


            // Add rows to recycler view based on number of JSON data rows.
            for (int i = 0; i < result.length; i++) {
                pairs.add(result[i]);
            }

            // Initialize adapter and adapt pairs data to our recyclerView.
            mAdapter = new StockDetailsAdapter(pairs);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
