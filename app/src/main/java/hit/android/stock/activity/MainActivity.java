package hit.android.stock.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import hit.android.stock.utlitiy.Favorites;
import hit.android.stock.R;
import hit.android.stock.utlitiy.AutoCompleteListenerTextChanged;
import hit.android.stock.utlitiy.DateUtility;

public class MainActivity extends AppCompatActivity implements AutoCompleteListenerTextChanged.ResultListener {

    private AutoCompleteTextView autoTextView;
    private TextView histDate;
    private final AutoCompleteListenerTextChanged autoCompleteListener = new AutoCompleteListenerTextChanged();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initAutoCompleteStockMarket();
        Favorites fav = new Favorites(this, MainActivity.this);
        fav.initialize();
    }

    private void performSearchStock() {
        String stockInput = autoTextView.getText().toString();
        String dateInput = histDate.getText().toString();
        if (stockInput.matches("")) {
            Toast.makeText(getApplicationContext(), "Please enter a Stock Name/Symbol", Toast.LENGTH_SHORT).show();
            return;
        } else if (!autoCompleteListener.checkStockNameValid(stockInput)) {
            Toast.makeText(getApplicationContext(), "Invalid Stock Name/Symbol", Toast.LENGTH_SHORT).show();
            return;
        }
        if (DateUtility.isDateValid(dateInput)) {
            Intent intent = new Intent(MainActivity.this, StockDetailsActivity.class);
            intent.putExtra("stockName", stockInput);
            intent.putExtra("histDate", dateInput);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Invalid Date Format", Toast.LENGTH_SHORT).show();
        }
    }

    private void initAutoCompleteStockMarket() {
        autoTextView = findViewById(R.id.editStockName);
        autoCompleteListener.setListener(this);
        autoTextView.addTextChangedListener(autoCompleteListener);
        // When a suggestion is chosen, remove the suggestion dropdown.
        autoTextView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(getApplicationContext(), "Stock symbol selected.", Toast.LENGTH_SHORT).show();
            autoTextView.dismissDropDown();
        });
    }

    private void initViews() {
        histDate = findViewById(R.id.editTextDate);
        findViewById(R.id.quoteBtn).setOnClickListener(v -> performSearchStock());
        findViewById(R.id.clearBtn).setOnClickListener(v -> {
            autoTextView.setText("");
            histDate.setText("");
        });
    }

    @Override
    public void onResultsReady(String[] result) {
        autoTextView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, result));
        autoTextView.showDropDown();
    }
}