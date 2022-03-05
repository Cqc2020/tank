package com.cqc.tank.model;

import com.cqc.tank.util.ImageUtil;
import lombok.Data;

import java.awt.*;

/**
 * @author Cqc
 * @date 2022/2/26
 */
@Data
public class Eagle extends GameObject {

    public Eagle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ImageUtil.eagle, x, y, null);
    }
}
