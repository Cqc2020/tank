package com.cqc.tank.frame;

import com.cqc.tank.entity.enums.GamePatternEnum;
import com.cqc.tank.model.Stage;
import com.cqc.tank.util.ImageUtil;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Cqc
 * @date 2022/2/26
 */
@Data
public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private GamePatternEnum gamePattern;
    private int selectTankY = 300;
    private int onePlayerX = 300, onePlayerY = 300;
    private int twoPlayerX = 300, twoPlayerY = 350;
    private int previewMapX = 300, previewMapY = 400;
    private int customMapX = 300, customMapY = 450;
    private LoginPanelKeyAdapter loginKeyAdapter;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        addKeyListener();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ImageUtil.background, 0, 0, getWidth(), getHeight(), this);
        g.setFont(new Font("黑体", Font.BOLD, 40));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("1 单人游戏模式", onePlayerX, onePlayerY);
        g.drawString("2 双人游戏模式", twoPlayerX, twoPlayerY);
        g.drawString("3 预览游戏关卡地图", previewMapX, previewMapY);
        g.drawString("4 自定义游戏关卡地图", customMapX, customMapY);
        g.drawImage(ImageUtil.selectTank, 240, selectTankY - 35, null);
    }

    /**
     * 添加键盘监听事件(选择游戏模式)
     */
    private void addKeyListener() {
        loginKeyAdapter = new LoginPanelKeyAdapter();
        mainFrame.addKeyListener(loginKeyAdapter);
    }

    /**
     * 键盘监听适配器
     */
    class LoginPanelKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    moveCursorUP();
                    break;
                case KeyEvent.VK_DOWN:
                    moveCursorDOWN();
                    break;
                case KeyEvent.VK_ENTER:
                    selectPattern();
                    break;
                default:
                    break;
            }
        }

        /**
         * 鼠标上移
         */
        private void moveCursorUP() {
            if (selectTankY == onePlayerY) {
                selectTankY = customMapY;
            } else if (selectTankY == twoPlayerY) {
                selectTankY = onePlayerY;
            } else if (selectTankY == previewMapY) {
                selectTankY = twoPlayerY;
            } else {
                selectTankY = previewMapY;
            }
        }

        /**
         * 鼠标下移
         */
        private void moveCursorDOWN() {
            if (selectTankY == onePlayerY) {
                selectTankY = twoPlayerY;
            } else if (selectTankY == twoPlayerY) {
                selectTankY = previewMapY;
            } else if (selectTankY == previewMapY) {
                selectTankY = customMapY;
            } else {
                selectTankY = onePlayerY;
            }
        }

        /**
         * 选择游戏模式
         */
        private void selectPattern() {
            if (selectTankY == onePlayerY) {
                gamePattern = GamePatternEnum.ONE_PLAYER;
                enterPanel(new StagePanel(mainFrame, Stage.getInstance().getFirstStage()));
            } else if (selectTankY == twoPlayerY) {
                gamePattern = GamePatternEnum.TWO_PLAYER;
                enterPanel(new StagePanel(mainFrame, Stage.getInstance().getFirstStage()));
            } else if (selectTankY == previewMapY) {
                gamePattern = GamePatternEnum.PREVIEW_MAP;
                enterPanel(new MapPreviewPanel(mainFrame, Stage.getInstance().getFirstStage()));
            } else {
                gamePattern = GamePatternEnum.CUSTOM_MAP;
                enterPanel(new MapEditPanel(mainFrame, Stage.getInstance().getCurStage()));
            }
        }

        /**
         * 跳到另一个游戏界面
         * @param panel
         */
        private void enterPanel(JPanel panel) {
            mainFrame.removeKeyListener(loginKeyAdapter);
            mainFrame.setPanel(panel);
        }
    }

}
