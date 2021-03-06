package com.cqc.tank.util;

import com.cqc.tank.objects.Tank;
import com.cqc.tank.objects.Wall;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/20
 */
public class TankWallCollisionDetector implements CollisionDetector {

    @Override
    public boolean collisionDetect(Object o1, Object o2, int x, int y) {
        if ((o1 instanceof Tank && o2 instanceof Wall)) {
            return doCollisdoionDetect((Tank) o1, (Wall) o2, x, y);
        } else if ((o1 instanceof Wall && o2 instanceof Tank)) {
            return doCollisdoionDetect((Tank) o2, (Wall) o1, x, y);
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
    private boolean doCollisdoionDetect(Tank tank, Wall wall, int x, int y) {
        Rectangle tankRect = tank.getTankRect();
        int lastX = tankRect.x;
        int lastY = tankRect.y;
        tankRect.setLocation(x, y);
        if (tankRect.intersects(wall.getWallRect())) {
            tankRect.setLocation(lastX, lastY);
            return true;
        }
        return false;
    }

}
