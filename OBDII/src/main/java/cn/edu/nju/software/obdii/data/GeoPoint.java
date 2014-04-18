package cn.edu.nju.software.obdii.data;

/**
 * Represent a geographical point with a latitude and a longitude
 */
public class GeoPoint {
    private double mLatitude;
    private double mLongitude;

    public GeoPoint(double mLatitude, double mLongitude) {
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
}
