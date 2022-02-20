package com.cqc.tank.objects;

import java.awt.*;

/**
 * 游戏物体抽象类
 * @author Cqc
 * @date 2022/2/20
 */
public abstract class AbstractGameObject {
    public int x;
    public int y;

    public abstract void paint(Graphics g);

}
