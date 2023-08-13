package com.ksyun.train.annotation;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 14:59
 */
public class TaskSchedule {

    @Schedule(dayOfMonth = 1)
    @Schedule(hour = 23)
    public void autoCleanResource() {

    }
}
