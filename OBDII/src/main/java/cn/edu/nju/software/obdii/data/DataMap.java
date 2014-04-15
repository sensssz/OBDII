package cn.edu.nju.software.obdii.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all data received from server
 */
public class DataMap {
    private static DataMap sInstance = new DataMap();

    private Map<String, String> mDataMap;
    private List<OnDataListener> mOnDataListeners;

    private DataMap() {
        mDataMap = new HashMap<String, String>();
        mOnDataListeners = new ArrayList<OnDataListener>();
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

    public void addOnDataListener(OnDataListener onDataListener) {
        if (!mOnDataListeners.contains(onDataListener)) {
            mOnDataListeners.add(onDataListener);
        }
    }

    public void removeOnDataListener(OnDataListener onDataListener) {
        mOnDataListeners.remove(onDataListener);
    }

    public void onDataReceived(String dataType, String dataValue) {
        mDataMap.put(dataType, dataValue);
        for (OnDataListener onDataListener : mOnDataListeners) {
            onDataListener.onDataReceived(dataType, dataValue);
        }
    }

    public String getData(DataType dataType) {
        String dataTypeName = DataConfig.getNameByType(dataType);
        return getData(dataTypeName);
    }

    public String getData(String dataType) {
        return mDataMap.get(dataType);
    }

    public interface OnDataListener {
        public void onDataReceived(String dataType, String dataValue);
    }
}
