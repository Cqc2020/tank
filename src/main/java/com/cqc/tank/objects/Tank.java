package com.cqc.tank.objects;

import com.cqc.tank.TankFrame;
import com.cqc.tank.config.ResourceMgr;
import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.strategy.FireStrategy;

import java.awt.*;
import java.util.Random;

/**
 * 坦克类
 *
 * @author Cqc on 2022/2/12 9:48 上午
 */
public class Tank {

    /**
     * 坦克x轴初始位置
     */
    private int x;

    /**
     * 坦克y轴初始位置
     */
    private int y;

    /**
     * 坦克移动方向
     */
    private DirectionEnum dir;

    /**
     * 坦克分组
     */
    private GroupEnum group;

    /**
     * 敌人移动方向
     */
    private Random random = new Random();

    /**
     * 坦克窗口
     */
    private TankFrame tankFrame;

    /**
     * 坦克存活状态
     */
    private boolean alive = true;

    /**
     * 玩家坦克移动标志
     */
    private boolean playerMoving = false;

    /**
     * 敌方坦克移动标志
     */
    private boolean enemyMoving = true;

    /**
     * 矩形
     */
    private Rectangle tankRect = new Rectangle();

    /**
     * 玩家坦克移动速度
     */
    private static final double PLAYER_TANK_SPEED = 4;

    /**
     * 敌人坦克移动速度
     */
    private static final double ENEMY_TANK_SPEED = 1;


    public Tank(int x, int y, DirectionEnum dir, GroupEnum group, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tankFrame = tankFrame;

        tankRect.x = this.x;
        tankRect.y = this.y;
        tankRect.width = getBulletWidth();
        tankRect.height = getBulletHeight();
    }

    /**
     * 画出坦克当前位置
     *
     * @param g
     */
    public void paint(Graphics g) {
        if (!alive) {
            tankFrame.getEnemyTankList().remove(this);
            tankFrame.getBlastList().add(new Blast(this.x, this.y, tankFrame));
        }
        if (GroupEnum.PLAYER.equals(this.group)) {
            switch (dir) {
                case UP:
                    g.drawImage(ResourceMgr.playerTankU, x, y, null);
                    break;
                case LEFT:
                    g.drawImage(ResourceMgr.playerTankL, x, y, null);
                    break;
                case DOWN:
                    g.drawImage(ResourceMgr.playerTankD, x, y, null);
                    break;
                case RIGHT:
                    g.drawImage(ResourceMgr.playerTankR, x, y, null);
                    break;
                default:
                    break;
            }
        } else if (GroupEnum.ENEMY.equals(this.group)) {
            switch (dir) {
                case UP:
                    g.drawImage(ResourceMgr.enemyTankU, x, y, null);
                    break;
                case LEFT:
                    g.drawImage(ResourceMgr.enemyTankL, x, y, null);
                    break;
                case DOWN:
                    g.drawImage(ResourceMgr.enemyTankD, x, y, null);
                    break;
                case RIGHT:
                    g.drawImage(ResourceMgr.enemyTankR, x, y, null);
                    break;
                default:
                    break;
            }
        }
        move();
    }

    /**
     * 坦克匀速向指定方向移动
     */
    private void move() {
        if (GroupEnum.PLAYER.equals(this.group)) {
            if (playerMoving) {
                // 根据按下的键，向不同方向移动
                switch (dir) {
                    case UP:
                        y -= PLAYER_TANK_SPEED;
                        break;
                    case LEFT:
                        x -= PLAYER_TANK_SPEED;
                        break;
                    case DOWN:
                        y += PLAYER_TANK_SPEED;
                        break;
                    case RIGHT:
                        x += PLAYER_TANK_SPEED;
                        break;
                    default:
                        break;
                }
            }
        } else if (GroupEnum.ENEMY.equals(this.group)) {
            if (enemyMoving) {
                // 根据按下的键，向不同方向移动
                switch (dir) {
                    case UP:
                        y -= ENEMY_TANK_SPEED;
                        break;
                    case LEFT:
                        x -= ENEMY_TANK_SPEED;
                        break;
                    case DOWN:
                        y += ENEMY_TANK_SPEED;
                        break;
                    case RIGHT:
                        x += ENEMY_TANK_SPEED;
                        break;
                    default:
                        break;
                }
                this.fire(tankFrame.fireStrategyFactory.getFireStrategy(GroupEnum.ENEMY));
            }
        }
        boundsCheck();
        // update rect
        tankRect.x = this.x;
        tankRect.y = this.y;
    }

