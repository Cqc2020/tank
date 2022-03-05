package com.cqc.tank.model;

import com.cqc.tank.util.ImageUtil;
import com.cqc.tank.entity.enums.MapObjEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;

/**
 * 墙体类
 *
 * @author Cqc
 * @date 2022/2/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Wall extends GameObject {
    private boolean alive = true;

    public Wall(int x, int y, MapObjEnum mapObjEnum) {
        this.x = x;
        this.y = y;
        this.mapObjEnum = mapObjEnum;
        setObjRectProfile(mapObjEnum);
    }

    private void setObjRectProfile(MapObjEnum mapObjEnum) {
        objRect = new Rectangle();
        objRect.x = this.x;
        objRect.y = this.y;
        switch (mapObjEnum) {
            case WALL:
                this.width = objRect.width = ImageUtil.wall.getWidth();
                this.height = objRect.height = ImageUtil.wall.getHeight();
                break;
            case WALLS:
                objRect.width = ImageUtil.walls.getWidth();
                objRect.height = ImageUtil.walls.getHeight();
                break;
            case STEEL:
                objRect.width = ImageUtil.steel.getWidth();
                objRect.height = ImageUtil.steel.getHeight();
                break;
            case STEELS:
                objRect.width = ImageUtil.steels.getWidth();
                objRect.height = ImageUtil.steels.getHeight();
                break;
            default:
        }
    }

    @Override
    public void paint(Graphics g) {
        switch (mapObjEnum) {
            case WALL:
                g.drawImage(ImageUtil.wall, x, y, null);
                break;
            case WALLS:
                g.drawImage(ImageUtil.walls, x, y, null);
                break;
            case STEEL:
                g.drawImage(ImageUtil.steel, x, y, null);
                break;
            case STEELS:
                g.drawImage(ImageUtil.steels, x, y, null);
                break;
            default:
                break;
        }
        updateRect();
    }

    /**
     * 更新墙体轮廓坐标
     */
    private void updateRect() {
        objRect.x = this.x;
        objRect.y = this.y;
    }

    /**
     * 墙体销毁
     */
    public void die() {
        this.alive = false;
    }

}
