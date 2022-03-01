package com.cqc.tank.model;

import com.cqc.tank.config.ResourceMgr;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/26
 */
public class Grass extends AbstractGameObject {

    public Grass(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.grass, x, y, null);
    }
}
