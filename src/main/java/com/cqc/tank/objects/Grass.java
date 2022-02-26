package com.cqc.tank.objects;

import com.cqc.tank.frame.TankFrame;
import com.cqc.tank.config.ResourceMgr;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/26
 */
public class Grass extends AbstractGameObject {
    /**
     * 坦克窗口
     */
    private TankFrame tankFrame;

    public Grass(int x, int y, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.tankFrame = tankFrame;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.grass, x, y, null);
    }
}
