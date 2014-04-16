package cn.edu.nju.software.obdii.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all data received from server
 */
public class DataMap {
    private static final String TAG = "DataMap";

    private static DataMap sInstance = new DataMap();

    private Map<String, String> mDataMap;
    private List<OnNamedDataListener> mOnNamedDataListeners;
    private List<OnLocationUpdateListener> mOnLocationUpdateListeners;

    private DataMap() {
        mDataMap = new HashMap<String, String>();
        mOnNamedDataListeners = new ArrayList<OnNamedDataListener>();
        mOnLocationUpdateListeners = new ArrayList<OnLocationUpdateListener>();
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

    public void addOnNamedDataListener(OnNamedDataListener onNamedDataListener) {
        if (!mOnNamedDataListeners.contains(onNamedDataListener)) {
            mOnNamedDataListeners.add(onNamedDataListener);
        }
    }

    public void removeOnNamedDataListener(OnNamedDataListener onNamedDataListener) {
        mOnNamedDataListeners.remove(onNamedDataListener);
    }

    public void addOnLocationUpdateListener(OnLocationUpdateListener onLocationUpdateListener) {
        mOnLocationUpdateListeners.add(onLocationUpdateListener);
    }

    public void onNameDataReceived(String dataType, String dataValue) {
        mDataMap.put(dataType, dataValue);
        for (OnNamedDataListener onNamedDataListener : mOnNamedDataListeners) {
            onNamedDataListener.onNamedDataReceived(dataType, dataValue);
        }
    }

    public void onUnnamedDataReceived(String data) {
        if (data.contains(",")) {
            String[] coordinates = data.split(",");
            try {
                double latitude = Double.parseDouble(coordinates[0]);
                double longitude = Double.parseDouble(coordinates[1]);
                for (OnLocationUpdateListener onLocationUpdateListener : mOnLocationUpdateListeners) {
                    onLocationUpdateListener.onLocationUpdate(latitude, longitude);
                }
            } catch (NumberFormatException exception) {
                Log.d(TAG, data);
            }
        }
    }

    public String getData(DataType dataType) {
        String dataTypeName = DataConfig.getNameByType(dataType);
        return getData(dataTypeName);
    }

    public String getData(String dataType) {
        return mDataMap.get(dataType);
    }

    public interface OnNamedDataListener {
        public void onNamedDataReceived(String dataType, String dataValue);
    }

    public interface OnLocationUpdateListener {
        public void onLocationUpdate(double latitude, double longitude);
    }
}
