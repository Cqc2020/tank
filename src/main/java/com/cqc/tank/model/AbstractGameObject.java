package com.cqc.tank.model;

import lombok.Data;

import java.awt.*;

/**
 * 游戏物体抽象类
 * @author Cqc
 * @date 2022/2/20
 */
@Data
public abstract class AbstractGameObject {
    /**
     * 游戏物体的起始x坐标
     */
    public int x;
    /**
     * 游戏物体的起始y坐标
     */
    public int y;

    public abstract void paint(Graphics g);

}
