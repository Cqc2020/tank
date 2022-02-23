package com.cqc.tank.factory;

import com.cqc.tank.util.CollisionDetector;

/**
 * 碰撞检测器工厂
 * @author Cqc
 * @date 2022/2/20
 */
public class CollisionDetectorFactory {

    /**
     * 获取碰撞检测器策略
     * @param clazz
     * @return
     */
    public static CollisionDetector getCollisionDetectStrategy(Class clazz) {
        CollisionDetector cd = null;
        try {
            cd = (CollisionDetector) Class.forName(clazz.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cd;
    }
}
