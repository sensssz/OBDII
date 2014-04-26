package cn.edu.nju.software.obdii.data.obd;

import java.util.HashMap;

/**
 * A type to name map
 */
public class DataConfig {
    private static HashMap<DataType, String> sTypeNameMap;
    private static HashMap<String, DataType> sNameTypeMap;

    static {
        sTypeNameMap = new HashMap<DataType, String>();
        sTypeNameMap.put(DataType.SPEED, "Obd速度");
        sTypeNameMap.put(DataType.VOLTAGE, "电池电压");
        sTypeNameMap.put(DataType.COOLANT_TEMPERATURE, "发动机冷却液温度");
        sTypeNameMap.put(DataType.ROTATE_SPEED, "发动机转速");
        sTypeNameMap.put(DataType.OIL_LEFT, "燃油液位输入");
        sTypeNameMap.put(DataType.PRESSURE, "大气压");
        sTypeNameMap.put(DataType.AIR_TEMPERATURE, "环境空气温度");

        sNameTypeMap = new HashMap<String, DataType>();
        sNameTypeMap.put("Obd速度", DataType.SPEED);
        sNameTypeMap.put("电池电压", DataType.VOLTAGE);
        sNameTypeMap.put("发动机冷却液温度", DataType.COOLANT_TEMPERATURE);
        sNameTypeMap.put("发动机转速", DataType.ROTATE_SPEED);
        sNameTypeMap.put("大气压", DataType.OIL_LEFT);
        sNameTypeMap.put("燃油液位输入", DataType.PRESSURE);
        sNameTypeMap.put("环境空气温度", DataType.AIR_TEMPERATURE);
    }

    public static String getNameByType(DataType dataType) {
        return sTypeNameMap.get(dataType);
    }

    public static DataType getTypeByName(String dataName) {
        return sNameTypeMap.get(dataName);
    }
}
