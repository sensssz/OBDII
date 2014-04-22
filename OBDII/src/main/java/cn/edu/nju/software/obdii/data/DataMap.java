package cn.edu.nju.software.obdii.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.nju.software.obdii.data.TravelInfo.TravelInfo;
import cn.edu.nju.software.obdii.data.TravelInfo.TravelInfoManager;

/**
 * Stores all data received from server
 */
public class DataMap {
    //    private static final String TAG = "DataMap";
    private static final String LOCATION = "0004";
    private static final String ERROR_CODE = "0005";
    private static final String TRAVEL_INFO = "0007";
    private static final String OBD = "0008";
    private static final String STATUS = "000b";

    private static DataMap sInstance = new DataMap();

    private OBDData mOBDData;
    private Map<String, String> mDataMap;
    private List<OnLocationDataListener> mOnLocationDataListeners;
    private List<OnErrorCodeListener> mOnErrorCodeListeners;
    private List<OnTravelInfoListener> mOnTravelInfoListeners;
    private List<OnOBDDataListener> mOnOBDDataListeners;
    private List<OnStatusListener> mOnStatusListeners;

    private DataMap() {
        mOBDData = new OBDData();
        mDataMap = new HashMap<String, String>();
        mOnLocationDataListeners = new ArrayList<OnLocationDataListener>();
        mOnErrorCodeListeners = new ArrayList<OnErrorCodeListener>();
        mOnTravelInfoListeners = new ArrayList<OnTravelInfoListener>();
        mOnOBDDataListeners = new ArrayList<OnOBDDataListener>();
        mOnStatusListeners = new ArrayList<OnStatusListener>();
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

    public void addOnLocationDataListener(OnLocationDataListener onLocationDataListener) {
        mOnLocationDataListeners.add(onLocationDataListener);
    }

    public void addOnErrorCodeListener(OnErrorCodeListener onErrorCodeListener) {
        mOnErrorCodeListeners.add(onErrorCodeListener);
    }

    public void addOnTravelInfoListener(OnTravelInfoListener onTravelInfoListener) {
        mOnTravelInfoListeners.add(onTravelInfoListener);
    }

    public void addOnOBDDataListener(OnOBDDataListener onOBDDataListener) {
        if (!mOnOBDDataListeners.contains(onOBDDataListener)) {
            mOnOBDDataListeners.add(onOBDDataListener);
        }
    }

    public void addOnStatusListener(OnStatusListener onStatusListener) {
        mOnStatusListeners.add(onStatusListener);
    }

    public void onDataReceived(String title, String message) {
        if (title.startsWith(LOCATION)) {
            handleLocationData(message);
        } else if (title.startsWith(ERROR_CODE)) {
            handleErrorCode(message);
        } else if (title.startsWith(TRAVEL_INFO)) {
        } else if (title.startsWith(OBD)) {
        } else if (title.startsWith(STATUS)) {
        }
    }

    private void handleLocationData(String message) {
        String[] coordinates = message.split(",");
        double latitude = Double.parseDouble(coordinates[0]);
        double longitude = Double.parseDouble(coordinates[1]);
        for (OnLocationDataListener onLocationDataListener : mOnLocationDataListeners) {
            onLocationDataListener.onLocationDataReceived(latitude, longitude);
        }
    }

    private void handleErrorCode(String message) {
        String[] errorMessageParts = message.split(":");
        for (OnErrorCodeListener onErrorCodeListener : mOnErrorCodeListeners) {
            onErrorCodeListener.onErrorCodeReceived(errorMessageParts[0], errorMessageParts[1]);
        }
    }

    private void handleTravelInfo(String message) {
        String[] travelInfoData = message.split(";");
        TravelInfoManager.getInstance().addTravelInfo(new TravelInfo(travelInfoData));
    }

    public String getData(DataType dataType) {
        String dataTypeName = DataConfig.getNameByType(dataType);
        return getData(dataTypeName);
    }

    public String getData(String dataType) {
        return mDataMap.get(dataType);
    }

    public interface OnLocationDataListener {
        public void onLocationDataReceived(double latitude, double longitude);
    }

    public interface OnErrorCodeListener {
        public void onErrorCodeReceived(String errorCode, String errorMessage);
    }

    public interface OnTravelInfoListener {
        public void onTravelInfoReceived(String travelInfo);
    }

    public interface OnOBDDataListener {
        public void onOBDDataReceived(String dataType, String dataValue);
    }

    public interface OnStatusListener {
        public void onStatusChanged(String status);
    }
}
