package com.cqc.tank.strategy.fire;

import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.model.Bullet;
import com.cqc.tank.model.Tank;
import com.cqc.tank.util.ImageUtil;

/**
 * @author Cqc on 2022/2/17 9:59 下午
 */
public class FourDirFireStrategy implements FireStrategy{
    @Override
    public void fire(Tank tank) {
        // 子弹的x
        int bX = 0;
        // 子弹的y
        int bY = 0;
        DirectionEnum tankDir = tank.getDir();
        GroupEnum group = tank.getGroup();
        int tankWidth = ImageUtil.getTankWidth(group, tankDir);
        int tankHeight = ImageUtil.getTankHeight(group, tankDir);
        int bulletWidth = ImageUtil.getBulletWidth(tankDir);
        int bulletHeight = ImageUtil.getBulletHeight(tankDir);
        // 计算子弹发射位置(x,y)方式：假设一开坦克和子弹位置相同，再以此计算子弹位置
        switch (tankDir) {
            case UP:
                bX = tank.getX() + (tankWidth - bulletWidth) / 2;
                bY = tank.getY() - bulletHeight;
                break;
            case LEFT:
                bX = tank.getX() - bulletWidth;
                bY = tank.getY() + (tankHeight - bulletHeight) / 2;
                break;
            case DOWN:
                bX = tank.getX() + (tankWidth - bulletWidth) / 2;
                bY = tank.getY() + tankHeight;
                break;
            case RIGHT:
                bX = tank.getX() + tankWidth;
                bY = tank.getY() + (tankHeight - bulletHeight) / 2;
                break;
            default:
                break;
        }
        for (DirectionEnum dir : DirectionEnum.values()) {
            // 开火：坦克对象在窗口中生成一个子弹对象
            tank.getGamePanel().getBulletList().add(new Bullet(bX, bY, dir, tank.getGroup(), tank.getGamePanel()));
        }
    }

}
