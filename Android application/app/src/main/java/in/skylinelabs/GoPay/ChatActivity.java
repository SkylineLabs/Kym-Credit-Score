package in.skylinelabs.GoPay;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jay Lohokare on 24-06-2017.
 */

public class ChatActivity extends ActionBarActivity  implements AsyncTaskComplete {

    private EditText messageET, edt;
    private ListView messagesContainer;
    private ImageView sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private DatabaseHandler db;
    protected static final int RESULT_SPEECH = 1;
    private int account_flag = 0;
    String year, month, username;

    private ActionHandler actionHandler;

    private static final String [] DANGEROUS_PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private void initPermissions() {
        List<String> missingPermissions = new ArrayList<String>();
        for(String permission : DANGEROUS_PERMISSIONS) {
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (missingPermissions.size() > 0) {
            String [] permissions = new String[missingPermissions.size()];
            ActivityCompat.requestPermissions(
                    this,
                    missingPermissions.toArray(permissions),
                    1);
        } else {
            // we have all permissions, move on
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        actionHandler = new ActionHandler(this, ChatActivity.this);
        Date d = new Date( );
        SimpleDateFormat mft = new SimpleDateFormat("MM");
        SimpleDateFormat yft = new SimpleDateFormat("yyyy");
        month = mft.format(d);
        year = yft.format(d);
        final String PREFS_NAME = "Kym_App_Preferences";
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
        username = preferences.getString("name","null");

        initPermissions();
        initControls();

        ImageView mic = (ImageView) findViewById(R.id.micButton);
        mic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    edt = (EditText) findViewById(R.id.messageEdit);
                    edt.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        loadHistory();
        DisplayContent("Hey! Kym here.");
        //actionHandler.getUserSummary(username);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initControls() {
        db = new DatabaseHandler(this);
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (ImageView) findViewById(R.id.sendButton);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setTag(1);

                messageET.setText("");
                Log.d("Insert: ", "Inserting ..");
                db.addtodatabase(chatMessage);
                displayMessage(chatMessage);
                String[] tokens = messageText.split(" ");

                String account = "";
                String keys[] = {"balance", "bill", "Bill"};
                String main_key = "";
                int i = 0;
                for (String token : tokens) {
                    if(token.matches("account")) {
                        account = tokens[i + 1] ;
                        account_flag = 1;
                    }
                    for (String key : keys) {
                        if (token.matches(key)) {
                            main_key = key;
                            break;
                        }
                    }
                    i++;
                }

                if (main_key.matches("balance") || account_flag == 1) {
                    Toast.makeText(getApplicationContext(),"asked for balance", Toast.LENGTH_LONG).show();
                    if(account_flag == 1){
                        Log.i("account no", account);
                        Toast.makeText(getApplicationContext(),"account number present", Toast.LENGTH_LONG).show();
                        if(account.equals(""))
                            account = tokens[0];
                        actionHandler.get_account_balance(username, account, month, year);
                        account_flag = 0;

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"account number not there", Toast.LENGTH_LONG).show();
                        actionHandler.get_accounts(username);
                        account_flag = 1;
                    }
                }
                //else if()

            }
        });
        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();

    }
    public void DisplayContent(String message1)
    {
        ChatMessage m = new ChatMessage();
        m.setMessage(message1);
        m.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        m.setTag(0);
        db.addtodatabase(m);
        displayMessage(m);
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    edt.setText(text.get(0));
                }
                break;
            }

        }
    }


    private void loadHistory(){

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);
        Log.d("Reading: ", "Reading all contacts..");
        List<ChatMessage> cm = db.getAllMessages();

        for (ChatMessage cn : cm) {
            String log = "Id: " + cn.getId() + " ,Date: " + cn.getDate() + " ,Message: " + cn.getMessage() + " ,Tag: " + cn.getTag();
            Log.d("Row: ", log);
        }
        for(int i=0; i<cm.size(); i++) {
            ChatMessage message = cm.get(i);
            displayMessage(message);
        }

    }

    @Override
    public void handleResult(JsonObject result, String action) throws JSONException {
        if (result.get("success").getAsInt() == -1) {
            return;
        }
        switch (action) {
            case "Account_balance":
                DisplayContent(result.get("msg").getAsString());
                break;
            case "get_accounts" :
                DisplayContent(result.get("msg").getAsString());
                break;
            case "Get_User_Summary":
                DisplayContent(result.get("msg").getAsString());
                break;
        }

    }
}
