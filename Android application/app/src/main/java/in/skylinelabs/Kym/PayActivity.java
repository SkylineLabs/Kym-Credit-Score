package in.skylinelabs.Kym;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;

/**
 * Created by Jay Lohokare on 24-06-2017.
 */


public class PayActivity extends AppCompatActivity implements AsyncTaskComplete{
    String username;
    private ActionHandler actionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_activity);
        actionHandler = new ActionHandler(this, PayActivity.this);
        Bundle b = getIntent().getExtras();
        String amount = b.getString("amount");
        actionHandler.pay_check(username, amount);

    }

    @Override
    public void handleResult(JsonObject result, String action) throws JSONException {

        switch (action) {
            case "PayCheck":
               Toast.makeText(getApplicationContext(),"bello",Toast.LENGTH_LONG).show();
                int output = result.get("output").getAsInt();
                int amount = result.get("amount").getAsInt();
                int credlim = result.get("credit_limit").getAsInt();
                int credbal = result.get("credit_balance").getAsInt();

                switch(output){
                    case 1:
                        Toast.makeText(getApplicationContext(),"bello",Toast.LENGTH_LONG).show();

                        break;
                }
                break;

        }
    }
}
