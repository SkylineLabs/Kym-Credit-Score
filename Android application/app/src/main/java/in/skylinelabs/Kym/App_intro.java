package in.skylinelabs.Kym;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Jay Lohokare on 24-06-2017.
 */


public class App_intro extends ActionBarActivity {

    TextView skip, next;
    Boolean is_called_from_login;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_intro);
        is_called_from_login = getIntent().getBooleanExtra("is_called_from_login",false);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        skip = (TextView) findViewById(R.id.skip);
        next = (TextView) findViewById(R.id.next);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }






        final ViewPager customViewpager = (ViewPager) findViewById(R.id.viewpager_custom);
        CircleIndicator customIndicator = (CircleIndicator) findViewById(R.id.indicator_custom);
        DemoPagerAdapter customPagerAdapter = new DemoPagerAdapter(getSupportFragmentManager());
        customViewpager.setAdapter(customPagerAdapter);
        customIndicator.setViewPager(customViewpager);

         customViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if(customViewpager.getCurrentItem()==4) {
                    next.setText("Done");
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                }

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        if(customViewpager.getCurrentItem()!=5) {
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customViewpager.setCurrentItem(customViewpager.getCurrentItem() + 1);
                 }

            });

        }


        customViewpager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                // Ensures the views overlap each other.
                view.setTranslationX(view.getWidth() * -position);

                // Alpha property is based on the view position.
                if (position <= -1.0F || position >= 1.0F) {
                    view.setAlpha(0.0F);
                } else if (position == 0.0F) {
                    view.setAlpha(1.0F);
                } else { // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                    view.setAlpha(1.0F - Math.abs(position));
                }


            }
        });
            }

    @Override
    public void onBackPressed(){
        if(is_called_from_login){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else{
            finish();
        }
    }


        }

