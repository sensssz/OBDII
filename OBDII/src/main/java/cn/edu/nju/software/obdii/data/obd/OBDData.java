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

    private String mTime;

    private OnOBDUpdateListener mOnOBDUpdateListener;

    public OBDData() {
    }

    public OBDData(String line) {
        String[] data = line.split(",");
        mSpeed = Integer.parseInt(data[0]);
        mVoltage = Integer.parseInt(data[1]);
        mCoolantTemperature = Integer.parseInt(data[2]);
        mRotateSpeed = Integer.parseInt(data[3]);
        mOilLeft = Integer.parseInt(data[4]);
        mPressure = Integer.parseInt(data[5]);
        mAirTemperature = Integer.parseInt(data[6]);
        mTime = data[7];
    }

    public static int dataValueToInt(String dataValue) {
        if (dataValue.length() > 0) {
            try {
                int indexOfOpenParenthesis = dataValue.indexOf("(");
                String valuePart = dataValue.substring(0, indexOfOpenParenthesis);
                return Integer.parseInt(valuePart);
            } catch (NumberFormatException exception) {
                return 0;
            }
        }
        return 0;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public int getVoltage() {
        return mVoltage;
    }

    public int getCoolantTemperature() {
        return mCoolantTemperature;
    }

    public int getRotateSpeed() {
        return mRotateSpeed;
    }

    public int getOilLeft() {
        return mOilLeft;
    }

    public int getPressure() {
        return mPressure;
    }

    public int getAirTemperature() {
        return mAirTemperature;
    }

    public String getTime() {
        return mTime;
    }

    public void set(String dataType, String dataValue, String time) {
        mTime = time;
        int value = dataValueToInt(dataValue);
        // To avoid using a series of if statements, first convert the 
        // dataType String to a DataType enum, and then use the switch statement
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
                    if (mOnOBDUpdateListener != null) {
                        mOnOBDUpdateListener.onVoltageUpdate(value);
                    }
                    break;
                case COOLANT_TEMPERATURE:
                    mCoolantTemperature = value;
                    if (mOnOBDUpdateListener != null) {
                        mOnOBDUpdateListener.onCoolantTemperatureUpdate(value);
                    }
                    break;
                case ROTATE_SPEED:
                    mRotateSpeed = value;
                    if (mOnOBDUpdateListener != null) {
                        mOnOBDUpdateListener.onRotateSpeedUpdate(value);
                    }
                    break;
                case OIL_LEFT:
                    mOilLeft = value;
                    if (mOnOBDUpdateListener != null) {
                        mOnOBDUpdateListener.onOilLeftUpdate(value);
                    }
                    break;
                case PRESSURE:
                    mPressure = value;
                    if (mOnOBDUpdateListener != null) {
                        mOnOBDUpdateListener.onPressureUpdate(value);
                    }
                    break;
                case AIR_TEMPERATURE:
                    mAirTemperature = value;
                    if (mOnOBDUpdateListener != null) {
                        mOnOBDUpdateListener.onAirTemperatureUpdate(value);
                    }
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return mSpeed + "," + mVoltage + "," + mCoolantTemperature + ","
                + mRotateSpeed + "," + mOilLeft + "," + mPressure + ","
                + mAirTemperature + "," + mTime;

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