    /**
     * 坦克窗口边界检测
     */
    private void boundsCheck() {
        if (this.x <= 0) {
            this.x = 2;
        }
        if (this.x > TankFrame.GAME_WIDTH - getTankWidth()) {
            this.x = TankFrame.GAME_WIDTH - getTankWidth();
        }
        if (this.y <= 30) {
            this.y = 30;
        }
        if (this.y >= TankFrame.GAME_HEIGHT -getTankHeight()) {
            this.y = TankFrame.GAME_HEIGHT - getTankHeight();
        }
    }

    /**
     * 设置敌人随机方向
     */
    public void setRandomDir() {
        this.dir = DirectionEnum.values()[random.nextInt(4)];
    }

    /**
     * 坦克发射一枚子弹
     */
    public void fire(FireStrategy fireStrategy) {
        fireStrategy.fire(this);
    }

    /**
     * 坦克死亡
     */
    public void die() {
        this.alive = false;
    }

    /**
     * 获取坦克的宽度
     *
     * @return
     */
    public int getTankWidth() {
        if (GroupEnum.PLAYER.equals(this.group)) {
            switch (dir) {
                case UP:
                    return ResourceMgr.playerTankU.getWidth();
                case LEFT:
                    return ResourceMgr.playerTankL.getWidth();
                case DOWN:
                    return ResourceMgr.playerTankD.getWidth();
                case RIGHT:
                    return ResourceMgr.playerTankR.getWidth();
                default:
                    return 0;
            }
        } else {
            switch (dir) {
                case UP:
                    return ResourceMgr.enemyTankU.getWidth();
                case LEFT:
                    return ResourceMgr.enemyTankL.getWidth();
                case DOWN:
                    return ResourceMgr.enemyTankD.getWidth();
                case RIGHT:
                    return ResourceMgr.enemyTankR.getWidth();
                default:
                    return 0;
            }
        }
    }

    /**
     * 获取坦克的高度
     *
     * @return
     */
    public int getTankHeight() {
        if (GroupEnum.PLAYER.equals(this.group)) {
            switch (dir) {
                case UP:
                    return ResourceMgr.playerTankU.getHeight();
                case LEFT:
                    return ResourceMgr.playerTankL.getHeight();
                case DOWN:
                    return ResourceMgr.playerTankD.getHeight();
                case RIGHT:
                    return ResourceMgr.playerTankR.getHeight();
                default:
                    return 0;
            }
        } else {
            switch (dir) {
                case UP:
                    return ResourceMgr.enemyTankU.getHeight();
                case LEFT:
                    return ResourceMgr.enemyTankL.getHeight();
                case DOWN:
                    return ResourceMgr.enemyTankD.getHeight();
                case RIGHT:
                    return ResourceMgr.enemyTankR.getHeight();
                default:
                    return 0;
            }
        }
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

    public boolean isPlayerMoving() {
        return playerMoving;
    }

    public void setPlayerMoving(boolean playerMoving) {
        this.playerMoving = playerMoving;
    }

    public boolean isEnemyMoving() {
        return enemyMoving;
    }

    public void setEnemyMoving(boolean enemyMoving) {
        this.enemyMoving = enemyMoving;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public GroupEnum getGroup() {
        return group;
    }

    public void setGroup(GroupEnum group) {
        this.group = group;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Rectangle getTankRect() {
        return tankRect;
    }

    public void setTankRect(Rectangle tankRect) {
        this.tankRect = tankRect;
    }

    public TankFrame getTankFrame() {
        return tankFrame;
    }

    public void setTankFrame(TankFrame tankFrame) {
        this.tankFrame = tankFrame;
    }
}
