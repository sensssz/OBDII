package cn.edu.nju.software.obdii.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all data received from server
 */
public class DataMap {
//    private static final String TAG = "DataMap";

    private static DataMap sInstance = new DataMap();

    private OBDData mOBDData;
    private Map<String, String> mDataMap;
    private List<OnOBDDataListener> mOnOBDDataListeners;
    private List<OnLocationDataListener> mOnLocationDataListeners;

    private DataMap() {
        mOBDData = new OBDData();
        mDataMap = new HashMap<String, String>();
        mOnOBDDataListeners = new ArrayList<OnOBDDataListener>();
        mOnLocationDataListeners = new ArrayList<OnLocationDataListener>();
    }

    public static DataMap getInstance() {
        return sInstance;
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

    public void addOnOBDDataListener(OnOBDDataListener onOBDDataListener) {
        if (!mOnOBDDataListeners.contains(onOBDDataListener)) {
            mOnOBDDataListeners.add(onOBDDataListener);
        }
    }

    public void removeOnOBDDataListener(OnOBDDataListener onOBDDataListener) {
        mOnOBDDataListeners.remove(onOBDDataListener);
    }

    public void addOnLocationUpdateListener(OnLocationDataListener onLocationDataListener) {
        mOnLocationDataListeners.add(onLocationDataListener);
    }

    public void onDataReceived(String title, String message) {
    }

    public String getData(DataType dataType) {
        String dataTypeName = DataConfig.getNameByType(dataType);
        return getData(dataTypeName);
    }

    public String getData(String dataType) {
        return mDataMap.get(dataType);
    }

    public interface OnOBDDataListener {
        public void onOBDDataReceived(String dataType, String dataValue);
    }

    public interface OnTravelInfoListener {
        public void onTravelInfoReceived(String travelInfo);
    }

    public interface OnLocationDataListener {
        public void onLocationDataReceived(double latitude, double longitude);
    }
}
