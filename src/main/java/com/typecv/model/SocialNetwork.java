package com.typecv.model;

/**
 * Social network link for CV header.
 */
public record SocialNetwork(
    String network,
    String username
) {
    
    /**
     * Get the URL for this social network.
     */
    public String getUrl() {
        return switch (network.toLowerCase()) {
            case "linkedin" -> "https://linkedin.com/in/" + username;
            case "github" -> "https://github.com/" + username;
            case "twitter", "x" -> "https://twitter.com/" + username;
            case "gitlab" -> "https://gitlab.com/" + username;
            case "stackoverflow" -> "https://stackoverflow.com/users/" + username;
            case "mastodon" -> username; // Full URL expected
            case "orcid" -> "https://orcid.org/" + username;
            case "researchgate" -> "https://researchgate.net/profile/" + username;
            case "youtube" -> "https://youtube.com/@" + username;
            case "instagram" -> "https://instagram.com/" + username;
            default -> username;
        };
    }
}
