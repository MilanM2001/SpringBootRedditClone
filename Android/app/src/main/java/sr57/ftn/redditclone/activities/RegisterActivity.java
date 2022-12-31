package sr57.ftn.redditclone.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sr57.ftn.redditclone.MainActivity;
import sr57.ftn.redditclone.R;
import sr57.ftn.redditclone.model.User;
import sr57.ftn.redditclone.service.RetrofitClient;
import sr57.ftn.redditclone.service.UserService;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText email;
    EditText display_name;

    UserService userService;
    Button buttonRegister;
    Button buttonGoBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        username = findViewById(R.id.usernameRegistration);
        password = findViewById(R.id.passwordRegistration);
        email = findViewById(R.id.emailRegistration);
        display_name = findViewById(R.id.display_nameRegistration);

        buttonRegister = findViewById(R.id.registerButtonRegistration);
        buttonRegister.setOnClickListener(v -> register());

        buttonGoBack = findViewById(R.id.backButtonRegistration);
        buttonGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void register() {
        userService = RetrofitClient.userService;
        final User user = new User();
        String usernameNew = username.getText().toString();
        String passwordNew = password.getText().toString();
        String emailNew = email.getText().toString();
        String display_nameNew = display_name.getText().toString();

        if (usernameNew.length() < 3 || usernameNew.length() > 15) {
            username.requestFocus();
            username.setError("Username must be 3-15 characters long");
        } else if (passwordNew.length() < 3 || passwordNew.length() > 15) {
            password.requestFocus();
            password.setError("Password must be 3-15 characters long");
        } else if (emailNew.length() < 3 || emailNew.length() > 15) {
            email.requestFocus();
            email.setError("Email must be 3-15 characters long");
        } else if (display_nameNew.length() < 3 || display_nameNew.length() > 15) {
            display_name.requestFocus();
            display_name.setError("Display Name must be 3-15 characters long");
        } else {
            Toast.makeText(RegisterActivity.this, "Validation Successful", Toast.LENGTH_LONG).show();
            user.setUsername(usernameNew);
            user.setPassword(passwordNew);
            user.setEmail(emailNew);
            user.setDisplay_name(display_nameNew);

            Call<Boolean> call = userService.signup(user);

            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (Boolean.TRUE.equals(response.body())) {
                        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, ListPostActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (Boolean.FALSE.equals(response.body())) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                        alert.setTitle("Error");
                        alert.setMessage("Username " + usernameNew + " is taken, choose another one");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
