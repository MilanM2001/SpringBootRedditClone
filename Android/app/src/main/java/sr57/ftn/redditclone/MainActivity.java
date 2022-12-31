package sr57.ftn.redditclone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sr57.ftn.redditclone.activities.ListPostActivity;
import sr57.ftn.redditclone.activities.RegisterActivity;
import sr57.ftn.redditclone.model.DTO.Login;
import sr57.ftn.redditclone.model.User;
import sr57.ftn.redditclone.service.RetrofitClient;
import sr57.ftn.redditclone.service.UserService;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    UserService userService;

    SharedPreferences sharedPreferences;

    public static final String MyPres = "MyPre";
    public static final String Username = "usernameKey";
    public static final String Token = "tokenKey";
    public static final String Role = "role";
    public static final Integer ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        usernameEditText = findViewById(R.id.activity_main_usernameEditText);
        passwordEditText = findViewById(R.id.activity_main_passwordEditText);
        loginButton = findViewById(R.id.activity_main_loginButton);
        registerButton = findViewById(R.id.activity_main_registerButton);

        sharedPreferences = getSharedPreferences(MyPres, Context.MODE_PRIVATE);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (validate(username, password)) {
                doLogin(username, password);
            }
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public boolean validate(String username, String password) {
        if (username == null || username.trim().length() == 0) {
            Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void doLogin(final String username, final String password) {

        userService = RetrofitClient.userService;
        Login login = new Login();
        login.setUsername(username);
        login.setPassword(password);
        Call<User> call = userService.loginAndroid(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.body() != null) {
                    User user = response.body();
                    RetrofitClient.setToken(user.getToken());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Token, user.getToken());
                    editor.putString(Username, user.getUsername());
                    editor.putString(Role, user.getRole());
                    editor.putInt(String.valueOf(ID), user.getUser_id());
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, ListPostActivity.class);

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                    usernameEditText.requestFocus();
                    usernameEditText.setError("Invalid Username or Password");
                    passwordEditText.requestFocus();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

