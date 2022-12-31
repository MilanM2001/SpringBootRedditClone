package sr57.ftn.redditclone.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class MyProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    UserService userService;
    TextView usernameProfile;
    TextView displayNameProfile;
    TextView emailProfile;
    TextView roleProfile;
    TextView descriptionProfile;
    DrawerLayout drawer;

    SharedPreferences sharedPreferences;
    String loggedInUserRole;

    Button editInfoButton;
    Button editPasswordButton;
    Button viewReportedPostsButton;
    Button viewReportedCommentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_activity);

        usernameProfile = findViewById(R.id.usernameProfile);
        displayNameProfile = findViewById(R.id.displayNameProfile);
        emailProfile = findViewById(R.id.emailProfile);
        roleProfile = findViewById(R.id.roleProfile);
        descriptionProfile = findViewById(R.id.descriptionProfile);

        sharedPreferences = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);

        editInfoButton = findViewById(R.id.editInfoButton);
        editInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        editPasswordButton = findViewById(R.id.editPasswordButton);
        editPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, EditPasswordActivity.class);
            startActivity(intent);
        });

        viewReportedPostsButton = findViewById(R.id.viewReportedPostsButton);
        viewReportedPostsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, ListReportPostActivity.class);
            startActivity(intent);
        });

        viewReportedCommentsButton = findViewById(R.id.viewReportedCommentsButton);
        viewReportedCommentsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, ListReportCommentActivity.class);
            startActivity(intent);
        });

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_my_profile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSelectedUser();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(MyProfileActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(MyProfileActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(MyProfileActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getSelectedUser() {
        userService = RetrofitClient.userService;
        Call<User> call = userService.getMyInfo();

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                assert response.body() != null;
                usernameProfile.setText(response.body().getUsername());
                emailProfile.setText(response.body().getEmail());
                displayNameProfile.setText(response.body().getDisplay_name());
                roleProfile.setText(response.body().getRole());
                descriptionProfile.setText(response.body().getDescription());

                sharedPreferences = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);
                loggedInUserRole = sharedPreferences.getString(MainActivity.Role, "");

                if (loggedInUserRole.equals("USER")) {
                    viewReportedPostsButton.setVisibility(View.INVISIBLE);
                    viewReportedCommentsButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(MyProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
