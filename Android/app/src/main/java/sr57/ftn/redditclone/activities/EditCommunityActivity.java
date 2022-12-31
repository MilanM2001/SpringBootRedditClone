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
import androidx.annotation.Nullable;
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
import sr57.ftn.redditclone.model.Community;
import sr57.ftn.redditclone.service.CommunityService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class EditCommunityActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CommunityService communityService;
    Community community = new Community();

    Button buttonEditCommunity;
    Button buttonEditCommunityGoBack;

    int id;

    EditText editDescription;
    DrawerLayout drawer;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.edit_community_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            id = bundle.getInt("ID2");

        editDescription = findViewById(R.id.descriptionEdit);

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

        communityService = RetrofitClient.communityService;
        Call<Community> call = communityService.getSelectedCommunityById(id);
        call.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(@NonNull Call<Community> call, @NonNull Response<Community> response) {
                community = response.body();

                EditText description = findViewById(R.id.descriptionEdit);
                description.setText(community.getDescription());
            }

            @Override
            public void onFailure(@NonNull Call<Community> call, @NonNull Throwable t) {

            }
        });

        buttonEditCommunityGoBack = findViewById(R.id.buttonEditCommunityGoBack);
        buttonEditCommunityGoBack.setOnClickListener(view -> {
            finish();
            EditCommunityActivity.super.onRestart();
        });

        buttonEditCommunity = findViewById(R.id.buttonEditCommunity);
        buttonEditCommunity.setOnClickListener(view -> updateCommunity());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(EditCommunityActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(EditCommunityActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(EditCommunityActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(EditCommunityActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateCommunity() {
        communityService = RetrofitClient.communityService;
        Call<Community> call = communityService.updateCommunity(community, community.getCommunity_id());

        call.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(@NonNull Call<Community> call, @NonNull Response<Community> response) {
                String descriptionCommunity = editDescription.getText().toString();

                if (descriptionCommunity.length() < 5 || descriptionCommunity.length() > 85) {
                    editDescription.requestFocus();
                    editDescription.setError("Description must be 5-85 characters long!");
                } else {
                    community.setDescription(descriptionCommunity);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Community> call, @NonNull Throwable t) {
                Toast.makeText(EditCommunityActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
