package com.cqc.tank.factory;

import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.strategy.fire.FireStrategy;

/**
 * @author Cqc on 2022/2/17 10:51 上午
 */
public abstract class StrategyFactory {

    public FireStrategy getFireStrategy(GroupEnum group) {
        throw new UnsupportedOperationException();
    }

}
