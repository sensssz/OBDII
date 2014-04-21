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
import java.util.List;
import java.util.Map;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataMap;
import cn.edu.nju.software.obdii.data.TravelInfo;

/**
 * Display a list of the travel info list
 */
public class TravelInfoFragment extends Fragment {
    private ListView mTravelInfoList;
    private SimpleAdapter adapter;
    private List<Map<String, String>> mData;
    private List<String> mTravelInfo;

    public TravelInfoFragment() {
        mData = new ArrayList<Map<String, String>>();
        mTravelInfo = new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.id.travel_info_list, container);

        mTravelInfoList = (ListView) view.findViewById(R.id.travel_info_list);
        mTravelInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String travelInfo = mTravelInfo.get(position);
                Intent intent = new Intent(getActivity(), TravelInfoActivity.class);
                intent.putExtra("travel_info", travelInfo);
                startActivity(intent);
            }
        });

        if (adapter == null) {
            adapter = new SimpleAdapter(getActivity(), mData,
                    R.layout.item_travel_info,
                    new String[]{"from_time", "to_time"},
                    new int[]{R.id.from_time, R.id.to_time});
        }
        mTravelInfoList.setAdapter(adapter);

        return view;
    }
}
