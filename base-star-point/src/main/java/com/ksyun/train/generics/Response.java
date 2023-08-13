package com.ksyun.train.generics;

/**
 * @author: sunjinfu
 * @date: 2023/6/7 19:57
 */
public class Response<T> {

    private String requestId;

    private T data;

    public static void main(String[] args) {
        ParameterizedTypeReference reference = new ParameterizedTypeReference<Response<Integer>>() {};
        ParameterizedTypeReference reference2 = new ParameterizedTypeReference<Response<Double>>() {};
        System.out.println(reference.getType());
    }

}
