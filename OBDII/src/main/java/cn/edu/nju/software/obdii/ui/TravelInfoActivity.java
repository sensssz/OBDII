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
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> contents = new ArrayList<String>();
        //initialize titles and contents


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
}
