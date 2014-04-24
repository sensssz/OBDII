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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataDispatcher;
import cn.edu.nju.software.obdii.data.TravelInfo.TravelInfo;

/**
 * Display a list of the travel info list
 */
public class TravelInfoFragment extends Fragment {
    private ListView mTravelInfoList;
    private SimpleAdapter adapter;
    private List<Map<String, String>> mAdapterData;
    private List<TravelInfo> mTravelInfo;

    public TravelInfoFragment() {
        mAdapterData = new ArrayList<Map<String, String>>();
        mTravelInfo = DataDispatcher.getInstance()
                .getTravelInfoManager().getTravelInfoList();
    }

    public void initAdapterData() {
        for (TravelInfo travelInfo : mTravelInfo) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("start", travelInfo.getStartTime());
            map.put("end", travelInfo.getEndTime());
            mAdapterData.add(map);
        }
    }

    private String format(String time) {
        String dateFormat = "yyyy/MM/dd\nHH:mm:ss";
        DateFormat formatter = DateFormat.getDateInstance();
        long timeInMilliSeconds = Long.parseLong(time) * 1000;
        Date date = new Date(timeInMilliSeconds);
        return formatter.format(date);
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

        if (adapter == null) {
            adapter = new SimpleAdapter(getActivity(), mAdapterData,
                    R.layout.item_travel_info,
                    new String[]{"from_time", "to_time"},
                    new int[]{R.id.from_time, R.id.to_time});
        }
        mTravelInfoList.setAdapter(adapter);

        return view;
    }
}
