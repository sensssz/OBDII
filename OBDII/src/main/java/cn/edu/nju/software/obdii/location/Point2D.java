package cn.edu.nju.software.obdii.location;

import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Represent a geographical point with a latitude and a longitude
 */
public class Point2D {
    private double mLatitude;
    private double mLongitude;

    public Point2D(double mLatitude, double mLongitude) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public GeoPoint toGeoPoint() {
        return new GeoPoint(toBaiduFormat(mLatitude), toBaiduFormat(mLongitude));
    }

    private int toBaiduFormat(double coordinate) {
        return (int) (coordinate * 1E6);
    }

    @Override
    public String toString() {
        return mLatitude + "," + mLongitude;
    }
}
