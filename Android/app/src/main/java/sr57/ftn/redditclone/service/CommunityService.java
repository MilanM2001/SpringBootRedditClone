package sr57.ftn.redditclone.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sr57.ftn.redditclone.model.Community;
import sr57.ftn.redditclone.model.DTO.AddCommunityDTO;
import sr57.ftn.redditclone.model.DTO.AddPostDTO;
import sr57.ftn.redditclone.model.Post;

public interface CommunityService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("api/communities/all")
    Call<List<Community>> getAll();

    @GET("api/communities/{community_id}")
    Call<Community> getSelectedCommunityById(@Path("community_id") int community_id);

    @GET("api/communities/{community_id}/posts")
    Call<List<Post>> getSelectedCommunityPosts(@Path("community_id") int community_id);

    @POST("api/communities/addCommunityAndroid")
    Call<AddCommunityDTO> addCommunityAndroid(@Body AddCommunityDTO addCommunityDTO);

    @POST("api/communities/{community_id}/addPost")
    Call<AddPostDTO> addPost(@Path("community_id") int community_id, @Body AddPostDTO addPostDTO);

    @PUT("api/communities/updateCommunity/{community_id}")
    Call<Community> updateCommunity(@Body Community community, @Path("community_id") int community_id);

    @PUT("api/communities/suspendCommunity/{community_id}")
    Call<Community> suspendCommunity(@Body Community community, @Path("community_id") int community_id);
}
