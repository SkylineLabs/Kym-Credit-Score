package in.skylinelabs.GoPay;


import com.google.gson.JsonObject;

import org.json.JSONException;

/**
 * Created by Jay Lohokare on 14-01-2017.
 */

public interface AsyncTaskComplete {
    void handleResult(JsonObject result, String action) throws JSONException;


}
