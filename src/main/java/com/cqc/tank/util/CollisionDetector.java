package com.cqc.tank.util;

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
    boolean collisionDetect(Object o1, Object o2, int x, int y);
}

