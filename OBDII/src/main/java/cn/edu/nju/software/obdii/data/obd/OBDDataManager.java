package cn.edu.nju.software.obdii.data.obd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OBDDataManager {
    private String mUserDirectory;
    private OBDData mOBDData;

    public OBDDataManager(String userDirectory) {
        mUserDirectory = userDirectory;
        readData();
    }

    private void readData() {
        String filename = mUserDirectory + "obd";
        File file = new File(filename);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                String line = reader.readLine();
                mOBDData = new OBDData(line);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mOBDData = new OBDData();
        }
    }

    private void writeData() {
        String filename = mUserDirectory + "obd";
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            writer.println(mOBDData);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onOBDDataUpdate(String dataType, String dataValue, String time) {
        mOBDData.set(dataType, dataValue, time);
        writeData();
    }

    public OBDData getOBDData() {
        return mOBDData;
    }
}
