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
import android.widget.LinearLayout;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sr57.ftn.redditclone.MainActivity;
import sr57.ftn.redditclone.R;
import sr57.ftn.redditclone.adapters.PostListAdapter;
import sr57.ftn.redditclone.model.Community;
import sr57.ftn.redditclone.model.Post;
import sr57.ftn.redditclone.service.CommunityService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class SelectedCommunityActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    int id;
    int id2;
    CommunityService communityService;
    DrawerLayout drawer;

    Button buttonEdit;
    Button buttonSuspend;
    Button buttonAddPost;
    TextView communityName;
    TextView communityDescription;
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_community_activity);
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null)
            id = bundle1.getInt("ID");

        communityName = findViewById(R.id.selectedCommunityName);
        communityDescription = findViewById(R.id.selectedCommunityDescription);

        recyclerView = findViewById(R.id.rvPostsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonEdit = findViewById(R.id.selectedCommunityEditButton);
        buttonEdit.setOnClickListener(view -> {
            Intent intent = new Intent(SelectedCommunityActivity.this, EditCommunityActivity.class);
            Bundle bundle2 = new Bundle();
            bundle2.putInt("ID2", id2);
            intent.putExtras(bundle2);
            startActivity(intent);
        });

        buttonSuspend = findViewById(R.id.selectedCommunitySuspendButton);
        buttonSuspend.setOnClickListener(view -> {
            Intent intent = new Intent(SelectedCommunityActivity.this, SuspendCommunityActivity.class);
            Bundle bundle3 = new Bundle();
            bundle3.putInt("ID2", id2);
            intent.putExtras(bundle3);
            startActivity(intent);
        });

        buttonAddPost = findViewById(R.id.selectedCommunityAddPostButton);
        buttonAddPost.setOnClickListener(view -> {
            Intent intent = new Intent(SelectedCommunityActivity.this, AddPostActivity.class);
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

        sharedPreferences = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);
        String loggedInUserRole = sharedPreferences.getString(MainActivity.Role, "");
        linearLayout = findViewById(R.id.communityOwnerOptions);

        if (loggedInUserRole.equals("USER")) {
            linearLayout.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(SelectedCommunityActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(SelectedCommunityActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(SelectedCommunityActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(SelectedCommunityActivity.this, MainActivity.class);
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
        getSelectedCommunity();
        getPosts();
    }

    private void getSelectedCommunity() {
        communityService = RetrofitClient.communityService;
        Call<Community> call = communityService.getSelectedCommunityById(id);

        call.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(@NonNull Call<Community> call, @NonNull Response<Community> response) {
                assert response.body() != null;
                communityName.setText(response.body().getName());
                communityDescription.setText(response.body().getDescription());
                id2 = response.body().getCommunity_id();
            }

            @Override
            public void onFailure(@NonNull Call<Community> call, @NonNull Throwable t) {
                Toast.makeText(SelectedCommunityActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPosts() {
        communityService = RetrofitClient.communityService;
        Call<List<Post>> call = communityService.getSelectedCommunityPosts(id);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                recyclerView.setAdapter(new PostListAdapter(response.body(), getApplicationContext()));
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Toast.makeText(SelectedCommunityActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
