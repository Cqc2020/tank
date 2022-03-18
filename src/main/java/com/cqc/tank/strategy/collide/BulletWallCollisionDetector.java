package com.cqc.tank.strategy.collide;

import com.cqc.tank.model.Bullet;
import com.cqc.tank.model.Wall;

import java.util.Objects;

/**
 * @author Cqc
 * @date 2022/2/20
 */
public class BulletWallCollisionDetector implements CollisionDetector {
    private static volatile BulletWallCollisionDetector INSTANCE;

    private BulletWallCollisionDetector() {}

    public static BulletWallCollisionDetector getInstance() {
        if (Objects.isNull(INSTANCE)) {
            synchronized (BulletWallCollisionDetector.class) {
                if (Objects.isNull(INSTANCE)) {
                    INSTANCE = new BulletWallCollisionDetector();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public boolean collisionDetect(Object o1, Object o2) {
        if (o1 instanceof Bullet && o2 instanceof Wall) {
            return doCollisionDetect((Bullet) o1, (Wall) o2);
        } else if ((o1 instanceof Wall && o2 instanceof Bullet)) {
            return doCollisionDetect((Bullet) o2, (Wall) o1);
        }
        return false;
    }

    /**
     * 执行碰撞检测
     * @param bullet
     * @param wall
     * @return
     */
    private boolean doCollisionDetect(Bullet bullet, Wall wall) {
        if (bullet.getObjRect().intersects(wall.getObjRect())) {
            return true;
        }
        return false;
    }

}
