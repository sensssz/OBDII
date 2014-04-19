package cn.edu.nju.software.obdii.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.util.List;

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
    private GeoPoint mCenter;
    private float mZoomLevel;

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

        mMapView = new MapView(getActivity());
    }

    private int toBaiduFormat(double coordinate) {
        return (int) (coordinate * 1E6);
    }

    private OverlayItem toOverlayItem(GeoPoint point) {
        return new OverlayItem(point, "", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trajectory, container, false);

        if (mLocationData == null) {
            mLocationData = new LocationData(getActivity(), mUsername);
        }

        final LinearLayout mapContainer = (LinearLayout) view.findViewById(R.id.map_container);
//        mMapView = (MapView) view.findViewById(R.id.map);

        mapContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mapContainer.addView(mMapView, layoutParams);
                configMapView();
            }
        }, 200);

        return view;
    }

    private void configMapView() {
        Log.d("map", "config");
        List<Point2D> points = mLocationData.getLocationData();
        if (mCenter != null) {
            Log.d("map", "center not null");
            mMapView.getController().setCenter(mCenter);
            mMapView.getController().setZoom(mZoomLevel);
        } else if (points.size() == 0) {
            Log.d("map", "size is 0");
            mMapView.getController().setZoom(12);
        } else {
            Log.d("map", "size > 0");
            mOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.marker),
                    mMapView);
            double latitudeSum = 0;
            double longitudeSum = 0;
            double minLatitude = Double.MAX_VALUE;
            double maxLatitude = Double.MIN_VALUE;
            double minLongitude = Double.MAX_VALUE;
            double maxLongitude = Double.MIN_VALUE;
            for (Point2D point : mLocationData.getLocationData()) {
                mOverlay.addItem(toOverlayItem(point.toGeoPoint()));

                latitudeSum += point.getLatitude();
                longitudeSum += point.getLongitude();

                if (point.getLatitude() < minLatitude) {
                    minLatitude = point.getLatitude();
                } else if (point.getLatitude() > maxLatitude) {
                    maxLatitude = point.getLatitude();
                }

                if (point.getLongitude() < minLongitude) {
                    minLongitude = point.getLongitude();
                } else if (point.getLongitude() > maxLongitude) {
                    maxLongitude = point.getLongitude();
                }
            }

            int latitudeCenter = toBaiduFormat(latitudeSum / points.size());
            int longitudeCenter = toBaiduFormat(longitudeSum / points.size());
            GeoPoint center = new GeoPoint(latitudeCenter, longitudeCenter);
            mMapView.getController().setCenter(center);

            int latitudeSpan = toBaiduFormat(maxLatitude - minLatitude);
            int longitudeSpan = toBaiduFormat(maxLongitude - minLongitude);
            mMapView.getController().zoomToSpan(latitudeSpan, longitudeSpan);

            mMapView.refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("", "onResume");
        if (mMapView != null) {
//            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("", "onPause");
        if (mMapView != null) {
//            mMapView.onPause();

            mCenter = mMapView.getMapCenter();
            mZoomLevel = mMapView.getZoomLevel();
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
