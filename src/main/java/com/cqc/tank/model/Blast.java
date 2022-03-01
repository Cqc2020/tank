package com.cqc.tank.model;

import com.cqc.tank.config.Audio;
import com.cqc.tank.config.ResourceMgr;
import com.cqc.tank.frame.GamePanel;
import lombok.Data;

import java.awt.*;

/**
 * 爆炸
 * @author Cqc on 2022/2/13 8:27 下午
 */
@Data
public class Blast extends AbstractGameObject {
    private GamePanel gamePanel;
    private boolean alive = true;
    private static int WIDTH = ResourceMgr.blasts[0].getWidth();
    private static int HEIGHT = ResourceMgr.blasts[0].getHeight();

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
        Audio.playBgm();
        for (int i = 0; i < ResourceMgr.blasts.length; i++) {
            g.drawImage(ResourceMgr.blasts[i], x, y, null);
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
