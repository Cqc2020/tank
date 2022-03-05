package com.cqc.tank.model;

import com.cqc.tank.util.AudioUtil;
import com.cqc.tank.util.ImageUtil;
import com.cqc.tank.frame.GamePanel;
import lombok.Data;

import java.awt.*;

/**
 * 爆炸
 * @author Cqc on 2022/2/13 8:27 下午
 */
@Data
public class Blast extends GameObject {
    private GamePanel gamePanel;
    private boolean alive = true;
    private static int WIDTH = ImageUtil.blasts[0].getWidth();
    private static int HEIGHT = ImageUtil.blasts[0].getHeight();

    public Blast(int x, int y, GamePanel gamePanel) {
        this.x = x;
        this.y = y;
        this.gamePanel = gamePanel;
    }

    /**
     * 爆炸画笔
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        if (!alive) {
            gamePanel.getBlastList().remove(this);
        }
        // 添加背景音乐
        AudioUtil.playBgm();
        for (int i = 0; i < ImageUtil.blasts.length; i++) {
            g.drawImage(ImageUtil.blasts[i], x, y, null);
        }
        // 爆炸完后，爆炸效果消失
        die();
    }

    /**
     * 爆炸死亡
     */
    private void die() {
        this.alive = false;
    }
}