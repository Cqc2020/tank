package com.cqc.tank.model;

import com.cqc.tank.util.ImageUtil;
import com.cqc.tank.entity.enums.MapObjEnum;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/26
 */
public class Water extends Wall {

    public Water(int x, int y, MapObjEnum mapObjEnum) {
        super(x, y, mapObjEnum);
        this.x = x;
        this.y = y;
        this.mapObjEnum = mapObjEnum;
        init();
    }

    private void init() {
        objRect = new Rectangle(x, y, ImageUtil.water.getWidth(), ImageUtil.water.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ImageUtil.water, x, y, null);
        updateRect();
    }

    /**
     * 更新水轮廓坐标
     */
    private void updateRect() {
        objRect.setLocation(x, y);
    }
}
