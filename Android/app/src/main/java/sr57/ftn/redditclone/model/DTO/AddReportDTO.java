package sr57.ftn.redditclone.model.DTO;

import com.google.gson.annotations.SerializedName;

public class AddReportDTO {
    @SerializedName("report_reason")
    String report_reason;

    public String getReport_reason() {
        return report_reason;
    }

    public void setReport_reason(String report_reason) {
        this.report_reason = report_reason;
    }
}
