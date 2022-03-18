package com.cqc.tank.frame;

import com.cqc.tank.config.GameConfig;
import com.cqc.tank.entity.enums.AudioTypeEnum;
import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;
import com.cqc.tank.entity.enums.MapObjEnum;
import com.cqc.tank.model.*;
import com.cqc.tank.util.AudioUtil;
import com.cqc.tank.util.MapIOUtil;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 游戏界面面板
 *
 * @author Cqc
 * @date 2022/2/27
 */
// @Data：会重写toString方法，debug模式会调用对象toString，A引用B，B引用A，不断循环引用，最终报StackOverflowError，因此，改成@Getter和@Setter
@Getter
@Setter
public class GamePanel extends JPanel {
    private MainFrame mainFrame;
    private GroupEnum groupEnum;
    private Integer enemyTankSpeed;
    private Integer playerTankSpeed;
    private Integer initialEnemyCount;
    private Buff buff;
    private Random r = new Random();
    /**
     * 坦克集合
     */
    private List<Tank> tankList;
    /**
     * 爆炸集合
     */
    private List<Blast> blastList;
    /**
     * 子弹集合
     */
    private List<Bullet> bulletList;
    /**
     * 地图元素集合(墙，水，草，基地)
     */
    private List<GameObject> mapObjList;
    /**
     * 游戏界面键盘事件监听适配器
     */
    private GamePanelKeyAdapter gamePanelKeyAdapter;
    private ScheduledExecutorService ses;
    /**
     * 按键反馈
     */
    private boolean bU = false;
    private boolean bL = false;
    private boolean bD = false;
    private boolean bR = false;

    public GamePanel(MainFrame mainFrame) throws HeadlessException {
        this.mainFrame = mainFrame;
        init();
        addKeyListener();
    }

