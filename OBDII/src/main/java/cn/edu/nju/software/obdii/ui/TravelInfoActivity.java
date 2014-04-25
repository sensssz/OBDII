package cn.edu.nju.software.obdii.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.TravelInfo.TravelInfo;

/**
 */
public class TravelInfoActivity extends Activity {
    private TravelInfo mTravelInfo;
    private ListView mListView;
    private MyAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_travel_info);

        Intent intent = getIntent();
        mTravelInfo = intent.getParcelableExtra("travel_info");
        mListView = (ListView) findViewById(R.id.detail_travel_list);
        //set time and distance
        TextView startTextView = (TextView) findViewById(R.id.detail_from_time);
        startTextView.setText(format(mTravelInfo.getmStartTime()));
        TextView endTextView = (TextView) findViewById(R.id.detail_to_time);
        endTextView.setText(format(mTravelInfo.getEndTime()));
        TextView distanceTextView = (TextView) findViewById(R.id.detail_distance);
        distanceTextView.setText(mTravelInfo.getmDistance() + "km");

        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> contents = new ArrayList<String>();
        //initialize titles and contents
        titles.add(getString(R.string.max_speed));
        titles.add(getString(R.string.timeout_length));
        titles.add(getString(R.string.brake_times));
        titles.add(getString(R.string.urgent_brake_time));
        titles.add(getString(R.string.accelerate_times));
        titles.add(getString(R.string.urgent_accelerate_times));
        titles.add(getString(R.string.engine_highest_temperature));
        titles.add(getString(R.string.engine_highest_rotation_speed));
        titles.add(getString(R.string.voltage));
        titles.add(getString(R.string.total_oil_consuming));
        titles.add(getString(R.string.fatigue_driving_time));
        titles.add(getString(R.string.average_oil));
        titles.add(getString(R.string.average_speed));

        contents.add(mTravelInfo.getmMaxSpeed() + "km/h");
        contents.add(mTravelInfo.getmTimeoutLength() + "秒");
        contents.add(mTravelInfo.getmBrakingTimes() + "次");
        contents.add(mTravelInfo.getmUrgentBrakingTimes()+ "次");
        contents.add(mTravelInfo.getmAccelerateTimes()+ "次");
        contents.add(mTravelInfo.getmHighestTemperature() + "摄氏度");
        contents.add(mTravelInfo.getmHighestRotateSpeed() + "转/分钟");
        contents.add(mTravelInfo.getmVoltage() + "0.1V");
        contents.add(mTravelInfo.getmTotalOilConsumption() + "0.01升");
        contents.add(mTravelInfo.getmFatigueDrivingLength() + "min");
        contents.add(mTravelInfo.getmAverageOilConsumption() + "0.01百公里升");
        contents.add(mTravelInfo.getmAverageSpeed() + "km/h");

        mAdapter = new MyAdapter(getApplicationContext(), R.layout.detail_travel_info_list_item, titles, contents);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class MyAdapter extends ArrayAdapter<String> {
        private ArrayList<String> mTitles;
        private ArrayList<String> mContents;
        private LayoutInflater mInflater;
        private int mResourceId;
        public MyAdapter(Context context, int layout, ArrayList<String> titles, ArrayList<String> contents) {
            super(context,layout, contents);
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mResourceId = layout;
            mTitles = titles;
            mContents = contents;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(mResourceId, null);
            TextView titleView = (TextView)convertView.findViewById(R.id.detail_travel_info_list_item_title);
            titleView.setText(mTitles.get(position));
            TextView contentView = (TextView) convertView.findViewById(R.id.detail_travel_info_list_item_content);
            contentView.setText(mContents.get(position));
            return convertView;

        }
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
}
