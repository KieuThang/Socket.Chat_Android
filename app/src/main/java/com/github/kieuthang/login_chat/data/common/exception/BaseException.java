package com.github.kieuthang.login_chat.data.common.exception;


class BaseException extends Exception {
    private String msg;

    BaseException(String message) {
        super();
        this.msg = message;
    }

    BaseException(){
        super();
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }
}
