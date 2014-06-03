package cn.edu.nju.software.obdii.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.edu.nju.software.obdii.R;

/**
 * Show alerts about the status of the car
 */
public class AlertCheckFragment extends Fragment {
    private ImageView[] mAlertViews;
    private int[] mNormalAlertImages;
    private int[] mAbnormalAlertImages;
    private String mAlerts[]; //save alert states

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alertcheck, container, false);
        mAlerts = new String[16];
//        updateAlerts();

        return view;
    }

    private void updateAlerts() {
        for (int index = 0; index < mAlerts.length; ++index) {
            if (mAlerts[index].equals("1")) {
                mAlertViews[index].setImageResource(mNormalAlertImages[index]);
            } else if (mAlerts[index].equals("0")) {
                mAlertViews[index].setImageResource(mAbnormalAlertImages[index]);
            }
        }
    }
}
