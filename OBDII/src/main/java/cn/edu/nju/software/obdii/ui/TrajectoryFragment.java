package cn.edu.nju.software.obdii.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapFragment;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataMap;
import cn.edu.nju.software.obdii.location.LocationData;
import cn.edu.nju.software.obdii.location.Point2D;

/**
 * Show the trajectory of the user's car
 */
public class TrajectoryFragment extends Fragment {
    private MapView mMapView;
    private ItemizedOverlay mOverlay;
    private LocationData mLocationData;
    private String mUsername;

    public TrajectoryFragment(String username) {
        mUsername = username;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataMap.getInstance().addOnLocationUpdateListener(new DataMap.OnLocationUpdateListener() {
            @Override
            public void onLocationUpdate(double latitude, double longitude) {
                if (isVisible() && mMapView != null && mOverlay != null) {
                    GeoPoint point = new GeoPoint(toBaiduFormat(latitude), toBaiduFormat(longitude));
                    OverlayItem item = new OverlayItem(point, "", "");
                    mOverlay.addItem(item);
                    mMapView.refresh();
                }
            }
        });
    }

    private int toBaiduFormat(double coordinate) {
        return (int) (coordinate * 1E6);
    }

    private OverlayItem toOverlayItem(GeoPoint point) {
        return new OverlayItem(point, "", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trajectory, container, false);

        if (mLocationData == null) {
            mLocationData = new LocationData(getActivity(), mUsername);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.getController().setZoom(12);
        mOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.marker),
                mMapView);
        for (Point2D point : mLocationData.getLocationData()) {
            mOverlay.addItem(toOverlayItem(point.toGeoPoint()));
        }
        mMapView.refresh();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("", "onResume");
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("", "onPause");
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
