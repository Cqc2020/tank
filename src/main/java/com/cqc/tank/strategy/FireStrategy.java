package com.cqc.tank.strategy;

import com.cqc.tank.objects.Tank;

/**
 * 开火策略
 * @author Cqc on 2022/2/17 10:05 上午
 */
public interface FireStrategy {

    void fire(Tank tank);
}
