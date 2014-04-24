package cn.edu.nju.software.obdii.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataDispatcher;
import cn.edu.nju.software.obdii.data.location.LocationDataManager;
import cn.edu.nju.software.obdii.data.location.Point2D;

/**
 * Show the trajectory of the user's car
 */
public class TrajectoryFragment extends Fragment {
    private ClickableItemizedOverlay mItemOverlay;
    private PopupOverlay mPopupOverlay;
    private MapView mMapView;
    private LocationDataManager mLocationDataManager;
    private List<GeoPoint> mGeoPoints;
    private List<String> mAddresses;
    private GeoPoint mCenter;
    private float mZoomLevel;
    private View mPopupView;
    private TextView mTimeView;
    private TextView mAddressView;

    private Animation mPopupAnimation;

    public TrajectoryFragment() {
        mGeoPoints = new ArrayList<GeoPoint>();
        mAddresses = new ArrayList<String>();
    }

    private int toBaiduFormat(double coordinate) {
        return (int) (coordinate * 1E6);
    }

    private OverlayItem toOverlayItem(GeoPoint point) {
        return new OverlayItem(point, "", "");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trajectory, container, false);

        if (mLocationDataManager == null) {
            mLocationDataManager = DataDispatcher.getInstance().getLocationData();
            toGeoPoints(mLocationDataManager.getLocationData());
            mLocationDataManager.setOnLocationListener(new LocationDataManager.OnLocationListener() {
                @Override
                public void onLocationUpdate() {
                    int size = mLocationDataManager.getLocationData().size();
                    Point2D lastPoint = mLocationDataManager.getLocationData().get(size - 1);
                    mGeoPoints.add(lastPoint.toGeoPoint());
                    mAddresses.add(null);
                    if (isVisible() && mItemOverlay != null) {
                        configMapView(true);
                    }
                }
            });
        }

        if (mPopupAnimation == null) {
            mPopupAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.pop_up);
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
        mItemOverlay = new ClickableItemizedOverlay(getResources().getDrawable(R.drawable.marker),
                mMapView);
        mPopupOverlay = new PopupOverlay(mMapView, new PopupClickListener() {
            @Override
            public void onClickedPopup(int index) {
            }
        });
        mMapView.getOverlays().clear();
        mMapView.getOverlays().add(mItemOverlay);
        mMapView.post(new Runnable() {
            @Override
            public void run() {
                if (mItemOverlay != null) {
                    configMapView(false);
                }
            }
        });

        mPopupView = inflater.inflate(R.layout.pop_up_marker, null);
        mTimeView = (TextView) mPopupView.findViewById(R.id.time);
        mAddressView = (TextView) mPopupView.findViewById(R.id.address);

        mItemOverlay.setOnItemClickedListener(new ClickableItemizedOverlay.OnItemClickedListener() {
            @Override
            public void onItemClicked(int index) {
                GeoPoint geoPoint = mGeoPoints.get(index);
                String timestamp = mLocationDataManager.getLocationData().get(index).getTimestamp();
                String address = mAddresses.get(index);
                mTimeView.setText(timestamp);
                if (address != null) {
                    mAddressView.setText(address);
                }
                mPopupOverlay.showPopup(mPopupView, geoPoint, dpToPx(12));
                mPopupView.startAnimation(mPopupAnimation);
            }
        });

        return view;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
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
            addPointToOverlay(mGeoPoints.get(size - 1));
        } else {
            addPointsToOverlay(mGeoPoints);
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

    private void toGeoPoints(List<Point2D> points) {
        mGeoPoints.clear();
        for (Point2D point2D : points) {
            mGeoPoints.add(point2D.toGeoPoint());
            mAddresses.add(null);
        }
    }

    private void addPointToOverlay(GeoPoint point) {
        mItemOverlay.addItem(toOverlayItem(point));
    }

    private void addPointsToOverlay(List<GeoPoint> points) {
        for (GeoPoint point : points) {
            addPointToOverlay(point);
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

        mItemOverlay = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMapView != null) {
            mMapView.destroy();
        }
    }
}
