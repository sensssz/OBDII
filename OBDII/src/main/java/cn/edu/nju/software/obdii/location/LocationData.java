package cn.edu.nju.software.obdii.location;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.obdii.data.DataMap;
import cn.edu.nju.software.obdii.util.Utilities;

/**
 */
public class LocationData {
    private List<Point2D> mLocations;
    private String mDirectory;

    public LocationData(Context context, String username) {
        mLocations = new ArrayList<Point2D>();
        mDirectory = context.getFilesDir() + "/" + Utilities.sha1(username) + "/";
        createDirIfNotExists();
        readData();
        DataMap.getInstance().addOnLocationUpdateListener(new DataMap.OnLocationDataListener() {
            @Override
            public void onLocationDataReceived(double latitude, double longitude) {
                Point2D point2D = new Point2D(latitude, longitude);
                mLocations.add(point2D);
                writeData();
            }
        });
    }

    private void createDirIfNotExists() {
        File directory = new File(mDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void readData() {
        String filename = mDirectory + "locations";
        File file = new File(filename);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    Point2D point2D = toGeoPoint(line);
                    if (point2D != null) {
                        mLocations.add(point2D);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeData() {
        String filename = mDirectory + "locations";
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            for (Point2D point : mLocations) {
                writer.println(point);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Point2D toGeoPoint(String line) {
        String[] coordinates = line.split(",");
        try {
            double latitude = Double.parseDouble(coordinates[0]);
            double longitude = Double.parseDouble(coordinates[1]);
            return new Point2D(latitude, longitude);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public List<Point2D> getLocationData() {
        return mLocations;
    }
}
