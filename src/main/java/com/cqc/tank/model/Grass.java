package com.cqc.tank.model;

import com.cqc.tank.util.ImageUtil;
import com.cqc.tank.entity.enums.MapObjEnum;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/26
 */
public class Grass extends GameObject {

    public Grass(int x, int y, MapObjEnum mapObjEnum) {
        this.x = x;
        this.y = y;
        this.mapObjEnum = mapObjEnum;

        objRect = new Rectangle(x, y, ImageUtil.grass.getWidth(), ImageUtil.grass.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ImageUtil.grass, x, y, null);
        updateRect();
    }

    /**
     * 更新草地轮廓坐标
     */
    private void updateRect() {
        objRect.setLocation(x, y);
    }
}
