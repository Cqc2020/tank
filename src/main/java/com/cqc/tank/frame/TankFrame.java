package com.cqc.tank.frame;

import com.cqc.tank.util.ImageUtil;
import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.entity.enums.MapObjEnum;
import com.cqc.tank.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cqc on 2022/2/11 11:26 下午
 */
public class TankFrame extends JFrame {
    /**
     * 游戏窗体宽度
     */
    public static final int GAME_WIDTH = 1000;
    /**
     * 游戏窗体高度
     */
    public static final int GAME_HEIGHT = 600;
    /**
     * 基地老鹰
     */
    private Eagle eagle;
    /**
     * 玩家子弹集合
     */
    private List<Bullet> playerBulletList = new ArrayList<>();
    /**
     * 墙体集合
     */
    private List<Wall> wallList = new ArrayList<>();
    /**
     * 坦克集合
     */
    private List<Tank> tankList = new ArrayList<>();
    /**
     * 爆炸集合
     */
    private List<Blast> blastList = new ArrayList<>();
    /**
     * 草地集合
     */
    private List<Grass> grassList = new ArrayList<>();
    /**
     * 水块集合
     */
    private List<Water> waterList = new ArrayList<>();

    {
        wallList.add(new Wall(0, 200, MapObjEnum.WALLS));
        wallList.add(new Wall(GAME_WIDTH - ImageUtil.walls.getWidth(), GAME_HEIGHT - 200, MapObjEnum.WALLS));
        wallList.add(new Wall((GAME_WIDTH)/2 - ImageUtil.eagle.getWidth() - ImageUtil.wall.getWidth(), GAME_HEIGHT - ImageUtil.wall.getHeight(), MapObjEnum.WALL));
        wallList.add(new Wall(300, 300, MapObjEnum.STEEL));
        eagle = new Eagle((GAME_WIDTH)/2 - ImageUtil.eagle.getWidth(), GAME_HEIGHT - ImageUtil.eagle.getHeight());
        grassList.add(new Grass(450, 256, MapObjEnum.GRASS));
        waterList.add(new Water(667, 400, MapObjEnum.WATER));
    }

    // 按键反馈
    boolean bU = false;
    boolean bL = false;
    boolean bD = false;
    boolean bR = false;

    public TankFrame() throws HeadlessException {
        setVisible(true);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setResizable(false);
        setTitle("坦克大战");

        // 按键事件监听器
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                // 根据按下的键，向不同方向移动
                switch (keyCode) {
                    case KeyEvent.VK_W:
                        bU = true;
                        break;
                    case KeyEvent.VK_A:
                        bL = true;
                        break;
                    case KeyEvent.VK_S:
                        bD = true;
                        break;
                    case KeyEvent.VK_D:
                        bR = true;
                        break;
                    case KeyEvent.VK_J:
                        getPlayerTank().fire(GroupEnum.PLAYER);
                        break;
                    default:
                        break;
                }
                setDirection(getPlayerTank());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_W:
                        bU = false;
                        break;
                    case KeyEvent.VK_A:
                        bL = false;
                        break;
                    case KeyEvent.VK_S:
                        bD = false;
                        break;
                    case KeyEvent.VK_D:
                        bR = false;
                        break;
                    default:
                        break;
                }
                setDirection(getPlayerTank());
            }
        });

        // 关闭游戏窗口
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e){
                System.exit(0);
            }
        });
    }

    /*
     * update()方法用于写每帧更新时的逻辑.
     * 每一帧更新的时候, 我们会把该帧的图片画到屏幕中.
     * 但是这样做是有缺陷的, 因为把一副图片画到屏幕上会有延时, 游戏显示不够流畅
     * 所以这里用到了一种缓冲技术.
     * 先把图像画到一块幕布上, 每帧更新的时候直接把画布推到窗口中显示
     */
    Image offScreenImage = null;
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics graphics = offScreenImage.getGraphics();
        Color color = graphics.getColor();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        graphics.setColor(color);
        paint(graphics);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    /**
     * 坦克大战画笔
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        Color color = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("子弹数量：" + playerBulletList.size(), 10, 60);
        g.drawString("敌人数量：" + tankList.size(), 10, 80);
        g.setColor(color);
        // 画出所有坦克
        for (int i = 0; i < tankList.size(); i++) {
            tankList.get(i).paint(g);
        }
        // 画出子弹
        for (int i = 0; i < playerBulletList.size(); i++) {
            playerBulletList.get(i).paint(g);
        }
        // 画出爆炸
        for (int i = 0; i < blastList.size(); i++) {
            blastList.get(i).paint(g);
        }
        // 画出墙体
        for (int i = 0; i < wallList.size(); i++) {
            wallList.get(i).paint(g);
        }
        // 画出老鹰
        eagle.paint(g);
        // 画出草地
        for (int i = 0; i < grassList.size(); i++) {
            grassList.get(i).paint(g);
        }
        // 画出水区域
        for (int i = 0; i < waterList.size(); i++) {
            waterList.get(i).paint(g);
        }
        // 子弹与坦克的碰撞检测
        for (int i = 0; i < playerBulletList.size(); i++) {
            for (int j = 0; j < tankList.size(); j++) {
                playerBulletList.get(i).collideWith(tankList.get(j));
            }
        }
    }

    /**
     * 设置坦克方向
     */
    public void setDirection(Tank p1Tank) {
        if (!bU && !bL && !bD && !bR) {
            p1Tank.setMoveFlag(false);
        } else {
            p1Tank.setMoveFlag(true);
            if (bU) {
                p1Tank.setDir(DirectionEnum.UP);
            }
            if (bL) {
                p1Tank.setDir(DirectionEnum.LEFT);
            }
            if (bD) {
                p1Tank.setDir(DirectionEnum.DOWN);
            }
            if (bR) {
                p1Tank.setDir(DirectionEnum.RIGHT);
            }
        }
    }

    public Tank getPlayerTank() {
        // 取出玩家坦克
        Tank tank = null;
        for (int i = 0; i < tankList.size(); i++) {
            Tank aTank = tankList.get(i);
            if (aTank != null) {
                if (aTank.getGroup().equals(GroupEnum.PLAYER)) {
                    tank = aTank;
                }
            }
        }
        return tank;
    }

    public List<Bullet> getPlayerBulletList() {
        return playerBulletList;
    }

    public void setPlayerBulletList(List<Bullet> playerBulletList) {
        this.playerBulletList = playerBulletList;
    }

    public List<Tank> getTankList() {
        return tankList;
    }

    public void setTankList(List<Tank> tankList) {
        this.tankList = tankList;
    }

    public List<Blast> getBlastList() {
        return blastList;
    }

    public List<Wall> getWallList() {
        return wallList;
    }
}
