package com.cqc.tank.strategy.collide;

import com.cqc.tank.model.Bullet;

import java.util.Objects;

/**
 * @author Cqc
 * @date 2022/3/10
 */
public class BulletBulletCollisionDetector implements CollisionDetector {
    private static volatile BulletBulletCollisionDetector INSTANCE;

    private BulletBulletCollisionDetector() {}

    public static BulletBulletCollisionDetector getInstance() {
        if (Objects.isNull(INSTANCE)) {
            synchronized (BulletBulletCollisionDetector.class) {
                if (Objects.isNull(INSTANCE)) {
                    INSTANCE = new BulletBulletCollisionDetector();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public boolean collisionDetect(Object o1, Object o2) {
        return doCollisionDetect((Bullet) o1, (Bullet) o2);
    }

    /**
     * 执行碰撞检测
     * @param bullet1
     * @param bullet2
     * @return
     */
    private boolean doCollisionDetect(Bullet bullet1, Bullet bullet2) {
        if (bullet1.getObjRect().intersects(bullet2.getObjRect())) {
            return true;
        }
        return false;
    }

}
