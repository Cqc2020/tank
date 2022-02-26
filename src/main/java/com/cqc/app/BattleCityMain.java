package com.cqc.app;

import com.cqc.tank.frame.TankFrame;
import com.cqc.tank.config.TankWarConfiguration;
import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.objects.Tank;

/**
 * @author Cqc on 2022/2/11 11:00 下午
 */
public class BattleCityMain {
    public static void main(String[] args) {
        TankFrame tankFrame = new TankFrame();
        Integer initialEnemyTankCount = Integer.valueOf(TankWarConfiguration.getInstance().get("initialEnemyTankCount").toString());
        Integer playerTankSpeed = Integer.valueOf(TankWarConfiguration.getInstance().get("playerTankSpeed").toString());
        Integer enemyTankSpeed = Integer.valueOf(TankWarConfiguration.getInstance().get("enemyTankSpeed").toString());
        // 初始化玩家坦克
        tankFrame.getTankList().add(new Tank(500, 500, DirectionEnum.UP, GroupEnum.PLAYER, tankFrame, playerTankSpeed, false));
        // 初始化敌方坦克
        for (int i = 0; i < initialEnemyTankCount; i++) {
            tankFrame.getTankList().add(new Tank(150 + i * 100, 150, DirectionEnum.DOWN, GroupEnum.ENEMY, tankFrame, enemyTankSpeed, true));
        }
        // 画面重画
        while (true) {
            tankFrame.repaint();
        }
    }
}
