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
import sr57.ftn.redditclone.model.DTO.AddCommunityDTO;
import sr57.ftn.redditclone.service.CommunityService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class AddCommunityActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    CommunityService communityService;

    EditText name;
    EditText description;

    Button addCommunityButton;
    Button buttonGoBack;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_community_activity);

        sharedPreferences = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);

        name = findViewById(R.id.addCommunityName);
        description = findViewById(R.id.addCommunityDescription);

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

        addCommunityButton = findViewById(R.id.addCommunityButton);
        addCommunityButton.setOnClickListener(v -> addCommunityAndroid());

        buttonGoBack = findViewById(R.id.backButtonAddCommunity);
        buttonGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddCommunityActivity.this, ListCommunityActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(AddCommunityActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(AddCommunityActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(AddCommunityActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(AddCommunityActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addCommunityAndroid() {
        final AddCommunityDTO addCommunity = new AddCommunityDTO();
        String communityName = name.getText().toString();
        String communityDescription = description.getText().toString();

        if (communityName.length() < 3 || communityName.length() > 15) {
            name.requestFocus();
            name.setError("Name must be 3-15 characters long");
        } else if (communityDescription.length() < 5 || communityDescription.length() > 85) {
            description.requestFocus();
            description.setError("Description must be 5-85 characters long");
        } else {
            addCommunity.setName(communityName);
            addCommunity.setDescription(communityDescription);

            communityService = RetrofitClient.communityService;
            Call<AddCommunityDTO> call = communityService.addCommunityAndroid(addCommunity);
            call.enqueue(new Callback<AddCommunityDTO>() {
                @Override
                public void onResponse(@NonNull Call<AddCommunityDTO> call, @NonNull Response<AddCommunityDTO> response) {
                    Toast.makeText(AddCommunityActivity.this, "Community Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCommunityActivity.this, ListCommunityActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<AddCommunityDTO> call, @NonNull Throwable t) {
                    Toast.makeText(AddCommunityActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
