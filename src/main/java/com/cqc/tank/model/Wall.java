package com.cqc.tank.model;

import com.cqc.tank.config.ResourceMgr;
import com.cqc.tank.entity.enums.MapObjEnum;
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
    private Rectangle wallRect = new Rectangle();
    private MapObjEnum type;
    private boolean alive = true;

    public Wall(int x, int y, MapObjEnum type) {
        this.x = x;
        this.y = y;
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
