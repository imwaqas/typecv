package com.typecv.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Design configuration for the CV.
 * For MVP, we only support the "classic" theme with defaults.
 */
public record Design(
    String theme,
    Page page,
    Colors colors,
    Typography typography
) {
    
    /**
     * Create default design settings.
     */
    public static Design defaults() {
        return new Design(
            "classic",
            Page.defaults(),
            Colors.defaults(),
            Typography.defaults()
        );
    }
    
    /**
     * Page settings.
     */
    public record Page(
        String size,
        @JsonProperty("top_margin") String topMargin,
        @JsonProperty("bottom_margin") String bottomMargin,
        @JsonProperty("left_margin") String leftMargin,
        @JsonProperty("right_margin") String rightMargin
    ) {
        public static Page defaults() {
            return new Page("us-letter", "0.7in", "0.7in", "0.7in", "0.7in");
        }
    }
    
    /**
     * Color settings.
     */
    public record Colors(
        String body,
        String name,
        String headline,
        String connections,
        @JsonProperty("section_titles") String sectionTitles,
        String links
    ) {
        public static Colors defaults() {
            return new Colors(
                "rgb(0, 0, 0)",
                "rgb(0, 79, 144)",
                "rgb(0, 79, 144)",
                "rgb(0, 79, 144)",
                "rgb(0, 79, 144)",
                "rgb(0, 79, 144)"
            );
        }
    }
    
    /**
     * Typography settings.
     */
    public record Typography(
        @JsonProperty("font_family") String fontFamily,
        @JsonProperty("font_size") String fontSize
    ) {
        public static Typography defaults() {
            return new Typography("Source Sans 3", "10pt");
        }
    }
}
