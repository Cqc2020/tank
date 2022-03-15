package com.cqc.tank.strategy.collide;

import com.cqc.tank.model.Bullet;
import com.cqc.tank.model.Tank;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/20
 */
public class BulletTankCollisionDetector implements CollisionDetector {

    @Override
    public boolean collisionDetect(Object o1, Object o2) {
        if (o1 instanceof Bullet && o2 instanceof Tank) {
            return doCollisionDetect((Bullet) o1, (Tank) o2);
        } else if (o1 instanceof Tank && o2 instanceof Bullet) {
            return doCollisionDetect((Bullet) o2, (Tank) o1);
        }
        return false;
    }

    /**
     * 执行碰撞检测
     * @param bullet
     * @param tank
     * @return
     */
    private boolean doCollisionDetect(Bullet bullet, Tank tank) {
        Rectangle bulletRect = bullet.getObjRect();
        if (bulletRect.intersects(tank.getObjRect())) {
            return true;
        }
        return false;
    }

}
