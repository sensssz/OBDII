package cn.edu.nju.software.obdii.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
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
    private ProgressBar mQueryProgress;

    private int mCurrentSelected;

    public TrajectoryFragment() {
        mGeoPoints = new ArrayList<GeoPoint>();
        mAddresses = new ArrayList<String>();
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
        mQueryProgress = (ProgressBar) mPopupView.findViewById(R.id.query_progress);

        mItemOverlay.setOnItemClickedListener(new ClickableItemizedOverlay.OnItemClickedListener() {
            @Override
            public void onItemClicked(final int index) {
                mCurrentSelected = index;
                final GeoPoint geoPoint = mGeoPoints.get(index);
                String timestamp = mLocationDataManager.getLocationData().get(index).getTimestamp();
                String address = mAddresses.get(index);
                mTimeView.setText(timestamp);
                if (address != null) {
                    mQueryProgress.setVisibility(View.INVISIBLE);
                    mAddressView.setText(address);
                } else {
                    mAddressView.setText("");
                    mQueryProgress.setVisibility(View.VISIBLE);
                    MKSearch search = new MKSearch();
                    search.init(((OBDApplication) getActivity().getApplication())
                                    .getMapManager(),
                            new MKSearchListener() {
                                @Override
                                public void onGetPoiResult(MKPoiResult mkPoiResult, int i, int i2) {
                                }

                                @Override
                                public void onGetTransitRouteResult(MKTransitRouteResult mkTransitRouteResult, int i) {
                                }

                                @Override
                                public void onGetDrivingRouteResult(MKDrivingRouteResult mkDrivingRouteResult, int i) {
                                }

                                @Override
                                public void onGetWalkingRouteResult(MKWalkingRouteResult mkWalkingRouteResult, int i) {
                                }

                                @Override
                                public void onGetAddrResult(MKAddrInfo mkAddrInfo, int errorNumber) {
                                    if (errorNumber == 0) {
                                        mAddresses.set(index, mkAddrInfo.strAddr);
                                        if (mCurrentSelected == index) {
                                            mQueryProgress.setVisibility(View.INVISIBLE);
                                            mAddressView.setText(mkAddrInfo.strAddr);
                                            mPopupOverlay.hidePop();
                                            mPopupOverlay.showPopup(mPopupView, geoPoint, dpToPx(12));
                                        }
                                    } else {
                                        Log.d("OBDII", "error occurred");
                                    }
                                }

                                @Override
                                public void onGetBusDetailResult(MKBusLineResult mkBusLineResult, int i) {
                                }

                                @Override
                                public void onGetSuggestionResult(MKSuggestionResult mkSuggestionResult, int i) {
                                }

                                @Override
                                public void onGetPoiDetailSearchResult(int i, int i2) {
                                }

                                @Override
                                public void onGetShareUrlResult(MKShareUrlResult mkShareUrlResult, int i, int i2) {
                                }
                            }
                    );
                    search.reverseGeocode(geoPoint);
                }
                mPopupOverlay.showPopup(mPopupView, geoPoint, dpToPx(12));
            }
        });

        mMapView.regMapTouchListner(new MKMapTouchListener() {
            @Override
            public void onMapClick(GeoPoint geoPoint) {
                mPopupOverlay.hidePop();
                mCurrentSelected = -1;
            }

            @Override
            public void onMapDoubleClick(GeoPoint geoPoint) {
            }

            @Override
            public void onMapLongClick(GeoPoint geoPoint) {
            }
        });

        return view;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void configMapView(boolean newPoint) {
        List<Point2D> points = mLocationDataManager.getLocationData();
        double latitudeSum = 0;
        double longitudeSum = 0;
        int size = mLocationDataManager.getLocationData().size();

        if (newPoint) {
            addPointToOverlay(mGeoPoints.get(size - 1));
        } else {
            addPointsToOverlay(mGeoPoints);
        }

        for (Point2D point : mLocationDataManager.getLocationData()) {
            latitudeSum += point.getLatitudeE6();
            longitudeSum += point.getLongitudeE6();
        }

        if (mCenter != null) {
            mMapView.getController().setCenter(mCenter);
            mMapView.getController().setZoom(mZoomLevel);
        } else {
            if (mLocationDataManager.getLocationData().size() <= 1) {
                mMapView.getController().setZoom(12);
            }
            mZoomLevel = mMapView.getZoomLevel();

            if (mLocationDataManager.getLocationData().size() > 0) {
                int latitudeCenter = (int) (latitudeSum / points.size());
                int longitudeCenter = (int) (longitudeSum / points.size());
                GeoPoint center = new GeoPoint(latitudeCenter, longitudeCenter);
                mMapView.getController().setCenter(center);
                mMapView.getController().setZoom(10);
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
