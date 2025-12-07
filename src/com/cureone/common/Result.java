package com.cureone.common;


//generic result wrapper used by the services to return success, message and optional data

public class Result<T> {
    private boolean success;
    private String message;
    private T data;

    public Result(){}
    public Result(boolean success, String message){
        this.success = success;
        this.message = message;
    }
    public Result(boolean success, String message, T data){
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess(){
        return this.success;
    }
    public void setSuccess(boolean success){
        this.success = success;
    }
    public String getMessage(){
        return this.message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public T getData(){
        return this.data;
    }
    public void setData(T data){
        this.data = data;
    }

    @Override
    public String toString(){
        return "Result{" + "success=" + success + ", message='" + message + '\'' + ", data=" + data + '}';
    }

}
