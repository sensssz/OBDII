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
import cn.edu.nju.software.obdii.data.DataDispatcher;
import cn.edu.nju.software.obdii.data.location.LocationDataManager;
import cn.edu.nju.software.obdii.data.location.Point2D;

/**
 * Show the trajectory of the user's car
 */
public class TrajectoryFragment extends Fragment {
    private ItemizedOverlay mOverlay;
    private MapView mMapView;
    private LocationDataManager mLocationDataManager;
    private GeoPoint mCenter;
    private float mZoomLevel;

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

        if (mLocationDataManager == null) {
            mLocationDataManager = DataDispatcher.getInstance().getLocationData();
            mLocationDataManager.setOnLocationListener(new LocationDataManager.OnLocationListener() {
                @Override
                public void onLocationUpdate() {
                    if (isVisible() && mOverlay != null) {
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
                        mLocationDataManager.getLocationData().size() > 0) {
                    mCenter = mMapView.getMapCenter();
                    mZoomLevel = mMapView.getZoomLevel();
                }
                return false;
            }
        });
        mOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.marker),
                mMapView);
        mMapView.getOverlays().clear();
        mMapView.getOverlays().add(mOverlay);
        mMapView.post(new Runnable() {
            @Override
            public void run() {
                if (mOverlay != null) {
                    configMapView(false);
                }
            }
        });

        return view;
    }

    private void configMapView(boolean newPoint) {
        List<Point2D> points = mLocationDataManager.getLocationData();
        double latitudeSum = 0;
        double longitudeSum = 0;
        int minLatitudeE6 = Integer.MAX_VALUE;
        int maxLatitudeE6 = Integer.MIN_VALUE;
        int minLongitudeE6 = Integer.MAX_VALUE;
        int maxLongitudeE6 = Integer.MIN_VALUE;
        int size = mLocationDataManager.getLocationData().size();

        if (newPoint) {
            addPointToOverlay(mLocationDataManager.getLocationData().get(size - 1));
        } else {
            addPointsToOverlay(mLocationDataManager.getLocationData());
        }

        for (Point2D point : mLocationDataManager.getLocationData()) {

            latitudeSum += point.getLatitude();
            longitudeSum += point.getLongitude();

            GeoPoint geoPoint = point.toGeoPoint();
            int latitudeE6 = geoPoint.getLatitudeE6();
            int longitudeE6 = geoPoint.getLongitudeE6();

            if (latitudeE6 < minLatitudeE6) {
                minLatitudeE6 = latitudeE6;
            }
            if (latitudeE6 > maxLatitudeE6) {
                maxLatitudeE6 = latitudeE6;
            }

            if (longitudeE6 < minLongitudeE6) {
                minLongitudeE6 = longitudeE6;
            }
            if (longitudeE6 > maxLongitudeE6) {
                maxLongitudeE6 = longitudeE6;
            }
        }

        if (mCenter != null) {
            mMapView.getController().setCenter(mCenter);
            mMapView.getController().setZoom(mZoomLevel);
        } else {
            if (mLocationDataManager.getLocationData().size() <= 1) {
                mMapView.getController().setZoom(12);
            } else {
                int latitudeSpan = maxLatitudeE6 - minLatitudeE6;
                int longitudeSpan = maxLongitudeE6 - minLongitudeE6;
                mMapView.getController().zoomToSpanWithAnimation(latitudeSpan, longitudeSpan,
                        MapController.DEFAULT_ANIMATION_TIME);
            }
            mZoomLevel = mMapView.getZoomLevel();

            if (mLocationDataManager.getLocationData().size() > 0) {
                int latitudeCenter = toBaiduFormat(latitudeSum / points.size());
                int longitudeCenter = toBaiduFormat(longitudeSum / points.size());
                GeoPoint center = new GeoPoint(latitudeCenter, longitudeCenter);
                mMapView.getController().setCenter(center);
                mCenter = center;
            }
        }
        mMapView.refresh();
    }

    private void addPointToOverlay(Point2D point) {
        mOverlay.addItem(toOverlayItem(point.toGeoPoint()));
    }

    private void addPointsToOverlay(List<Point2D> points) {
        for (Point2D point : points) {
            mOverlay.addItem(toOverlayItem(point.toGeoPoint()));
        }
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
