package cn.edu.nju.software.obdii.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.nju.software.obdii.R;

/**
 * Show alerts about the status of the car
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
