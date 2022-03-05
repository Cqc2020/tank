package com.cqc.tank.util;

import com.cqc.tank.entity.enums.MapObjEnum;
import com.cqc.tank.model.GameObject;
import com.cqc.tank.model.Grass;
import com.cqc.tank.model.Wall;
import com.cqc.tank.model.Water;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Cqc
 * @date 2022/3/4
 */
public class MapIOUtil {
    /**
     * 地图文件名前缀
     */
    private static final String MAP_PREFIX = "stage";
    /**
     * 地图文件名后缀
     */
    private static final String MAP_SUFFIX = ".map";
    /**
     * 地图保存路径
     */
    private static final String MAP_SAVE_PATH = "src/main/resources/map/";
    /**
     * 等于号
     */
    private static final String EQUAL = "=";
    /**
     * 逗号
     */
    private static final String COMMA = ",";
    /**
     * 分号
     */
    private static final String SEMICOLON = ";";

    /**
     * 保存地图
     *
     * @param mapObjList
     * @param stage
     * @return
     */
    public static boolean writeMap(List<GameObject> mapObjList, int stage) {
        StringBuilder wallBuff = new StringBuilder(MapObjEnum.WALL.getDesc()).append(EQUAL);
        StringBuilder wallsBuff = new StringBuilder(MapObjEnum.WALLS.getDesc()).append(EQUAL);
        StringBuilder steelBuff = new StringBuilder(MapObjEnum.STEEL.getDesc()).append(EQUAL);
        StringBuilder steelsBuff = new StringBuilder(MapObjEnum.STEELS.getDesc()).append(EQUAL);
        StringBuilder grassBuff = new StringBuilder(MapObjEnum.GRASS.getDesc()).append(EQUAL);
        StringBuilder waterBuff = new StringBuilder(MapObjEnum.WATER.getDesc()).append(EQUAL);
        // 提取地图元素坐标数据
        for (GameObject mapObj : mapObjList) {
            String mapData = mapObj.x + COMMA + mapObj.y + SEMICOLON;
            switch (mapObj.mapObjEnum) {
                case WALL:
                    wallBuff.append(mapData);
                    break;
                case WALLS:
                    wallsBuff.append(mapData);
                    break;
                case STEEL:
                    steelBuff.append(mapData);
                    break;
                case STEELS:
                    steelsBuff.append(mapData);
                    break;
                case GRASS:
                    grassBuff.append(mapData);
                    break;
                case WATER:
                    waterBuff.append(mapData);
                    break;
                default:
            }
        }

        // 将地图数据写入文件
        File file = new File(MAP_SAVE_PATH + MAP_PREFIX + stage + MAP_SUFFIX);
        try {
            // 创建空文件
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    return false;
                }
            }
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {
            if (!Objects.equals(wallBuff.toString(), MapObjEnum.WALL.getDesc() + EQUAL)) {
                bw.write(wallBuff.toString().toCharArray());
                bw.newLine();
            }
            if (!Objects.equals(wallsBuff.toString(), MapObjEnum.WALLS.getDesc() + EQUAL)) {
                bw.write(wallsBuff.toString().toCharArray());
                bw.newLine();
            }
            if (!Objects.equals(steelBuff.toString(), MapObjEnum.STEEL.getDesc() + EQUAL)) {
                bw.write(steelBuff.toString().toCharArray());
                bw.newLine();
            }
            if (!Objects.equals(steelsBuff.toString(), MapObjEnum.STEELS.getDesc() + EQUAL)) {
                bw.write(steelsBuff.toString().toCharArray());
                bw.newLine();
            }
            if (!Objects.equals(grassBuff.toString(), MapObjEnum.GRASS.getDesc() + EQUAL)) {
                bw.write(grassBuff.toString().toCharArray());
                bw.newLine();
            }
            if (!Objects.equals(waterBuff.toString(), MapObjEnum.WATER.getDesc() + EQUAL)) {
                bw.write(waterBuff.toString().toCharArray());
                bw.newLine();
            }
            bw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取地图
     */
    public static List<GameObject> readMap(int stage) {
        File file = new File(MAP_SAVE_PATH + MAP_PREFIX + stage + MAP_SUFFIX);
        Properties props = new Properties();
        try (FileReader fr = new FileReader(file)) {
            props.load(fr);
            return Objects.requireNonNull(readMapObj(props));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<GameObject> readMapObj(Properties props) {
        List<GameObject> mapObjList = new ArrayList<>();
        for (MapObjEnum mapObjEnum : MapObjEnum.values()) {
            // 取出每种类型的地图元素集合，转成数组
            String mapObjs = props.getProperty(mapObjEnum.getDesc());
            String[] mapObjArr = new String[0];
            if (Objects.nonNull(mapObjs)) {
                mapObjArr = mapObjs.split(SEMICOLON);
            }
            // 取出同种类型每个地图元素
            for (String mapObj : mapObjArr) {
                // 获取每个地图元素的坐标coordinate：x，y
                String[] coor = mapObj.split(COMMA);
                int x = Integer.parseInt(coor[0]);
                int y = Integer.parseInt(coor[1]);
                switch (mapObjEnum) {
                    case WALL:
                        mapObjList.add(new Wall(x, y, MapObjEnum.WALL));
                        break;
                    case WALLS:
                        mapObjList.add(new Wall(x, y, MapObjEnum.WALLS));
                        break;
                    case STEEL:
                        mapObjList.add(new Wall(x, y, MapObjEnum.STEEL));
                        break;
                    case STEELS:
                        mapObjList.add(new Wall(x, y, MapObjEnum.STEELS));
                        break;
                    case GRASS:
                        mapObjList.add(new Grass(x, y, MapObjEnum.GRASS));
                        break;
                    case WATER:
                        mapObjList.add(new Water(x, y, MapObjEnum.WATER));
                        break;
                    default:
                }
            }
        }
        return mapObjList;
    }
}
