package cn.edu.nju.software.obdii.data.location;

import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Represent a geographical point with a latitude and a longitude
 */
public class Point2D {
    private double mLatitude;
    private double mLongitude;
    private String mTimestamp;

    public Point2D(double latitude, double longitude, String time) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mTimestamp = time;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public GeoPoint toGeoPoint() {
        return new GeoPoint(toBaiduFormat(mLatitude), toBaiduFormat(mLongitude));
    }

    private int toBaiduFormat(double coordinate) {
        return (int) (coordinate * 1E6);
    }

    @Override
    public String toString() {
        return mLatitude + "," + mLongitude + "," + mTimestamp;
    }
}
