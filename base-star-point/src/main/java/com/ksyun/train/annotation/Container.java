package com.ksyun.train.annotation;

/**
 * @author: sunjinfu
 * @date: 2023/7/1 16:14
 */
public class Container {

    @LengthLimitAnnotation(max = 20, min = 1)
    private String name;

    @LengthLimitAnnotation(max = 50, min = 5)
    private String image;

    private String restartPolicy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRestartPolicy() {
        return restartPolicy;
    }
}
