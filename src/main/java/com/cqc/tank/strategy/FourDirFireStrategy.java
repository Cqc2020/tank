package com.cqc.tank.strategy;

import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.objects.Bullet;
import com.cqc.tank.objects.Tank;

/**
 * @author Cqc on 2022/2/17 9:59 下午
 */
public class FourDirFireStrategy implements FireStrategy{
    @Override
    public void fire(Tank tank) {
        int bX = 0;
        int bY = 0;
        // 计算子弹发射位置(x,y)方式：假设一开坦克和子弹位置相同，再以此计算子弹位置
        switch (tank.getDir()) {
            case UP:
                bX = tank.getX() + (tank.getTankWidth() - tank.getBulletWidth()) / 2;
                bY = tank.getY() - tank.getBulletHeight();
                break;
            case LEFT:
                bX = tank.getX() - tank.getBulletWidth();
                bY = tank.getY() + (tank.getTankHeight() - tank.getBulletHeight()) / 2;
                break;
            case DOWN:
                bX = tank.getX() + (tank.getTankWidth() - tank.getBulletWidth()) / 2;
                bY = tank.getY() + tank.getTankHeight();
                break;
            case RIGHT:
                bX = tank.getX() + tank.getTankWidth();
                bY = tank.getY() + (tank.getTankHeight() - tank.getBulletHeight()) / 2;
                break;
            default:
                break;
        }
        for (DirectionEnum dir : DirectionEnum.values()) {
            // 开火：坦克对象在窗口中生成一个子弹对象
            tank.getTankFrame().getPlayerBulletList().add(new Bullet(bX, bY, dir, tank.getGroup(), tank.getTankFrame()));
        }
    }

}
