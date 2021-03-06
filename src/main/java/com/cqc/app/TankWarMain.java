package com.cqc.app;

import com.cqc.tank.TankFrame;
import com.cqc.tank.config.TankWarConfiguration;
import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.objects.Tank;

/**
 * @author Cqc on 2022/2/11 11:00 下午
 */
public class TankWarMain {
    public static void main(String[] args) throws InterruptedException {
        TankFrame tankFrame = new TankFrame();
        Integer initialTankCount = Integer.valueOf(TankWarConfiguration.getInstance().get("initialTankCount").toString());
        // 初始化敌方坦克
        for (int i = 0; i < initialTankCount; i++) {
            tankFrame.getEnemyTankList().add(new Tank(150 + i * 100, 150, DirectionEnum.DOWN, GroupEnum.ENEMY, tankFrame));
        }
        while (true) {
            // Thread.sleep(10);
            tankFrame.repaint();
        }
    }
}