    private void init() {
        tankList = new ArrayList<>();
        blastList = new ArrayList<>();
        bulletList = new ArrayList<>();

        mapObjList = MapIOUtil.readMap(Stage.getInstance().getCurStage());
        initialEnemyCount = Integer.valueOf(GameConfig.getInstance().get("initialEnemyTankCount").toString());
        playerTankSpeed = Integer.valueOf(GameConfig.getInstance().get("playerTankSpeed").toString());
        enemyTankSpeed = Integer.valueOf(GameConfig.getInstance().get("enemyTankSpeed").toString());
        // 初始化玩家坦克
        tankList.add(new Tank(500, 500, DirectionEnum.UP, GroupEnum.PLAYER, this, playerTankSpeed, false));
        // 初始化敌方坦克
        for (int i = 0; i < initialEnemyCount; i++) {
            tankList.add(new Tank(150 + i * 100, 150, DirectionEnum.DOWN, GroupEnum.ENEMY, this, enemyTankSpeed, true));
        }
        // 创建一个定时任务，每10秒随机产生一个游戏增益道具
        ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            buff = new Buff(r.nextInt(MainFrame.GAME_WIDTH), r.nextInt(MainFrame.GAME_HEIGHT), this);
        }, 0, 10, TimeUnit.SECONDS);
        // 进入游戏画面后的背景音乐
        AudioUtil.asyncPlayAudio(AudioTypeEnum.START);
    }

    private void addKeyListener() {
        gamePanelKeyAdapter = new GamePanelKeyAdapter();
        mainFrame.addKeyListener(gamePanelKeyAdapter);
    }

    /**
     * 键盘监听适配器
     */
    class GamePanelKeyAdapter extends KeyAdapter {
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
            }
            setDirection(getPlayerTank());
        }
    }

    /**
     * update()方法用于写每帧更新时的逻辑.每一帧更新的时候, 我们会把该帧的图片画到屏幕中.
     * 但是这样做是有缺陷的, 因为把一副图片画到屏幕上会有延时, 游戏显示不够流畅；
     * 所以这里用到了一种缓冲技术：先把图像画到一块幕布上, 每帧更新的时候直接把画布推到窗口中显示；
     *
     * @param g
     */
    @Override
    public void update(Graphics g) {
        Image offScreenImage = this.createImage(mainFrame.getWidth(), mainFrame.getHeight());
        Graphics graphics = offScreenImage.getGraphics();
        Color color = graphics.getColor();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
        graphics.setColor(color);
        paint(graphics);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    /**
     * 坦克大战画笔
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        // 画出面板统计信息
        paintStatisticInfo(g);
        // 画出所有坦克
        paintTankList(g);
        // 画出子弹
        paintBulletList(g);
        // 画出爆炸
        paintBlastList(g);
        // 画出地图元素
        paintMapObjList(g);
        // 画出游戏道具
        paintBuff(g);
        // 进入下一关
        if (getEnemyTankCount() == 0) {
            enterNextStage();
        }
    }

    /**
     * 画出游戏道具
     * @param g
     */
    private void paintBuff(Graphics g) {
        if (Objects.nonNull(buff)) {
            if (!buff.alive) {
                buff = null;
            } else {
                buff.paint(g);
            }
        }
    }

    /**
     * 画出地图元素
     * @param g
     */
    private void paintMapObjList(Graphics g) {
        if (Objects.nonNull(mapObjList)) {
            for (int i = 0; i < mapObjList.size(); i++) {
                GameObject mapObj = mapObjList.get(i);
                if (!mapObj.alive) {
                    mapObjList.remove(mapObj);
                }
                mapObj.paint(g);
            }
        }
    }

    /**
     * 画出爆炸
     * @param g
     */
    private void paintBlastList(Graphics g) {
        for (int i = 0; i < blastList.size(); i++) {
            Blast blast = blastList.get(i);
            if (!blast.alive) {
                blastList.remove(blast);
            }
            blast.paint(g);
        }
    }

    /**
     * 画出子弹
     * @param g
     */
    private void paintBulletList(Graphics g) {
        for (int i = 0; i < bulletList.size(); i++) {
            Bullet bullet = bulletList.get(i);
            if (!bullet.alive) {
                bulletList.remove(bullet);
            }
            bullet.paint(g);
        }
    }

    /**
     * 画出所有坦克
     * @param g
     */
    private void paintTankList(Graphics g) {
        for (int i = 0; i < tankList.size(); i++) {
            Tank tank = tankList.get(i);
            if (!tank.alive) {
                tankList.remove(tank);
                blastList.add(new Blast(tank.x, tank.y, MapObjEnum.TANK));
            }
            tank.paint(g);
        }
    }

    /**
     * 画出面板统计信息
     * @param g
     */
    private void paintStatisticInfo(Graphics g) {
        Color color = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("子弹数量：" + bulletList.size(), 10, 60);
        g.drawString("敌人数量：" + getEnemyTankCount(), 10, 80);
        g.setColor(color);
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

    /**
     * 进入下一关
     */
    private void enterNextStage() {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mainFrame.setPanel(new StagePanel(mainFrame, Stage.getInstance().getNextStage()));
    }

    /**
     * 取出玩家坦克
     * @return
     */
    public Tank getPlayerTank() {
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

    /**
     * 取出玩家坦克
     * @return
     */
    public List<Tank> getPlayerTankList() {
        List<Tank> playerList = new ArrayList<>();
        for (int i = 0; i < tankList.size(); i++) {
            Tank aTank = tankList.get(i);
            if (aTank != null) {
                if (aTank.getGroup().equals(GroupEnum.PLAYER)) {
                    playerList.add(aTank);
                }
            }
        }
        return playerList;
    }

    /**
     * 获取场上存活敌方坦克数量
     *
     * @return
     */
    private int getEnemyTankCount() {
        return (int) tankList.stream().filter(tank -> tank.getGroup().equals(GroupEnum.ENEMY)).count();
    }

}
