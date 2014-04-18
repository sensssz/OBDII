package cn.edu.nju.software.obdii.data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.obdii.util.Utilities;

/**
 */
public class LocationData implements DataMap.OnLocationUpdateListener {
    private Context mContext;
    private List<GeoPoint> mLocations;
    private String directory;

    public LocationData(Context context, String username) {
        mContext = context;
        mLocations = new ArrayList<GeoPoint>();
        directory = context.getFilesDir() + "/" + Utilities.MD5(username) + "/";
        readData();
    }

    private void readData() {
        String filename = directory + "locations";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    mContext.openFileInput(filename)));
            String line = null;
            while ((line = reader.readLine()) != null) {
                GeoPoint geoPoint = toGeoPoint(line);
                if (geoPoint != null) {
                    mLocations.add(geoPoint);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GeoPoint toGeoPoint(String line) {
        String[] coordinates = line.split(",");
        try {
            double latitude = Double.parseDouble(coordinates[0]);
            double longitude = Double.parseDouble(coordinates[1]);
            return new GeoPoint(latitude, longitude);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public List<GeoPoint> getLocationData() {
        return mLocations;
    }

    @Override
    public void onLocationUpdate(double latitude, double longitude) {
        GeoPoint geoPoint = new GeoPoint(latitude, longitude);
        mLocations.add(geoPoint);
    }
}
