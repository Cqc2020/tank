package com.cqc.tank.objects;

import com.cqc.tank.config.ResourceMgr;
import com.cqc.tank.TankFrame;
import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;

import java.awt.*;

/**
 * 子弹类
 *
 * @author Cqc on 2022/2/12 3:01 下午
 */
public class Bullet {

    /**
     * 子弹x轴初始位置
     */
    private int x;

    /**
     * 子弹y轴初始位置
     */
    private int y;

    /**
     * 子弹移动方向
     */
    private DirectionEnum dir;

    /**
     * 子弹分组
     */
    private final GroupEnum group;

    /**
     * 坦克窗口
     */
    private TankFrame tankFrame;

    /**
     * 子弹存活状态
     */
    private boolean alive = true;

    /**
     * 矩形
     */
    private Rectangle bulletRect = new Rectangle();

    /**
     * 子弹移动速度
     */
    private static final int PLAYER_BULLET_SPEED = 10;

    /**
     * 子弹移动速度
     */
    private static final int ENEMY_BULLET_SPEED = 5;


    public Bullet(int x, int y, DirectionEnum dir, GroupEnum goup, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = goup;
        this.tankFrame = tankFrame;

        bulletRect.x = this.x;
        bulletRect.y = this.y;
        bulletRect.width = getBulletWidth();
        bulletRect.height = getBulletHeight();
    }

    /**
     * 画出子弹当前位置
     *
     * @param g
     */
    public void paint(Graphics g) {
        if (!alive) {
            tankFrame.getPlayerBulletList().remove(this);
        }
        switch (dir) {
            case UP:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case LEFT:
                g.drawImage(ResourceMgr.bulletL, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
            default:
                break;
        }
        move();
    }

    /**
     * 子弹向指定方向移动
     */
    private void move() {
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
        bulletRect.x = this.x;
        bulletRect.y = this.y;
        if (x < 0 || y < 0 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT) {
            alive = false;
        }
    }

    /**
     * 检测子弹是否与坦克碰撞
     * @param tank
     */
    public void collideWith(Tank tank) {
        // 己方子弹不应与己方坦克相撞
        if (this.group.equals(tank.getGroup())) {
            return;
        }
        if (bulletRect.intersects(tank.getTankRect())) {
            tank.die();
            this.die();
        }
    }

    /**
     * 子弹死亡
     */
    private void die() {
        this.alive = false;
    }

    /**
     * 获取子弹的宽度
     *
     * @return
     */
    public int getBulletWidth() {
        switch (dir) {
            case UP:
                return ResourceMgr.bulletU.getWidth();
            case LEFT:
                return ResourceMgr.bulletL.getWidth();
            case DOWN:
                return ResourceMgr.bulletD.getWidth();
            case RIGHT:
                return ResourceMgr.bulletR.getWidth();
            default:
                return 0;
        }
    }

    /**
     * 获取子弹的高度
     *
     * @return
     */
    public int getBulletHeight() {
        switch (dir) {
            case UP:
                return ResourceMgr.bulletU.getHeight();
            case LEFT:
                return ResourceMgr.bulletL.getHeight();
            case DOWN:
                return ResourceMgr.bulletD.getHeight();
            case RIGHT:
                return ResourceMgr.bulletR.getHeight();
            default:
                return 0;
        }
    }

    public DirectionEnum getDir() {
        return dir;
    }

    public void setDir(DirectionEnum dir) {
        this.dir = dir;
    }

}
