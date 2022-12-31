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
import sr57.ftn.redditclone.model.Post;
import sr57.ftn.redditclone.service.PostService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class EditPostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    PostService postService;
    Post post = new Post();

    Button buttonEditPost;
    Button buttonEditPostGoBack;

    int id;

    EditText editTitle;
    EditText editText;

    DrawerLayout drawer;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.edit_post_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            id = bundle.getInt("ID2");

        editTitle = findViewById(R.id.postTitleEdit);
        editText = findViewById(R.id.postTextEdit);

        postService = RetrofitClient.postService;
        Call<Post> call = postService.getSelectedPostById(id);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                post = response.body();

                EditText title = findViewById(R.id.postTitleEdit);
                title.setText(post.getTitle());

                EditText text = findViewById(R.id.postTextEdit);
                text.setText(post.getText());
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {

            }
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

        buttonEditPostGoBack = findViewById(R.id.buttonEditPostGoBack);
        buttonEditPostGoBack.setOnClickListener(view -> {
            finish();
            EditPostActivity.super.onRestart();
        });

        buttonEditPost = findViewById(R.id.buttonEditPost);
        buttonEditPost.setOnClickListener(view -> updatePost());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(EditPostActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(EditPostActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(EditPostActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(EditPostActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updatePost() {
        postService = RetrofitClient.postService;
        Call<Post> call = postService.updatePost(post, post.getPost_id());

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                String titlePost = editTitle.getText().toString();
                String textPost = editText.getText().toString();

                if (titlePost.length() < 3 || titlePost.length() > 20) {
                    editTitle.requestFocus();
                    editTitle.setError("Title must be 3-20 characters long!");
                }
                if (textPost.length() < 3 || textPost.length() > 85) {
                    editText.requestFocus();
                    editText.setError("Text must be 3-85 characters long!");
                } else {
                    post.setTitle(titlePost);
                    post.setText(textPost);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                Toast.makeText(EditPostActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
