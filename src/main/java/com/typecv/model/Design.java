package com.typecv.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Design configuration for the CV supporting multiple themes.
 */
public record Design(
    String theme,
    Page page,
    Colors colors,
    Typography typography,
    Links links,
    Header header,
    @JsonProperty("section_titles") SectionTitles sectionTitles,
    Sections sections,
    Entries entries
) {
    
    /**
     * Create default design settings for the classic theme.
     */
    public static Design defaults() {
        return new Design(
            "classic",
            Page.defaults(),
            Colors.defaults(),
            Typography.defaults(),
            Links.defaults(),
            Header.defaults(),
            SectionTitles.defaults(),
            Sections.defaults(),
            Entries.defaults()
        );
    }
    
    /**
     * Merge this design with another, using values from 'other' where non-null.
     */
    public Design mergeWith(Design other) {
        if (other == null) return this;
        return new Design(
            other.theme() != null ? other.theme() : this.theme(),
            this.page() != null ? this.page().mergeWith(other.page()) : other.page(),
            this.colors() != null ? this.colors().mergeWith(other.colors()) : other.colors(),
            this.typography() != null ? this.typography().mergeWith(other.typography()) : other.typography(),
            this.links() != null ? this.links().mergeWith(other.links()) : other.links(),
            this.header() != null ? this.header().mergeWith(other.header()) : other.header(),
            this.sectionTitles() != null ? this.sectionTitles().mergeWith(other.sectionTitles()) : other.sectionTitles(),
            this.sections() != null ? this.sections().mergeWith(other.sections()) : other.sections(),
            this.entries() != null ? this.entries().mergeWith(other.entries()) : other.entries()
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
        @JsonProperty("right_margin") String rightMargin,
        @JsonProperty("show_footer") Boolean showFooter,
        @JsonProperty("show_top_note") Boolean showTopNote
    ) {
        public static Page defaults() {
            return new Page("us-letter", "0.7in", "0.7in", "0.7in", "0.7in", true, false);
        }
        
        public Page mergeWith(Page other) {
            if (other == null) return this;
            return new Page(
                other.size() != null ? other.size() : this.size(),
                other.topMargin() != null ? other.topMargin() : this.topMargin(),
                other.bottomMargin() != null ? other.bottomMargin() : this.bottomMargin(),
                other.leftMargin() != null ? other.leftMargin() : this.leftMargin(),
                other.rightMargin() != null ? other.rightMargin() : this.rightMargin(),
                other.showFooter() != null ? other.showFooter() : this.showFooter(),
                other.showTopNote() != null ? other.showTopNote() : this.showTopNote()
            );
        }
    }
    
    /**
     * Color settings for various CV elements.
     */
    public record Colors(
        String body,
        String name,
        String headline,
        String connections,
        @JsonProperty("section_titles") String sectionTitles,
        String links,
        String footer,
        @JsonProperty("top_note") String topNote
    ) {
        public static Colors defaults() {
            return new Colors(
                "rgb(0, 0, 0)",      // body
                "rgb(0, 79, 144)",   // name (blue)
                "rgb(0, 79, 144)",   // headline
                "rgb(0, 79, 144)",   // connections
                "rgb(0, 79, 144)",   // sectionTitles
                "rgb(0, 79, 144)",   // links
                "rgb(128, 128, 128)", // footer
                "rgb(128, 128, 128)"  // topNote
            );
        }
        
        public Colors mergeWith(Colors other) {
            if (other == null) return this;
            return new Colors(
                other.body() != null ? other.body() : this.body(),
                other.name() != null ? other.name() : this.name(),
                other.headline() != null ? other.headline() : this.headline(),
                other.connections() != null ? other.connections() : this.connections(),
                other.sectionTitles() != null ? other.sectionTitles() : this.sectionTitles(),
                other.links() != null ? other.links() : this.links(),
                other.footer() != null ? other.footer() : this.footer(),
                other.topNote() != null ? other.topNote() : this.topNote()
            );
        }
    }
    
    /**
     * Typography settings.
     */
    public record Typography(
        @JsonProperty("font_family") FontFamily fontFamily,
        @JsonProperty("font_size") FontSize fontSize,
        @JsonProperty("line_spacing") String lineSpacing,
        String alignment,
        @JsonProperty("date_and_location_column_alignment") String dateAndLocationColumnAlignment,
        @JsonProperty("small_caps") SmallCaps smallCaps,
        Bold bold
    ) {
        public static Typography defaults() {
            return new Typography(
                FontFamily.defaults(),
                FontSize.defaults(),
                "0.6em",
                "justified",
                "right",
                SmallCaps.defaults(),
                Bold.defaults()
            );
        }
        
        public Typography mergeWith(Typography other) {
            if (other == null) return this;
            return new Typography(
                this.fontFamily() != null ? this.fontFamily().mergeWith(other.fontFamily()) : other.fontFamily(),
                this.fontSize() != null ? this.fontSize().mergeWith(other.fontSize()) : other.fontSize(),
                other.lineSpacing() != null ? other.lineSpacing() : this.lineSpacing(),
                other.alignment() != null ? other.alignment() : this.alignment(),
                other.dateAndLocationColumnAlignment() != null ? other.dateAndLocationColumnAlignment() : this.dateAndLocationColumnAlignment(),
                this.smallCaps() != null ? this.smallCaps().mergeWith(other.smallCaps()) : other.smallCaps(),
                this.bold() != null ? this.bold().mergeWith(other.bold()) : other.bold()
            );
        }
    }
    
    /**
     * Font family settings for different elements.
     */
    public record FontFamily(
        String body,
        String name,
        String headline,
        String connections,
        @JsonProperty("section_titles") String sectionTitles
    ) {
        public static FontFamily defaults() {
            return new FontFamily(
                "Arial",
                "Arial",
                "Arial",
                "Arial",
                "Arial"
            );
        }
        
        public FontFamily mergeWith(FontFamily other) {
            if (other == null) return this;
            return new FontFamily(
                other.body() != null ? other.body() : this.body(),
                other.name() != null ? other.name() : this.name(),
                other.headline() != null ? other.headline() : this.headline(),
                other.connections() != null ? other.connections() : this.connections(),
                other.sectionTitles() != null ? other.sectionTitles() : this.sectionTitles()
            );
        }
    }
    
    /**
     * Font size settings for different elements.
     */
    public record FontSize(
        String body,
        String name,
        String headline,
        String connections,
        @JsonProperty("section_titles") String sectionTitles
    ) {
        public static FontSize defaults() {
            return new FontSize("10pt", "30pt", "10pt", "10pt", "1.4em");
        }
        
        public FontSize mergeWith(FontSize other) {
            if (other == null) return this;
            return new FontSize(
                other.body() != null ? other.body() : this.body(),
                other.name() != null ? other.name() : this.name(),
                other.headline() != null ? other.headline() : this.headline(),
                other.connections() != null ? other.connections() : this.connections(),
                other.sectionTitles() != null ? other.sectionTitles() : this.sectionTitles()
            );
        }
    }
    
    /**
     * Small caps settings for different elements.
     */
    public record SmallCaps(
        Boolean name,
        Boolean headline,
        Boolean connections,
        @JsonProperty("section_titles") Boolean sectionTitles
    ) {
        public static SmallCaps defaults() {
            return new SmallCaps(false, false, false, false);
        }
        
        public SmallCaps mergeWith(SmallCaps other) {
            if (other == null) return this;
            return new SmallCaps(
                other.name() != null ? other.name() : this.name(),
                other.headline() != null ? other.headline() : this.headline(),
                other.connections() != null ? other.connections() : this.connections(),
                other.sectionTitles() != null ? other.sectionTitles() : this.sectionTitles()
            );
        }
    }
    
    /**
     * Bold settings for different elements.
     */
    public record Bold(
        Boolean name,
        Boolean headline,
        Boolean connections,
        @JsonProperty("section_titles") Boolean sectionTitles
    ) {
        public static Bold defaults() {
            return new Bold(true, false, false, true);
        }
        
        public Bold mergeWith(Bold other) {
            if (other == null) return this;
            return new Bold(
                other.name() != null ? other.name() : this.name(),
                other.headline() != null ? other.headline() : this.headline(),
                other.connections() != null ? other.connections() : this.connections(),
                other.sectionTitles() != null ? other.sectionTitles() : this.sectionTitles()
            );
        }
    }
    
    /**
     * Link styling settings.
     */
    public record Links(
        Boolean underline,
        @JsonProperty("show_external_link_icon") Boolean showExternalLinkIcon
    ) {
        public static Links defaults() {
            return new Links(false, false);
        }
        
        public Links mergeWith(Links other) {
            if (other == null) return this;
            return new Links(
                other.underline() != null ? other.underline() : this.underline(),
                other.showExternalLinkIcon() != null ? other.showExternalLinkIcon() : this.showExternalLinkIcon()
            );
        }
    }
    
    /**
     * Header layout settings.
     */
    public record Header(
        String alignment,
        @JsonProperty("photo_width") String photoWidth,
        @JsonProperty("photo_position") String photoPosition,
        @JsonProperty("space_below_name") String spaceBelowName,
        @JsonProperty("space_below_headline") String spaceBelowHeadline,
        @JsonProperty("space_below_connections") String spaceBelowConnections,
        Connections connections
    ) {
        public static Header defaults() {
            return new Header(
                "center",
                "3.5cm",
                "left",
                "0.7cm",
                "0.7cm",
                "0.7cm",
                Connections.defaults()
            );
        }
        
        public Header mergeWith(Header other) {
            if (other == null) return this;
            return new Header(
                other.alignment() != null ? other.alignment() : this.alignment(),
                other.photoWidth() != null ? other.photoWidth() : this.photoWidth(),
                other.photoPosition() != null ? other.photoPosition() : this.photoPosition(),
                other.spaceBelowName() != null ? other.spaceBelowName() : this.spaceBelowName(),
                other.spaceBelowHeadline() != null ? other.spaceBelowHeadline() : this.spaceBelowHeadline(),
                other.spaceBelowConnections() != null ? other.spaceBelowConnections() : this.spaceBelowConnections(),
                this.connections() != null ? this.connections().mergeWith(other.connections()) : other.connections()
            );
        }
    }
    
    /**
     * Connection (contact info) display settings.
     */
    public record Connections(
        @JsonProperty("phone_number_format") String phoneNumberFormat,
        Boolean hyperlink,
        @JsonProperty("show_icons") Boolean showIcons,
        @JsonProperty("display_urls_instead_of_usernames") Boolean displayUrlsInsteadOfUsernames,
        String separator,
        @JsonProperty("space_between_connections") String spaceBetweenConnections
    ) {
        public static Connections defaults() {
            return new Connections("national", true, true, false, "", "0.5cm");
        }
        
        public Connections mergeWith(Connections other) {
            if (other == null) return this;
            return new Connections(
                other.phoneNumberFormat() != null ? other.phoneNumberFormat() : this.phoneNumberFormat(),
                other.hyperlink() != null ? other.hyperlink() : this.hyperlink(),
                other.showIcons() != null ? other.showIcons() : this.showIcons(),
                other.displayUrlsInsteadOfUsernames() != null ? other.displayUrlsInsteadOfUsernames() : this.displayUrlsInsteadOfUsernames(),
                other.separator() != null ? other.separator() : this.separator(),
                other.spaceBetweenConnections() != null ? other.spaceBetweenConnections() : this.spaceBetweenConnections()
            );
        }
    }
    
    /**
     * Section title styling settings.
     */
    public record SectionTitles(
        String type,  // "with_partial_line", "with_full_line", "without_line", "moderncv"
        @JsonProperty("line_thickness") String lineThickness,
        @JsonProperty("space_above") String spaceAbove,
        @JsonProperty("space_below") String spaceBelow
    ) {
        public static SectionTitles defaults() {
            return new SectionTitles("with_partial_line", "0.5pt", "0.5cm", "0.3cm");
        }
        
        public SectionTitles mergeWith(SectionTitles other) {
            if (other == null) return this;
            return new SectionTitles(
                other.type() != null ? other.type() : this.type(),
                other.lineThickness() != null ? other.lineThickness() : this.lineThickness(),
                other.spaceAbove() != null ? other.spaceAbove() : this.spaceAbove(),
                other.spaceBelow() != null ? other.spaceBelow() : this.spaceBelow()
            );
        }
    }
    
    /**
     * Section layout settings.
     */
    public record Sections(
        @JsonProperty("allow_page_break") Boolean allowPageBreak,
        @JsonProperty("space_between_regular_entries") String spaceBetweenRegularEntries,
        @JsonProperty("space_between_text_based_entries") String spaceBetweenTextBasedEntries
    ) {
        public static Sections defaults() {
            return new Sections(true, "1.2em", "0.3em");
        }
        
        public Sections mergeWith(Sections other) {
            if (other == null) return this;
            return new Sections(
                other.allowPageBreak() != null ? other.allowPageBreak() : this.allowPageBreak(),
                other.spaceBetweenRegularEntries() != null ? other.spaceBetweenRegularEntries() : this.spaceBetweenRegularEntries(),
                other.spaceBetweenTextBasedEntries() != null ? other.spaceBetweenTextBasedEntries() : this.spaceBetweenTextBasedEntries()
            );
        }
    }
    
    /**
     * Entry layout settings.
     */
    public record Entries(
        @JsonProperty("date_and_location_width") String dateAndLocationWidth,
        @JsonProperty("side_space") String sideSpace,
        @JsonProperty("space_between_columns") String spaceBetweenColumns,
        @JsonProperty("allow_page_break") Boolean allowPageBreak,
        @JsonProperty("short_second_row") Boolean shortSecondRow,
        Summary summary,
        Highlights highlights
    ) {
        public static Entries defaults() {
            return new Entries(
                "4.15cm",
                "0.2cm",
                "0.1cm",
                false,
                true,
                Summary.defaults(),
                Highlights.defaults()
            );
        }
        
        public Entries mergeWith(Entries other) {
            if (other == null) return this;
            return new Entries(
                other.dateAndLocationWidth() != null ? other.dateAndLocationWidth() : this.dateAndLocationWidth(),
                other.sideSpace() != null ? other.sideSpace() : this.sideSpace(),
                other.spaceBetweenColumns() != null ? other.spaceBetweenColumns() : this.spaceBetweenColumns(),
                other.allowPageBreak() != null ? other.allowPageBreak() : this.allowPageBreak(),
                other.shortSecondRow() != null ? other.shortSecondRow() : this.shortSecondRow(),
                this.summary() != null ? this.summary().mergeWith(other.summary()) : other.summary(),
                this.highlights() != null ? this.highlights().mergeWith(other.highlights()) : other.highlights()
            );
        }
    }
    
    /**
     * Summary text styling.
     */
    public record Summary(
        @JsonProperty("space_above") String spaceAbove,
        @JsonProperty("space_left") String spaceLeft
    ) {
        public static Summary defaults() {
            return new Summary("0cm", "0cm");
        }
        
        public Summary mergeWith(Summary other) {
            if (other == null) return this;
            return new Summary(
                other.spaceAbove() != null ? other.spaceAbove() : this.spaceAbove(),
                other.spaceLeft() != null ? other.spaceLeft() : this.spaceLeft()
            );
        }
    }
    
    /**
     * Highlight bullet point styling.
     */
    public record Highlights(
        String bullet,
        @JsonProperty("nested_bullet") String nestedBullet,
        @JsonProperty("space_left") String spaceLeft,
        @JsonProperty("space_above") String spaceAbove,
        @JsonProperty("space_between_items") String spaceBetweenItems,
        @JsonProperty("space_between_bullet_and_text") String spaceBetweenBulletAndText
    ) {
        public static Highlights defaults() {
            return new Highlights("•", "•", "0.15cm", "0cm", "0cm", "0.5em");
        }
        
        public Highlights mergeWith(Highlights other) {
            if (other == null) return this;
            return new Highlights(
                other.bullet() != null ? other.bullet() : this.bullet(),
                other.nestedBullet() != null ? other.nestedBullet() : this.nestedBullet(),
                other.spaceLeft() != null ? other.spaceLeft() : this.spaceLeft(),
                other.spaceAbove() != null ? other.spaceAbove() : this.spaceAbove(),
                other.spaceBetweenItems() != null ? other.spaceBetweenItems() : this.spaceBetweenItems(),
                other.spaceBetweenBulletAndText() != null ? other.spaceBetweenBulletAndText() : this.spaceBetweenBulletAndText()
            );
        }
    }
}
