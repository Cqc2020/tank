package com.cqc.tank.factory;

import com.cqc.tank.config.TankWarConfiguration;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.strategy.DefaultFireStrategy;
import com.cqc.tank.strategy.FireStrategy;
import com.cqc.tank.strategy.FourDirFireStrategy;
import com.cqc.tank.strategy.RandomDirFireStrategy;

/**
 * @author Cqc on 2022/2/17 10:55 上午
 */
public class FireStrategyFactory extends StrategyFactory {

    private String playerLevel = TankWarConfiguration.getInstance().get("playerLevel").toString();
    private String enemyLevel = TankWarConfiguration.getInstance().get("enemyLevel").toString();

    @Override
    public FireStrategy getFireStrategy(GroupEnum group) {
        if (GroupEnum.PLAYER.equals(group)) {
            if ("1".equals(playerLevel)) {
                return new DefaultFireStrategy();
            }
            if ("2".equals(playerLevel)) {
                return new FourDirFireStrategy();
            }
        }
        if (GroupEnum.ENEMY.equals(group)) {
            if ("1".equals(enemyLevel)) {
                return new RandomDirFireStrategy();
            }
            if ("2".equals(enemyLevel)) {
                return new FourDirFireStrategy();
            }
        }
        throw new RuntimeException("没有此种分组对应的策略，请重新确认！");
    }
}
