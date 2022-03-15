package com.cqc.tank.model;

import com.cqc.tank.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Cqc
 * @date 2022/3/12
 */
public class Buff extends GameObject {

    private Random random;
    private int index;
    private List<BufferedImage> buffList = new ArrayList<BufferedImage>() {
        {
            add(ImageUtil.star);
            add(ImageUtil.timer);
            add(ImageUtil.bomb);
            add(ImageUtil.gun);
            add(ImageUtil.helmet);
            add(ImageUtil.boat);
            add(ImageUtil.tankLife);
        }
    };
    {
        this.random = new Random();
    }

    public Buff(int x, int y) {
        this.x = x;
        this.y = y;
        init();
    }

    private void init() {
        index = random.nextInt(buffList.size());
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(buffList.get(index), x, y, null);
    }

}
