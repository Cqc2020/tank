package com.cqc.tank.frame;

import com.cqc.tank.entity.enums.MapObjEnum;
import com.cqc.tank.model.*;
import com.cqc.tank.util.MapIOUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 地图编辑器面板
 *
 * @author Cqc
 * @date 2022/2/26
 */
public class MapEditPanel extends JPanel {
    private int stage;
    private int sidebarHeight;
    private int sidebarWidth;
    private MainFrame mainFrame;
    private MapObjEnum mapObjEnum;
    private GameObject selectedMapObj;
    private MapEditMouseAdapter mapEditMouseAdapter;
    private MapEditMouseMotionAdapter mapEditMouseMotionAdapter;
    private List<GameObject> mapObjList = new ArrayList<>();
    private List<GameObject> sidebarObjList = new ArrayList<>();

    public MapEditPanel(MainFrame mainFrame, int stage) {
        this.mainFrame = mainFrame;
        this.stage = stage;
        // 侧边栏宽度
        sidebarWidth = 60;
        // 侧边栏高度
        sidebarHeight = 60;
        // 初始化侧边栏地图元素
        setSideBarObjList();
        // 添加鼠标监听事件
        addMouseListener();
        // 保存和返回按钮
        addJButton();
        // 初始化地图元素
        initMapObjList();
    }

