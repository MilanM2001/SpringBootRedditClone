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
import sr57.ftn.redditclone.model.DTO.AddCommentDTO;
import sr57.ftn.redditclone.service.PostService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class AddCommentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    PostService postService;

    int id;

    Button addCommentButton;
    Button addCommentButtonGoBack;

    EditText text;
    DrawerLayout drawer;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.add_comment_activity);

        sharedPreferences = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            id = bundle.getInt("ID2");

        text = findViewById(R.id.addCommentText);

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

        addCommentButton = findViewById(R.id.buttonAddReportPost);
        addCommentButton.setOnClickListener(view -> addComment());

        addCommentButtonGoBack = findViewById(R.id.buttonAddReportPostGoBack);
        addCommentButtonGoBack.setOnClickListener(view -> {
            Intent intent = new Intent(AddCommentActivity.this, ListPostActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(AddCommentActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(AddCommentActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(AddCommentActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(AddCommentActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addComment() {
        final AddCommentDTO addCommentDTO = new AddCommentDTO();
        String commentText = text.getText().toString();

        if (commentText.length() < 3 || commentText.length() > 85) {
            text.requestFocus();
            text.setError("Text must be at least 5-85 characters long");
        } else {
            addCommentDTO.setText(commentText);

            postService = RetrofitClient.postService;
            Call<AddCommentDTO> call = postService.addComment(id, addCommentDTO);
            call.enqueue(new Callback<AddCommentDTO>() {
                @Override
                public void onResponse(@NonNull Call<AddCommentDTO> call, @NonNull Response<AddCommentDTO> response) {
                    Toast.makeText(AddCommentActivity.this, "Comment Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<AddCommentDTO> call, @NonNull Throwable t) {
                    Toast.makeText(AddCommentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
