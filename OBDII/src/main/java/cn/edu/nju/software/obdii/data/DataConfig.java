package cn.edu.nju.software.obdii.data;

import java.util.HashMap;

/**
 * A type to name map
 */
public class DataConfig {
    private static HashMap<DataType, String> sTypeNameMap;
    private static HashMap<String, DataType> sNameTypeMap;

    static {
        sTypeNameMap = new HashMap<DataType, String>();
        sTypeNameMap.put(DataType.VOLTAGE, "电池电压");
        sTypeNameMap.put(DataType.ROTATE_SPEED, "发动机转速");
        sTypeNameMap.put(DataType.TEMPERATURE, "发动机冷却液温度");
        sTypeNameMap.put(DataType.PRESSURE, "大气压");
        sTypeNameMap.put(DataType.SPEED, "Obd速度");
        sTypeNameMap.put(DataType.TOTAL_DISTANCE, "总里程");
        sTypeNameMap.put(DataType.AVERAGE_FUEL_CONSUMPTION, "平均油耗");
        sTypeNameMap.put(DataType.TOTAL_FUEL_CONSUMPTION, "耗油总量");

        sNameTypeMap = new HashMap<String, DataType>();
        sNameTypeMap.put("电池电压", DataType.VOLTAGE);
        sNameTypeMap.put("发动机转速", DataType.ROTATE_SPEED);
        sNameTypeMap.put("发动机冷却液温度", DataType.TEMPERATURE);
        sNameTypeMap.put("大气压", DataType.PRESSURE);
        sNameTypeMap.put("Obd速度", DataType.SPEED);
        sNameTypeMap.put("总里程", DataType.TOTAL_DISTANCE);
        sNameTypeMap.put("平均油耗", DataType.AVERAGE_FUEL_CONSUMPTION);
        sNameTypeMap.put("耗油总量", DataType.TOTAL_FUEL_CONSUMPTION);
    }

    public static String getNameByType(DataType dataType) {
        return sTypeNameMap.get(dataType);
    }

    public static DataType getTypeByName(String dataName) {
        return sNameTypeMap.get(dataName);
    }
}
