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
import sr57.ftn.redditclone.model.User;
import sr57.ftn.redditclone.service.RetrofitClient;
import sr57.ftn.redditclone.service.UserService;

public class EditProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    UserService userService;
    User user = new User();

    Button buttonEditInfo;
    Button buttonEditInfoGoBack;

    EditText editDisplayName;
    EditText editDescription;

    SharedPreferences sharedPreferences;
    DrawerLayout drawer;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        sharedPreferences = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);

        editDisplayName = findViewById(R.id.displayNameEdit);
        editDescription = findViewById(R.id.userDescriptionEdit);

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

        userService = RetrofitClient.userService;
        Call<User> call = userService.getMyInfo();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                user = response.body();

                EditText displayName = findViewById(R.id.displayNameEdit);
                displayName.setText(user.getDisplay_name());

                EditText description = findViewById(R.id.userDescriptionEdit);
                description.setText(user.getDescription());
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

            }
        });

        buttonEditInfo = findViewById(R.id.buttonEditInfo);
        buttonEditInfo.setOnClickListener(v -> updateUser());

        buttonEditInfoGoBack = findViewById(R.id.buttonEditInfoGoBack);
        buttonEditInfoGoBack.setOnClickListener(view -> {
            finish();
            EditProfileActivity.super.onRestart();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(EditProfileActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(EditProfileActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(EditProfileActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateUser() {
        userService = RetrofitClient.userService;
        Call<User> call = userService.updateInfo(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                String displayNamePost = editDisplayName.getText().toString();
                String descriptionPost = editDescription.getText().toString();
                if (displayNamePost.length() < 3 || displayNamePost.length() > 15) {
                    editDisplayName.requestFocus();
                    editDisplayName.setError("Display Name must be 3-15 characters long");
                }
                if (descriptionPost.length() < 3 || descriptionPost.length() > 85) {
                    editDescription.requestFocus();
                    editDescription.setError("Description must be 3-85 characters long");
                } else {
                    user.setDisplay_name(displayNamePost);
                    user.setDescription(descriptionPost);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
