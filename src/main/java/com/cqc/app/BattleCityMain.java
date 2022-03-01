package com.cqc.app;

import com.cqc.tank.frame.MainFrame;

/**
 * @author Cqc on 2022/2/11 11:00 下午
 */
public class BattleCityMain {
    public static void main(String[] args) throws InterruptedException {
        MainFrame mainFrame = new MainFrame();
        while (true) {
            Thread.sleep(10);
            mainFrame.repaint();
        }
    }
}
