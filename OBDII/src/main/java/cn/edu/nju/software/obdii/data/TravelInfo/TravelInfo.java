package cn.edu.nju.software.obdii.data.TravelInfo;

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

    private long mStartTime;
    private long mEndTime;
    private String mDistance;
    private String mMaxSpeed;
    private String mTimeoutLength;
    private String mBrakingTimes;
    private String mUrgentBrakingTimes;
    private String mAccelerateTimes;
    private String mUrgentAccelerateTimes;
    private String mAverageSpeed;
    private String mHighestTemperature;
    private String mHighestRotateSpeed;
    private String mVoltage;
    private String mTotalOilConsumption;
    private String mAverageOilConsumption;
    private String mFatigueDrivingLength;
    private String mSerial;

    public TravelInfo() {
    }

    public TravelInfo(long mStartTime, long mEndTime, String mDistance,
                      String mMaxSpeed, String mTimeoutLength,
                      String mBrakingTimes, String mUrgentBrakingTimes,
                      String mAccelerateTimes, String mUrgentAccelerateTimes,
                      String mAverageSpeed, String mHighestTemperature,
                      String mHighestRotateSpeed, String mVoltage,
                      String mTotalOilConsumption, String mAverageOilConsumption,
                      String mFatigueDrivingLength, String mSerial) {
        init(mStartTime, mEndTime, mDistance,
                mMaxSpeed, mTimeoutLength,
                mBrakingTimes, mUrgentBrakingTimes,
                mAccelerateTimes, mUrgentAccelerateTimes,
                mAverageSpeed, mHighestTemperature,
                mHighestRotateSpeed, mVoltage,
                mTotalOilConsumption, mAverageOilConsumption,
                mFatigueDrivingLength, mSerial);
    }

    private TravelInfo(Parcel in) {
        String[] data = in.createStringArray();
        long startTime = Long.parseLong(data[0]);
        long endTime = Long.parseLong(data[1]);
        init(startTime, endTime, data[2], data[3],
                data[4], data[5], data[6], data[7], data[8],
                data[9], data[10], data[11], data[12], data[13],
                data[14], data[15], data[16]);
    }

    public void init(long mStartTime, long mEndTime, String mDistance,
                     String mMaxSpeed, String mTimeoutLength,
                     String mBrakingTimes, String mUrgentBrakingTimes,
                     String mAccelerateTimes, String mUrgentAccelerateTimes,
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
        this.mAccelerateTimes = mAccelerateTimes;
        this.mUrgentAccelerateTimes = mUrgentAccelerateTimes;
        this.mAverageSpeed = mAverageSpeed;
        this.mHighestTemperature = mHighestTemperature;
        this.mHighestRotateSpeed = mHighestRotateSpeed;
        this.mVoltage = mVoltage;
        this.mTotalOilConsumption = mTotalOilConsumption;
        this.mAverageOilConsumption = mAverageOilConsumption;
        this.mFatigueDrivingLength = mFatigueDrivingLength;
        this.mSerial = mSerial;
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
                mAccelerateTimes, mUrgentAccelerateTimes,
                mAverageSpeed, mHighestTemperature,
                mHighestRotateSpeed, mVoltage,
                mTotalOilConsumption, mAverageOilConsumption,
                mFatigueDrivingLength, mSerial});
    }
}
