package hit.android.stock.utlitiy;

import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class AutoCompleteListenerTextChanged implements TextWatcher {

    private String input;
    private final ArrayList<String> stocksArrayResults = new ArrayList<>();
    private ResultListener listener;

    public void setListener(ResultListener listener) {
        this.listener = listener;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (input == null || input != null && !s.toString().equals(input)) {
            input = s.toString();
            if (input.length() >= 1) {
                String url = String.format("https://api.tiingo.com/tiingo/utilities/search?query=%s&token=%s", input, "c18ece8126dfb33a981d8f5801de65d2b21c956c");
                new JsonTask().execute(url);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private class JsonTask extends AsyncTask<String, Void, String[]> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String[] doInBackground(String... params) {
            String jsonStr = null;
            try {
                URL url = new URL(params[0]);
                jsonStr = JSONParser.getJsonFromUrl(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return JSONParser.getValueArray(jsonStr, "ticker");
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            stocksArrayResults.clear();
            stocksArrayResults.addAll(Arrays.asList(result));
            if (listener != null) {
                listener.onResultsReady(result);
            }
        }
    }

    public boolean checkStockNameValid(String stockName) {
        for (String currStockName : stocksArrayResults) {
            if (stockName.equals(currStockName)) {
                return true;
            }
        }
        return false;
    }

    public interface ResultListener {
        void onResultsReady(String[] result);
    }

}
