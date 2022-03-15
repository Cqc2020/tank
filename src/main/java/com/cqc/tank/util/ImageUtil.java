package com.cqc.tank.util;

import com.cqc.tank.entity.enums.DirectionEnum;
import com.cqc.tank.entity.enums.GroupEnum;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Cqc on 2022/2/12 11:46 下午
 */
public class ImageUtil {
    private static String path = "resources/image/";
    
    /** 玩家坦克图片 */
    public static BufferedImage playerTankU, playerTankD, playerTankL, playerTankR;
    /**
     * 敌人坦克图片
     */
    public static BufferedImage enemyTankU, enemyTankD, enemyTankL, enemyTankR;
    /** 子弹图片 */
    public static BufferedImage bulletU, bulletD, bulletL, bulletR;
    /** 坦克爆炸图片 */
    public static BufferedImage[] blasts = new BufferedImage[8];
    /**
     * 墙体图片
     */
    public static BufferedImage wall, walls, steel, steelH, steelV, steels;
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
    /**
     * 背景图片
     */
    public static BufferedImage background;
    /**
     * 游戏模式选择指针坦克
     */
    public static BufferedImage selectTank;
    /**
     * 关卡进入前画面
     */
    public static BufferedImage stagePrepare;
    /**
     * 游戏加强道具
     */
    public static BufferedImage star, timer, bomb, gun, helmet, boat, tankLife;

    static {
        // 打包成exe文件后，需要使用exe文件的相对路径
        String exePath = System.getProperty("exe.path");
        if (Objects.nonNull(exePath)) {
            path = exePath + path;
        }
        try {
            playerTankU = ImageIO.read(new File(path + "p1tankU.gif"));
            playerTankD = ImageIO.read(new File(path + "p1tankD.gif"));
            playerTankL = ImageIO.read(new File(path + "p1tankL.gif"));
            playerTankR = ImageIO.read(new File(path + "p1tankR.gif"));

            enemyTankU = ImageIO.read(new File(path + "enemy1U.gif"));
            enemyTankD = ImageIO.read(new File(path + "enemy1D.gif"));
            enemyTankL = ImageIO.read(new File(path + "enemy1L.gif"));
            enemyTankR = ImageIO.read(new File(path + "enemy1R.gif"));

            bulletU = ImageIO.read(new File(path + "bulletU.gif"));
            bulletD = ImageIO.read(new File(path + "bulletD.gif"));
            bulletL = ImageIO.read(new File(path + "bulletL.gif"));
            bulletR = ImageIO.read(new File(path + "bulletR.gif"));

            for (int i = 0; i < blasts.length; i++) {
                blasts[i] = ImageIO.read(new File(path + "blast" + (i + 1) + ".gif"));
            }

            walls = ImageIO.read(new File(path + "walls.gif"));
            wall = ImageIO.read(new File(path + "wall.gif"));
            steel = ImageIO.read(new File(path + "steel.gif"));
            steelV = ImageIO.read(new File(path + "steelV.gif"));
            steelH = ImageIO.read(new File(path + "steelH.gif"));
            steels = ImageIO.read(new File(path + "steels.gif"));
            eagle = ImageIO.read(new File(path + "eagle.gif"));
            grass = ImageIO.read(new File(path + "grass.png"));
            water = ImageIO.read(new File(path + "water.gif"));

            background = ImageIO.read(new File(path + "background.jpg"));

            selectTank = ImageIO.read(new File(path + "selecttank.gif"));

            stagePrepare = ImageIO.read(new File(path + "stageBg.jpg"));

            star = ImageIO.read(new File(path + "star.gif"));
            timer = ImageIO.read(new File(path + "timer.gif"));
            bomb = ImageIO.read(new File(path + "bomb.gif"));
            gun = ImageIO.read(new File(path + "gun.png"));
            helmet = ImageIO.read(new File(path + "helmet.png"));
            boat = ImageIO.read(new File(path + "boat.png"));
            tankLife = ImageIO.read(new File(path + "tankLife.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取坦克的宽度
     *
     * @return
     */
    public static int getTankWidth(GroupEnum group, DirectionEnum dir) {
        if (GroupEnum.PLAYER.equals(group)) {
            switch (dir) {
                case UP:
                    return ImageUtil.playerTankU.getWidth();
                case LEFT:
                    return ImageUtil.playerTankL.getWidth();
                case DOWN:
                    return ImageUtil.playerTankD.getWidth();
                case RIGHT:
                    return ImageUtil.playerTankR.getWidth();
                default:
                    return 0;
            }
        } else {
            switch (dir) {
                case UP:
                    return ImageUtil.enemyTankU.getWidth();
                case LEFT:
                    return ImageUtil.enemyTankL.getWidth();
                case DOWN:
                    return ImageUtil.enemyTankD.getWidth();
                case RIGHT:
                    return ImageUtil.enemyTankR.getWidth();
                default:
                    return 0;
            }
        }
    }

    /**
     * 获取坦克的高度
     *
     * @return
     */
    public static int getTankHeight(GroupEnum group, DirectionEnum dir) {
        if (GroupEnum.PLAYER.equals(group)) {
            switch (dir) {
                case UP:
                    return ImageUtil.playerTankU.getHeight();
                case LEFT:
                    return ImageUtil.playerTankL.getHeight();
                case DOWN:
                    return ImageUtil.playerTankD.getHeight();
                case RIGHT:
                    return ImageUtil.playerTankR.getHeight();
                default:
                    return 0;
            }
        } else {
            switch (dir) {
                case UP:
                    return ImageUtil.enemyTankU.getHeight();
                case LEFT:
                    return ImageUtil.enemyTankL.getHeight();
                case DOWN:
                    return ImageUtil.enemyTankD.getHeight();
                case RIGHT:
                    return ImageUtil.enemyTankR.getHeight();
                default:
                    return 0;
            }
        }
    }

    /**
     * 获取子弹的宽度
     *
     * @return
     */
    public static int getBulletWidth(DirectionEnum dir) {
        switch (dir) {
            case UP:
                return ImageUtil.bulletU.getWidth();
            case LEFT:
                return ImageUtil.bulletL.getWidth();
            case DOWN:
                return ImageUtil.bulletD.getWidth();
            case RIGHT:
                return ImageUtil.bulletR.getWidth();
            default:
                return 0;
        }
    }

    /**
     * 获取子弹的高度
     *
     * @return
     */
    public static int getBulletHeight(DirectionEnum dir) {
        switch (dir) {
            case UP:
                return ImageUtil.bulletU.getHeight();
            case LEFT:
                return ImageUtil.bulletL.getHeight();
            case DOWN:
                return ImageUtil.bulletD.getHeight();
            case RIGHT:
                return ImageUtil.bulletR.getHeight();
            default:
                return 0;
        }
    }

}
