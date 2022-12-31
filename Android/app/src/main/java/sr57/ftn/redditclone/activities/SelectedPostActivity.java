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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sr57.ftn.redditclone.MainActivity;
import sr57.ftn.redditclone.R;
import sr57.ftn.redditclone.adapters.CommentListAdapter;
import sr57.ftn.redditclone.enums.ReactionType;
import sr57.ftn.redditclone.model.Comment;
import sr57.ftn.redditclone.model.DTO.PostReactionAndroidDTO;
import sr57.ftn.redditclone.model.Post;
import sr57.ftn.redditclone.model.Reaction;
import sr57.ftn.redditclone.model.Report;
import sr57.ftn.redditclone.service.PostService;
import sr57.ftn.redditclone.service.ReactionService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class SelectedPostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    int id;
    int id2;

    PostService postService;
    ReactionService reactionService;
    DrawerLayout drawer;

    Button buttonEdit;
    Button buttonDelete;
    Button buttonAddComment;
    Button buttonAddReport;

    TextView postTitle;
    TextView postText;
    TextView postDisplayName;
    TextView postSubredditName;
    TextView postKarma;

    ImageView upvoteImage;
    ImageView downvoteImage;

    Set<Reaction> reactionsOnPost;
    Set<Report> reportsOnPost;
    String postOwnerUsername;
    String loggedInUserUsername;

    LinearLayout linearLayout;
    RelativeLayout relativeLayoutPostVoteOptions;
    RelativeLayout relativeLayoutPostReportOptions;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_post_activity);
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null)
            id = bundle1.getInt("ID");

        postTitle = findViewById(R.id.selectedPostTitle);
        postText = findViewById(R.id.selectedPostText);
        postDisplayName = findViewById(R.id.selectedPostDisplayName);
        postSubredditName = findViewById(R.id.selectedPostSubredditName);
        postKarma = findViewById(R.id.selectedPostKarma);

        recyclerView = findViewById(R.id.rvCommentsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonEdit = findViewById(R.id.selectedPostEditButton);
        buttonEdit.setOnClickListener(view -> {
            Intent intent = new Intent(SelectedPostActivity.this, EditPostActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID2", id2);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        buttonDelete = findViewById(R.id.selectedPostDeleteButton);
        buttonDelete.setOnClickListener(view -> deleteSelectedPost(id));

        buttonAddComment = findViewById(R.id.selectedPostAddCommentButton);
        buttonAddComment.setOnClickListener(view -> {
            Intent intent = new Intent(SelectedPostActivity.this, AddCommentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID2", id2);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        buttonAddReport = findViewById(R.id.selectedPostAddReportButton);
        buttonAddReport.setOnClickListener(view -> {
            Intent intent = new Intent(SelectedPostActivity.this, AddReportPostActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID2", id2);
            intent.putExtras(bundle);
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
            navigationView.setCheckedItem(0);
        }

        upvoteImage = findViewById(R.id.upvotePost);
        downvoteImage = findViewById(R.id.downvotePost);

        upvoteImage.setOnClickListener(view -> upvoteReaction(id));

        downvoteImage.setOnClickListener(view -> downvoteReaction(id));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(SelectedPostActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(SelectedPostActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(SelectedPostActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(SelectedPostActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getSelectedPost();
        getComments();
    }

    private void getSelectedPost() {
        postService = RetrofitClient.postService;
        Call<Post> call = postService.getSelectedPostById(id);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                assert response.body() != null;
                postTitle.setText(response.body().getTitle());
                postText.setText(response.body().getText());
                postDisplayName.setText(response.body().getUser().getDisplay_name());
                postSubredditName.setText(response.body().getCommunity().getName());
                id2 = response.body().getPost_id();
                postOwnerUsername = response.body().getUser().getUsername();
                reactionsOnPost = response.body().getReactions();
                reportsOnPost = response.body().getReports();

                postKarma.setText(String.valueOf(calculateKarma()));

                sharedPreferences = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);
                loggedInUserUsername = sharedPreferences.getString(MainActivity.Username, "");

                linearLayout = findViewById(R.id.postOwnerOptions);
                relativeLayoutPostVoteOptions = findViewById(R.id.postVoteOptions);
                relativeLayoutPostReportOptions = findViewById(R.id.postReportOptions);

                for (Reaction reaction : reactionsOnPost) {
                    if (reaction.getUser().getUsername().equals(loggedInUserUsername)) {
                        upvoteImage.setOnClickListener(view -> Toast.makeText(SelectedPostActivity.this, "You have already Reacted to this Post", Toast.LENGTH_SHORT).show());
                        downvoteImage.setOnClickListener(view -> Toast.makeText(SelectedPostActivity.this, "You have already Reacted to this Post", Toast.LENGTH_SHORT).show());
                    }
                }

                for (Report report : reportsOnPost) {
                    if (report.getUser().getUsername().equals(loggedInUserUsername)) {
                        buttonAddReport.setOnClickListener(view -> Toast.makeText(SelectedPostActivity.this, "You have already Reported this Post", Toast.LENGTH_SHORT).show());
                    }
                }

                if (!(loggedInUserUsername.equals(postOwnerUsername))) {
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                Toast.makeText(SelectedPostActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Integer calculateKarma() {
        Integer karma = 0;
        for (Reaction reaction : reactionsOnPost) {
            if (reaction.getReaction_type().equals(ReactionType.UPVOTE)) {
                karma++;
            } else if (reaction.getReaction_type().equals(ReactionType.DOWNVOTE)) {
                karma--;
            }
        }
        return karma;
    }

    private void getComments() {
        postService = RetrofitClient.postService;
        Call<List<Comment>> call = postService.getSelectedPostComments(id);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                recyclerView.setAdapter(new CommentListAdapter(response.body(), getApplicationContext()));
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                Toast.makeText(SelectedPostActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSelectedPost(Integer id) {
        postService = RetrofitClient.postService;
        Call<Void> call = postService.deletePost(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                System.out.println("Deleted");
                finish();
                SelectedPostActivity.super.onRestart();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(SelectedPostActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upvoteReaction(Integer post_id) {
        PostReactionAndroidDTO postReactionAndroidDTO = new PostReactionAndroidDTO(post_id);

        reactionService = RetrofitClient.reactionService;
        Call<PostReactionAndroidDTO> call = reactionService.upvotePostReaction(postReactionAndroidDTO);
        call.enqueue(new Callback<PostReactionAndroidDTO>() {
            @Override
            public void onResponse(@NonNull Call<PostReactionAndroidDTO> call, @NonNull Response<PostReactionAndroidDTO> response) {
                PostReactionAndroidDTO createReaction = response.body();

                assert createReaction != null;
                Integer post_id = createReaction.getPost_id();

                postReactionAndroidDTO.setPost_id(post_id);

                recreate();
                Toast.makeText(SelectedPostActivity.this, "You have upvoted this post", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<PostReactionAndroidDTO> call, @NonNull Throwable t) {
                Toast.makeText(SelectedPostActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downvoteReaction(Integer post_id) {
        PostReactionAndroidDTO postReactionAndroidDTO = new PostReactionAndroidDTO(post_id);

        reactionService = RetrofitClient.reactionService;
        Call<PostReactionAndroidDTO> call = reactionService.downvotePostReaction(postReactionAndroidDTO);
        call.enqueue(new Callback<PostReactionAndroidDTO>() {
            @Override
            public void onResponse(@NonNull Call<PostReactionAndroidDTO> call, @NonNull Response<PostReactionAndroidDTO> response) {
                PostReactionAndroidDTO createReaction = response.body();

                assert createReaction != null;
                Integer post_id = createReaction.getPost_id();

                postReactionAndroidDTO.setPost_id(post_id);

                recreate();
                Toast.makeText(SelectedPostActivity.this, "You have downvoted this post", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<PostReactionAndroidDTO> call, @NonNull Throwable t) {
                Toast.makeText(SelectedPostActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
