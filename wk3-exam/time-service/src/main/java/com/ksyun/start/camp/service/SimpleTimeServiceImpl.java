package com.ksyun.start.camp.service;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 代表简单时间服务实现
 */
@Component
public class SimpleTimeServiceImpl implements SimpleTimeService {

    @Override
    public String getDateTime(String style) {
        // 开始编写简单时间服务的核心逻辑
        // 获取时间、格式化时间、返回
        Date date = new Date();

        SimpleDateFormat dateFormat = null;

        switch (style) {
            case "full":
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            case "date":
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case "time":
                dateFormat = new SimpleDateFormat("HH:mm:ss");
                break;
            case "unix":
                return String.valueOf(date.getTime());
        }

        // 设置时区为 GMT
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        // 格式化时间
        String formattedDateTime = dateFormat.format(date);

        return formattedDateTime;
    }
}
