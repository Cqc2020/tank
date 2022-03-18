package com.cqc.tank.strategy.collide;

import com.cqc.tank.model.Tank;
import com.cqc.tank.model.Wall;

import java.awt.*;
import java.util.Objects;

/**
 * @author Cqc
 * @date 2022/2/20
 */
public class TankWallCollisionDetector implements CollisionDetector {
    private static volatile TankWallCollisionDetector INSTANCE;

    private TankWallCollisionDetector() {}

    public static TankWallCollisionDetector getInstance() {
        if (Objects.isNull(INSTANCE)) {
            synchronized (TankWallCollisionDetector.class) {
                if (Objects.isNull(INSTANCE)) {
                    INSTANCE = new TankWallCollisionDetector();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public boolean collisionDetect(Object o1, Object o2, int x, int y) {
        if (o1 instanceof Tank && o2 instanceof Wall) {
            return doCollisionDetect((Tank) o1, (Wall) o2, x, y);
        } else if (o1 instanceof Wall && o2 instanceof Tank) {
            return doCollisionDetect((Tank) o2, (Wall) o1, x, y);
        }
        return false;
    }

    /**
     * 执行碰撞检测
     * @param tank
     * @param wall
     * @param x 坦克x坐标
     * @param y 坦克y坐标
     * @return
     */
    private boolean doCollisionDetect(Tank tank, Wall wall, int x, int y) {
        Rectangle tankRect = tank.getObjRect();
        int lastX = tankRect.x;
        int lastY = tankRect.y;
        tankRect.setLocation(x, y);
        if (tankRect.intersects(wall.getObjRect())) {
            tankRect.setLocation(lastX, lastY);
            return true;
        }
        return false;
    }

}
