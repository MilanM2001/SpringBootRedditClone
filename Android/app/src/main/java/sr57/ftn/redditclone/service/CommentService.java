package sr57.ftn.redditclone.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sr57.ftn.redditclone.model.Comment;
import sr57.ftn.redditclone.model.DTO.AddReportDTO;

public interface CommentService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("api/comments/{comment_id}")
    Call<Comment> getSelectedCommentById(@Path("comment_id") int comment_id);

    @PUT("api/comments/updateComment/{comment_id}")
    Call<Comment> updateComment(@Body Comment comment, @Path("comment_id") int comment_id);

    @POST("api/comments/{comment_id}/addReport")
    Call<AddReportDTO> addReportComment(@Path("comment_id") int comment_id, @Body AddReportDTO addReportDTO);

    @DELETE("api/comments/{comment_id}")
    Call<Void> deleteComment(@Path("comment_id") Integer comment_id);
}
