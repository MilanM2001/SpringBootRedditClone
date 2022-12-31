package sr57.ftn.redditclone.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
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
import sr57.ftn.redditclone.model.Report;
import sr57.ftn.redditclone.service.ReportService;
import sr57.ftn.redditclone.service.RetrofitClient;

public class SelectedReportCommentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int id;
    int id2;

    ReportService reportService;
    DrawerLayout drawer;

    Button buttonEdit;

    TextView reportReportReason;
    TextView reportCommentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_comment_report_activity);
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null)
            id = bundle1.getInt("ID");

        reportReportReason = findViewById(R.id.selectedCommentReportReportReason);
        reportCommentText = findViewById(R.id.selectedCommentReportCommentText);

        buttonEdit = findViewById(R.id.selectedCommentReportEditButton);
        buttonEdit.setOnClickListener(view -> {
            Intent intent = new Intent(SelectedReportCommentActivity.this, EditCommentReportActivity.class);
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
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                Intent intentMain = new Intent(SelectedReportCommentActivity.this, ListPostActivity.class);
                startActivity(intentMain);
                finish();
                break;
            case R.id.nav_communities:
                Intent intentCommunities = new Intent(SelectedReportCommentActivity.this, ListCommunityActivity.class);
                startActivity(intentCommunities);
                finish();
                break;
            case R.id.nav_my_profile:
                Intent intentProfile = new Intent(SelectedReportCommentActivity.this, MyProfileActivity.class);
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
                            Intent intent = new Intent(SelectedReportCommentActivity.this, MainActivity.class);
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
        getSelectedReport();
    }

    private void getSelectedReport() {
        reportService = RetrofitClient.reportService;
        Call<Report> call = reportService.getSelectedReportById(id);

        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(@NonNull Call<Report> call, @NonNull Response<Report> response) {
                assert response.body() != null;
                reportReportReason.setText(response.body().getReport_reason());
                reportCommentText.setText(response.body().getComment().getText());
                id2 = response.body().getReport_id();
            }

            @Override
            public void onFailure(@NonNull Call<Report> call, @NonNull Throwable t) {
                Toast.makeText(SelectedReportCommentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
