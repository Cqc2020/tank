package com.cqc.tank.objects;

import com.cqc.tank.TankFrame;
import com.cqc.tank.config.ResourceMgr;
import lombok.Data;

import java.awt.*;

/**
 * 墙体类
 *
 * @author Cqc
 * @date 2022/2/20
 */
@Data
public class Wall extends AbstractGameObject {

    /**
     * 坦克窗口
     */
    private TankFrame tankFrame;

    /**
     * 墙体矩形
     */
    private Rectangle wallRect = new Rectangle();

    private boolean alive = true;

    public Wall(int x, int y, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.tankFrame = tankFrame;

        wallRect.x = this.x;
        wallRect.y = this.y;
        wallRect.width = ResourceMgr.walls.getWidth();
        wallRect.height = ResourceMgr.walls.getHeight();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.walls, x, y, null);
    }

    /**
     * 墙体销毁
     */
    public void die() {
        this.alive = false;
    }
}
