package cn.edu.nju.software.obdii.data;

import android.content.Context;

import java.io.File;

import cn.edu.nju.software.obdii.data.TravelInfo.TravelInfo;
import cn.edu.nju.software.obdii.data.TravelInfo.TravelInfoManager;
import cn.edu.nju.software.obdii.data.location.LocationDataManager;
import cn.edu.nju.software.obdii.data.obd.OBDData;
import cn.edu.nju.software.obdii.util.Utilities;

/**
 * Stores all data received from server
 */
public class DataDispatcher {
    //    private static final String TAG = "DataMap";
    private static final String LOCATION = "0004";
    private static final String ERROR_CODE = "0005";
    private static final String TRAVEL_INFO = "0007";
    private static final String OBD = "0008";

    private static DataDispatcher sInstance = new DataDispatcher();

    private OBDData mOBDData;
    private LocationDataManager mLocationDataManager;
    private TravelInfoManager mTravelInfoManager;
    private OnFaultReceivedListener mOnFaultReceivedListener;

    private DataDispatcher() {
        mOBDData = new OBDData();
    }

    public static DataDispatcher getInstance() {
        return sInstance;
    }

    public void setUsername(Context context, String username) {
        String userDirectory = context.getFilesDir() + "/" + Utilities.sha1(username) + "/";
        createDirIfNotExists(userDirectory);
        mLocationDataManager = new LocationDataManager(userDirectory);
        mTravelInfoManager = new TravelInfoManager(userDirectory);
    }

    private void createDirIfNotExists(String userDirectory) {
        File directory = new File(userDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void onDataReceived(String title, String message) {
        if (title.startsWith(LOCATION)) {
            handleLocationData(title, message);
        } else if (title.startsWith(ERROR_CODE)) {
            handleFaults(message);
        } else if (title.startsWith(TRAVEL_INFO)) {
            handleTravelInfo(message);
        } else if (title.startsWith(OBD)) {
            handleOBDInfo(message);
        }
    }

    private void handleLocationData(String title, String message) {
        String[] coordinates = message.split(",");
        double latitude = Double.parseDouble(coordinates[0]);
        double longitude = Double.parseDouble(coordinates[1]);
        String timestamp = getTimestamp(title);
        if (mLocationDataManager != null) {
            mLocationDataManager.onLocationReceived(latitude, longitude, timestamp);
        }
    }

    private String getTimestamp(String title) {
        int indexOfLeft = title.indexOf("(");
        int indexOfRight = title.indexOf(")");
        return title.substring(indexOfLeft + 1, indexOfRight);
    }

    private void handleFaults(String message) {
        if (mOnFaultReceivedListener != null) {
            String[] faults = message.split(";");
            mOnFaultReceivedListener.onFaultReceived(faults);
        }
    }

    private void handleTravelInfo(String message) {
        String[] travelInfoData = message.split(";");
        if (mTravelInfoManager != null) {
            mTravelInfoManager.onTravelInfoReceived(new TravelInfo(travelInfoData));
        }
    }

    private void handleOBDInfo(String message) {
        String[] data = message.split(":");
        mOBDData.set(data[0], data[1]);
    }

    public LocationDataManager getLocationData() {
        return mLocationDataManager;
    }

    public TravelInfoManager getTravelInfoManager() {
        return mTravelInfoManager;
    }

    public OBDData getOBDData() {
        return mOBDData;
    }

    public void setOnFaultReceivedListener(OnFaultReceivedListener onFaultReceivedListener) {
        this.mOnFaultReceivedListener = onFaultReceivedListener;
    }

    public interface OnFaultReceivedListener {
        public void onFaultReceived(String[] faults);
    }
}
