package cn.edu.nju.software.obdii.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 */
public class TravelInfo implements Parcelable {
    private long mStartTime;
    private long mEndTime;
    private String mDistance;
    private String mMaxSpeed;
    private String mTimeoutLength;
    private String mBrakingTimes;
    private String mUrgentBrakingTimes;
    private String mAccerlerateTimes;
    private String mUrgentAccerlerateTimes;
    private String mAverageSpeed;
    private String mHighestTemperature;
    private String mHighestRotateSpeed;
    private String mVoltage;
    private String mTotalOilConsumption;
    private String mAverageOilConsumption;
    private String mFatigueDrivingLength;
    private String mSerial;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
