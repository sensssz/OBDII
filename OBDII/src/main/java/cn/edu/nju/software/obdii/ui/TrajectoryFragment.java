package cn.edu.nju.software.obdii.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.baidu.mapapi.map.SupportMapFragment;

import cn.edu.nju.software.obdii.R;

/**
 * Show the trajectory of the user's car
 */
public class TrajectoryFragment extends Fragment {
    private SupportMapFragment mMapFragment;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapFragment = SupportMapFragment.newInstance();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().add(R.id.map_container, mMapFragment, "map_fragment").commit();
    }
}
