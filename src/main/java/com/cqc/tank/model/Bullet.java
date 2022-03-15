package com.cqc.tank.model;

import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.entity.enums.MapObjEnum;
import com.cqc.tank.factory.CollisionDetectorFactory;
import com.cqc.tank.frame.GamePanel;
import com.cqc.tank.frame.MainFrame;
import com.cqc.tank.strategy.collide.BulletBulletCollisionDetector;
import com.cqc.tank.strategy.collide.BulletTankCollisionDetector;
import com.cqc.tank.strategy.collide.BulletWallCollisionDetector;
import com.cqc.tank.util.ImageUtil;
import lombok.Data;

import java.awt.*;
import java.util.List;

/**
 * 子弹类
 *
 * @author Cqc on 2022/2/12 3:01 下午
 */
@Data
public class Bullet extends GameObject {
    /**
     * 子弹分组
     */
    private GroupEnum group;
    /**
     * 子弹移动方向
     */
    private DirectionEnum dir;
    /**
     * 坦克窗口
     */
    private GamePanel gamePanel;
    /**
     * 子弹移动速度
     */
    private static final int PLAYER_BULLET_SPEED = 15;
    /**
     * 子弹移动速度
     */
    private static final int ENEMY_BULLET_SPEED = 5;

    public Bullet(int x, int y, DirectionEnum dir, GroupEnum group, GamePanel gamePanel) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.gamePanel = gamePanel;
        init();
    }

    private void init() {
        alive = true;
        objRect = new Rectangle(x, y, ImageUtil.getBulletWidth(dir), ImageUtil.getBulletHeight(dir));
    }

    /**
     * 画出子弹当前位置
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        paintFrame(g);
        handleCollision();
        handleMoving();
        updateRect();
    }

    private void paintFrame(Graphics g) {
        switch (dir) {
            case UP:
                g.drawImage(ImageUtil.bulletU, x, y, null);
                break;
            case LEFT:
                g.drawImage(ImageUtil.bulletL, x, y, null);
                break;
            case DOWN:
                g.drawImage(ImageUtil.bulletD, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ImageUtil.bulletR, x, y, null);
                break;
            default:
                break;
        }
    }

    /**
     * 更新子弹轮廓坐标
     */
    private void updateRect() {
        objRect.setLocation(x, y);
    }

    private void handleCollision() {
        // 子弹撞上坦克
        List<Tank> tankList = gamePanel.getTankList();
        for (int i = 0; i < tankList.size(); i++) {
            Tank tank = tankList.get(i);
            // 坦克不能和自己进行碰撞检测，否则会出现坦克无法移动的bug
            if (this.group.equals(tank.getGroup())) {
                continue;
            }
            if (CollisionDetectorFactory.getCollisionDetectStrategy(BulletTankCollisionDetector.class)
                    .collisionDetect(this, tank)) {
                this.die();
                if (!tank.getGroup().equals(GroupEnum.PLAYER)) {
                    tank.die();
                }
                return;
            }
        }

        // 子弹撞上地图元素砖墙，白墙，水，草， 鹰
        for (GameObject mapObj : gamePanel.getMapObjList()) {
            if (CollisionDetectorFactory.getCollisionDetectStrategy(BulletWallCollisionDetector.class)
                    .collisionDetect(this, mapObj)) {
                switch (mapObj.getMapObjEnum()) {
                    case WALL:
                    case WALLS:
                    case EAGLE:
                        this.die();
                        mapObj.die();
                        return;
                    case STEEL:
                    case STEELS:
                        this.die();
                        break;
                    case GRASS:
                    case WATER:
                        break;
                    default:
                }
            }
        }

        // 子弹与子弹相撞处理
        for (Bullet bullet : gamePanel.getBulletList()) {
            if (CollisionDetectorFactory.getCollisionDetectStrategy(BulletBulletCollisionDetector.class)
                    .collisionDetect(this, bullet)) {
                if (!group.equals(bullet.group)) {
                    this.die();
                    bullet.die();
                    if (GroupEnum.ENEMY.equals(group)) {
                        gamePanel.getBlastList().add(new Blast(x, y, MapObjEnum.BULLET));
                    }
                }
            }
        }

    }

    /**
     * 子弹向指定方向移动
     */
    private void handleMoving() {
        // 根据按下的键，向不同方向移动
        if (GroupEnum.PLAYER.equals(this.group)) {
            switch (dir) {
                case UP:
                    y -= PLAYER_BULLET_SPEED;
                    break;
                case LEFT:
                    x -= PLAYER_BULLET_SPEED;
                    break;
                case DOWN:
                    y += PLAYER_BULLET_SPEED;
                    break;
                case RIGHT:
                    x += PLAYER_BULLET_SPEED;
                    break;
                default:
                    break;
            }
        } else if (GroupEnum.ENEMY.equals(this.group)) {
            switch (dir) {
                case UP:
                    y -= ENEMY_BULLET_SPEED;
                    break;
                case LEFT:
                    x -= ENEMY_BULLET_SPEED;
                    break;
                case DOWN:
                    y += ENEMY_BULLET_SPEED;
                    break;
                case RIGHT:
                    x += ENEMY_BULLET_SPEED;
                    break;
                default:
                    break;
            }
        }
        objRect.setLocation(x, y);
        if (x < 0 || y < 0 || x > MainFrame.GAME_WIDTH || y > MainFrame.GAME_HEIGHT) {
            alive = false;
        }
    }

}
