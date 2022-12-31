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

import com.google.android.material.navigation.NavigationView;

import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sr57.ftn.redditclone.MainActivity;
import sr57.ftn.redditclone.R;
import sr57.ftn.redditclone.enums.ReactionType;
import sr57.ftn.redditclone.model.Comment;
import sr57.ftn.redditclone.model.DTO.CommentReactionAndroidDTO;
import sr57.ftn.redditclone.model.Reaction;
import sr57.ftn.redditclone.service.CommentService;
import sr57.ftn.redditclone.service.ReactionService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class SelectedCommentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int id;
    int id2;

    CommentService commentService;
    ReactionService reactionService;
    DrawerLayout drawer;

    Button buttonEdit;
    Button buttonDelete;
    Button buttonAddReport;

    TextView commentCommenter;
    TextView commentText;
    TextView commentKarma;

    ImageView upvoteImage;
    ImageView downvoteImage;

    Set<Reaction> reactionsOnComment;
    String commentOwnerUsername;
    String loggedInUserUsername;

    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_comment_activity);
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null)
            id = bundle1.getInt("ID");

        commentCommenter = findViewById(R.id.selectedCommentCommenter);
        commentText = findViewById(R.id.selectedCommentText);
        commentKarma = findViewById(R.id.selectedCommentKarma);

        buttonEdit = findViewById(R.id.selectedCommentEditButton);
        buttonEdit.setOnClickListener(view -> {
            Intent intent = new Intent(SelectedCommentActivity.this, EditCommentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID2", id2);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        buttonDelete = findViewById(R.id.selectedCommentDeleteButton);
        buttonDelete.setOnClickListener(view -> deleteSelectedComment(id));

        buttonAddReport = findViewById(R.id.selectedCommentAddReportButton);
        buttonAddReport.setOnClickListener(view -> {
            Intent intent = new Intent(SelectedCommentActivity.this, AddReportCommentActivity.class);
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

        upvoteImage = findViewById(R.id.upvoteComment);
        downvoteImage = findViewById(R.id.downvoteComment);

        upvoteImage.setOnClickListener(view -> upvoteReaction(id));

        downvoteImage.setOnClickListener(view -> downvoteReaction(id));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(SelectedCommentActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(SelectedCommentActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(SelectedCommentActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(SelectedCommentActivity.this, MainActivity.class);
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
        getSelectedComment();
    }

    private void getSelectedComment() {
        commentService = RetrofitClient.commentService;
        Call<Comment> call = commentService.getSelectedCommentById(id);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                assert response.body() != null;
                commentCommenter.setText(response.body().getUser().getDisplay_name() + " says:");
                commentText.setText(response.body().getText());
                id2 = response.body().getComment_id();
                commentOwnerUsername = response.body().getUser().getUsername();
                reactionsOnComment = response.body().getReactions();

                commentKarma.setText(String.valueOf(calculateKarma()));

                sharedPreferences = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);
                loggedInUserUsername = sharedPreferences.getString(MainActivity.Username, "");

                linearLayout = findViewById(R.id.commentOwnerOptions);
                relativeLayout = findViewById(R.id.commentVoteOptions);

                for (Reaction reaction : reactionsOnComment) {
                    if (reaction.getUser().getUsername().equals(loggedInUserUsername)) {
                        upvoteImage.setOnClickListener(view -> Toast.makeText(SelectedCommentActivity.this, "You have already reacted to this Post", Toast.LENGTH_SHORT).show());
                        downvoteImage.setOnClickListener(view -> Toast.makeText(SelectedCommentActivity.this, "You have already reacted to this Post", Toast.LENGTH_SHORT).show());
                    }
                }

                if (!(loggedInUserUsername.equals(commentOwnerUsername))) {
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                Toast.makeText(SelectedCommentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Integer calculateKarma() {
        Integer karma = 0;
        for (Reaction reaction : reactionsOnComment) {
            if (reaction.getReaction_type().equals(ReactionType.UPVOTE)) {
                karma++;
            } else if (reaction.getReaction_type().equals(ReactionType.DOWNVOTE)) {
                karma--;
            }
        }
        return karma;
    }

    private void deleteSelectedComment(Integer id) {
        commentService = RetrofitClient.commentService;
        Call<Void> call = commentService.deleteComment(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                System.out.println("Deleted");
                finish();
                SelectedCommentActivity.super.onRestart();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                System.out.println("Not Deleted");
            }
        });
    }

    private void upvoteReaction(Integer comment_id) {
        CommentReactionAndroidDTO commentReactionAndroidDTO = new CommentReactionAndroidDTO(comment_id);

        reactionService = RetrofitClient.reactionService;
        Call<CommentReactionAndroidDTO> call = reactionService.upvoteCommentReaction(commentReactionAndroidDTO);
        call.enqueue(new Callback<CommentReactionAndroidDTO>() {
            @Override
            public void onResponse(@NonNull Call<CommentReactionAndroidDTO> call, @NonNull Response<CommentReactionAndroidDTO> response) {
                CommentReactionAndroidDTO createReaction = response.body();

                assert createReaction != null;
                Integer comment_id = createReaction.getComment_id();

                commentReactionAndroidDTO.setComment_id(comment_id);

                recreate();
                Toast.makeText(SelectedCommentActivity.this, "You have upvoted this post", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<CommentReactionAndroidDTO> call, @NonNull Throwable t) {

            }
        });
    }

    private void downvoteReaction(Integer comment_id) {
        CommentReactionAndroidDTO commentReactionAndroidDTO = new CommentReactionAndroidDTO(comment_id);

        reactionService = RetrofitClient.reactionService;
        Call<CommentReactionAndroidDTO> call = reactionService.downvoteCommentReaction(commentReactionAndroidDTO);
        call.enqueue(new Callback<CommentReactionAndroidDTO>() {
            @Override
            public void onResponse(@NonNull Call<CommentReactionAndroidDTO> call, @NonNull Response<CommentReactionAndroidDTO> response) {
                CommentReactionAndroidDTO createReaction = response.body();

                assert createReaction != null;
                Integer comment_id = createReaction.getComment_id();

                commentReactionAndroidDTO.setComment_id(comment_id);

                recreate();
                Toast.makeText(SelectedCommentActivity.this, "You have upvoted this post", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<CommentReactionAndroidDTO> call, @NonNull Throwable t) {

            }
        });
    }
}
