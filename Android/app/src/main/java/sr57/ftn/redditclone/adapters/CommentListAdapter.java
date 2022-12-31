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
import java.util.Set;

import sr57.ftn.redditclone.R;
import sr57.ftn.redditclone.activities.SelectedCommentActivity;
import sr57.ftn.redditclone.model.Comment;
import sr57.ftn.redditclone.model.Report;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private final List<Comment> commentList;
    private final Context context;

    public CommentListAdapter(List<Comment> data, Context context) {
        this.commentList = data;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView commenter;
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            commenter = itemView.findViewById(R.id.commenterComment);
            text = itemView.findViewById(R.id.textComment);
        }
    }

    @NonNull
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentListAdapter.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.commenter.setText(comment.getUser().getDisplay_name() + " says:");
        holder.text.setText(comment.getText());

        boolean isAccepted = false;
        Set<Report> reportsOnPost = comment.getReports();
        for (Report report : reportsOnPost) {
            isAccepted = report.getAccepted();
        }

        if (isAccepted) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SelectedCommentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID", comment.getComment_id());
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
