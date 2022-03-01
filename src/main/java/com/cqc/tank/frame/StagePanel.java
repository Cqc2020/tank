package com.cqc.tank.frame;

import com.cqc.tank.config.ResourceMgr;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

/**
 * 关卡准备界面
 * @author Cqc
 * @date 2022/2/27
 */
@Data
public class StagePanel extends JPanel {
    private MainFrame mainFrame;
    private int stage;

    public StagePanel(MainFrame mainFrame, int stage) {
        this.mainFrame = mainFrame;
        this.stage = stage;
        // 创建一个线程，在paint方法完后，进入游戏界面
        new Thread(() -> {
            try {
                Thread.sleep(500);
                enterGamePanel(stage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "EnterGamePanelThread").start();
    }

    /**
     * 进入游戏界面
     */
    private void enterGamePanel(int stage) {
        System.gc();
        mainFrame.setPanel(new GamePanel(mainFrame));
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.stagePrepare, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        g.setFont(new Font("黑体", Font.BOLD, 100));
        g.drawString("STAGE 1", getWidth() / 2 - 220, getHeight() / 2 - 20);
    }
}
