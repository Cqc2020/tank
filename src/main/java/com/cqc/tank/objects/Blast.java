package com.cqc.tank.objects;

import com.cqc.tank.config.Audio;
import com.cqc.tank.config.ResourceMgr;
import com.cqc.tank.TankFrame;
import lombok.Data;

import java.awt.*;

/**
 * 爆炸
 * @author Cqc on 2022/2/13 8:27 下午
 */
@Data
public class Blast extends AbstractGameObject {
    private TankFrame tankFrame;
    private boolean alive = true;
    private static int WIDTH = ResourceMgr.blasts[0].getWidth();
    private static int HEIGHT = ResourceMgr.blasts[0].getHeight();

    public Blast(int x, int y, TankFrame tankFrame) {
        this.x = x;
        this.y = y;
        this.tankFrame = tankFrame;
    }

    /**
     * 爆炸画笔
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        if (!alive) {
            tankFrame.getBlastList().remove(this);
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
