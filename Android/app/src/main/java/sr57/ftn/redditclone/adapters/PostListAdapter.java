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
import sr57.ftn.redditclone.activities.SelectedPostActivity;
import sr57.ftn.redditclone.model.Post;
import sr57.ftn.redditclone.model.Report;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private final List<Post> postList;
    private final Context context;

    public PostListAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.postListItem_Title);
            text = itemView.findViewById(R.id.postListItem_Text);
        }
    }

    @NonNull
    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostListAdapter.ViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.title.setText(post.getTitle());
        holder.text.setText(post.getText());

        boolean isAccepted = false;
        Set<Report> reportsOnPost = post.getReports();
        for (Report report : reportsOnPost) {
            isAccepted = report.getAccepted();
        }

        if (isAccepted) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SelectedPostActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID", post.getPost_id());
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
