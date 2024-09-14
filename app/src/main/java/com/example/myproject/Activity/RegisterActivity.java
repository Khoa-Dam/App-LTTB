package com.example.myproject.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myproject.R;
import com.example.myproject.ViewModel.User;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText etPassword, edConfirmPassword, edName, edEmail;
    private boolean isPasswordVisible = false;
    Button btnLogin;
    TextView textViewSignIn;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        etPassword = findViewById(R.id.edtPassword);
        edConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edName = findViewById(R.id.edtName);
        edEmail = findViewById(R.id.edtEmailAddress);
        btnLogin = findViewById(R.id.buttonLogin);
        textViewSignIn = findViewById(R.id.textViewSignIn);
        etPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Kiểm tra nếu chạm vào drawableEnd (biểu tượng mắt)
                if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[2].getBounds().width())) {
                    // Đổi trạng thái hiển thị mật khẩu
                    if (isPasswordVisible) {
                        // Ẩn mật khẩu
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_off, 0);
                    } else {
                        // Hiển thị mật khẩu
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_on, 0);
                    }
                    isPasswordVisible = !isPasswordVisible;
                    // Đặt lại vị trí con trỏ
                    etPassword.setSelection(etPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        edConfirmPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Kiểm tra nếu chạm vào drawableEnd (biểu tượng mắt)
                if (event.getRawX() >= (edConfirmPassword.getRight() - edConfirmPassword.getCompoundDrawables()[2].getBounds().width())) {
                    // Đổi trạng thái hiển thị mật khẩu
                    if (isPasswordVisible) {
                        // Ẩn mật khẩu
                        edConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_off, 0);
                    } else {
                        // Hiển thị mật khẩu
                        edConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        edConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_on, 0);
                    }
                    isPasswordVisible = !isPasswordVisible;
                    // Đặt lại vị trí con trỏ
                    edConfirmPassword.setSelection(edConfirmPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        textViewSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Optional: Close the RegisterActivity if you don't want to keep it in the back stack
        });

        btnLogin.setOnClickListener(view -> {
            String name = edName.getText().toString();
            String email = edEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = edConfirmPassword.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Hãy điền tất cả các thông tin cần thiết để tạo tài khoản", Toast.LENGTH_SHORT).show();
            } else if (!EmailValidator.isValidEmail(email)) {
                Toast.makeText(RegisterActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            } else if (confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Hãy điền vào ô xác nhận mật khẩu", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            } else {

                User user = new User(email, password, name);
                writeUser(RegisterActivity.this, "user_data.txt", user);

                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                bundle.putString("password", password);
                bundle.putString("name", name);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    public void writeUser(Context context, String fileName, User newUser) {
        List<User> list;
        // Đọc danh sách người dùng hiện tại từ file, nếu không có thì tạo mới danh sách
        list = readUser(context, fileName);
        if (list == null) {
            list = new ArrayList<>();
        }

        for (User user : list) {
            if (user.getEmail().equals(newUser.getEmail())) {
                Toast.makeText(context, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                return; // Exit early if duplicate found
            }
        }

        list.add(newUser);

        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            StringBuilder data = new StringBuilder();
            for (User user : list) {
                data.append(user.getEmail()).append("=").append(user.getPassword()).append("=").append(user.getName()).append("\n");
            }
            fos.write(data.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> readUser(Context context, String fileName) {
        List<User> users = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 3) {
                    String email = parts[0];
                    String password = parts[1];
                    String name = parts[2];
                    users.add(new User(email, password, name));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static class UserNameValidator {

        public static final String USERNAME_LENGTH_ERROR = "Username phải có tối thiểu 4 kí tự và tối đa 20 kí tự!";
        public static final String USERNAME_INVALID_ERROR = "Username chỉ có thể chứa chữ cái và số!";
        public static final String USERNAME_CONTENT_ERROR = "Username phải có ít nhất 1 kí tự chữ cái hoặc số.";

        public static ValidationResult validateUserName(String username) {
            if (username.length() < 4 || username.length() > 20) {
                return new ValidationResult(false, USERNAME_LENGTH_ERROR);
            }
            if (!username.matches("^[a-zA-Z0-9]+$")) {
                return new ValidationResult(false, USERNAME_INVALID_ERROR);
            }

            boolean hasLetter = false;
            boolean hasDigit = false;
            // Duyệt từng ký tự trong username
            for (char c : username.toCharArray()) {
                if (Character.isLetter(c)) {
                    hasLetter = true;
                }
                if (Character.isDigit(c)) {
                    hasDigit = true;
                }
            }
            // Kiểm tra điều kiện ít nhất một ký tự hoặc số
            if (!hasLetter || !hasDigit) {
                return new ValidationResult(false, USERNAME_CONTENT_ERROR);
            }
            return new ValidationResult(true, "Username is valid.");

        }

        public static class ValidationResult {
            public final boolean isValid;
            public final String errorMessage;

            ValidationResult(boolean isValid, String errorMessage) {
                this.isValid = isValid;
                this.errorMessage = errorMessage;
            }
        }
    }
}