package com.cqc.tank.config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Cqc on 2022/2/12 11:46 下午
 */
public class ResourceMgr {
    /** 玩家坦克图片 */
    public static BufferedImage playerTankU, playerTankD, playerTankL, playerTankR;
    /** 敌人坦克图片 */
    public static BufferedImage enemyTankU, enemyTankD, enemyTankL, enemyTankR;
    /** 子弹图片 */
    public static BufferedImage bulletU, bulletD, bulletL, bulletR;
    /** 坦克爆炸图片 */
    public static BufferedImage[] blasts = new BufferedImage[8];
    /**
     * 小红墙体图片
     */
    public static BufferedImage wall;
    /**
     * 大红墙体图片
     */
    public static BufferedImage walls;
    /**
     * 小加强白墙体图片
     */
    public static BufferedImage steel;
    /**
     * 大加强白墙体图片
     */
    public static BufferedImage steels;
    /**
     * 基地老鹰图片
     */
    public static BufferedImage eagle;
    /**
     * 草地图片
     */
    public static BufferedImage grass;
    /**
     * 水图片
     */
    public static BufferedImage water;

    static {
        try {
            playerTankU = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/p1tankU.gif")));
            playerTankD = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/p1tankD.gif")));
            playerTankL = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/p1tankL.gif")));
            playerTankR = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/p1tankR.gif")));

            enemyTankU = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/enemy1U.gif")));
            enemyTankD = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/enemy1D.gif")));
            enemyTankL = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/enemy1L.gif")));
            enemyTankR = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/enemy1R.gif")));

            bulletU = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/bulletU.gif")));
            bulletD = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/bulletD.gif")));
            bulletL = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/bulletL.gif")));
            bulletR = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/bulletR.gif")));

            for (int i = 0; i < blasts.length; i++) {
                blasts[i] = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/blast" + (i + 1) + ".gif")));
            }

            walls = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/walls.gif")));
            wall = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/wall.gif")));
            steel = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/steel.gif")));
            steels = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/steels.gif")));

            eagle = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/eagle.gif")));

            grass = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/grass.png")));

            water = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("image/water.gif")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
