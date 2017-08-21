package in.skylinelabs.Kym;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jay Lohokare on 24-06-2017.
 */


public class ColorFragment extends Fragment {

    private static final String ARG_COLOR = "color";
    private static final String ARG_INT = "position";
    private int mColor, pos;

    public static ColorFragment newInstance(int param1, int param2) {
        ColorFragment fragment = new ColorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLOR, param1);
        args.putInt(ARG_INT, param2);

        fragment.setArguments(args);
        return fragment;
    }

    public ColorFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColor = getArguments().getInt(ARG_COLOR);
            pos = getArguments().getInt(ARG_INT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (pos == 0) {
            View v = inflater.inflate(R.layout.intro_1, container, false);
            v.setBackgroundColor(mColor);
            return v;
        }
        if (pos == 1) {
            View v = inflater.inflate(R.layout.intro_2, container, false);
            v.setBackgroundColor(mColor);
            return v;
        }
        if (pos == 2) {
            View v = inflater.inflate(R.layout.intro_3, container, false);
            v.setBackgroundColor(mColor);
            return v;
        }
        if (pos == 3) {
            View v = inflater.inflate(R.layout.intro_4, container, false);
            v.setBackgroundColor(mColor);
            return v;
        }
        if (pos == 4) {
            View v = inflater.inflate(R.layout.intro_5, container, false);
            v.setBackgroundColor(mColor);
            return v;
        }

        else
            return null;

    }
}
