package com.cqc.tank.model;

import com.cqc.tank.entity.enums.MapObjEnum;
import com.cqc.tank.util.ImageUtil;
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

    public Wall(int x, int y, MapObjEnum mapObjEnum) {
        this.x = x;
        this.y = y;
        this.mapObjEnum = mapObjEnum;
        init();
    }

    private void init() {
        switch (mapObjEnum) {
            case WALL:
                objRect = new Rectangle(x, y, ImageUtil.wall.getWidth(), ImageUtil.wall.getHeight());
                break;
            case WALLS:
                objRect = new Rectangle(x, y, ImageUtil.walls.getWidth(), ImageUtil.walls.getHeight());
                break;
            case STEEL:
                objRect = new Rectangle(x, y, ImageUtil.steel.getWidth(), ImageUtil.steel.getHeight());
                break;
            case STEELS:
                objRect = new Rectangle(x, y, ImageUtil.steels.getWidth(), ImageUtil.steels.getHeight());
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
        }
        updateRect();
    }

    /**
     * 更新墙体轮廓坐标
     */
    private void updateRect() {
        objRect.setLocation(x, y);
    }

}
