package cn.edu.nju.software.obdii.data.obd;

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

    private OnOBDUpdateListener mOnOBDUpdateListener;

    public OBDData() {
        mSpeed = "0(km/h)";
    }

    public static int dataValueToInt(String dataValue) {
        if (dataValue.length() > 0) {
            try {
                int indexOfOpenParenthesis = dataValue.indexOf("(");
                String valuePart = dataValue.substring(0, indexOfOpenParenthesis);
                return Integer.parseInt(valuePart);
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
        }
        return 0;
    }

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

    public void set(String dataType, String dataValue) {
        DataType type = DataConfig.getTypeByName(dataType);
        if (type != null) {
            switch (type) {
                case SPEED:
                    mSpeed = dataValue;
                    break;
                case VOLTAGE:
                    mVoltage = dataValue;
                    break;
                case COOLANT_TEMPERATURE:
                    mCoolantTemperature = dataValue;
                    break;
                case ROTATE_SPEED:
                    mRotateSpeed = dataValue;
                    break;
                case OIL_LEFT:
                    mOilLeft = dataValue;
                    break;
                case PRESSURE:
                    mPressure = dataValue;
                    break;
                case AIR_TEMPERATURE:
                    mAirTemperature = dataValue;
                    break;
            }
        }
    }

    public int getSpeedInInt() {
        return dataValueToInt(mSpeed);
    }

    public void setOnOBDUpdateListener(OnOBDUpdateListener onOBDUpdateListener) {
        mOnOBDUpdateListener = onOBDUpdateListener;
    }

    public interface OnOBDUpdateListener {
        public void onSpeedUpdate(String speed);

        public void onVoltageUpdate(String voltage);

        public void onCoolantTemperatureUpdate(String coolantTemperature);

        public void onRotateSpeedUpdate(String rotateSpeed);

        public void onOilLeftUpdate(String oilLeft);

        public void onPressureUpdate(String pressure);

        public void onAirTemperatureUpdate(String airTemperature);
    }
}