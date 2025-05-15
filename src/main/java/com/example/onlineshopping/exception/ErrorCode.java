package com.example.onlineshopping.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_EXISTED(1001, "Người dùng đã tồn tại", HttpStatus.CONFLICT),
    EMAIL_EXISTED(1002, "Email đã tồn tại", HttpStatus.CONFLICT),
    NO_CHANGE_TO_UPDATE(1003, "Không có thay đổi để cập nhật", HttpStatus.BAD_REQUEST),
    UPDATE_FAILED(1004, "Không cập nhật được người dùng", HttpStatus.INTERNAL_SERVER_ERROR),
    WRONG_PASSWORD(1005, "Mật khẩu không đúng", HttpStatus.UNAUTHORIZED),
    PRODUCT_EXISTED(1006, "Sản phẩm đã tồn tại", HttpStatus.CONFLICT),
    PRODUCT_NO_EXISTED(1007, "Sản phẩm không tồn tại", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND_OR_EMPTY(1008, "Giỏ hàng không tồn tại hoặc trống", HttpStatus.BAD_REQUEST),
    USER_NO_EXISTED(1009, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    ACC_BLOCK(1010, "Tài khoản bị khóa", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(1011, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_PARSING_ERROR(1012, "Lỗi khi phân tích token", HttpStatus.BAD_REQUEST),
    AUTH_TOKEN_INVALID(1013, "Lỗi xác thực token", HttpStatus.UNAUTHORIZED),
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
