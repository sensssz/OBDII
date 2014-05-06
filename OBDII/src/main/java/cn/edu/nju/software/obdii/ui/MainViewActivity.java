package cn.edu.nju.software.obdii.ui;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.edu.nju.software.obdii.R;

/**
 * Main activity. User navigation drawer to navigate between fragments
 */
public class MainViewActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int[] mDrawerIcons;
    private String[] mDrawerOptions;
    private ActionBarDrawerToggle mDrawerToggle;

    private int mCurrentPosition;
    private int mNewPosition;
    private Fragment[] mFragments;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Animation mFadeOutAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerIcons = new int[]{ //set icon of each drawer item
                R.drawable.drawer_obd_data,
                R.drawable.drawer_alert_check,
                R.drawable.drawer_travel_info,
                R.drawable.drawer_car_route,
                R.drawable.drawer_statistic,
        };
        mDrawerOptions = new String[]{ //set title of each drawer item
                getString(R.string.OBD_data),
                getString(R.string.check_alert),
                getString(R.string.travel_info),
                getString(R.string.car_route),
                getString(R.string.statistics),

        };
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            mDrawerList.setAdapter(new MyAdapter(
                    getActionBar().getThemedContext(), R.layout.drawer_item_title,
                    mDrawerIcons, mDrawerOptions));
        }

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.apptheme_ic_navigation_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

                changeFragment(mNewPosition);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mCurrentPosition = -1;
        mFragments = new Fragment[mDrawerIcons.length];

        mFragments[0] = new OBDFragment();
        mFragments[1] = new AlertCheckFragment();
        mFragments[2] = new TravelInfoFragment();
        mFragments[3] = new TrajectoryFragment();
        mFragments[4] = new StatisticsFragment();
        if (savedInstanceState == null) {
            selectItem(0);
        }

        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerOptions[position]);
        if (mCurrentPosition != -1 && mCurrentPosition != position) {
            if (mFragments[mCurrentPosition] != null) {
                mFragments[mCurrentPosition].getView().startAnimation(mFadeOutAnimation);
            }
        } else if (mCurrentPosition == -1) {
            changeFragment(position);
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void changeFragment(int position) {
        if (mCurrentPosition != position) {
            if (mFragments[position] != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, mFragments[position]).commit();

                mCurrentPosition = position;
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            getActionBar().setTitle(mTitle);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public class MyAdapter extends ArrayAdapter<String> {
        private int[] mImgs;
        private String[] mTexts;
        private int mViewSourceId;
        private LayoutInflater mInflater;

        public MyAdapter(Context context, int viewResourceId, int[] imgs, String[] texts) {
            super(context, viewResourceId, texts);
            mImgs = imgs;
            mTexts = texts;
            mViewSourceId = viewResourceId;
            mInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(mViewSourceId, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.option_icon);
            imageView.setImageResource(mImgs[position]);
            TextView textView = (TextView) convertView.findViewById(R.id.option_text);
            textView.setText(mTexts[position]);
            return convertView;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mNewPosition = position;
            selectItem(position);
        }
    }
}
