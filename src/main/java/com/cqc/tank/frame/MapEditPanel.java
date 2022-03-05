package com.cqc.tank.frame;

import com.cqc.tank.util.ImageUtil;
import com.cqc.tank.entity.enums.MapObjEnum;
import com.cqc.tank.model.GameObject;
import com.cqc.tank.model.Grass;
import com.cqc.tank.model.Wall;
import com.cqc.tank.model.Water;
import com.cqc.tank.util.MapIOUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图编辑器面板
 *
 * @author Cqc
 * @date 2022/2/26
 */
public class MapEditPanel extends JPanel {
    private int stage;
    private int stageCount;
    private MainFrame mainFrame;
    private MapObjEnum mapObjEnum = MapObjEnum.WALLS;
    private Rectangle outLineRect;
    private GameObject selectedMapObj;
    private MapEditMouseAdapter mapEditMouseAdapter;
    private MapEditMouseMotionAdapter mapEditMouseMotionAdapter;
    private final List<GameObject> mapObjList = new ArrayList<>();
    private final List<GameObject> sidebarObjList = new ArrayList<>();

    public MapEditPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        // 初始化侧边栏地图元素
        setSideBarObjList();
        // 添加鼠标监听事件
        addMouseListener();
        // 保存和返回按钮
        addJButton();
    }

    private void addMouseListener() {
        mapEditMouseAdapter = new MapEditMouseAdapter();
        mapEditMouseMotionAdapter = new MapEditMouseMotionAdapter();
        mainFrame.addMouseListener(mapEditMouseAdapter);
        mainFrame.addMouseMotionListener(mapEditMouseMotionAdapter);
    }

    private void addJButton() {
        JButton saveButton = new JButton("保存");
        saveButton.addActionListener((arg) -> {
            System.out.println(arg);
            if (MapIOUtil.writeMap(mapObjList, stage)) {
                JOptionPane.showMessageDialog(null, "保存成功");
            } else {
                JOptionPane.showMessageDialog(null, "保存失败");
            }
        });

        JButton backButton = new JButton("返回");
        backButton.addActionListener((arg) -> {
            System.out.println(arg);
            // 返回后会失去聚焦，按键失效
            mainFrame.requestFocus();
            // 清除此界面的鼠标事件监听
            mainFrame.removeMouseListener(mapEditMouseAdapter);
            mainFrame.removeMouseMotionListener(mapEditMouseMotionAdapter);
            // 返回登陆界面
            mainFrame.setPanel(new LoginPanel(mainFrame));
        });
        add(saveButton);
        add(backButton);
    }


    /**
     * 鼠标按压点坐标
     */
    Point point;
    /**
     * 鼠标点击事件适配器
     */
    class MapEditMouseAdapter extends MouseAdapter {
        private static final int DOUBLE_CLICK = 2;
        private static final int SINGLE_CLICK = 1;
        private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        /**
         * 鼠标选中侧边栏的地图元素，用于绘制地图
         *
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            Point pp = e.getPoint();
            point = new Point(pp.x, pp.y);
            // 鼠标点中某个物体区域，则用该物体在地图中描绘
            if (isPickSideBarObj(pp)) {
                setCursor(cursor);
            }
        }

        /**
         * 鼠标释放：鼠标离开的点就往该点所在区域添加地图元素
         *
         * @param e
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            Point rp = e.getPoint();
            if (!isPickSideBarObj(rp)) {
                if (isObjIntersect(selectedMapObj)) {
                    // outLineRect = selectedMapObj.objRect;
                } else {
                    // 碰撞检测
                    // if (!collideDetect(selectedMapObj)) {
                    // }
                    // 鼠标释放：在释放处画出选中的地图元素
                    mapObjList.add(selectedMapObj);
                }
            }
        }

        /**
         * 鼠标点击事件
         *
         * @param e
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == SINGLE_CLICK) {
                // selectedMapObj = getMapObj(cp);
            } else if (e.getClickCount() == DOUBLE_CLICK) {
                // 鼠标左键双击：移除选中地图元素
                removeMapObj(point);
            }
        }

    }

    /**
     * 鼠标移动事件适配器
     */
    class MapEditMouseMotionAdapter extends MouseMotionAdapter {
        /**
         * 鼠标拖拽：鼠标会拖拽选中的侧边栏地图元素
         *
         * @param e
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectedMapObj == null || selectedMapObj.moveFlag) {
                Point dp = e.getPoint();
                // 获取鼠标所在的地图元素
                GameObject mapObj = getMapObj(point);
                if (mapObj != null) {
                    mapObj.x = dp.x;
                    mapObj.y = dp.y;
                    selectedMapObj = mapObj;
                    mapObjEnum = mapObj.mapObjEnum;
                    mapObjList.remove(mapObj);
                }
                // 给鼠标选中的地图元素赋值
                selectedMapObj = setMapObj(dp.x, dp.y);
            }
        }
    }

    /**
     * 是否选中侧边栏的地图元素
     *
     * @param p
     * @return
     */
    private boolean isPickSideBarObj(Point p) {
        // 鼠标点击panel的y坐标会多23，减去后，点击更加精准了
        p.y -= 23;
        for (GameObject gameObject : sidebarObjList) {
            if (checkClickPoint(p, gameObject)) {
                mapObjEnum = gameObject.getMapObjEnum();
                return true;
            }
        }
        return false;
    }

    /**
     * 判断选中元素是否与地图上已有元素相交
     * ______________
     * |		   __|__________
     * |	      |	 |          |
     * |__________|__|          |
     *            |_____________|
     *
     * @param selectedObj
     * @return
     */
    private boolean isObjIntersect(GameObject selectedObj) {
        if (mapObjList != null && mapObjList.size() > 0) {
            for (GameObject gameObject : mapObjList) {
                if (gameObject.objRect.intersects(selectedObj.objRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    private GameObject getMapObj(Point p) {
        GameObject obj = null;
        // 鼠标点击panel的y坐标会多23，减去后，点击更加精准了
        p.y -= 23;
        if (mapObjList != null && mapObjList.size() > 0) {
            for (int i = 0; i < mapObjList.size(); i++) {
                GameObject gameObject = mapObjList.get(i);
                if (checkClickPoint(p, gameObject)) {
                    return gameObject;
                }
            }
        }
        return obj;
    }

    @Override
    public void paint(Graphics g) {
        super.setBackground(Color.BLACK);
        super.paint(g);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("黑体", Font.BOLD, 12));
        g.drawString("当前关卡：" + stage, 0, 12);
        g.drawString("关卡总数：" + stageCount, 0, 30);
        g.drawString("当前元素类型：" + mapObjEnum.getDesc(), 0, 48);
        g.drawString("地图元素总数：" + mapObjList.size(), 0, 66);
        g.setColor(Color.LIGHT_GRAY);
        // 画出网格参考线
        for (int i = 0; i < getHeight(); i += 60) {
            g.drawLine(0, i, getWidth(), i);
            g.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getWidth(); i += 60) {
            g.drawLine(i, 0, i, getHeight());
        }
        // 画出鼠标选中地图元素
        if (selectedMapObj != null) {
            selectedMapObj.paint(g);
        }
        // 自定义地图右侧边栏显示样例元素 sidebar
        if (sidebarObjList != null && sidebarObjList.size() > 0) {
            for (int i = 0; i < sidebarObjList.size(); i++) {
                sidebarObjList.get(i).paint(g);
            }
        }
        // 画出地图元素
        if (mapObjList != null && mapObjList.size() > 0) {
            for (int i = 0; i < mapObjList.size(); i++) {
                mapObjList.get(i).paint(g);
            }
        }
        // 描绘出地图元素的轮廓，以作提示
        if (outLineRect != null) {
            g.setColor(Color.RED);
            g.setFont(new Font("黑体", Font.BOLD, 28));
            g.drawRect(outLineRect.x, outLineRect.y, outLineRect.width, outLineRect.height);
        }
    }

    private void setSideBarObjList() {
        int lastHeight = 0;
        for (int i = 0; i < MapObjEnum.values().length; i++) {
            MapObjEnum mapObjEnumByCode = MapObjEnum.getMapObjEnumByCode(i);
            if (i > 0) {
                lastHeight += sidebarObjList.get(i - 1).objRect.height;
            }
            GameObject mapObj;
            switch (mapObjEnumByCode) {
                case WALL:
                    mapObj = new Wall(mainFrame.getWidth() - ImageUtil.wall.getWidth(), lastHeight, MapObjEnum.WALL);
                    sidebarObjList.add(mapObj);
                    break;
                case WALLS:
                    mapObj = new Wall(mainFrame.getWidth() - ImageUtil.walls.getWidth(), lastHeight, MapObjEnum.WALLS);
                    sidebarObjList.add(mapObj);
                    break;
                case STEEL:
                    mapObj = new Wall(mainFrame.getWidth() - ImageUtil.steel.getWidth(), lastHeight, MapObjEnum.STEEL);
                    sidebarObjList.add(mapObj);
                    break;
                case STEELS:
                    mapObj = new Wall(mainFrame.getWidth() - ImageUtil.steels.getWidth(), lastHeight, MapObjEnum.STEELS);
                    sidebarObjList.add(mapObj);
                    break;
                case GRASS:
                    mapObj = new Grass(mainFrame.getWidth() - ImageUtil.grass.getWidth(), lastHeight, MapObjEnum.GRASS);
                    sidebarObjList.add(mapObj);
                    break;
                case WATER:
                    mapObj = new Water(mainFrame.getWidth() - ImageUtil.water.getWidth(), lastHeight, MapObjEnum.WATER);
                    sidebarObjList.add(mapObj);
                    break;
                default:
            }
        }
    }

    private GameObject setMapObj(int x, int y) {
        GameObject mapObj = null;
        switch (mapObjEnum) {
            case WALL:
                mapObj = new Wall(x - ImageUtil.wall.getWidth() / 2, y - ImageUtil.wall.getHeight() / 2, MapObjEnum.WALL);
                break;
            case WALLS:
                mapObj = new Wall(x - ImageUtil.walls.getWidth() / 2, y - ImageUtil.walls.getHeight() / 2, MapObjEnum.WALLS);
                break;
            case STEEL:
                mapObj = new Wall(x - ImageUtil.steel.getWidth() / 2, y - ImageUtil.steel.getHeight() / 2, MapObjEnum.STEEL);
                break;
            case STEELS:
                mapObj = new Wall(x - ImageUtil.steels.getWidth() / 2, y - ImageUtil.steels.getHeight() / 2, MapObjEnum.STEELS);
                break;
            case GRASS:
                mapObj = new Grass(x - ImageUtil.grass.getWidth() / 2, y - ImageUtil.grass.getHeight() / 2, MapObjEnum.GRASS);
                break;
            case WATER:
                mapObj = new Water(x - ImageUtil.water.getWidth() / 2, y - ImageUtil.water.getHeight() / 2, MapObjEnum.WATER);
                break;
            default:
        }
        return mapObj;
    }

    private void addMapObj(List<GameObject> list, MapObjEnum mapObjEnum, int x, int y) {
        GameObject mapObj;
        switch (mapObjEnum) {
            case WALL:
                mapObj = new Wall(x, y, MapObjEnum.WALL);
                if (!collideDetect(mapObj)) {
                    list.add(mapObj);
                }
                break;
            case WALLS:
                mapObj = new Wall(x, y, MapObjEnum.WALLS);
                if (!collideDetect(mapObj)) {
                    list.add(mapObj);
                }
                break;
            case STEEL:
                mapObj = new Wall(x, y, MapObjEnum.STEEL);
                if (!collideDetect(mapObj)) {
                    list.add(mapObj);
                }
                break;
            case STEELS:
                mapObj = new Wall(x, y, MapObjEnum.STEELS);
                if (!collideDetect(mapObj)) {
                    list.add(mapObj);
                }
                break;
            case GRASS:
                mapObj = new Grass(x, y, MapObjEnum.GRASS);
                if (!collideDetect(mapObj)) {
                    list.add(mapObj);
                }
                break;
            case WATER:
                mapObj = new Water(x, y, MapObjEnum.WATER);
                if (!collideDetect(mapObj)) {
                    list.add(mapObj);
                }
                break;
            default:
        }
    }

    private void removeMapObj(Point p) {
        mapObjList.removeIf(mapObj -> checkClickPoint(p, mapObj));
    }

    /**
     * 判定鼠标点中某个物体区域
     *
     * @param p
     * @param obj
     * @return
     */
    private boolean checkClickPoint(Point p, GameObject obj) {
        return p.x >= obj.objRect.getMinX()
                && p.x <= obj.objRect.getMaxX()
                && p.y >= obj.objRect.getMinY()
                && p.y <= obj.objRect.getMaxY();
    }

    private boolean collideDetect(GameObject curMapObj) {
        for (GameObject mapObj : mapObjList) {
            if (!mapObj.equals(curMapObj)) {
                if (mapObj.objRect.intersects(curMapObj.objRect)) {
                    mapObj.moveFlag = false;
                    System.out.println("要撞上啦");
                    return true;
                }
            }
        }
        return false;
    }

}