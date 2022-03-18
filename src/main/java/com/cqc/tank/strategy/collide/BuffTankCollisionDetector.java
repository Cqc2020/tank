package com.cqc.tank.strategy.collide;

import com.cqc.tank.model.Buff;
import com.cqc.tank.model.Tank;

import java.awt.*;
import java.util.Objects;

/**
 * 坦克碰撞游戏道具检测
 * @author Cqc
 * @date 2022/3/17
 */
public class BuffTankCollisionDetector implements CollisionDetector {
    private static volatile BuffTankCollisionDetector INSTANCE;

    private BuffTankCollisionDetector() {}

    public static BuffTankCollisionDetector getInstance() {
        if (Objects.isNull(INSTANCE)) {
            synchronized (BuffTankCollisionDetector.class) {
                if (Objects.isNull(INSTANCE)) {
                    INSTANCE = new BuffTankCollisionDetector();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public boolean collisionDetect(Object o1, Object o2) {
        if (o1 instanceof Buff && o2 instanceof Tank) {
            return doCollisionDetect((Buff) o1, (Tank) o2);
        } else if (o1 instanceof Tank && o2 instanceof Buff) {
            return doCollisionDetect((Buff) o2, (Tank) o1);
        }
        return false;
    }

    /**
     * 执行碰撞检测
     * @param buff
     * @param tank
     * @return
     */
    private boolean doCollisionDetect(Buff buff, Tank tank) {
        Rectangle buffRect = buff.getObjRect();
        if (buffRect.intersects(tank.getObjRect())) {
            return true;
        }
        return false;
    }

}
