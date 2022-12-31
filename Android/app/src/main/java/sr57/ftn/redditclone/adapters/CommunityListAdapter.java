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
import sr57.ftn.redditclone.activities.SelectedCommunityActivity;
import sr57.ftn.redditclone.model.Community;

public class CommunityListAdapter extends RecyclerView.Adapter<CommunityListAdapter.ViewHolder> {

    private final List<Community> communityList;
    private final Context context;

    public CommunityListAdapter(List<Community> communityList, Context context) {
        this.communityList = communityList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.communityListItem_Name);
            description = itemView.findViewById(R.id.communityListItem_Description);
        }
    }

    @NonNull
    @Override
    public CommunityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_community_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommunityListAdapter.ViewHolder holder, int position) {
        Community community = communityList.get(position);

        holder.name.setText(community.getName());
        holder.description.setText(community.getDescription());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SelectedCommunityActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID", community.getCommunity_id());
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }
}
