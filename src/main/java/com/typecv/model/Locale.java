package com.typecv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Locale settings for the CV.
 * For MVP, we only support English.
 */
public record Locale(
    String language,
    @JsonProperty("last_updated") String lastUpdated,
    String month,
    String months,
    String year,
    String years,
    String present,
    @JsonProperty("month_abbreviations") List<String> monthAbbreviations,
    @JsonProperty("month_names") List<String> monthNames
) {
    
    /**
     * Create default English locale.
     */
    public static Locale defaults() {
        return new Locale(
            "english",
            "Last updated in",
            "month",
            "months",
            "year",
            "years",
            "present",
            List.of("Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"),
            List.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        );
    }
}
