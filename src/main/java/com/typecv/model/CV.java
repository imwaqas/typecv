package com.typecv.model;

/**
 * Root CV model containing all CV data, design, and locale settings.
 */
public record CV(
    CvData cv,
    Design design,
    Locale locale
) {
    
    /**
     * Get the CV with default design and locale if not specified.
     */
    public CV withDefaults() {
        return new CV(
            cv,
            design != null ? design : Design.defaults(),
            locale != null ? locale : Locale.defaults()
        );
    }
}
