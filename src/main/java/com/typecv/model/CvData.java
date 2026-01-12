package com.typecv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * CV data section containing personal info and all CV sections.
 */
public record CvData(
    String name,
    String headline,
    String location,
    String email,
    String photo,
    String phone,
    String website,
    @JsonProperty("social_networks") List<SocialNetwork> socialNetworks,
    Map<String, List<Object>> sections
) {
    
    /**
     * Get the name formatted for filenames (spaces replaced with underscores).
     */
    public String getFileNameBase() {
        return name.replace(" ", "_");
    }
}
