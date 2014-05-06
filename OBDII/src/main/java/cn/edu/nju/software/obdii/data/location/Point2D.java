package cn.edu.nju.software.obdii.data.location;

import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Represent a geographical point with a latitude and a longitude
 */
public class Point2D {
    private int mLatitudeE6;
    private int mLongitudeE6;
    private String mTimestamp;

    public Point2D(int latitudeE6, int longitudeE6, String time) {
        mLatitudeE6 = latitudeE6;
        mLongitudeE6 = longitudeE6;
        mTimestamp = time;
    }

    public Point2D(GeoPoint geoPoint, String time) {
        mLatitudeE6 = geoPoint.getLatitudeE6();
        mLongitudeE6 = geoPoint.getLongitudeE6();
        mTimestamp = time;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public GeoPoint toGeoPoint() {
        return new GeoPoint(mLatitudeE6, mLongitudeE6);
    }

    @Override
    public String toString() {
        return mLatitudeE6 + "," + mLongitudeE6 + "," + mTimestamp;
    }

    public int getLatitudeE6() {
        return mLatitudeE6;
    }

    public int getLongitudeE6() {
        return mLongitudeE6;
    }
}
