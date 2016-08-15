package com.application.pramod.guidedmeditation;

/**
 * Created by pram on 8/12/2016.
 */
public class Params {
    private static final Integer DEFAULT_COUNT_LIMIT = 108;
    private static Integer _count;
    private static Integer _count_limit;

    public Params() {
        _count = 0;
        _count_limit = DEFAULT_COUNT_LIMIT;
    }

    public static Integer getCount() {
        return _count;
    }
    public static void setCount(Integer cnt) {
        _count = cnt;
    }
    public static void increment_count() {
        _count++;
    }

    public static Integer getCount_limit() {
        return _count_limit;
    }
    public static void setCount_limit(Integer cnt_lim) {
        _count_limit = cnt_lim;
    }
}
