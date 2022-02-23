package com.cqc.tank.objects;

import com.cqc.tank.TankFrame;
import com.cqc.tank.config.ResourceMgr;
import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.factory.CollisionDetectorFactory;
import com.cqc.tank.strategy.FireStrategy;
import com.cqc.tank.util.TankWallCollisionDetector;
import lombok.Data;

import java.awt.*;
import java.util.Random;

/**
 * 坦克类
 *
 * @author Cqc on 2022/2/12 9:48 上午
 */
@Data
public class Tank extends AbstractGameObject {

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
     * 玩家坦克与物体碰撞标志
     */
    private boolean playerTankCollideFlag = false;
    /**
     * 敌方坦克移动标志
     */
    private boolean enemyMoving = true;
    /**
     * 坦克形状
     */
    private Rectangle tankRect = new Rectangle();
    /**
     * 玩家坦克移动速度
     */
    private static final int PLAYER_TANK_SPEED = 4;
    /**
     * 敌人坦克移动速度
     */
    private static final int ENEMY_TANK_SPEED = 1;

    public Tank(int x, int y, DirectionEnum dir, GroupEnum group, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tankFrame = tankFrame;

        tankRect.x = this.x;
        tankRect.y = this.y;
        tankRect.width = getTankWidth();
        tankRect.height = getTankHeight();
    }

    /**
     * 画出坦克当前位置
     *
     * @param g
     */
    @Override
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
                        upward();
                        break;
                    case LEFT:
                        leftward();
                        break;
                    case DOWN:
                        downward();
                        break;
                    case RIGHT:
                        rightward();
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
     * 向上移动
     */
    private void upward() {
        for (Wall wall : tankFrame.getWallList()) {
            if (CollisionDetectorFactory.getCollisionDetectStrategy(TankWallCollisionDetector.class).collisionDetect(this, wall, x, y - PLAYER_TANK_SPEED)) {
                playerTankCollideFlag = true;
                return;
            }
        }
        y -= PLAYER_TANK_SPEED;
    }

    /**
     * 向下移动
     */
    private void downward() {
        for (Wall wall : tankFrame.getWallList()) {
            if (CollisionDetectorFactory.getCollisionDetectStrategy(TankWallCollisionDetector.class).collisionDetect(this, wall, x, y + PLAYER_TANK_SPEED)) {
                playerTankCollideFlag = true;
                return;
            }
        }
        y += PLAYER_TANK_SPEED;
    }

    /**
     * 向左移动
     */
    private void leftward() {
        for (Wall wall : tankFrame.getWallList()) {
            if (CollisionDetectorFactory.getCollisionDetectStrategy(TankWallCollisionDetector.class).collisionDetect(this, wall, x - PLAYER_TANK_SPEED, y)) {
                playerTankCollideFlag = true;
                return;
            }
        }
        x -= PLAYER_TANK_SPEED;
    }

    /**
     * 向右移动
     */
    private void rightward() {
        for (Wall wall : tankFrame.getWallList()) {
            if (CollisionDetectorFactory.getCollisionDetectStrategy(TankWallCollisionDetector.class).collisionDetect(this, wall, x + PLAYER_TANK_SPEED, y)) {
                playerTankCollideFlag = true;
                return;
            }
        }
        x += PLAYER_TANK_SPEED;
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
        if (this.y >= TankFrame.GAME_HEIGHT - getTankHeight()) {
            this.y = TankFrame.GAME_HEIGHT - getTankHeight();
        }
    }

    /**
     * 设置敌人随机方向
     */
    public void setEnemyTankDir() {
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
}
