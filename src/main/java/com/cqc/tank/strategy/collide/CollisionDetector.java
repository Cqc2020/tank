package com.cqc.tank.strategy.collide;

/**
 * 碰撞检测器
 * @author Cqc
 * @date 2022/2/20
 */
public interface CollisionDetector {

    /**
     * 碰撞检测
     * @param o1
     * @param o2
     * @param x
     * @param y
     * @return
     */
    default boolean collisionDetect(Object o1, Object o2, int x, int y) {
        return false;
    }

    /**
     * 碰撞检测
     * @param o1
     * @param o2
     * @return
     */
    default boolean collisionDetect(Object o1, Object o2) {
        return false;
    }
}

