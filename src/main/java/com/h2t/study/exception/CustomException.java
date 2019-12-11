package com.h2t.study.exception;

/**
 * 自定义异常
 *
 * @author hetiantian
 * @version 1.0
 * @Date 2019/12/10 10:35
 */
public class CustomException extends RuntimeException {
    private String msg;

    public CustomException(String msg) {
        super(msg);
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
