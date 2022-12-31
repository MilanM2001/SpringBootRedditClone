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
import sr57.ftn.redditclone.model.DTO.AddPostDTO;
import sr57.ftn.redditclone.service.CommunityService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class AddPostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    CommunityService communityService;

    int id;

    Button addPostButton;
    Button buttonGoBack;

    EditText title;
    EditText text;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_activity);

        sharedPreferences = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            id = bundle.getInt("ID2");

        title = findViewById(R.id.addPostTitle);
        text = findViewById(R.id.addPostText);

        addPostButton = findViewById(R.id.addPostButton);
        addPostButton.setOnClickListener(v -> addPost());

        buttonGoBack = findViewById(R.id.backButtonAddPost);
        buttonGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddPostActivity.this, ListPostActivity.class);
            startActivity(intent);
            finish();
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
            navigationView.setCheckedItem(R.id.nav_communities);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(AddPostActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(AddPostActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(AddPostActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(AddPostActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addPost() {
        final AddPostDTO addPostDTO = new AddPostDTO();
        String postTitle = title.getText().toString();
        String postText = text.getText().toString();

        if (postTitle.length() < 3 || postTitle.length() > 20) {
            title.requestFocus();
            title.setError("Title must be 3-20 characters long");
        } else if (postText.length() < 3 || postText.length() > 85) {
            text.requestFocus();
            text.setError("Text must be at least 5-85 characters long");
        } else {
            addPostDTO.setTitle(postTitle);
            addPostDTO.setText(postText);

            communityService = RetrofitClient.communityService;
            Call<AddPostDTO> call = communityService.addPost(id, addPostDTO);
            call.enqueue(new Callback<AddPostDTO>() {
                @Override
                public void onResponse(@NonNull Call<AddPostDTO> call, @NonNull Response<AddPostDTO> response) {
                    Toast.makeText(AddPostActivity.this, "Post Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<AddPostDTO> call, @NonNull Throwable t) {
                    Toast.makeText(AddPostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
