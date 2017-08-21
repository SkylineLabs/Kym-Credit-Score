package in.skylinelabs.Kym;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Random;

/**
 * Created by Jay Lohokare on 24-06-2017.
 */

public class DemoPagerAdapter extends FragmentPagerAdapter {

    private int pagerCount = 5;

    private Random random = new Random();

    public DemoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0)
            return ColorFragment.newInstance(Color.parseColor("#0277bd"), i);//red

        if(i==1)
        return ColorFragment.newInstance(Color.parseColor("#4baf4f"), i);//pink

        if(i==2)
            return ColorFragment.newInstance(Color.parseColor("#f29d0f"), i);//green
        if(i==3)
            return ColorFragment.newInstance(Color.parseColor("#5f7d88"), i);

        if(i==4)
            return ColorFragment.newInstance(Color.parseColor("#0277bd"), i);

        else
            return null;

    }

    @Override
    public int getCount() {
        return pagerCount;
    }
}