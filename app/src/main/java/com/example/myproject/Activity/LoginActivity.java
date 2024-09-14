package com.example.myproject.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myproject.R;
import com.example.myproject.ViewModel.User;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText edtPassword, edtEmail;
    private boolean isPasswordVisible = false;
    Button btnLogin, btnSignup;
    String email, password;
    List<User> registeredUser;
    CheckBox chkRemember;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_USERNAME = "email";
    private static final String KEY_PASSWORD = "password";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        edtPassword = findViewById(R.id.etPassword);
        edtEmail = findViewById(R.id.edEmailAddress);
        btnLogin = findViewById(R.id.buttonLogin);
        btnSignup = findViewById(R.id.buttonSignUp);
        chkRemember = findViewById(R.id.checkRemember);

        checkRemember();

        if (email != null && password != null) {
            edtEmail.setText(email);
            edtPassword.setText(password);
        }

        btnSignup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            getResultLauncher.launch(intent);
        });

        btnLogin.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            String passwordLog = edtPassword.getText().toString();

            registeredUser = readUser(LoginActivity.this, "user_data.txt");

            if (email.isEmpty() && passwordLog.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Chưa nhập username và password để đăng nhập!", Toast.LENGTH_SHORT).show();
                return;
            } else if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Chưa nhập username!", Toast.LENGTH_SHORT).show();
                return;
            } else if (passwordLog.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Chưa nhập password!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isValidUser = false;
            String name = "";
            for (User user : registeredUser) {
                if (email.equals(user.getEmail()) && passwordLog.equals(user.getPassword())) {
                    isValidUser = true;
                    name = user.getName();
                    break;
                }
            }

            if (isValidUser) {
                if (chkRemember.isChecked()) {
                    remember(email, passwordLog, true);
                } else {
                    remember("", "", false);
                }
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                homeIntent.putExtra("name", name);
                homeIntent.putExtra("email", email);
                homeIntent.putExtra("password", passwordLog);
                startActivity(homeIntent);
            } else {
                Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }

        });

        edtPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[2].getBounds().width())) {
                    if (isPasswordVisible) {
                        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_off, 0);
                    } else {
                        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_on, 0);
                    }
                    isPasswordVisible = !isPasswordVisible;
                    edtPassword.setSelection(edtPassword.getText().length());
                    return true;
                }
            }
            return false;
        });
    }

    ActivityResultLauncher<Intent> getResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 1) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            Bundle bundle = intent.getExtras();
                            if (bundle != null) {
                                email = bundle.getString("email");
                                password = bundle.getString("password");
                                String name = bundle.getString("name");

                                User newUser = new User(email, password, name);
                                saveUserToFile(LoginActivity.this, "user_data.txt", newUser);

                            }
                        }
                    }
                }
            }
    );
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

    public void saveUserToFile(Context context, String fileName, User user) {
        String data = user.getEmail() + "=" + user.getPassword() + "=" + user.getName() + "\n";
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE | Context.MODE_APPEND)) {
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void remember(String user, String pass, boolean chkRemember){
        SharedPreferences sharedPreferences = getSharedPreferences("remember", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", user);
        editor.putString("pass", pass);
        editor.putBoolean("chkRemember", chkRemember);
        editor.apply();
    }
    public void checkRemember(){
        SharedPreferences sharedPreferences = getSharedPreferences("remember", MODE_PRIVATE);
        String user = sharedPreferences.getString("email", "");
        String pass = sharedPreferences.getString("pass", "");
        boolean chkRemember1 = sharedPreferences.getBoolean("chkRemember", false);
        chkRemember.setChecked(chkRemember1);
        if(chkRemember.isChecked()){
            edtEmail.setText(user);
            edtPassword.setText(pass);
        }
    }
}