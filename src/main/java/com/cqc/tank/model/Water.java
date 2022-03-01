package com.cqc.tank.model;

import com.cqc.tank.config.ResourceMgr;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/26
 */
public class Water extends AbstractGameObject {

    public Water(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.water, x, y, null);
    }
}
