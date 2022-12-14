package hit.android.stock.utlitiy;

import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

public class JSONParser {


    public static Pair[] getKeyValuePairArray(String jsonStr) {
        JSONObject jsonObj = null;
        JSONArray jsonArr = null;
        Pair[] pairArr = {};
        try {
            jsonObj = new JSONObject(jsonStr);
        }
        catch (JSONException eObj) {
            try {
                jsonArr = new JSONArray(jsonStr);
                jsonObj = jsonArr.getJSONObject(0);
            }
            catch (Exception eArr) {
                eObj.printStackTrace();
                eArr.printStackTrace();
            }
        }
        if (jsonObj != null) {
            try {
                pairArr = new Pair[jsonObj.length()];
                Iterator<String> keys = jsonObj.keys();
                int i = 0;
                while(keys.hasNext()) {
                    String key = keys.next();
                    String value = jsonObj.getString(key);

                    if (key.equals("mid")) {
                        LocalDateTime currentTimestamp = LocalDateTime.now();
                        String timestampStr = jsonObj.getString("timestamp");
                        LocalDateTime timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        long minutes = ChronoUnit.MINUTES.between(timestamp, currentTimestamp);
                        if (minutes < 1 && value == null) {
                            value = "-";
                        }
                    }

                    pairArr[i] = new Pair(key, value);
                    i++;
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pairArr;
    }


    public static String[] getValueArray(String jsonStr, String key) {
        JSONArray jsonArr = null;
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (JSONException eObj) {
            try {
                jsonArr = new JSONArray(jsonStr);
            }
            catch (JSONException eArr) {
                eObj.printStackTrace();
                eArr.printStackTrace();
            }
        }
        if (jsonArr == null) {
            if (jsonObj == null) {
                return new String[]{};
            }
            else {
                String value = getValue(jsonObj, key);
                if (value != null) {
                    return new String[]{value};
                }
                return new String[]{};
            }
        }
        else {
            return getValues(jsonArr, key);
        }

    }


    public static String[] getValues(JSONArray jsonArr, String key) {
        String[] valueArr = {};
        if (jsonArr != null) {
            try {
                valueArr = new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    valueArr[i] = jsonObj.getString(key);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return valueArr;
    }


    public static String getValue(JSONObject jsonObj, String key) {
        String value = null;
        if (jsonObj != null) {
            try {
                value = jsonObj.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    public static String getJsonFromUrl(URL url) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);
            }
            return buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject getJsonObjectFromUrl(URL url) {
        String jsonStr = getJsonFromUrl(url);
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            return jsonObj;
        } catch (JSONException eObj) {
            try {
                JSONArray jsonArr = new JSONArray(jsonStr);
                JSONObject jsonObj = jsonArr.getJSONObject(0);
                return jsonObj;
            }
            catch (Exception eArr) {
                eObj.printStackTrace();
                eArr.printStackTrace();
                return null;
            }
        }
    }
}
