package in.skylinelabs.GoPay;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Jay Lohokare on 14-01-2017.
 */

public class HttpJsonPost extends AsyncTask<JsonObject, Integer, JsonObject> {

    private String url, progressinfo, action;
    private Context context;
    private ProgressDialog progressDialog;
    private AsyncTaskComplete callback;
    private boolean progressbar;

    public HttpJsonPost(String url, String action, String progressinfo, Context context, AsyncTaskComplete callback) {
        this.context = context;
        this.url = url;
        this.callback = callback;
        this.action = action;
        this.progressbar = !progressinfo.isEmpty();
        this.progressinfo = progressinfo;
    }


    public void setProgressinfo(String progressinfo) {
        this.progressinfo = progressinfo;
    }

    @Override
    protected void onPreExecute() {
        if (progressbar && action=="Login") {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(progressinfo);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @Override
    protected JsonObject doInBackground(JsonObject... params) {
        return postJsonObject(params[0]);
    }

    @Override
    protected void onPostExecute(JsonObject result) {
        if (progressbar && action=="Login") {
            progressDialog.dismiss();
        }
        try {
            callback.handleResult(result, action);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private JsonObject postJsonObject(JsonObject jsonObject) {
        JsonObject nullresult = new JsonObject();
        nullresult.addProperty("success", -1);
        try {
            String data = jsonObject.toString();
            URL object = new URL(url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) object.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setConnectTimeout(4000);
            httpURLConnection.setReadTimeout(15000);

           OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(data);
            wr.flush();

            StringBuilder sb = new StringBuilder();
            int HttpResult = httpURLConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
            }

            JsonObject result = new JsonParser().parse(sb.toString()).getAsJsonObject();
            Log.i("This is the result" + result, "This is the result" + result);
            if (result != null)
                return result;
            else
                return nullresult;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedJsonException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nullresult;
    }
}
