package com.example.myproject.Activity;

import java.util.regex.Pattern;

public class EmailValidator {
    // Biểu thức chính quy để kiểm tra định dạng email
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // Tạo một mẫu Pattern từ biểu thức chính quy
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    // Phương thức kiểm tra email
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return pattern.matcher(email).matches();
    }
}
