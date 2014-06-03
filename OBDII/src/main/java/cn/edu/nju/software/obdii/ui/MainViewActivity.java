package cn.edu.nju.software.obdii.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
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
import android.widget.Toast;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataDispatcher;

/**
 * Main activity. User navigation drawer to navigate between fragments
 */
public class MainViewActivity extends FragmentActivity {
    private static final int PRESS_INTERVAL = 2000;
    private static final int NOTIFICATION_ID = 42;

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

    private Dialog mDialog;
    private boolean mDismissed = true;

    private long mLastPressTime = -1;

    private boolean mInBackground;

    private boolean mNotification = false;

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
            mDrawerList.setAdapter(new MyAdapter(this, R.layout.drawer_item_title,
                    mDrawerIcons, mDrawerOptions));
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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

        DataDispatcher.getInstance().setOnFaultReceivedListener(new DataDispatcher.OnFaultReceivedListener() {
            @Override
            public void onFaultReceived(String[] faults) {
                if (mDialog != null && !mDismissed) {
                    mDialog.dismiss();
                    mDismissed = true;
                }

                if (faults.length > 0) {
                    showFaultDialog(faults);
                    if (mInBackground) {
                        // If the application is put in background
                        // then show a notification
                        showFaultNotification(faults);
                    }
                }
            }
        });

        Intent intent = getIntent();
        String[] faults = intent.getStringArrayExtra("faults");
        if (faults != null) {
            showFaultDialog(faults);
            intent.removeExtra("faults");
        }

//        if (!mNotification) {
//            showFaultNotification(new String[]{
//                    "P0133:前侧含氧感知器(O2B1S1)变动率太慢",
//                    "P0153:氧传感器(O2B2S1)信号变动率太慢",
//                    "P0301:第1缸间歇性不点火",
//                    "P0303:第3缸间歇性不点火",
//                    "P0305:第5缸间歇性不点火",
//                    "P0420:前方触媒转换器效能不佳",
//                    "P0430:前方触媒转换器效能不佳"});
//            mNotification = true;
//        }
    }

    /**
     * Create a dialog that shows a list of faults occurred to the car
     */
    private void showFaultDialog(String[] faults) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.fault_warning)
                .setItems(faults, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mDialog != null) {
                            // Dismiss the previous dialog is there is one
                            mDialog.dismiss();
                            mDismissed = true;
                        }
                    }
                });
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mDismissed = false;
    }

    private void showFaultNotification(String[] faults) {
        // Create notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.fault_warning))
                        .setContentTitle(getString(R.string.fault_warning_notification))
                        .setContentText(getString(R.string.expand_for_details))
                        .setAutoCancel(true);

        // Set the big view when the notification is expanded
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.fault_warning));
        for (String fault : faults) {
            if (fault != null && fault.length() > 0) {
                inboxStyle.addLine(fault);
            }
        }
        mBuilder.setStyle(inboxStyle);

        // Start MainViewActivity when the notification is clicked
        Intent resultIntent = new Intent(this, MainViewActivity.class);
        // Put the fault data in the intent for the MainViewActivity to show 
        // after it's started
        resultIntent.putExtra("faults", faults);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Show notification
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onResume() {
        super.onResume();
        mInBackground = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mInBackground = true;
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

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (now - mLastPressTime < PRESS_INTERVAL) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_LONG).show();
        }
        mLastPressTime = now;
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
