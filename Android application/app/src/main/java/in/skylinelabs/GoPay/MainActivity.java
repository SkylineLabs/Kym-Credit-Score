package in.skylinelabs.GoPay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;

/**
 * Created by Jay Lohokare on 14-01-2017.
 */


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncTaskComplete {

    private ActionHandler actionHandler;
    TextView wallet_bal;
    String username;
    AlertDialog alertDialog ;
    PieChart mPieChart;
    View.OnClickListener snackaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        snackaction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button add = (Button) findViewById(R.id.button2);
        FloatingActionButton pay = (FloatingActionButton) findViewById(R.id.button);

        final String PREFS_NAME = "Kym_App_Preferences";
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
        username = preferences.getString("name","null");

        actionHandler = new ActionHandler(this, MainActivity.this);

        wallet_bal = (TextView) findViewById(R.id.textView9);

        actionHandler.getBalanceAndLimit(username);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertadd = new android.app.AlertDialog.Builder(
                        MainActivity.this);
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);

                final View view2 = factory.inflate(R.layout.add_alert, null);


                final EditText amount = (EditText) view2.findViewById(R.id.editText);

                alertadd.setView(view2);


                alertadd.setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dlg, int sumthin) {

                        actionHandler.updateBalance(username, amount.getText().toString());

                    }
                });
                alertDialog = alertadd.create();
                alertDialog.show();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertadd = new android.app.AlertDialog.Builder(
                        MainActivity.this);
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);

                final View view2 = factory.inflate(R.layout.add_alert, null);


                final EditText amount = (EditText) view2.findViewById(R.id.editText);

                alertadd.setView(view2);


                alertadd.setPositiveButton("Pay", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dlg, int sumthin) {
                        /*Intent intent = new Intent(MainActivity.this, PayActivity.class);
                        intent.putExtra("amount", amount.getText().toString());
                        startActivity(intent); */
                        actionHandler.pay_check(username, amount.getText().toString());

                    }
                });
                alertDialog = alertadd.create();
                alertDialog.show();
            }
        });

        mPieChart = (PieChart) findViewById(R.id.piechart);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.chat_bot) {
            Intent i = new Intent(this, ChatActivity.class);
            startActivity(i);

        } else if (id == R.id.tutorial) {
            Intent i = new Intent(this, App_intro.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        actionHandler.getBalanceAndLimit(username);

    }

    @Override
    protected void onResume() {
        super.onResume();
        actionHandler.getBalanceAndLimit(username);

    }

    @Override
    public void handleResult(JsonObject result, String action) throws JSONException {

        switch (action) {
            case "Balance":

                int creditBalance = result.get("credit_balance").getAsInt();
                int creditLimit = result.get("credit_limit").getAsInt();
                int creditLeft = creditLimit - creditBalance;
                int walletbal = result.get("wallet_balance").getAsInt();
                //Toast.makeText(getApplicationContext(), String.valueOf(creditLimit), Toast.LENGTH_LONG).show();
                //int percentage = creditBalance/creditLimit *100;
                //int balance_remaining = 100 - percentage;
                mPieChart.clearChart();
                mPieChart = (PieChart) findViewById(R.id.piechart);
                mPieChart.addPieSlice(new PieModel("Credit utilized", creditBalance, Color.parseColor("#37474f")));
                mPieChart.addPieSlice(new PieModel("Credit left", creditLeft, Color.parseColor("#5CA5DA")));
                wallet_bal.setText(String.valueOf(walletbal));
                mPieChart.startAnimation();

                break;
            case "Update":
                actionHandler.getBalanceAndLimit(username);
                break;
            case "PayCheck":
               // Toast.makeText(getApplicationContext(), "bello", Toast.LENGTH_LONG).show();
                int output = result.get("output").getAsInt();
                int amount = result.get("amount").getAsInt();
                int credlim = result.get("credit_limit").getAsInt();
                int credbal = result.get("credit_balance").getAsInt();

                switch(output){
                    case 1:
                        actionHandler.payBalance(username, String.valueOf(amount));
                        break;
                    case 2:
                        Snackbar.make(findViewById(android.R.id.content), "Money taken from Credit Balance", Snackbar.LENGTH_INDEFINITE)
								.setAction("Ok", snackaction)
								.setActionTextColor(Color.WHITE)
								.show();
                        actionHandler.getBalanceAndLimit(username);
                        break;
                    case 3:
                        Snackbar.make(findViewById(android.R.id.content), "You do not have enough credits left", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Ok", snackaction)
                                .setActionTextColor(Color.WHITE)
                                .show();
                }
                break;
            case "Pay":
                actionHandler.getBalanceAndLimit(username);
                break;
        }
    }
}
