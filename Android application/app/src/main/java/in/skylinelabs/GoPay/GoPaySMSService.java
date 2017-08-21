package in.skylinelabs.GoPay;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;

/**
 * Created by Jay Lohokare on 14-01-2017.
 */

public class GoPaySMSService extends Service implements AsyncTaskComplete{

    private ActionHandler actionHandler;

    @Override
    public void handleResult(JsonObject result, String action) throws JSONException {
        if (result.get("success").getAsInt() == -1) {

            return;
        }
        switch (action) {
            case "SMS":
                if (result.get("success").getAsInt() == 1) {
                    Toast.makeText(getApplicationContext(),"Response 1", Toast.LENGTH_SHORT).show();
                }
                else if(result.get("success").getAsInt() == 2){
                    Toast.makeText(getApplicationContext(),"Response 2", Toast.LENGTH_SHORT).show();
                }



                else {
                    Toast.makeText(getApplicationContext(),"Amount is "+ result.get("success").getAsInt(), Toast.LENGTH_SHORT).show();
                }
                break;

            case "Bill":
                if (result.get("success").getAsInt() == 1) {
                    Toast.makeText(getApplicationContext(),"Response 1", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        Toast.makeText(getApplicationContext(),"Got to the Service", Toast.LENGTH_SHORT).show();
        Log.i("In service", "In service");


        actionHandler = new ActionHandler(this,this);


        String bankname = "";
        String card = "";
        String account_no = "";
        String vendor = "";
        String amount = "";
        String balance = "";
        String month = "";
        String year = "";
        int p_d = -1;
        final String PREFS_NAME = "Kym_App_Preferences";
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
        String username = preferences.getString("name","null");

        String type = intent.getStringExtra("TYPE");
        if(type.equals("credit") || type.equals("debit")) {
            bankname = intent.getStringExtra("BANK");
            card = intent.getStringExtra("CARD");
            vendor = intent.getStringExtra("VENDOR");
            amount = intent.getStringExtra("AMOUNT");
            balance = intent.getStringExtra("BALANCE");
            month = intent.getStringExtra("MONTH");
            year = intent.getStringExtra("YEAR");
            actionHandler.sendSMS(bankname, card, vendor, amount, balance, month, year, type, username);

        }
        if(type.equals("EMI")) {
            account_no = intent.getStringExtra("ACCOUNT_NO");
            amount = intent.getStringExtra("EMI_AMOUNT");
            p_d = intent.getIntExtra("P_D", -1);
            actionHandler.sendLoanSMS(type, account_no, amount, p_d, username);
        }

        if(type.equals("Bill")) {
            amount = intent.getStringExtra("AMOUNT");
            actionHandler.sendBill(type, amount, username);
        }

        return START_NOT_STICKY;
    }



}
