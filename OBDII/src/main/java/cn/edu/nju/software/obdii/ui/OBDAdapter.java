package cn.edu.nju.software.obdii.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 */
public class OBDAdapter extends FragmentPagerAdapter {
    private OBDPart1Fragment mOBDPart1Fragment;
    private OBDPart2Fragment mOBDPart2Fragment;

    public OBDAdapter(FragmentManager fragmentManager, OBDPart1Fragment obdPart1Fragment,
                      OBDPart2Fragment obdPart2Fragment) {
        super(fragmentManager);

        mOBDPart1Fragment = obdPart1Fragment;
        mOBDPart2Fragment = obdPart2Fragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return mOBDPart1Fragment;
        } else if (position == 1) {
            return mOBDPart2Fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}