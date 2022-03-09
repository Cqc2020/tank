package com.cqc.tank.model;

import com.cqc.tank.frame.GamePanel;
import com.cqc.tank.frame.MainFrame;
import com.cqc.tank.util.ImageUtil;
import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.factory.CollisionDetectorFactory;
import com.cqc.tank.factory.FireStrategyFactory;
import com.cqc.tank.strategy.collide.TankTankCollisionDetector;
import com.cqc.tank.strategy.collide.TankWallCollisionDetector;
import lombok.Data;

import java.awt.*;
import java.util.Random;

/**
 * 坦克类
 *
 * @author Cqc on 2022/2/12 9:48 上午
 */
@Data
public class Tank extends GameObject {
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
    private GamePanel gamePanel;
    /**
     * 坦克存活状态
     */
    private boolean alive = true;
    /**
     * 玩家坦克移动标志
     */
    private boolean moveFlag;
    /**
     * 坦克形状
     */
    private Rectangle tankRect = new Rectangle();
    /**
     * 坦克移动速度
     */
    private int speed;
    /**
     * 坦克开火策略工厂
     */
    private FireStrategyFactory fireStrategyFactory = new FireStrategyFactory();

    public Tank(int x, int y, DirectionEnum dir, GroupEnum group, GamePanel gamePanel, int speed, boolean moveFlag) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.gamePanel = gamePanel;
        this.speed = speed;
        this.moveFlag = moveFlag;

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
            gamePanel.getTankList().remove(this);
            gamePanel.getBlastList().add(new Blast(this.x, this.y, gamePanel));
        }
        if (GroupEnum.PLAYER.equals(this.group)) {
            switch (dir) {
                case UP:
                    g.drawImage(ImageUtil.playerTankU, x, y, null);
                    break;
                case LEFT:
                    g.drawImage(ImageUtil.playerTankL, x, y, null);
                    break;
                case DOWN:
                    g.drawImage(ImageUtil.playerTankD, x, y, null);
                    break;
                case RIGHT:
                    g.drawImage(ImageUtil.playerTankR, x, y, null);
                    break;
                default:
                    break;
            }
        } else if (GroupEnum.ENEMY.equals(this.group)) {
            switch (dir) {
                case UP:
                    g.drawImage(ImageUtil.enemyTankU, x, y, null);
                    break;
                case LEFT:
                    g.drawImage(ImageUtil.enemyTankL, x, y, null);
                    break;
                case DOWN:
                    g.drawImage(ImageUtil.enemyTankD, x, y, null);
                    break;
                case RIGHT:
                    g.drawImage(ImageUtil.enemyTankR, x, y, null);
                    break;
                default:
                    break;
            }
        }
        getNextFrame();
    }

    /**
     * 获取坦克下一帧图像
     */
    private void getNextFrame() {
        // 先处理碰撞
        handleCollision();
        // 再判断是否移动
        move();
        if (GroupEnum.ENEMY.equals(group)) {
            fire(GroupEnum.ENEMY);
        }
        boundsCheck();
        // update rect
        updateRect();
    }

    /**
     * 坦克与游戏物体碰撞处理
     */
    private void handleCollision() {
        switch (dir) {
            case UP:
                doHandleCollision(x, y - speed);
                break;
            case LEFT:
                doHandleCollision(x - speed, y);
                break;
            case DOWN:
                doHandleCollision(x, y + speed);
                break;
            case RIGHT:
                doHandleCollision(x + speed, y);
                break;
            default:
                break;
        }
    }

    /**
     * 坦克朝不同方向移动
     */
    private void move() {
        if (!moveFlag) {
            return;
        }
        switch (dir) {
            case UP:
                y -= speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case RIGHT:
                x += speed;
                break;
            default:
                break;
        }
    }

    /**
     * 处理坦克与游戏物体碰撞
     * @param x tank1的x
     * @param y tank1的y
     */
    private void doHandleCollision(int x, int y) {
        // 坦克撞墙处理
        for (GameObject gameObject : gamePanel.getMapObjList()) {
            if (gameObject instanceof Wall || gameObject instanceof Water) {
                if (CollisionDetectorFactory.getCollisionDetectStrategy(TankWallCollisionDetector.class).collisionDetect(this, gameObject, x, y)) {
                    moveFlag = false;
                    return;
                }
            }
        }
        for (Tank tank : gamePanel.getTankList()) {
            // 坦克不能和自己进行碰撞检测，否则会出现坦克无法移动的bug
            if (!this.equals(tank)) {
                if (CollisionDetectorFactory.getCollisionDetectStrategy(TankTankCollisionDetector.class).collisionDetect(this, tank, x, y)) {
                    stopMoving(tank);
                    return;
                }
                if (GroupEnum.ENEMY.equals(tank.getGroup())) {
                    tank.moveFlag = true;
                }
            }
        }
    }

    /**
     * 坦克停止移动
     * @param tank
     */
    private void stopMoving(Tank tank) {
        if (group.equals(tank.getGroup())) {
            // 都是玩家坦克
            if (GroupEnum.PLAYER.equals(group)) {
                // TODO:当有两个玩家时，再处理

            } else { // 都是敌方坦克
                // 两坦克都掉头
                setTankCounterDir(dir);
            }
        } else {
            // this是玩家坦克，tank是敌方坦克
            if (GroupEnum.PLAYER.equals(group)) {
                // 玩家坦克停止前进
                moveFlag = false;
                tank.moveFlag = false;
            } else { // this是敌方坦克，tank是玩家坦克
                // 敌方坦克停止前进
                moveFlag = false;
            }
        }
    }

    /**
     * 坦克窗口边界检测
     */
    public void boundsCheck() {
        if (this.x <= 0) {
            this.x = 2;
        }
        if (this.x > MainFrame.GAME_WIDTH - getTankWidth()) {
            this.x = MainFrame.GAME_WIDTH - getTankWidth();
        }
        if (this.y <= 30) {
            this.y = 30;
        }
        if (this.y >= MainFrame.GAME_HEIGHT - getTankHeight()) {
            this.y = MainFrame.GAME_HEIGHT - getTankHeight();
        }
    }

    /**
     * 更新坦克轮廓坐标
     */
    private void updateRect() {
        tankRect.x = this.x;
        tankRect.y = this.y;
    }

    /**
     * 设置敌人随机方向
     */
    public void setEnemyTankDir() {
        this.dir = DirectionEnum.values()[random.nextInt(4)];
    }

    /**
     * 设置坦克当前相反方向
     */
    public void setTankCounterDir(DirectionEnum oldDir) {
        switch (oldDir) {
            case UP:
                dir = DirectionEnum.DOWN;
                break;
            case LEFT:
                dir = DirectionEnum.RIGHT;
                break;
            case DOWN:
                dir = DirectionEnum.UP;
                break;
            case RIGHT:
                dir = DirectionEnum.LEFT;
                break;
            default:
                break;
        }
    }

    /**
     * 坦克发射一枚子弹
     */
    public void fire(GroupEnum group) {
        fireStrategyFactory.getFireStrategy(group).fire(this);
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
                    return ImageUtil.playerTankU.getWidth();
                case LEFT:
                    return ImageUtil.playerTankL.getWidth();
                case DOWN:
                    return ImageUtil.playerTankD.getWidth();
                case RIGHT:
                    return ImageUtil.playerTankR.getWidth();
                default:
                    return 0;
            }
        } else {
            switch (dir) {
                case UP:
                    return ImageUtil.enemyTankU.getWidth();
                case LEFT:
                    return ImageUtil.enemyTankL.getWidth();
                case DOWN:
                    return ImageUtil.enemyTankD.getWidth();
                case RIGHT:
                    return ImageUtil.enemyTankR.getWidth();
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
                    return ImageUtil.playerTankU.getHeight();
                case LEFT:
                    return ImageUtil.playerTankL.getHeight();
                case DOWN:
                    return ImageUtil.playerTankD.getHeight();
                case RIGHT:
                    return ImageUtil.playerTankR.getHeight();
                default:
                    return 0;
            }
        } else {
            switch (dir) {
                case UP:
                    return ImageUtil.enemyTankU.getHeight();
                case LEFT:
                    return ImageUtil.enemyTankL.getHeight();
                case DOWN:
                    return ImageUtil.enemyTankD.getHeight();
                case RIGHT:
                    return ImageUtil.enemyTankR.getHeight();
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
                return ImageUtil.bulletU.getWidth();
            case LEFT:
                return ImageUtil.bulletL.getWidth();
            case DOWN:
                return ImageUtil.bulletD.getWidth();
            case RIGHT:
                return ImageUtil.bulletR.getWidth();
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
                return ImageUtil.bulletU.getHeight();
            case LEFT:
                return ImageUtil.bulletL.getHeight();
            case DOWN:
                return ImageUtil.bulletD.getHeight();
            case RIGHT:
                return ImageUtil.bulletR.getHeight();
            default:
                return 0;
        }
    }
}
