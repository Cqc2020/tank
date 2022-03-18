package com.cqc.tank.config;

import java.io.IOException;
import java.util.Properties;

/**
 * 坦克大战配置类
 * @author Cqc on 2022/2/14 10:49 下午
 */
public class GameConfig {
    /**
     * 不加volatile，会发生指令重排问题
     */
    private static volatile GameConfig INSTANCE;
    private GameConfig() {}
    public static GameConfig getInstance() {
        if (INSTANCE == null) {
            synchronized(GameConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GameConfig();
                }
            }
        }
        return INSTANCE;
    }

    private static Properties props = new Properties();

    static {
        try {
            props.load(GameConfig.class.getClassLoader().getResourceAsStream("config"));
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
