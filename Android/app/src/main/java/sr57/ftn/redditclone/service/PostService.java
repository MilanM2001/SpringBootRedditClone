package sr57.ftn.redditclone.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sr57.ftn.redditclone.model.Comment;
import sr57.ftn.redditclone.model.DTO.AddCommentDTO;
import sr57.ftn.redditclone.model.DTO.AddReportDTO;
import sr57.ftn.redditclone.model.Post;

public interface PostService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("api/posts/all")
    Call<List<Post>> getAll();

    @GET("api/posts/{post_id}")
    Call<Post> getSelectedPostById(@Path("post_id") int post_id);

    @GET("api/posts/{post_id}/comments")
    Call<List<Comment>> getSelectedPostComments(@Path("post_id") int post_id);

    @POST("api/posts/{post_id}/addComment")
    Call<AddCommentDTO> addComment(@Path("post_id") int post_id, @Body AddCommentDTO addCommentDTO);

    @POST("api/posts/{post_id}/addReport")
    Call<AddReportDTO> addReportPost(@Path("post_id") int post_id, @Body AddReportDTO addReportDTO);

    @PUT("api/posts/updatePost/{post_id}")
    Call<Post> updatePost(@Body Post post, @Path("post_id") int post_id);

    @DELETE("api/posts/{post_id}")
    Call<Void> deletePost(@Path("post_id") Integer post_id);
}
