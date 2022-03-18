package com.cqc.tank.model;

import com.cqc.tank.entity.enums.AudioTypeEnum;
import com.cqc.tank.factory.CollisionDetectorFactory;
import com.cqc.tank.frame.GamePanel;
import com.cqc.tank.frame.MainFrame;
import com.cqc.tank.strategy.collide.BuffTankCollisionDetector;
import com.cqc.tank.util.AudioUtil;
import com.cqc.tank.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Cqc
 * @date 2022/3/12
 */
public class Buff extends GameObject {
    /**
     * 坦克窗口
     */
    private GamePanel gamePanel;
    private Random random;
    private int index;
    private List<BufferedImage> buffList = new ArrayList<BufferedImage>() {
        {
            add(ImageUtil.star);
            add(ImageUtil.timer);
            add(ImageUtil.bomb);
            add(ImageUtil.gun);
            add(ImageUtil.helmet);
            add(ImageUtil.boat);
            add(ImageUtil.tankLife);
        }
    };

    {
        this.random = new Random();
    }

    public Buff(int x, int y, GamePanel gamePanel) {
        this.x = x;
        this.y = y;
        this.gamePanel = gamePanel;
        init();
    }

    private void init() {
        // 游戏增益道具的宽高都是40
        objRect = new Rectangle(x, y, 40, 40);
        index = random.nextInt(buffList.size());
    }

    @Override
    public void paint(Graphics g) {
        // 道具不能超出游戏界面
        Point p = MainFrame.boundsCheck(new Point(x, y), 40, 80);
        g.drawImage(buffList.get(index), p.x, p.y, null);
        // 碰撞检测
        handleCollision();
    }

    private void handleCollision() {
        // 玩家坦克吃到增益道具处理
        for (Tank tank : gamePanel.getPlayerTankList()) {
            if (CollisionDetectorFactory.getCollisionDetectStrategy(BuffTankCollisionDetector.class).collisionDetect(this, tank)) {
                // 添加音效
                AudioUtil.syncPlayAudio(AudioTypeEnum.ADD);
                // 道具被吃后消亡
                die();
            }
        }
    }

}
