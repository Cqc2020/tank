package com.cqc.tank.entity.enums;

/**
 * @author Cqc
 * @date 2022/2/26
 */
public enum MapObjEnum {
    /**
     * 小红墙体图片
     */
    WALL(0, "WALL"),

    /**
     * 大红墙体图片
     */
    WALLS(1, "WALLS"),

    /**
     * 小加强白墙体图片
     */
    STEEL(2, "STEEL"),

    /**
     * 大加强白墙体图片
     */
    STEELS(3, "STEELS"),

    /**
     * 草地图片
     */
    GRASS(4, "GRASS"),

    /**
     * 水图片
     */
    WATER(5, "WATER"),

    /**
     * 基地图片
     */
    EAGLE(6, "EAGLE");

    private final Integer code;
    private final String desc;

    MapObjEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MapObjEnum getMapObjEnumByCode(int code) {
        MapObjEnum mapObjEnum = null;
        if (MapObjEnum.WALL.getCode().equals(code)) {
            mapObjEnum = MapObjEnum.WALL;
        } else if (MapObjEnum.WALLS.getCode().equals(code)) {
            mapObjEnum = MapObjEnum.WALLS;
        } else if (MapObjEnum.STEEL.getCode().equals(code)) {
            mapObjEnum = MapObjEnum.STEEL;
        } else if (MapObjEnum.STEELS.getCode().equals(code)) {
            mapObjEnum = MapObjEnum.STEELS;
        } else if (MapObjEnum.GRASS.getCode().equals(code)) {
            mapObjEnum = MapObjEnum.GRASS;
        } else if (MapObjEnum.WATER.getCode().equals(code)) {
            mapObjEnum = MapObjEnum.WATER;
        } else if (MapObjEnum.EAGLE.getCode().equals(code)) {
            mapObjEnum = MapObjEnum.EAGLE;
        }
        return mapObjEnum;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
