package com.cqc.tank.entity.enums;

/**
 * @author Cqc on 2022/2/12 9:57 上午
 */
public enum DirectionEnum {
    /**
     * 向上
     */
    UP("UP", "上"),

    /**
     * 向下
     */
    DOWN("DOWN", "下"),

    /**
     * 向左
     */
    LEFT("LEFT", "左"),

    /**
     * 向右
     */
    RIGHT("RIGHT", "右");


    private final String code;
    private final String desc;

    DirectionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }
}
