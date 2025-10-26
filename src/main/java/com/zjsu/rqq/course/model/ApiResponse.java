package com.zjsu.rqq.course.model;

// API响应类
public class ApiResponse<T> {
    private int code;// 状态码
    private String message;// 消息
    private T data;// 数据

    // 构造方法
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    // 带消息的成功响应
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    // 创建响应
    public static <T> ApiResponse<T> created(String message,T data) {
        return new ApiResponse<>(201, message, data);
    }

    // 无内容响应
    public static ApiResponse<Void> noContent(String message) {
        return new ApiResponse<>(204, message, null);
    }

    // 错误响应
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    // 带数据的错误响应
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    // Getter和Setter
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
