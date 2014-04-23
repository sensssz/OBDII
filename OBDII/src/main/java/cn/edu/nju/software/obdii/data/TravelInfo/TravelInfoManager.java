package cn.edu.nju.software.obdii.data.TravelInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 */
public class TravelInfoManager {
    private static final int MAX_TRAVEL_INFO_NUMBER = 30;

    private String mUserDirectory;
    private List<TravelInfo> mTravelInfoList;
    private List<OnTravelInfoListener> onTravelInfoListeners;

    public TravelInfoManager(String userDirectory) {
        mUserDirectory = userDirectory;
        mTravelInfoList = new LinkedList<TravelInfo>();
        onTravelInfoListeners = new ArrayList<OnTravelInfoListener>();

        readData();
    }

    private void readData() {
        String filename = mUserDirectory + "travel_info";
        File file = new File(filename);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    TravelInfo travelInfo = new TravelInfo(line.split(","));
                    mTravelInfoList.add(travelInfo);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeData() {
        String filename = mUserDirectory + "travel_info";
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            for (TravelInfo travelInfo : mTravelInfoList) {
                writer.println(travelInfo);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTravelInfoReceived(TravelInfo travelInfo) {
        mTravelInfoList.add(0, travelInfo);
        int size = mTravelInfoList.size();
        while (size > MAX_TRAVEL_INFO_NUMBER) {
            mTravelInfoList.remove(--size);
        }
        writeData();

        notifyListeners(travelInfo);
    }

    private void notifyListeners(TravelInfo travelInfo) {
        for (OnTravelInfoListener onTravelInfoListener : onTravelInfoListeners) {
            onTravelInfoListener.onTravelInfoReceived(travelInfo);
        }
    }

    public List<TravelInfo> getTravelInfoList() {
        return mTravelInfoList;
    }

    public void addTravelInfoListener(OnTravelInfoListener onTravelInfoListener) {
        onTravelInfoListeners.add(onTravelInfoListener);
    }

    public interface OnTravelInfoListener {
        public void onTravelInfoReceived(TravelInfo travelInfo);
    }
}
