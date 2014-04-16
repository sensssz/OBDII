package cn.edu.nju.software.obdii.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.SupportMapFragment;

import cn.edu.nju.software.obdii.R;

/**
 * Show the trajectory of the user's car
 */
public class TrajectoryFragment extends Fragment {
    private SupportMapFragment mMapFragment;
    private MapView mMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trajectory, container, false);

        mMapFragment = SupportMapFragment.newInstance();

        view.findViewById(R.id.map_container).postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.map_container, mMapFragment, "map_fragment").commit();
                mMapView = mMapFragment.getMapView();
                if (mMapView != null) {
                    mMapView.getController().setZoom(12);
                }
            }
        }, 300);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMapView != null) {
            mMapView.destroy();
        }
    }
}
