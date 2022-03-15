package com.cqc.tank.model;

import com.cqc.tank.entity.enums.MapObjEnum;
import lombok.Data;

import java.awt.*;

/**
 * 游戏物体抽象类
 *
 * @author Cqc
 * @date 2022/2/20
 */
@Data
public abstract class GameObject {
    /**
     * 游戏物体的起始x坐标
     */
    public int x;
    /**
     * 游戏物体的起始y坐标
     */
    public int y;
    /**
     * 游戏物体轮廓
     */
    public Rectangle objRect;
    /**
     * 地图元素类型枚举
     */
    public MapObjEnum mapObjEnum;
    public boolean moveFlag = true;
    public boolean alive = true;

    /**
     * 画笔
     *
     * @param g
     */
    public abstract void paint(Graphics g);

    /**
     * 游戏物体消亡
     */
    public void die() {
        this.alive = false;
    }
}
