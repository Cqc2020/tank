package com.cqc.tank.frame;

import com.cqc.tank.config.ResourceMgr;
import com.cqc.tank.entity.enums.MapObjEnum;
import com.cqc.tank.model.AbstractGameObject;
import com.cqc.tank.model.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图编辑器面板
 *
 * @author Cqc
 * @date 2022/2/26
 */
public class MapEditPanel extends JPanel {
    private MainFrame mainFrame;
    private MapEditMouseAdapter mouseAdapter;
    private MapObjEnum mapObjEnum = MapObjEnum.WALLS;
    private int stage;
    private int stageCount;
    private List<AbstractGameObject> mapObjList = new ArrayList<>();
    private List<BufferedImage> sidBarObjList = new ArrayList<>();

    {
        sidBarObjList.add(ResourceMgr.wall);
        sidBarObjList.add(ResourceMgr.walls);
        sidBarObjList.add(ResourceMgr.steel);
        sidBarObjList.add(ResourceMgr.steels);
        sidBarObjList.add(ResourceMgr.grass);
        sidBarObjList.add(ResourceMgr.water);
    }

    public MapEditPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        addMouseListener();
    }

    private void addMouseListener() {
        mouseAdapter = new MapEditMouseAdapter();
        mainFrame.addMouseListener(mouseAdapter);
    };

    class MapEditMouseAdapter extends MouseAdapter {
        private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        @Override
        public void mousePressed(MouseEvent e) {
            Point clickPoint = e.getPoint();
            // 鼠标点中某个物体区域，则用该物体在地图中描绘
            if (isPickSideBarObj(clickPoint)) {
                setCursor(cursor);
            }
            // repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point releasedPoint = e.getPoint();
            if (!isPickSideBarObj(releasedPoint)) {
                setMapObjList();
            }

        }

        /**
         * 是否选中侧边栏的地图元素
         * @param point
         * @return
         */
        private boolean isPickSideBarObj(Point point) {
            int objY = 0;
            for (int i = 0; i < sidBarObjList.size(); i++) {
                if (i > 0) {
                    objY += sidBarObjList.get(i - 1).getHeight();
                }
                if (checkClickPoint(point, sidBarObjList.get(i), objY)) {
                    mapObjEnum = MapObjEnum.WALLS;
                    return true;
                }
            }
            return false;
        }

        /**
         * 判定鼠标点中某个物体区域
         * @param point
         * @param obj
         * @param objY
         * @return
         */
        private boolean checkClickPoint(Point point, BufferedImage obj, int objY) {
            return point.x >= getWidth() - obj.getWidth()
                    && point.x <= getWidth()
                    && point.y >= objY
                    && point.y <= objY + obj.getHeight();
        }
    }


    @Override
    public void paint(Graphics g) {
        g.setColor(Color.ORANGE);
        g.setFont(new Font("黑体", Font.BOLD, 12));
        g.drawString("当前关卡" + stage, 0, 12);
        g.drawString("关卡总数" + stageCount, 0, 30);
        g.setColor(Color.LIGHT_GRAY);
        // 画出网格参考线
        for (int i = 0; i < getHeight(); i += 60) {
            g.drawLine(0, i, getWidth(), i);
            g.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getWidth(); i += 60) {
            g.drawLine(i, 0, i, getHeight());
        }
        // 自定义地图右侧边栏显示样例元素 sidebar
        drawSidebarImage(g);
        // 画个擦子
        g.setColor(Color.MAGENTA);
        g.drawRect(getWidth(), 100, 20, 19);
        drawMapObj(g);

    }

    private void drawMapObj(Graphics g) {
        for (int i = 0; i < mapObjList.size(); i++) {
            mapObjList.get(i).paint(g);
        }
    }

    /**
     * 画出侧边栏样例元素
     *
     * @param g
     */
    private void drawSidebarImage(Graphics g) {
        int lastHeight = 0;
        for (int i = 0; i < sidBarObjList.size(); i++) {
            BufferedImage obj = sidBarObjList.get(i);
            if (i > 0) {
                lastHeight += sidBarObjList.get(i - 1).getHeight();
            }
            if (i == 0) {
                g.drawImage(obj, getWidth() - obj.getWidth(), 0, null);
            } else {
                g.drawImage(obj, getWidth() - obj.getWidth(), lastHeight, null);
            }
        }
    }

    /**
     * 编辑地图
     */
    private void setMapObjList() {
        switch (mapObjEnum) {
            case WALL:
                mapObjList.add(new Wall(0, 200, MapObjEnum.WALL));
                break;
            case WALLS:
                mapObjList.add(new Wall(0, 200, MapObjEnum.WALLS));
                break;
            case STEEL:
                mapObjList.add(new Wall(0, 200, MapObjEnum.STEEL));
                break;
            case STEELS:
                mapObjList.add(new Wall(0, 200, MapObjEnum.STEELS));
                break;
            case GRASS:
                mapObjList.add(new Wall(0, 200, MapObjEnum.GRASS));
                break;
            case WATER:
                mapObjList.add(new Wall(0, 200, MapObjEnum.WATER));
                break;
            default:
        }
    }
}