package cn.edu.nju.software.obdii.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.edu.nju.software.obdii.R;

/**
 * Created by rogers on 2014/4/21.
 */
public class AlertCheckFragment extends Fragment {
    boolean mAlerts[]; //save alert states
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alertcheck, container, false);
        mAlerts = new boolean[16];
        return view;

    }
}
