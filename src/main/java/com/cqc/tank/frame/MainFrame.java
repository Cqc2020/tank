package com.cqc.tank.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 游戏主框架窗口面板
 *  LoginPanel -> StagePanel -> GamePanel
 *  LoginPanel -> MapPreviewPanel
 *  LoginPanel -> MapEditPanel
 * @author Cqc
 * @date 2022/2/27
 */
public class MainFrame extends JFrame {
    /**
     * 游戏窗体宽度
     */
    public static final int GAME_WIDTH = 960;
    /**
     * 游戏窗体高度
     */
    public static final int GAME_HEIGHT = 660;

    public MainFrame() throws HeadlessException {
        setTitle("坦克大战");
        setVisible(true);
        setResizable(false);
        setBackground(Color.BLACK);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        // 获取当前显示器尺寸
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        // 设置窗口坐标，是的窗口总是在屏幕中间打开
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
        // 添加窗口监听事件
        addWindowListener();
        // 设置游戏启动面板
        setPanel(new LoginPanel(this));
    }

    /**
     * 添加窗口监听事件
     */
    private void addWindowListener() {
        // 关闭游戏窗口
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e){
                System.exit(0);
            }
        });
    }

    /**
     * 设置主容器中的面板
     */
    public void setPanel(JPanel panel) {
        // 获取主容器
        Container c = getContentPane();
        // 移除所有面板
        c.removeAll();
        // 重新添加一个面板
        c.add(panel);
        // 验证面板
        c.validate();
    }

    /**
     * 窗口边界检测
     */
    public static Point boundsCheck(Point p, int width, int height) {
        if (p.x <= 0) {
            p.x = 0;
        }
        if (p.x > MainFrame.GAME_WIDTH - width) {
            p.x = MainFrame.GAME_WIDTH - width;
        }
        if (p.y <= 0) {
            p.y = 0;
        }
        if (p.y >= MainFrame.GAME_HEIGHT - height) {
            p.y = MainFrame.GAME_HEIGHT - height;
        }
        return p;
    }

}
