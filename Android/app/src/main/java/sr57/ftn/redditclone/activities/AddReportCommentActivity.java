package sr57.ftn.redditclone.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import sr57.ftn.redditclone.enums.ReportReason;
import sr57.ftn.redditclone.model.DTO.AddReportDTO;
import sr57.ftn.redditclone.service.CommentService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class AddReportCommentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    CommentService commentService;

    int id;

    Button addReportCommentButton;
    Button addReportCommentButtonGoBack;
    Spinner report_reason;

    DrawerLayout drawer;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.add_report_comment_activity);

        sharedPreferences = getSharedPreferences(MainActivity.MyPres, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            id = bundle.getInt("ID2");

        report_reason = (Spinner) findViewById(R.id.addReportCommentReportReason);
        report_reason.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ReportReason.values()));

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

        addReportCommentButton = findViewById(R.id.buttonAddReportComment);
        addReportCommentButton.setOnClickListener(view -> addReport());

        addReportCommentButtonGoBack = findViewById(R.id.buttonAddReportCommentGoBack);
        addReportCommentButtonGoBack.setOnClickListener(view -> {
            Intent intent = new Intent(AddReportCommentActivity.this, ListPostActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(AddReportCommentActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(AddReportCommentActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(AddReportCommentActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(AddReportCommentActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addReport() {
        final AddReportDTO addReportDTO = new AddReportDTO();
        String postReport = report_reason.getSelectedItem().toString();

        addReportDTO.setReport_reason(postReport);

        commentService = RetrofitClient.commentService;
        Call<AddReportDTO> call = commentService.addReportComment(id, addReportDTO);
        call.enqueue(new Callback<AddReportDTO>() {
            @Override
            public void onResponse(@NonNull Call<AddReportDTO> call, @NonNull Response<AddReportDTO> response) {
                Toast.makeText(AddReportCommentActivity.this, "Report Added Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<AddReportDTO> call, @NonNull Throwable t) {
                Toast.makeText(AddReportCommentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
