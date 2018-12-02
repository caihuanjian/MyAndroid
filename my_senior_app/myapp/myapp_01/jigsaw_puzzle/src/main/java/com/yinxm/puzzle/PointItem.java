package com.yinxm.puzzle;

/**
 * Created by yinxuming on 2018/5/6.
 * 子方块位置
 */

public class PointItem {
    public int x;// col
    public int y; //row


    public PointItem(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 根据一维索引建立二维坐标
     * @param index
     * @param row
     * @param col 每行多少个，（多少列）
     */
    public PointItem(int index, int row, int col) {
        this.x = index % col;
        this.y = index / col;
    }

    @Override
    public String toString() {
        return "PointItem{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public  enum Direct {
        UP(0),
        DOWN(1),
        RIGHT(2),
        LEFT(3);

        private int index;

        Direct(int i) {
            this.index = i;
        }

        public static Direct getIndex(int i) {
            if (i >= UP.index && i<= LEFT.index) {
                return values()[i];
            }
            return null;
        }
    }
}
