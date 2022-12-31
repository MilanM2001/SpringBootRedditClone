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
import sr57.ftn.redditclone.activities.SelectedReportCommentActivity;
import sr57.ftn.redditclone.model.Report;

public class ReportCommentListAdapter extends RecyclerView.Adapter<ReportCommentListAdapter.ViewHolder> {

    private final List<Report> reportListComment;
    private final Context context;

    public ReportCommentListAdapter(List<Report> reportList, Context context) {
        this.reportListComment = reportList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView report_reason;
        TextView reportedComment_Text;

        ViewHolder(View itemView) {
            super(itemView);

            report_reason = itemView.findViewById(R.id.reportCommentListItem_ReportReason);
            reportedComment_Text = itemView.findViewById(R.id.reportCommentListItem_ReportedCommentText);
        }
    }

    @NonNull
    @Override
    public ReportCommentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_report_item, parent, false);
        return new ReportCommentListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportCommentListAdapter.ViewHolder holder, int position) {
        Report report = reportListComment.get(position);

        holder.report_reason.setText(report.getReport_reason());
        holder.reportedComment_Text.setText(report.getComment().getText());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SelectedReportCommentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID", report.getReport_id());
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reportListComment.size();
    }

}
