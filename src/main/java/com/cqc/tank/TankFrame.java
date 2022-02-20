package com.cqc.tank;

import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.factory.FireStrategyFactory;
import com.cqc.tank.objects.Blast;
import com.cqc.tank.objects.Bullet;
import com.cqc.tank.objects.Tank;

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
public class TankFrame extends Frame {
    public static final int GAME_WIDTH = 800;

    public static final int GAME_HEIGHT = 600;

    private List<Bullet> playerBulletList = new ArrayList<>();

    private Tank p1Tank = new Tank(500, 500, DirectionEnum.UP, GroupEnum.PLAYER, this);

    private List<Tank> enemyTankList = new ArrayList<>();

    private List<Blast> blastList = new ArrayList<>();

    public FireStrategyFactory fireStrategyFactory = new FireStrategyFactory();

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
                        p1Tank.fire(fireStrategyFactory.getFireStrategy(GroupEnum.PLAYER));
                        break;
                    default:
                        break;
                }
                setDirection();
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
                setDirection();
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
        g.drawString("敌人数量：" + enemyTankList.size(), 10, 80);
        g.setColor(color);
        // 画出玩家1坦克
        p1Tank.paint(g);
        // 画出敌方坦克
        for (int i = 0; i < enemyTankList.size(); i++) {
            enemyTankList.get(i).paint(g);
        }
        // 画出子弹
        for (int i = 0; i < playerBulletList.size(); i++) {
            playerBulletList.get(i).paint(g);
        }
        // 画出爆炸
        for (int i = 0; i < blastList.size(); i++) {
            blastList.get(i).paint(g);
        }
        // 子弹与坦克的碰撞检测
        for (int i = 0; i < playerBulletList.size(); i++) {
            for (int j = 0; j < enemyTankList.size(); j++) {
                playerBulletList.get(i).collideWith(enemyTankList.get(j));
            }
        }
    }

    /**
     * 设置坦克方向
     */
    public void setDirection() {
        if (!bU && !bL && !bD && !bR) {
            p1Tank.setPlayerMoving(false);
        } else {
            p1Tank.setPlayerMoving(true);
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

    public List<Bullet> getPlayerBulletList() {
        return playerBulletList;
    }

    public void setPlayerBulletList(List<Bullet> playerBulletList) {
        this.playerBulletList = playerBulletList;
    }

    public List<Tank> getEnemyTankList() {
        return enemyTankList;
    }

    public void setEnemyTankList(List<Tank> enemyTankList) {
        this.enemyTankList = enemyTankList;
    }

    public List<Blast> getBlastList() {
        return blastList;
    }

    public void setBlastList(List<Blast> blastList) {
        this.blastList = blastList;
    }
}
