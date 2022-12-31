package sr57.ftn.redditclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sr57.ftn.redditclone.R;
import sr57.ftn.redditclone.activities.SelectedReportPostActivity;
import sr57.ftn.redditclone.model.Report;

public class ReportPostListAdapter extends RecyclerView.Adapter<ReportPostListAdapter.ViewHolder> {

    private final List<Report> reportListPost;
    private final Context context;

    public ReportPostListAdapter(List<Report> reportList, Context context) {
        this.reportListPost = reportList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView report_reason;
        TextView reportedPost_Title;

        ViewHolder(View itemView) {
            super(itemView);

            report_reason = itemView.findViewById(R.id.reportPostListItem_ReportReason);
            reportedPost_Title = itemView.findViewById(R.id.reportPostListItem_ReportedPostTitle);
        }
    }

    @NonNull
    @Override
    public ReportPostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_report_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportPostListAdapter.ViewHolder holder, int position) {
        Report report = reportListPost.get(position);

        holder.report_reason.setText(report.getReport_reason());
        holder.reportedPost_Title.setText(report.getPost().getTitle());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SelectedReportPostActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID", report.getReport_id());
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reportListPost.size();
    }
}
