package com.cqc.tank.model;

import com.cqc.tank.entity.enums.AudioTypeEnum;
import com.cqc.tank.entity.enums.MapObjEnum;
import com.cqc.tank.util.AudioUtil;
import com.cqc.tank.util.ImageUtil;
import lombok.Data;

import java.awt.*;

/**
 * 爆炸
 * @author Cqc on 2022/2/13 8:27 下午
 */
@Data
public class Blast extends GameObject {

    public Blast(int x, int y, MapObjEnum mapObjEnum) {
        this.x = x;
        this.y = y;
        this.mapObjEnum = mapObjEnum;
    }

    /**
     * 爆炸画笔
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        // 添加背景音乐
        AudioUtil.syncPlayAudio(AudioTypeEnum.BLAST);
        switch (mapObjEnum) {
            case TANK:
                for (int i = 0; i < ImageUtil.blasts.length; i++) {
                    g.drawImage(ImageUtil.blasts[i], x, y, null);
                }
                break;
            case BULLET:
                int blastX = x - ImageUtil.blasts[1].getWidth() / 4;
                int blastY = y - ImageUtil.blasts[1].getHeight() / 4;
                g.drawImage(ImageUtil.blasts[1], blastX, blastY, null);
                break;
            default:
        }
        // 爆炸完后，爆炸效果消失
        die();
    }

}
