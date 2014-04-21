package cn.edu.nju.software.obdii.data;

/**
 * Represent OBD data
 */
public class OBDData {
    private String mSpeed;
    private String mVoltage;
    private String mCoolantTemperature;
    private String mRotateSpeed;
    private String mOilLeft;
    private String mPressure;
    private String mAirTemperature;

    public String getmSpeed() {
        return mSpeed;
    }

    public void setmSpeed(String mSpeed) {
        this.mSpeed = mSpeed;
    }

    public String getmVoltage() {
        return mVoltage;
    }

    public void setmVoltage(String mVoltage) {
        this.mVoltage = mVoltage;
    }

    public String getmCoolantTemperature() {
        return mCoolantTemperature;
    }

    public void setmCoolantTemperature(String mCoolantTemperature) {
        this.mCoolantTemperature = mCoolantTemperature;
    }

    public String getmRotateSpeed() {
        return mRotateSpeed;
    }

    public void setmRotateSpeed(String mRotateSpeed) {
        this.mRotateSpeed = mRotateSpeed;
    }

    public String getmOilLeft() {
        return mOilLeft;
    }

    public void setmOilLeft(String mOilLeft) {
        this.mOilLeft = mOilLeft;
    }

    public String getmPressure() {
        return mPressure;
    }

    public void setmPressure(String mPressure) {
        this.mPressure = mPressure;
    }

    public String getmAirTemperature() {
        return mAirTemperature;
    }

    public void setmAirTemperature(String mAirTemperature) {
        this.mAirTemperature = mAirTemperature;
    }
}