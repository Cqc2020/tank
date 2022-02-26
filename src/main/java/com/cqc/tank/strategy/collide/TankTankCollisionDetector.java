package com.cqc.tank.strategy.collide;

import com.cqc.tank.objects.Tank;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/21
 */
public class TankTankCollisionDetector implements CollisionDetector {

    @Override
    public boolean collisionDetect(Object o1, Object o2, int x, int y) {
        return doCollisdoionDetect((Tank) o1, (Tank) o2, x, y);
    }

    /**
     * 执行碰撞检测
     * @param tank1
     * @param tank2
     * @param x
     * @param y
     * @return
     */
    private boolean doCollisdoionDetect(Tank tank1, Tank tank2, int x, int y) {
        Rectangle tankRect = tank1.getTankRect();
        int lastX = tankRect.x;
        int lastY = tankRect.y;
        tankRect.setLocation(x, y);
        if (tankRect.intersects(tank2.getTankRect())) {
            tankRect.setLocation(lastX, lastY);
            return true;
        }
        return false;
    }
}