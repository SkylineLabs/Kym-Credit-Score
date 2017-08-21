package in.skylinelabs.Kym;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jay Lohokare on 24-06-2017.
 */

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String PREFS_NAME = "Kym_App_Preferences";
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);

        if (preferences.getBoolean("logged_in", false)) {
            Intent i2 = new Intent(Launcher.this, MainActivity.class);
            startActivity(i2);
            finish();
        }
        else {
            Intent i2 = new Intent(Launcher.this, Sign_up.class);
            startActivity(i2);
            finish();
        }
    }
}
