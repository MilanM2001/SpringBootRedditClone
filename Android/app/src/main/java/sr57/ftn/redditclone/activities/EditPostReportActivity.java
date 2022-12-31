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
import sr57.ftn.redditclone.enums.ReportStatus;
import sr57.ftn.redditclone.model.Report;
import sr57.ftn.redditclone.service.ReportService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class EditPostReportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ReportService reportService;
    Report report = new Report();

    Button buttonEditReportPost;
    Button buttonEditReportPostGoBack;
    Spinner report_status;

    int id;

    DrawerLayout drawer;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.edit_post_report_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            id = bundle.getInt("ID2");

        report_status = (Spinner) findViewById(R.id.editReportPostReportStatus);
        report_status.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ReportStatus.values()));

        reportService = RetrofitClient.reportService;
        Call<Report> call = reportService.getSelectedReportById(id);
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(@NonNull Call<Report> call, @NonNull Response<Report> response) {
                report = response.body();

            }

            @Override
            public void onFailure(@NonNull Call<Report> call, @NonNull Throwable t) {

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

        buttonEditReportPostGoBack = findViewById(R.id.buttonEditReportPostGoBack);
        buttonEditReportPostGoBack.setOnClickListener(view -> {
            finish();
            EditPostReportActivity.super.onRestart();
        });

        buttonEditReportPost = findViewById(R.id.buttonEditReportPost);
        buttonEditReportPost.setOnClickListener(view -> updateReport());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(EditPostReportActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(EditPostReportActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(EditPostReportActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(EditPostReportActivity.this, MainActivity.class);
                            startActivity(intent);
                        }).create().show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateReport() {
        reportService = RetrofitClient.reportService;
        Call<Report> call = reportService.updateReport(report, report.getReport_id());

        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(@NonNull Call<Report> call, @NonNull Response<Report> response) {
                String report_statusEdit = report_status.getSelectedItem().toString();
                report.setReport_status(report_statusEdit);
            }

            @Override
            public void onFailure(@NonNull Call<Report> call, @NonNull Throwable t) {
                Toast.makeText(EditPostReportActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
