package hit.android.stock.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import hit.android.stock.fragments.CurrentStockInfoFragment;
import hit.android.stock.R;
import hit.android.stock.db.SharedPreferencesJSON;

public class StockDetailsActivity extends AppCompatActivity {

    private Context context;
    private String stockName;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        context = getApplicationContext();
        if (getIntent().hasExtra("stockName")) {
            stockName = getIntent().getStringExtra("stockName");
        }
        fab = findViewById(R.id.fab);
        loadDetailsFragment();

        //When we are in stock fragment and the user press on the fab button
        //this will check if the stock key and value are already in the shared preferences list
        //if its not - it will ad it, and if it does - it will delete it
        fab.setOnClickListener(view -> {
            SharedPreferencesJSON favJson = new SharedPreferencesJSON(context);
            JSONObject favJsonObj = favJson.getSharedPreferencesJSON();
            if (!favJsonObj.has(stockName)) {
                favJson.addToSharedPreferencesJSON(stockName);
                fab.setBackgroundTintList(context.getResources().getColorStateList(R.color.green, null));
                Snackbar.make(view, "Successfully added to favorites!", Snackbar.LENGTH_SHORT).show();
            } else {
                favJson.removeFromSharedPreferencesJSON(stockName);
                fab.setBackgroundTintList(context.getResources().getColorStateList(R.color.red, null));
                Snackbar.make(view, stockName + " has been removed from favorites.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    //This loads the stock details fragment and then makes a server call for a new instance
    //in order to receive the wanted stock from the stocks API
    private void loadDetailsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, CurrentStockInfoFragment.newInstance(stockName), null);
        transaction.commit();
    }
}