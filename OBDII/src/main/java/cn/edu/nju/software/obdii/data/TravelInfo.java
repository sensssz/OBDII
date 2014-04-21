package cn.edu.nju.software.obdii.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 */
public class TravelInfo implements Parcelable {
    public static final Parcelable.Creator<TravelInfo> CREATOR
            = new Parcelable.Creator<TravelInfo>() {
        public TravelInfo createFromParcel(Parcel in) {
            return new TravelInfo(in);
        }

        public TravelInfo[] newArray(int size) {
            return new TravelInfo[size];
        }
    };
    private static final int NUMBER_OF_FILEDS = 17;
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

    public TravelInfo(long mStartTime, long mEndTime, String mDistance,
                      String mMaxSpeed, String mTimeoutLength,
                      String mBrakingTimes, String mUrgentBrakingTimes,
                      String mAccerlerateTimes, String mUrgentAccerlerateTimes,
                      String mAverageSpeed, String mHighestTemperature,
                      String mHighestRotateSpeed, String mVoltage,
                      String mTotalOilConsumption, String mAverageOilConsumption,
                      String mFatigueDrivingLength, String mSerial) {
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mDistance = mDistance;
        this.mMaxSpeed = mMaxSpeed;
        this.mTimeoutLength = mTimeoutLength;
        this.mBrakingTimes = mBrakingTimes;
        this.mUrgentBrakingTimes = mUrgentBrakingTimes;
        this.mAccerlerateTimes = mAccerlerateTimes;
        this.mUrgentAccerlerateTimes = mUrgentAccerlerateTimes;
        this.mAverageSpeed = mAverageSpeed;
        this.mHighestTemperature = mHighestTemperature;
        this.mHighestRotateSpeed = mHighestRotateSpeed;
        this.mVoltage = mVoltage;
        this.mTotalOilConsumption = mTotalOilConsumption;
        this.mAverageOilConsumption = mAverageOilConsumption;
        this.mFatigueDrivingLength = mFatigueDrivingLength;
        this.mSerial = mSerial;
    }

    private TravelInfo(Parcel in) {
        String[] data = in.createStringArray();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(new String[]{
                mStartTime + "", mEndTime + "",
                mDistance, mMaxSpeed, mTimeoutLength,
                mBrakingTimes, mUrgentBrakingTimes,
                mAccerlerateTimes, mUrgentAccerlerateTimes,
                mAverageSpeed, mHighestTemperature,
                mHighestRotateSpeed, mVoltage,
                mTotalOilConsumption, mAverageOilConsumption,
                mFatigueDrivingLength, mSerial});
    }
}
