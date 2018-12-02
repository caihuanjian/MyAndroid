package com.yinxm.puzzle;

import java.util.Random;

/**
 * Created by yinxuming on 2018/5/6.
 */

public class PuzzleHelper {
    private int[] array = null;
    //默认空白点位置
    private int blankIndex = 0;

    private int row = 0;
    private int col = 0;

    public int getBlankIndex() {
        return blankIndex;
    }


    private void createIntegerArray(int row, int col) {
        int total = row * col;
        array = new int[total];
        for (int i = 0; i < total; i++) {
            array[i] = i;
        }
        blankIndex = total - 1;
    }

    public int[] createRandomIndex(int row, int col, int randomTimes) {
        this.row = row;
        this.col = col;
        createIntegerArray(row, col);

        PointItem pointStart = new PointItem(blankIndex % col, blankIndex / col);//从默认空白位置开始交换

        int i = 0;
        while (i < randomTimes) {
            PointItem newPoint = getNextPointItem(pointStart);
            i++;
        }

        return array;
    }

    public PointItem getNextPointItem(PointItem src) {

        // TODO: 2018/5/6 随机方向
        Random random = new Random();
        int index = random.nextInt(4);
        PointItem newPoint = move(src, PointItem.Direct.getIndex(index));
        if (newPoint != null) {//移动到了正确位置
            return newPoint;
        }
        return getNextPointItem(src);
    }

    public PointItem move(PointItem pointItem, PointItem.Direct direct) {
        if (pointItem == null || direct == null) {
            return null;
        }
        int newX = pointItem.x;
        int newY = pointItem.y;
        switch (direct) {
            case UP:
                newX -= 1;
                break;
            case DOWN:
                newX += 1;
                break;
            case LEFT:
                newY -= 1;
                break;
            case RIGHT:
                newY += 1;
                break;
            default:
                break;
        }

        if (newX < 0 || newX >= col
                || newY < 0 || newY >= row) {
            //数据不合法
            return null;
        }
        //交换位置
        int temp = array[col * newY + newX];
        array[col * newY + newX] = array[col * pointItem.y + pointItem.x];
        array[col * pointItem.y + pointItem.x] = temp;
        //重新开始坐标点
        pointItem.x = newX;
        pointItem.y = newY;
        return pointItem;
    }

    /**
     * 交换两个位置
     * 1、判断相邻性
     * 2、交换
     *
     * @return
     */
    public boolean swapPoint(PointItem basePoint, PointItem newPoint) {

        boolean flag = false;
        if (basePoint == null || newPoint == null) {
            return flag;
        }
        int indexBase = getIndex(basePoint, col);
        int indexNew = getIndex(newPoint, col);
        LogUtil.d("swap check indexBase=" + indexBase + ", basePoint=" + basePoint + ", indexNew=" + indexNew + ", newPoint=" + newPoint);

        if (!(indexBase >= 0 && indexBase < array.length
                && indexNew >= 0 && indexNew < array.length)) {
            return flag;
        }

        //判断相邻性
        if (basePoint.x == newPoint.x && Math.abs(basePoint.y - newPoint.y) == 1
                || basePoint.y == newPoint.y && Math.abs(basePoint.x - newPoint.x) == 1) {

            LogUtil.d("swap");
            //交换数据
            int temp = array[indexBase];
            array[indexBase] = array[indexNew];
            array[indexNew] = temp;
            flag = true;
        }
        return flag;
    }

    /**
     * 是否拼图成功
     * @return
     */
    public boolean isSuccess() {
        boolean flag = true;
        //array升序排列
        for (int i = 1; i < array.length; i++) {
            if (array[i-1] >= array[i]) {
                flag = false;
            }
        }

        return flag;
    }

    /**
     * @param pointItem
     * @param col
     * @return
     */
    public static int getIndex(PointItem pointItem, int col) {
        int index = -1;
        if (pointItem == null || col <= 0) {
            return index;
        }
        index = col * pointItem.y + pointItem.x;
        return index;
    }


}
