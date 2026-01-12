package com.typecv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Experience entry for CV sections like "experience".
 */
public record ExperienceEntry(
    String company,
    String position,
    String date,
    @JsonProperty("start_date") String startDate,
    @JsonProperty("end_date") String endDate,
    String location,
    String summary,
    List<String> highlights
) implements Entry {
    
    /**
     * Get formatted date string for display.
     */
    public String getFormattedDate() {
        if (date != null && !date.isEmpty()) {
            return date;
        }
        if (startDate != null && endDate != null) {
            return startDate + " -- " + endDate;
        }
        return "";
    }
}
