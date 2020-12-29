package com.qty.pt;

import java.util.HashMap;

/**
 * 性能测试类
 */
public class QTPerfomanceTesting {

    /**
     * QTPerfomanceTesting 对象
     */
    private static final QTPerfomanceTesting INSTANCE = QTPerfomanceTestingInstance.sInstance;

    /**
     * 不带标记测试的开始时间，单位：纳秒
     */
    private long mStartTime;
    /**
     * 不带标记测试的计次时间，单位：纳秒
     */
    private long mCountDownTime;
    /**
     * 带标记测试的开始时间集合
     */
    private HashMap<String, Long> mStartTimes;
    /**
     * 带标记测试的计次时间集合
     */
    private HashMap<String, Long> mCountDownTimes;

    /**
     * 单例
     * @return 返回 QTPerfomanceTesting 对象
     */
    public static QTPerfomanceTesting getInstance() {
        return INSTANCE;
    }

    /**
     * 构造方法
     */
    private QTPerfomanceTesting() {
        mStartTime = -1;
        mCountDownTime = -1;
        mStartTimes = new HashMap<>();
        mCountDownTimes = new HashMap<>();
    }

    /**
     * 开始测试
     * @return 如果当前没有正在进行测试，返回 true，否则返回 false;
     */
    public boolean start() {
        if (mStartTime == -1) {
            mStartTime = System.nanoTime();
            mCountDownTime = -1;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 开始测试
     * @param tag   测试标记
     * @return  如果当前没有该标志的测试正在运行，返回 true，否则返回 false
     */
    public boolean start(String tag) {
        if (!isEmptyString(tag) && !hasPerfomanceTesting(tag)) {
            mStartTimes.put(tag, System.nanoTime());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计次，获取距离上次测试时间已过去的时间差，或者距离开始测试时间的时间差
     * @param fromStart 是否是计算从开始测试时到现在的时间差
     * @return  如果当前正在运行，则返回时间差，否则直接返回 -1，单位：微秒
     */
    public long countDown(boolean fromStart) {
        if (mStartTime != -1) {
            if (fromStart) {
                mCountDownTime = System.nanoTime();
                return mCountDownTime - mStartTime;
            } else {
                if (mCountDownTime == -1) {
                    mCountDownTime = System.nanoTime();
                    return mCountDownTime - mStartTime;
                } else {
                    long lastTime = mCountDownTime;
                    mCountDownTime = System.nanoTime();
                    return mCountDownTime - lastTime;
                }
            }
        } else {
            return -1;
        }
    }

    /**
     * 计次，获取距离上次测试时间已过去的时间差，或者距离开始测试时间的时间差
     * @param tag   测试标记
     * @param fromStart 是否是计算从开始测试时到现在的时间差
     * @return 如果 tag 对应的测试正在运行，则返回时间差，否则直接返回 -1，单位：微秒
     */
    public long countDown(String tag, boolean fromStart) {
        if (!isEmptyString(tag) && hasPerfomanceTesting(tag)) {
            if (fromStart) {
                long currentTime = System.nanoTime();
                mCountDownTimes.put(tag, currentTime);
                return currentTime - mStartTimes.get(tag);
            } else {
                if (mCountDownTimes.get(tag) == null) {
                    long currentTime = System.nanoTime();
                    mCountDownTimes.put(tag, currentTime);
                    return currentTime - mStartTimes.get(tag);
                } else {
                    long lastTime = mCountDownTimes.get(tag);
                    long currentTime = System.nanoTime();
                    mCountDownTimes.put(tag, currentTime);
                    return currentTime - lastTime;
                }
            }
        } else {
            return -1;
        }
    }

    /**
     * 结束测试，并返回距离上次测试时间已过去的时间差，或者距离开始测试时间的时间差
     * @param fromStart 是否是计算从开始测试时到现在的时间差
     * @return  如果当前正在运行，则返回时间差，否则直接返回 -1，单位：微秒
     */
    public long end(boolean fromStart) {
        if (mStartTime != -1) {
            long diff = -1;
            if (fromStart) {
                mCountDownTime = System.nanoTime();
                diff = mCountDownTime - mStartTime;
            } else {
                if (mCountDownTime == -1) {
                    mCountDownTime = System.nanoTime();
                    diff = mCountDownTime - mStartTime;
                } else {
                    long lastTime = mCountDownTime;
                    mCountDownTime = System.nanoTime();
                    diff = mCountDownTime - lastTime;
                }
            }
            mStartTime = -1;
            mCountDownTime = -1;
            return diff;
        } else {
            return -1;
        }
    }

    /**
     * 结束测试，并返回距离上次测试时间已过去的时间差，或者距离开始测试时间的时间差
     * @param tag   测试标记
     * @param fromStart 是否是计算从开始测试时到现在的时间差
     * @return  如果 tag 对应的测试正在运行，则返回时间差，否则直接返回 -1，单位：微秒
     */
    public long end(String tag, boolean fromStart) {
        if (!isEmptyString(tag) && hasPerfomanceTesting(tag)) {
            long diff = -1;
            if (fromStart) {
                long currentTime = System.nanoTime();
                mCountDownTimes.put(tag, currentTime);
                diff = currentTime - mStartTimes.get(tag);
            } else {
                if (mCountDownTimes.get(tag) == null) {
                    long currentTime = System.nanoTime();
                    mCountDownTimes.put(tag, currentTime);
                    diff = currentTime - mStartTimes.get(tag);
                } else {
                    long lastTime = mCountDownTimes.get(tag);
                    long currentTime = System.nanoTime();
                    mCountDownTimes.put(tag, currentTime);
                    diff = currentTime - lastTime;
                }
            }
            mStartTimes.remove(tag);
            mCountDownTimes.remove(tag);
            return diff;
        } else {
            return -1;
        }
    }

    /**
     * 判断对应 tag 的测试是否正在运行
     * @param tag   测试标记
     * @return  如果 tag 对应的测试正在运行，返回 true，否则返回 false.
     */
    public boolean hasPerfomanceTesting(String tag) {
        return mStartTimes.containsKey(tag);
    }

    /**
     * 判断字符串是否为空字符串
     * @param str 要判断的字符串
     * @return  如果字符串是 null  或者全部为空格， 返回 true，否则返回 false
     */
    private boolean isEmptyString(String str) {
        if (str == null) {
            return true;
        }
        if (str.trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 单例实现内部类
     */
    private static class QTPerfomanceTestingInstance {
        private static final QTPerfomanceTesting sInstance = new QTPerfomanceTesting();
    }
}