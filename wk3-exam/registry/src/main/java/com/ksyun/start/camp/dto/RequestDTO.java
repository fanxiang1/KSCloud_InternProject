package com.ksyun.start.camp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class RequestDTO {

    private String serviceName;

    private String serviceId;

    private String ipAddress;

    private Integer port;

    @JsonIgnore
    private Date beatTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestDTO that = (RequestDTO) o;
        return Objects.equals(serviceId, that.serviceId) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, ipAddress, port);
    }

}
