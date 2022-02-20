package com.cqc.tank;

import com.cqc.tank.config.TankWarConfiguration;

import java.io.IOException;

/**
 * @author Cqc on 2022/2/12 12:08 上午
 */
public class Test {
    public static void main(String[] args) throws IOException {
        char c = 'D';
        byte b = (byte) c;
        // System.out.println(c + " ==> " + b);
        // System.out.println(0x25);
        // for (DirectionEnum value : DirectionEnum.values()) {
        //     System.out.println(value);
        // }
        // System.out.println(ImageIO.read(ResourceMgr.class.getClassLoader().getResourceAsStream("image/blast1.gif")));
        // Audio.playBgm();
        System.out.println(TankWarConfiguration.getInstance().get("initialTankCount"));
    }
}
