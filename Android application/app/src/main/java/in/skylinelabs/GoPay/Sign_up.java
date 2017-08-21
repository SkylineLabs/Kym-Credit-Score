package in.skylinelabs.GoPay;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay Lohokare on 14-01-2017.
 */

public class Sign_up extends AppCompatActivity implements AsyncTaskComplete {
    Button btn;
    EditText edttxt1,edttxt2, edttxt3, edttxt4;
    TextInputLayout lNameLayout,lNameLayout1,lNameLayout2,lNameLayout4;
    String name;
    AlertDialog alertDialog;
    private ActionHandler actionHandler;


    final String PREFS_NAME = "Kym_App_Preferences";
    SharedPreferences preferences;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int grantResult : grantResults) {
            // handle denied permissions
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initPermissions();

        preferences = getSharedPreferences(PREFS_NAME, 0);

        actionHandler = new ActionHandler(this, Sign_up.this);

        lNameLayout = (TextInputLayout) findViewById(R.id
                .fNameLayoutName);
        lNameLayout1 = (TextInputLayout) findViewById(R.id
                .fNameLayoutContact);
        lNameLayout4 = (TextInputLayout) findViewById(R.id
                .fNameLayoutEmail);


        btn = (Button) findViewById(R.id.textView5);

        edttxt1 = (EditText) findViewById(R.id.editTextName);//Name

        edttxt2 = (EditText) findViewById(R.id.editTextTwitter);//twitter

        edttxt4 = (EditText) findViewById(R.id.editTextEmail);//Email

        edttxt2.clearFocus();
        edttxt4.clearFocus();
        edttxt1.requestFocus();




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String PREFS_NAME = "Kym_App_Preferences";
                final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

                name = edttxt1.getText().toString();
                String email = edttxt4.getText().toString();
                String twitter = edttxt2.getText().toString();


                lNameLayout.setErrorEnabled(false);
                lNameLayout1.setErrorEnabled(false);
                lNameLayout4.setErrorEnabled(false);


                if (edttxt1.getText().toString().matches("") || edttxt4.getText().toString().matches("") || edttxt2.getText().toString().matches("")) {
                    if (edttxt1.getText().toString().matches("")) {
                        lNameLayout.setErrorEnabled(true);
                        lNameLayout.setError("*Required field");
                    }

                    if (edttxt4.getText().toString().matches("")) {
                        lNameLayout4.setErrorEnabled(true);
                        lNameLayout4.setError("*Required field");
                    }

                    if (edttxt2.getText().toString().matches("")) {
                        lNameLayout1.setErrorEnabled(true);
                        lNameLayout1.setError("*Required field");
                    }


                } else {
                    settings.edit().putString("name", name).commit();
                    settings.edit().putString("email", email).commit();
                    settings.edit().putString("twitter_handle", twitter).commit();
                    actionHandler.login(name, twitter);
                }

            }
        });
    }

    @Override
    public void handleResult(JsonObject result, String action) throws JSONException {
        System.out.println("This is the result" + result);
        if (result.get("success").getAsInt() == -1) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.rel2), "No connection", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
        switch (action) {
            case "Login":
                if (result.get("success").getAsInt() == 1) {
                    preferences.edit().putBoolean("logged_in", true).apply();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(result.get("success").getAsInt() == 2){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Unable to signup");
                    alertDialogBuilder.setTitle("Milala re");
                    alertDialogBuilder.setCancelable(true);

                    alertDialogBuilder.setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }



                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Unable to signup");
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder.setCancelable(true);

                    alertDialogBuilder.setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;

        }
    }


}
