package cn.edu.nju.software.obdii.data.obd;

/**
 * Represent OBD data
 */
public class OBDData {
    private int mSpeed;
    private int mVoltage;
    private int mCoolantTemperature;
    private int mRotateSpeed;
    private int mOilLeft;
    private int mPressure;
    private int mAirTemperature;

    private OnOBDUpdateListener mOnOBDUpdateListener;

    public OBDData() {
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

    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int mSpeed) {
        this.mSpeed = mSpeed;
    }

    public int getVoltage() {
        return mVoltage;
    }

    public void setVoltage(int mVoltage) {
        this.mVoltage = mVoltage;
    }

    public int getCoolantTemperature() {
        return mCoolantTemperature;
    }

    public void setCoolantTemperature(int mCoolantTemperature) {
        this.mCoolantTemperature = mCoolantTemperature;
    }

    public int getRotateSpeed() {
        return mRotateSpeed;
    }

    public void setRotateSpeed(int mRotateSpeed) {
        this.mRotateSpeed = mRotateSpeed;
    }

    public int getOilLeft() {
        return mOilLeft;
    }

    public void setOilLeft(int mOilLeft) {
        this.mOilLeft = mOilLeft;
    }

    public int getPressure() {
        return mPressure;
    }

    public void setPressure(int mPressure) {
        this.mPressure = mPressure;
    }

    public int getAirTemperature() {
        return mAirTemperature;
    }

    public void setAirTemperature(int mAirTemperature) {
        this.mAirTemperature = mAirTemperature;
    }

    public void set(String dataType, String dataValue) {
        int value = dataValueToInt(dataValue);
        DataType type = DataConfig.getTypeByName(dataType);
        if (type != null) {
            switch (type) {
                case SPEED:
                    mSpeed = value;
                    if (mOnOBDUpdateListener != null) {
                        mOnOBDUpdateListener.onSpeedUpdate(value);
                    }
                    break;
                case VOLTAGE:
                    mVoltage = value;
                    break;
                case COOLANT_TEMPERATURE:
                    mCoolantTemperature = value;
                    break;
                case ROTATE_SPEED:
                    mRotateSpeed = value;
                    break;
                case OIL_LEFT:
                    mOilLeft = value;
                    break;
                case PRESSURE:
                    mPressure = value;
                    break;
                case AIR_TEMPERATURE:
                    mAirTemperature = value;
                    break;
            }
        }
    }

    public void setOnOBDUpdateListener(OnOBDUpdateListener onOBDUpdateListener) {
        mOnOBDUpdateListener = onOBDUpdateListener;
    }

    public interface OnOBDUpdateListener {
        public void onSpeedUpdate(int speed);

        public void onVoltageUpdate(int voltage);

        public void onCoolantTemperatureUpdate(int coolantTemperature);

        public void onRotateSpeedUpdate(int rotateSpeed);

        public void onOilLeftUpdate(int oilLeft);

        public void onPressureUpdate(int pressure);

        public void onAirTemperatureUpdate(int airTemperature);
    }
}