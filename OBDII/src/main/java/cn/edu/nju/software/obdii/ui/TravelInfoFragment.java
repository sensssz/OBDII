package cn.edu.nju.software.obdii.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataDispatcher;
import cn.edu.nju.software.obdii.data.TravelInfo.TravelInfo;
import cn.edu.nju.software.obdii.data.TravelInfo.TravelInfoManager;

/**
 * Display a list of the travel info list
 */
public class TravelInfoFragment extends Fragment {
    private ListView mTravelInfoList;
    private SimpleAdapter mAdapter;
    private List<Map<String, String>> mAdapterData;
    private List<TravelInfo> mTravelInfo;

    public TravelInfoFragment() {
        mAdapterData = new ArrayList<Map<String, String>>();
        mTravelInfo = DataDispatcher.getInstance()
                .getTravelInfoManager().getTravelInfoList();

        initAdapterData();

        DataDispatcher.getInstance().getTravelInfoManager()
                .addTravelInfoListener(new TravelInfoManager.OnTravelInfoListener() {
                    @Override
                    public void onTravelInfoReceived(TravelInfo travelInfo) {
                        mAdapterData.add(travelInfoToMap(travelInfo));
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void initAdapterData() {
        for (TravelInfo travelInfo : mTravelInfo) {
            mAdapterData.add(travelInfoToMap(travelInfo));
        }
    }

    private Map<String, String> travelInfoToMap(TravelInfo travelInfo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("from_time", format(travelInfo.getStartTime()));
        map.put("to_time", format(travelInfo.getEndTime()));
        return map;
    }

    private String format(String time) {
        StringBuilder stringBuilder = new StringBuilder("20");
        stringBuilder.append(time.substring(0, 2)).append("/")
                .append(time.substring(2, 4)).append("/")
                .append(time.substring(4, 6)).append("\n")
                .append(time.substring(6, 8)).append(":")
                .append(time.substring(8, 10)).append(":")
                .append(time.substring(10, 12));
        return stringBuilder.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_travel_info, null);

        mTravelInfoList = (ListView) view.findViewById(R.id.travel_info_list);
        mTravelInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TravelInfo travelInfo = mTravelInfo.get(position);
                Intent intent = new Intent(getActivity(), TravelInfoActivity.class);
                intent.putExtra("travel_info", travelInfo);
                startActivity(intent);
            }
        });

        if (mAdapter == null) {
            mAdapter = new SimpleAdapter(getActivity(), mAdapterData,
                    R.layout.item_travel_info,
                    new String[]{"from_time", "to_time"},
                    new int[]{R.id.from_time, R.id.to_time});
        }
        mTravelInfoList.setAdapter(mAdapter);

        return view;
    }
}
