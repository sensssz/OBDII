package cn.edu.nju.software.obdii.data.TravelInfo;

import java.util.LinkedList;
import java.util.List;

/**
 */
public class TravelInfoManager {
    private static final int MAX_TRAVEL_INFO_NUMBER = 30;
    private static TravelInfoManager ourInstance = new TravelInfoManager();

    private static List<TravelInfo> mTravelInfoList;

    private TravelInfoManager() {
        mTravelInfoList = new LinkedList<TravelInfo>();
    }

    public static TravelInfoManager getInstance() {
        return ourInstance;
    }

    public void addTravelInfo(TravelInfo travelInfo) {
        mTravelInfoList.add(0, travelInfo);
        int size = mTravelInfoList.size();
        while (size > MAX_TRAVEL_INFO_NUMBER) {
            mTravelInfoList.remove(--size);
        }
    }

    public List<TravelInfo> getTravelInfoList() {
        return mTravelInfoList;
    }
}
