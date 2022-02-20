package com.cqc.tank.config;

import java.io.IOException;
import java.util.Properties;

/**
 * 坦克大战配置类
 * @author Cqc on 2022/2/14 10:49 下午
 */
public class TankWarConfiguration {
    // 不加volatile，会发生指令重排问题
    private static volatile TankWarConfiguration INSTANCE;
    private TankWarConfiguration() {}
    public static TankWarConfiguration getInstance() {
        if (INSTANCE == null) {
            synchronized(TankWarConfiguration.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TankWarConfiguration();
                }
            }
        }
        return INSTANCE;
    }

    private static Properties props = new Properties();

    static {
        try {
            props.load(TankWarConfiguration.class.getClassLoader().getResourceAsStream("config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object get(String key) {
        if (props == null) {
            return null;
        }
        return props.get(key);
    }
}
