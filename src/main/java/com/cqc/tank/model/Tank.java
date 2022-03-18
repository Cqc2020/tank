package com.cqc.tank.model;

import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.factory.CollisionDetectorFactory;
import com.cqc.tank.factory.FireStrategyFactory;
import com.cqc.tank.frame.GamePanel;
import com.cqc.tank.frame.MainFrame;
import com.cqc.tank.strategy.collide.TankTankCollisionDetector;
import com.cqc.tank.strategy.collide.TankWallCollisionDetector;
import com.cqc.tank.util.ImageUtil;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Random;

/**
 * 坦克类
 *
 * @author Cqc on 2022/2/12 9:48 上午
 */
@Getter
@Setter
public class Tank extends GameObject {
    /**
     * 坦克移动速度
     */
    private int speed;
    /**
     * 敌人移动方向
     */
    private Random random;
    /**
     * 坦克分组
     */
    private GroupEnum group;
    /**
     * 玩家坦克移动标志
     */
    private boolean moveFlag;
    /**
     * 坦克移动方向
     */
    private DirectionEnum dir;
    /**
     * 坦克窗口
     */
    private GamePanel gamePanel;
    /**
     * 坦克开火策略工厂
     */
    private FireStrategyFactory fireStrategyFactory;

    public Tank(int x, int y, DirectionEnum dir, GroupEnum group, GamePanel gamePanel, int speed, boolean moveFlag) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.gamePanel = gamePanel;
        this.speed = speed;
        this.moveFlag = moveFlag;
        init();
    }

    private void init() {
        random = new Random();
        fireStrategyFactory = new FireStrategyFactory();
        objRect = new Rectangle(x, y, ImageUtil.getTankWidth(group,dir), ImageUtil.getTankHeight(group,dir));
    }

    /**
     * 画出坦克当前位置
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        // 画出当前坦克
        paintFrame(g);
        // 先处理碰撞
        handleCollision();
        // 再判断是否移动
        handleMoving();
        // 敌方坦克开火
        enemyFire();
        // 边界检测
        boundsCheck();
        // update rect
        updateRect();
    }

    /**
     * 画出当前画面
     * @param g
     */
    private void paintFrame(Graphics g) {
        if (GroupEnum.PLAYER.equals(group)) {
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
            }
        } else if (GroupEnum.ENEMY.equals(group)) {
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
            }
        }
    }

    /**
     * 敌方坦克开火
     */
    private void enemyFire() {
        if (GroupEnum.ENEMY.equals(group)) {
            fire(GroupEnum.ENEMY);
        }
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
    private void handleMoving() {
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
        }
    }

    /**
     * 处理坦克与游戏物体碰撞
     * @param x tank1的x
     * @param y tank1的y
     */
    private void doHandleCollision(int x, int y) {
        // 坦克撞墙处理
        for (GameObject mapObj : gamePanel.getMapObjList()) {
            if (mapObj instanceof Wall) {
                if (CollisionDetectorFactory.getCollisionDetectStrategy(TankWallCollisionDetector.class).collisionDetect(this, mapObj, x, y)) {
                    moveFlag = false;
                    return;
                }
            }
        }
        // 坦克与坦克的碰撞处理
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
        if (group.equals(tank.group)) {
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
     * 更新坦克轮廓坐标
     */
    private void updateRect() {
        objRect.setLocation(x, y);
    }

    /**
     * 设置敌人随机方向
     */
    public void setEnemyTankDir() {
        this.dir = DirectionEnum.values()[random.nextInt(4)];
    }

    /**
     * 设置坦克当前相反方向
     * @param oldDir
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
        }
    }

    /**
     * 坦克发射一枚子弹
     */
    public void fire(GroupEnum group) {
        fireStrategyFactory.getFireStrategy(group).fire(this);
    }

    /**
     * 窗口边界检测
     */
    private void boundsCheck() {
        Point p = MainFrame.boundsCheck(new Point(x, y),
                ImageUtil.getTankWidth(group, dir), ImageUtil.getTankHeight(group, dir));
        x = p.x;
        y = p.y;
    }

}
