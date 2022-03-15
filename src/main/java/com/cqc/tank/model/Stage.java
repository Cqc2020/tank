package com.cqc.tank.model;

import com.cqc.tank.util.MapIOUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * 关卡工具类
 * @author Cqc
 * @date 2022/3/5
 */
public class Stage {
    /**
     * 当前关卡
     */
    private static int stage = 1;
    /**
     * 关卡总数
     */
    private static int stageCount;
    /**
     * 双检锁单例Stage
     */
    private static Stage INSTANCE;

    /**
     * 私有化构造器
     */
    private Stage() {}

    public static Stage getInstance() {
        if (INSTANCE == null) {
            synchronized (Stage.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Stage();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 读取关卡地图总数
     * @return
     */
    public int getStageCount() {
        File file = new File(MapIOUtil.mapSavePath);
        try {
            if (!file.exists()) {
                throw new FileNotFoundException("当前文件夹无地图文件！" + file.getPath());
            }
            stageCount = Objects.requireNonNull(file.list()).length;
            if (stageCount == 0) {
                throw new FileNotFoundException("当前文件夹无地图文件！" + file.getPath());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stageCount;
    }

    public int getFirstStage() {
        return stage = 1;
    }

    /**
     * 当前关卡
     * @return
     */
    public int getCurStage() {
        return stage;
    }

    /**
     * 上一关
     * @return
     */
    public int getLastStage() {
        if (stage > 1) {
            stage--;
        } else {
            stage = getStageCount();
        }
        return stage;
    }

    /**
     * 下一关
     * @return
     */
    public int getNextStage() {
        if (stage < getStageCount()) {
            stage++;
        } else {
            stage = 1;
        }
        return stage;
    }

    /**
     * 新关卡
     * @return
     */
    public int getNewStage() {
        stage = getStageCount() + 1;
        return stage;
    }

}
