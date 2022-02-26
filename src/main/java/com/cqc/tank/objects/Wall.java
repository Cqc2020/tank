package com.cqc.tank.objects;

import com.cqc.tank.frame.TankFrame;
import com.cqc.tank.config.ResourceMgr;
import com.cqc.tank.entity.enums.WallTypeEnum;
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

    private WallTypeEnum type;

    private boolean alive = true;

    public Wall(int x, int y, TankFrame tankFrame, WallTypeEnum type) {
        this.x = x;
        this.y = y;
        this.tankFrame = tankFrame;
        this.type = type;

        wallRect.x = this.x;
        wallRect.y = this.y;
        wallRect.width = ResourceMgr.walls.getWidth();
        wallRect.height = ResourceMgr.walls.getHeight();
    }

    @Override
    public void paint(Graphics g) {
        switch (type) {
            case WALL:
                g.drawImage(ResourceMgr.wall, x, y, null);
                break;
            case WALLS:
                g.drawImage(ResourceMgr.walls, x, y, null);
                break;
            case STEEL:
                g.drawImage(ResourceMgr.steel, x, y, null);
                break;
            default:
                break;
        }

    }

    /**
     * 墙体销毁
     */
    public void die() {
        this.alive = false;
    }
}
