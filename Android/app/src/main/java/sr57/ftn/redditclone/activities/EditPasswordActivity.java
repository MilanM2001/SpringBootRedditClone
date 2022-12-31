package sr57.ftn.redditclone.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sr57.ftn.redditclone.MainActivity;
import sr57.ftn.redditclone.R;
import sr57.ftn.redditclone.model.DTO.EditPasswordDTO;
import sr57.ftn.redditclone.service.RetrofitClient;
import sr57.ftn.redditclone.service.UserService;

public class EditPasswordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button changePasswordButton;
    UserService userService;
    EditText oldPasswordEdit;
    EditText newPasswordEdit;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password_activity);

        oldPasswordEdit = findViewById(R.id.oldPasswordEdit);
        newPasswordEdit = findViewById(R.id.newPasswordEdit);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_communities);
        }

        changePasswordButton = findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(v -> editPassword());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(EditPasswordActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(EditPasswordActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(EditPasswordActivity.this, MyProfileActivity.class);
                startActivity(intentProfile);
                finish();
                break;
            case R.id.nav_logout:
                new AlertDialog.Builder(this)
                        .setTitle("Logout?")
                        .setMessage("Are you sure you want to Logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                            RetrofitClient.setToken("");
                            SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences1.edit();
                            editor.clear();
                            editor.apply();
                            finish();
                            Intent intent = new Intent(EditPasswordActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void editPassword() {
        String oldPassworChange = oldPasswordEdit.getText().toString();
        String newPasswordChange = newPasswordEdit.getText().toString();

        if (oldPassworChange.length() < 3 || oldPassworChange.length() > 15) {
            oldPasswordEdit.requestFocus();
            oldPasswordEdit.setError("Your password can only be 3-15 characters long");
        } else if (newPasswordChange.length() < 3 || newPasswordChange.length() > 15) {
            newPasswordEdit.requestFocus();
            newPasswordEdit.setError("Your new password must be 3-15 characters long");
        } else {
            EditPasswordDTO editPasswordDTO = new EditPasswordDTO();
            editPasswordDTO.setNewPassword(newPasswordChange);
            editPasswordDTO.setOldPassword(oldPassworChange);

            userService = RetrofitClient.userService;
            Call<Boolean> call = userService.editPassword(editPasswordDTO);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (Boolean.TRUE.equals(response.body())) {
                        Toast.makeText(EditPasswordActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditPasswordActivity.this, MyProfileActivity.class);
                        startActivity(intent);
                    } else if (Boolean.FALSE.equals(response.body())) {
                        oldPasswordEdit.requestFocus();
                        oldPasswordEdit.setError("This is not your current password");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    Toast.makeText(EditPasswordActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}

