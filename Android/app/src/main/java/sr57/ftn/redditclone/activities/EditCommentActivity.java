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
import sr57.ftn.redditclone.model.Comment;
import sr57.ftn.redditclone.service.CommentService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class EditCommentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final String TAG = SelectedPostActivity.class.getSimpleName();

    CommentService commentService;
    Comment comment = new Comment();

    Button buttonEditComment;
    Button buttonEditCommentGoBack;

    int id;

    EditText editText;
    DrawerLayout drawer;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.edit_comment_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            id = bundle.getInt("ID2");

        editText = findViewById(R.id.commentTextEdit);

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

        commentService = RetrofitClient.commentService;
        Call<Comment> call = commentService.getSelectedCommentById(id);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                comment = response.body();

                EditText text = findViewById(R.id.commentTextEdit);
                text.setText(comment.getText());
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {

            }
        });

        buttonEditCommentGoBack = findViewById(R.id.buttonEditCommentGoBack);
        buttonEditCommentGoBack.setOnClickListener(view -> {
            finish();
            EditCommentActivity.super.onRestart();
        });

        buttonEditComment = findViewById(R.id.buttonEditComment);
        buttonEditComment.setOnClickListener(view -> updateComment());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(EditCommentActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(EditCommentActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(EditCommentActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(EditCommentActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateComment() {
        commentService = RetrofitClient.commentService;
        Call<Comment> call = commentService.updateComment(comment, comment.getComment_id());

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                String textPost = editText.getText().toString();

                if (textPost.length() < 3 || textPost.length() > 85) {
                    editText.requestFocus();
                    editText.setError("Text must be 3-85 characters long!");
                } else {
                    comment.setText(textPost);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                Toast.makeText(EditCommentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
