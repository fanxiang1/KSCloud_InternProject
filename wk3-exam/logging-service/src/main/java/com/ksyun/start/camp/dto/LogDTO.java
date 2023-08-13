package com.ksyun.start.camp.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Objects;

@Data
public class LogDTO {

    private Integer logId;

    private String serviceName;

    private String serviceId;

    private String datetime;

    private String level;

    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogDTO logDTO = (LogDTO) o;
        return Objects.equals(serviceName, logDTO.serviceName) && Objects.equals(serviceId, logDTO.serviceId)  && Objects.equals(level, logDTO.level) && Objects.equals(message, logDTO.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, serviceId, level, message);
    }
}
