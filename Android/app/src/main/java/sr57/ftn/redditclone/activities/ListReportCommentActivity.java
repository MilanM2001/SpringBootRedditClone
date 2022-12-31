package sr57.ftn.redditclone.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
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
import sr57.ftn.redditclone.adapters.ReportCommentListAdapter;
import sr57.ftn.redditclone.model.Report;
import sr57.ftn.redditclone.service.ReportService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class ListReportCommentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ReportService reportService;
    RecyclerView recyclerView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_report_comment_activity);

        recyclerView = findViewById(R.id.reportCommentList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_main_page);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(ListReportCommentActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(ListReportCommentActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(ListReportCommentActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(ListReportCommentActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getAll() {
        reportService = RetrofitClient.reportService;
        reportService.getAllReportedComments()
                .enqueue(new Callback<List<Report>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Report>> call, @NonNull Response<List<Report>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Report>> call, @NonNull Throwable t) {
                        Toast.makeText(ListReportCommentActivity.this, "Failed to load Reports", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Report> reportList) {
        ReportCommentListAdapter reportListCommentAdapter = new ReportCommentListAdapter(reportList, getApplicationContext());
        recyclerView.setAdapter(reportListCommentAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAll();
    }
}
