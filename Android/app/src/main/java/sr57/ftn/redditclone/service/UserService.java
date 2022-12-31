package sr57.ftn.redditclone.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import sr57.ftn.redditclone.model.DTO.EditPasswordDTO;
import sr57.ftn.redditclone.model.DTO.Login;
import sr57.ftn.redditclone.model.User;

public interface UserService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @POST("api/users/loginAndroid")
    Call<User> loginAndroid(@Body Login login);

    @POST("api/users/signup")
    Call<Boolean> signup(@Body User user);

    @GET("api/users/myInfo")
    Call<User> getMyInfo();

    @PUT("api/users/updateUserAndroid")
    Call<User> updateInfo(@Body User user);

    @POST("api/users/updatePasswordAndroid")
    Call<Boolean> editPassword(@Body EditPasswordDTO editPasswordDTO);

}