package com.cqc.tank.model;

import com.cqc.tank.entity.enums.MapObjEnum;
import com.cqc.tank.util.ImageUtil;
import lombok.Data;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/26
 */
@Data
public class Eagle extends GameObject {

    public Eagle(int x, int y, MapObjEnum mapObjEnum) {
        this.x = x;
        this.y = y;
        this.mapObjEnum = mapObjEnum;

        objRect = new Rectangle();
        objRect.x = this.x;
        objRect.y = this.y;
        objRect.width = ImageUtil.water.getWidth();
        objRect.height = ImageUtil.water.getHeight();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ImageUtil.eagle, x, y, null);
    }

}