    private void initMapObjList() {
        mapObjEnum = MapObjEnum.WALLS;
        // 读取地图文件
        mapObjList = MapIOUtil.readMap(stage);
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
            if (MapIOUtil.writeMap(mapObjList, stage)) {
                JOptionPane.showMessageDialog(null, "保存成功");
            } else {
                JOptionPane.showMessageDialog(null, "保存失败");
            }
        });

        JButton backButton = new JButton("返回");
        backButton.addActionListener((arg) -> {
            // 返回后会失去聚焦，按键失效
            mainFrame.requestFocus();
            // 清除此界面的鼠标事件监听
            mainFrame.removeMouseListener(mapEditMouseAdapter);
            mainFrame.removeMouseMotionListener(mapEditMouseMotionAdapter);
            // 返回登陆界面
            mainFrame.setPanel(new LoginPanel(mainFrame));
        });

        JButton lastStage = new JButton("上一关");
        lastStage.addActionListener((arg) -> {
            mainFrame.setPanel(new MapEditPanel(mainFrame, Stage.getInstance().getLastStage()));
        });

        JButton nextStage = new JButton("下一关");
        nextStage.addActionListener((arg) -> {
            mainFrame.setPanel(new MapEditPanel(mainFrame, Stage.getInstance().getNextStage()));
        });

        JButton clearButton = new JButton("全部清除");
        clearButton.addActionListener((arg) -> {
            mapObjList.clear();
            selectedMapObj = null;
        });

        add(saveButton);
        add(backButton);
        add(lastStage);
        add(nextStage);
        add(clearButton);
    }

    /**
     * 鼠标点击事件适配器
     */
    class MapEditMouseAdapter extends MouseAdapter {
        private static final int DOUBLE_CLICK = 2;
        private static final int SINGLE_CLICK = 1;
        private final Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        /**
         * 鼠标选中侧边栏的地图元素，用于绘制地图
         *
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            Point pp = amendPoint(e.getPoint());
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
            Point rp = amendPoint(e.getPoint());
            drawMap(rp);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Point cp = amendPoint(e.getPoint());
            if (e.getButton() == MouseEvent.BUTTON3) {
                Point ap = formatPoint(cp);
                GameObject mapObj = getMapObj(ap);
                removeMapObj(mapObj);
            }
        }
    }

    /**
     * 鼠标移动事件适配器
     */
    class MapEditMouseMotionAdapter extends MouseMotionAdapter {
        /**
         * 鼠标拖拽，mouseDragged会不断检测坐标变化
         *
         * @param e
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            Point dp = amendPoint(e.getPoint());
            drawMap(dp);
        }
    }

    private void drawMap(Point p) {
        if (isPickSideBarObj(p)) {
            return;
        }
        // 创建一个点，不改变p点的坐标
        Point ap = formatPoint(p);
        // 鼠标拖拽起始点是否选中地图元素，为空则没选中
        GameObject pickObj = getMapObj(ap);
        if (Objects.nonNull(pickObj)) {
            mapObjList.remove(pickObj);
        }
        selectedMapObj = addMapObj(ap);
        mapObjList.add(selectedMapObj);
    }

    /**
     * 修正坐标panel的y坐标起始点是23，因此修正
     * @param p
     * @return
     */
    private Point amendPoint(Point p) {
        // 创建一个点，不改变原来点的坐标
        return new Point(p.x, p.y - 23);
    }

    /**
     * 格式化坐标
     * @param p
     * @return
     */
    private Point formatPoint(Point p) {
        // 创建一个点，不改变原来点的坐标
        Point fp = new Point(p.x, p.y);
        fp.setLocation(fp.x - fp.x % 60, fp.y - fp.y % 60);
        return fp;
    }

    /**
     * 获取鼠标点击的地图元素
     *
     * @param p
     * @return
     */
    private GameObject getMapObj(Point p) {
        GameObject obj = null;
        if (mapObjList != null && mapObjList.size() > 0) {
            for (GameObject gameObject : mapObjList) {
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
        g.drawString("当前关卡：" + Stage.getInstance().getCurStage(), 0, 12);
        g.drawString("关卡总数：" + Stage.getInstance().getStageCount(), 0, 30);
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
    }

    private void setSideBarObjList() {
        int lastHeight = 0;
        for (int i = 0; i < MapObjEnum.values().length; i++) {
            MapObjEnum mapObjEnumByCode = MapObjEnum.getMapObjEnumByCode(i);
            if (i > 0) {
                lastHeight += sidebarHeight;
            }
            GameObject mapObj;
            switch (mapObjEnumByCode) {
                case WALL:
                    mapObj = new Wall(mainFrame.getWidth() - sidebarWidth, lastHeight, MapObjEnum.WALL);
                    sidebarObjList.add(mapObj);
                    break;
                case WALLS:
                    mapObj = new Wall(mainFrame.getWidth() - sidebarWidth, lastHeight, MapObjEnum.WALLS);
                    sidebarObjList.add(mapObj);
                    break;
                case STEEL:
                    mapObj = new Wall(mainFrame.getWidth() - sidebarWidth, lastHeight, MapObjEnum.STEEL);
                    sidebarObjList.add(mapObj);
                    break;
                case STEELS:
                    mapObj = new Wall(mainFrame.getWidth() - sidebarWidth, lastHeight, MapObjEnum.STEELS);
                    sidebarObjList.add(mapObj);
                    break;
                case GRASS:
                    mapObj = new Grass(mainFrame.getWidth() - sidebarWidth, lastHeight, MapObjEnum.GRASS);
                    sidebarObjList.add(mapObj);
                    break;
                case WATER:
                    mapObj = new Water(mainFrame.getWidth() - sidebarWidth, lastHeight, MapObjEnum.WATER);
                    sidebarObjList.add(mapObj);
                    break;
                case EAGLE:
                    mapObj = new Eagle(mainFrame.getWidth() - sidebarWidth, lastHeight, MapObjEnum.EAGLE);
                    sidebarObjList.add(mapObj);
                    break;
                default:
            }
        }
    }

    private GameObject addMapObj(Point point) {
        GameObject mapObj = null;
        int x = point.x;
        int y = point.y;
        switch (mapObjEnum) {
            case WALL:
                mapObj = new Wall(x, y, MapObjEnum.WALL);
                break;
            case WALLS:
                mapObj = new Wall(x, y, MapObjEnum.WALLS);
                break;
            case STEEL:
                mapObj = new Wall(x, y, MapObjEnum.STEEL);
                break;
            case STEELS:
                mapObj = new Wall(x, y, MapObjEnum.STEELS);
                break;
            case GRASS:
                mapObj = new Grass(x, y, MapObjEnum.GRASS);
                break;
            case WATER:
                mapObj = new Water(x, y, MapObjEnum.WATER);
                break;
            case EAGLE:
                mapObj = new Eagle(x, y, MapObjEnum.EAGLE);
                break;
            default:
        }
        removeMapObj(mapObj);
        return mapObj;
    }

    private void removeMapObj(GameObject mapObj) {
        mapObjList.removeIf(gameObject -> gameObject.equals(mapObj));
    }

    /**
     * 判定鼠标点中某个物体区域
     *
     * @param p
     * @param obj
     * @return
     */
    private boolean checkClickPoint(Point p, GameObject obj) {
        return p.equals(new Point(obj.x, obj.y));
    }

    /**
     * 是否选中侧边栏的地图元素
     *
     * @param p
     * @return
     */
    private boolean isPickSideBarObj(Point p) {
        for (GameObject sidebarObj : sidebarObjList) {
            if (checkClickPoint(formatPoint(p), sidebarObj)) {
                mapObjEnum = sidebarObj.getMapObjEnum();
                return true;
            }
        }
        return false;
    }

}