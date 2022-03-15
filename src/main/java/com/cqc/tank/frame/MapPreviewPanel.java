package com.cqc.tank.frame;

import com.cqc.tank.model.GameObject;
import com.cqc.tank.model.Stage;
import com.cqc.tank.util.MapIOUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 关卡地图预览界面
 *
 * @author Cqc
 * @date 2022/2/27
 */
public class MapPreviewPanel extends JPanel {
    private int stage;
    private MainFrame mainFrame;
    private List<GameObject> mapObjList;

    public MapPreviewPanel(MainFrame mainFrame, int stage) {
        this.mainFrame = mainFrame;
        this.stage = stage;
        addButton();
        initMap();
    }

    private void initMap() {
        // 读取地图文件
        mapObjList = MapIOUtil.readMap(stage);
    }

    private void addButton() {
        JButton backButton = new JButton("返回");
        backButton.addActionListener((arg) -> {
            // 返回后会失去聚焦，按键失效
            mainFrame.requestFocus();
            // 返回登陆界面
            mainFrame.setPanel(new LoginPanel(mainFrame));
        });

        JButton lastStage = new JButton("上一关");
        lastStage.addActionListener((arg) -> {
            mainFrame.setPanel(new MapPreviewPanel(mainFrame, Stage.getInstance().getLastStage()));
        });

        JButton nextStage = new JButton("下一关");
        nextStage.addActionListener((arg) -> {
            mainFrame.setPanel(new MapPreviewPanel(mainFrame, Stage.getInstance().getNextStage()));
        });

        add(backButton);
        add(lastStage);
        add(nextStage);
    }

    @Override
    public void paint(Graphics g) {
        super.setBackground(Color.BLACK);
        super.paint(g);
        // 画出地图元素
        if (mapObjList != null && mapObjList.size() > 0) {
            for (int i = 0; i < mapObjList.size(); i++) {
                mapObjList.get(i).paint(g);
            }
        }
    }
}
