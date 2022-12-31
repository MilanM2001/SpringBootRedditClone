package sr57.ftn.redditclone.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import sr57.ftn.redditclone.model.Report;

public interface ReportService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("api/reports/{report_id}")
    Call<Report> getSelectedReportById(@Path("report_id") int report_id);

    @GET("api/reports/allReportedPost")
    Call<List<Report>> getAllReportedPosts();

    @GET("api/reports/allReportedComment")
    Call<List<Report>> getAllReportedComments();

    @PUT("api/reports/updateReport/{report_id}")
    Call<Report> updateReport(@Body Report report, @Path("report_id") int report_id);

    @DELETE("api/reports/{report_id}")
    Call<Void> deleteReport(@Path("report_id") Integer report_id);
}
