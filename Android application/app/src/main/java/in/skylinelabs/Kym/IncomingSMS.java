package in.skylinelabs.Kym;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jay Lohokare on 24-06-2017.
 */

public class IncomingSMS extends BroadcastReceiver {
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private ActionHandler actionHandler;
    String bankname;
    String card;
    String vendor;
    String date;
    String amount;
    String balance;
    String type;
    String month;
    String year;
    int p_d;



    public void onReceive(Context context, Intent intent) {

        /* Your SBI account XXX has been credited/debited with Rs 100 for transaction at Flipkart . Current balance: 56 */
        /*  Car loan EMI payment of Rs 1000 for account XXX is due
            Bill for your postpaid airtel mobile is Rs 500 .*/
        final Bundle bundle = intent.getExtras();
        try {

            //actionHandler = new ActionHandler(context, intent.);
            //Toast.makeText(context,"In SMS recieveer", Toast.LENGTH_SHORT).show();

            if (bundle != null) {
                String message = null;
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int j = 0; j < pdusObj.length; j++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[j]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();

                    //Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    // Show Alert
                    /*int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: " + senderNum + ", message: " + message, duration);
                    toast.show();*/
                    //String msg = "Thank you for using your SBI card 7798720001 for a transaction of Rs 99 at flipkart account balance: 2000";
                    String[] tokens = message.split(" ");
                    for (String token : tokens) {
                        Log.d("token", token);
                    }
                    Date d = new Date( );
                    SimpleDateFormat mft = new SimpleDateFormat("MM");
                    SimpleDateFormat yft = new SimpleDateFormat("yyyy");
                    month = mft.format(d);
                    year = yft.format(d);

                    bankname = null;
                    card = null;
                    vendor = null;
                    amount= null;
                    balance = null;
                    p_d = -1;
                    type = "null";
                    String keys[] = { "Rs", "at", "balance:", "account"};
                    String banknames[] = {"BOM", "SBI", "ICICI", "HDFC"};
                    String types[] = {"credited", "debited", "EMI", "Bill"};
                    String EMI_checks[] = {"received" , "due"};
                    for (String t : types)
                    {
                        for (String token : tokens) {
                            if (token.equals(t)) {
                                type = t;
                                break;
                            }
                        }
                    }

                    if(type.equals("EMI")) {
                        for (String token : tokens)
                        {
                            if(token.equals("received")) {
                                p_d = 1;
                                break;
                            }
                            if(token.equals("due")) {
                                p_d = 0;
                                break;
                            }
                        }
                    }

                    for (String bank : banknames)
                    {
                        for (String token : tokens) {
                            if (token.equals(bank)) {
                                bankname = bank;
                                break;
                            }
                        }
                    }
                    for(int i = 0; i < tokens.length; i++) {
                        if(tokens[i].equals(keys[0])) {
                            amount = tokens[i + 1];
                        }
                        if(tokens[i].equals(keys[1])) {
                            vendor = tokens[i + 1];
                        }
                        if(tokens[i].equals(keys[2])) {
                            balance = tokens[i + 1];
                        }
                        if(tokens[i].equals(keys[3])) {
                            card = tokens[i + 1];
                        }
                    }


                }

                if(type.equals("credited"))
                    type = "credit";
                if(type.equals("debited"))
                    type = "debit";
                if(!type.equals("null")) {
                    Toast.makeText(context,type, Toast.LENGTH_SHORT).show();
                }

                /*System.out.println(("bankname" + bankname));
                System.out.println("card" + card);
                System.out.println("vendor" + vendor);
                System.out.println("amount" + amount);
                System.out.println("balance" + balance);*/

                if(type.equals("credit") || type.equals("debit")) {
                    Intent i = new Intent(context, KymSMSService.class);
                    i.putExtra("BANK", bankname);
                    i.putExtra("CARD", card);
                    i.putExtra("VENDOR", vendor);
                    i.putExtra("AMOUNT", amount);
                    i.putExtra("BALANCE", balance);
                    i.putExtra("MONTH", month);
                    i.putExtra("YEAR", year);
                    i.putExtra("TYPE", type);

                    context.startService(i);
                }
                if(type.equals("EMI")) {
                    Intent i = new Intent(context, KymSMSService.class);
                    i.putExtra("ACCOUNT_NO", card);
                    i.putExtra("EMI_AMOUNT", amount);
                    i.putExtra("TYPE", type);
                    i.putExtra("P_D", p_d);
                    context.startService(i);
                }
                if(type.equals("Bill")) {
                    Intent i = new Intent(context, KymSMSService.class);
                    i.putExtra("TYPE", type);
                    i.putExtra("AMOUNT", amount);
                    Toast.makeText(context,amount, Toast.LENGTH_SHORT).show();
                    context.startService(i);

                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}
