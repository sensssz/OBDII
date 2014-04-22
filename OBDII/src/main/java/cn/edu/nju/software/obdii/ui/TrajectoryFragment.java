package cn.edu.nju.software.obdii.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.util.List;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.location.LocationData;
import cn.edu.nju.software.obdii.data.location.Point2D;

/**
 * Show the trajectory of the user's car
 */
public class TrajectoryFragment extends Fragment {
    private ItemizedOverlay mOverlay;
    private MapView mMapView;
    private LocationData mLocationData;
    private String mUsername;
    private GeoPoint mCenter;
    private float mZoomLevel;

    public TrajectoryFragment(String username) {
        mUsername = username;
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
            mLocationData.setOnLocationListener(new LocationData.OnLocationListener() {
                @Override
                public void onLocationUpdate() {
                    if (isVisible() && mMapView != null) {
                        configMapView(true);
                    }
                }
            });
        }

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP &&
                        mLocationData.getLocationData().size() > 0) {
                    mCenter = mMapView.getMapCenter();
                    mZoomLevel = mMapView.getZoomLevel();
                }
                return false;
            }
        });
        mMapView.post(new Runnable() {
            @Override
            public void run() {
                configMapView(false);
            }
        });

        return view;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        configMapView();
//    }

    private void configMapView(boolean newPoint) {
        List<Point2D> points = mLocationData.getLocationData();
        if (mOverlay == null) {
            mOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.marker),
                    mMapView);
        }
        double latitudeSum = 0;
        double longitudeSum = 0;
        double minLatitude = Double.MAX_VALUE;
        double maxLatitude = Double.MIN_VALUE;
        double minLongitude = Double.MAX_VALUE;
        double maxLongitude = Double.MIN_VALUE;
        int size = mLocationData.getLocationData().size();

        for (Point2D point : mLocationData.getLocationData()) {
            mOverlay.addItem(toOverlayItem(point.toGeoPoint()));

            latitudeSum += point.getLatitude();
            longitudeSum += point.getLongitude();

            if (point.getLatitude() < minLatitude) {
                minLatitude = point.getLatitude();
            }
            if (point.getLatitude() > maxLatitude) {
                maxLatitude = point.getLatitude();
            }

            if (point.getLongitude() < minLongitude) {
                minLongitude = point.getLongitude();
            }
            if (point.getLongitude() > maxLongitude) {
                maxLongitude = point.getLongitude();
            }
        }
        mMapView.getOverlays().clear();
        mMapView.getOverlays().add(mOverlay);

        if (mCenter != null) {
            mMapView.getController().setCenter(mCenter);
            mMapView.getController().setZoom(mZoomLevel);
        } else if (points.size() == 0) {
            mMapView.getController().setZoom(12);
        } else {
            int latitudeSpan = toBaiduFormat(maxLatitude - minLatitude);
            int longitudeSpan = toBaiduFormat(maxLongitude - minLongitude);
            mMapView.getController().zoomToSpanWithAnimation(latitudeSpan, longitudeSpan,
                    MapController.DEFAULT_ANIMATION_TIME);

            int latitudeCenter = toBaiduFormat(latitudeSum / points.size());
            int longitudeCenter = toBaiduFormat(longitudeSum / points.size());
            GeoPoint center = new GeoPoint(latitudeCenter, longitudeCenter);
            mMapView.getController().setCenter(center);
        }
        mMapView.refresh();
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

            mCenter = mMapView.getMapCenter();
            mZoomLevel = mMapView.getZoomLevel();
        }

        mOverlay = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMapView != null) {
            mMapView.destroy();
        }
    }
}
