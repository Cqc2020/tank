package com.cqc.tank.model;

import com.cqc.tank.config.ResourceMgr;
import lombok.Data;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/26
 */
@Data
public class Eagle extends AbstractGameObject {

    public Eagle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.eagle, x, y, null);
    }
}
