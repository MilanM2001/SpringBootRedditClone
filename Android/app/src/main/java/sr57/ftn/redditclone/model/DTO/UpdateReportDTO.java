package sr57.ftn.redditclone.model.DTO;

import com.google.gson.annotations.SerializedName;

public class UpdateReportDTO {
    @SerializedName("report_status")
    String report_status;

    public String getReport_status() {
        return report_status;
    }

    public void setReport_status(String report_status) {
        this.report_status = report_status;
    }
}
