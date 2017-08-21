package in.skylinelabs.Kym;


import com.google.gson.JsonObject;

import org.json.JSONException;

/**
 * Created by Jay Lohokare on 24-06-2017.
 */

public interface AsyncTaskComplete {
    void handleResult(JsonObject result, String action) throws JSONException;


}
