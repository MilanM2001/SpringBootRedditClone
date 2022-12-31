package sr57.ftn.redditclone.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import sr57.ftn.redditclone.model.DTO.CommentReactionAndroidDTO;
import sr57.ftn.redditclone.model.DTO.PostReactionAndroidDTO;

public interface ReactionService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })


    @POST("api/reactions/upvotePostAndroid")
    Call<PostReactionAndroidDTO> upvotePostReaction(@Body PostReactionAndroidDTO postReactionAndroidDTO);

    @POST("api/reactions/downvotePostAndroid")
    Call<PostReactionAndroidDTO> downvotePostReaction(@Body PostReactionAndroidDTO postReactionAndroidDTO);

    @POST("api/reactions/upvoteCommentAndroid")
    Call<CommentReactionAndroidDTO> upvoteCommentReaction(@Body CommentReactionAndroidDTO commentReactionAndroidDTO);

    @POST("api/reactions/downvoteCommentAndroid")
    Call<CommentReactionAndroidDTO> downvoteCommentReaction(@Body CommentReactionAndroidDTO commentReactionAndroidDTO);
}
