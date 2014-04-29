package cn.edu.nju.software.obdii.data.obd;

import java.util.HashMap;

/**
 * A type to name map
 */
public class DataConfig {
    private static HashMap<String, DataType> sNameTypeMap;

    static {
        sNameTypeMap = new HashMap<String, DataType>();
        sNameTypeMap.put("Obd速度", DataType.SPEED);
        sNameTypeMap.put("电池电压", DataType.VOLTAGE);
        sNameTypeMap.put("发动机冷却液温度", DataType.COOLANT_TEMPERATURE);
        sNameTypeMap.put("发动机转速", DataType.ROTATE_SPEED);
        sNameTypeMap.put("大气压", DataType.OIL_LEFT);
        sNameTypeMap.put("燃油液位输入", DataType.PRESSURE);
        sNameTypeMap.put("环境空气温度", DataType.AIR_TEMPERATURE);
    }

    public static DataType getTypeByName(String dataName) {
        return sNameTypeMap.get(dataName);
    }
}
