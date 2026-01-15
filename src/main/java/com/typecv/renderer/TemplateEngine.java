package com.typecv.renderer;

import com.typecv.model.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateMethodModelEx;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Template engine for rendering CV data to Typst format using Freemarker.
 * Supports multiple themes with full design customization.
 */
public class TemplateEngine {
    
    private final Configuration cfg;
    
    public TemplateEngine() {
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
    }
    
    /**
     * Render a CV to Typst format.
     */
    public String render(CV cv) throws IOException, TemplateException {
        StringBuilder output = new StringBuilder();
        
        // Render preamble
        output.append(renderPreamble(cv));
        
        // Render header
        output.append(renderHeader(cv));
        
        // Render each section
        for (Map.Entry<String, List<Object>> section : cv.cv().sections().entrySet()) {
            output.append(renderSection(section.getKey(), section.getValue(), cv));
        }
        
        return output.toString();
    }
    
    private Map<String, Object> createBaseModel(CV cv) {
        Map<String, Object> model = new HashMap<>();
        
        // Add escape function for templates to use
        model.put("escapeTypst", new EscapeTypstMethod());
        
        // Create a flat model for cv data
        CvData cvData = cv.cv();
        Map<String, Object> cvMap = new HashMap<>();
        cvMap.put("name", cvData.name());
        cvMap.put("headline", cvData.headline());
        cvMap.put("location", cvData.location());
        cvMap.put("email", cvData.email());
        cvMap.put("emailEscaped", escapeTypst(cvData.email()));
        cvMap.put("photo", cvData.photo());
        cvMap.put("phone", cvData.phone());
        cvMap.put("website", cvData.website());
        cvMap.put("socialNetworks", cvData.socialNetworks());
        cvMap.put("sections", cvData.sections());
        model.put("cv", cvMap);
        
        // Create comprehensive design model
        Design design = cv.design();
        if (design != null) {
            model.put("design", createDesignModel(design));
        }
        
        // Create flat model for locale
        Locale locale = cv.locale();
        if (locale != null) {
            Map<String, Object> localeMap = new HashMap<>();
            localeMap.put("language", locale.language());
            localeMap.put("present", locale.present());
            localeMap.put("monthAbbreviations", locale.monthAbbreviations());
            localeMap.put("monthNames", locale.monthNames());
            model.put("locale", localeMap);
        }
        
        return model;
    }
    
    /**
     * Create a comprehensive design model for templates.
     */
    private Map<String, Object> createDesignModel(Design design) {
        Map<String, Object> designMap = new HashMap<>();
        designMap.put("theme", design.theme());
        
        // Page settings
        if (design.page() != null) {
            Map<String, Object> pageMap = new HashMap<>();
            pageMap.put("size", design.page().size());
            pageMap.put("topMargin", design.page().topMargin());
            pageMap.put("bottomMargin", design.page().bottomMargin());
            pageMap.put("leftMargin", design.page().leftMargin());
            pageMap.put("rightMargin", design.page().rightMargin());
            pageMap.put("showFooter", design.page().showFooter());
            pageMap.put("showTopNote", design.page().showTopNote());
            designMap.put("page", pageMap);
        }
        
        // Color settings
        if (design.colors() != null) {
            Map<String, Object> colorsMap = new HashMap<>();
            colorsMap.put("body", design.colors().body());
            colorsMap.put("name", design.colors().name());
            colorsMap.put("headline", design.colors().headline());
            colorsMap.put("connections", design.colors().connections());
            colorsMap.put("sectionTitles", design.colors().sectionTitles());
            colorsMap.put("links", design.colors().links());
            colorsMap.put("footer", design.colors().footer());
            colorsMap.put("topNote", design.colors().topNote());
            designMap.put("colors", colorsMap);
        }
        
        // Typography settings
        if (design.typography() != null) {
            Map<String, Object> typoMap = new HashMap<>();
            typoMap.put("lineSpacing", design.typography().lineSpacing());
            typoMap.put("alignment", design.typography().alignment());
            typoMap.put("dateAndLocationColumnAlignment", design.typography().dateAndLocationColumnAlignment());
            
            if (design.typography().fontFamily() != null) {
                Map<String, Object> fontFamilyMap = new HashMap<>();
                fontFamilyMap.put("body", design.typography().fontFamily().body());
                fontFamilyMap.put("name", design.typography().fontFamily().name());
                fontFamilyMap.put("headline", design.typography().fontFamily().headline());
                fontFamilyMap.put("connections", design.typography().fontFamily().connections());
                fontFamilyMap.put("sectionTitles", design.typography().fontFamily().sectionTitles());
                typoMap.put("fontFamily", fontFamilyMap);
            }
            
            if (design.typography().fontSize() != null) {
                Map<String, Object> fontSizeMap = new HashMap<>();
                fontSizeMap.put("body", design.typography().fontSize().body());
                fontSizeMap.put("name", design.typography().fontSize().name());
                fontSizeMap.put("headline", design.typography().fontSize().headline());
                fontSizeMap.put("connections", design.typography().fontSize().connections());
                fontSizeMap.put("sectionTitles", design.typography().fontSize().sectionTitles());
                typoMap.put("fontSize", fontSizeMap);
            }
            
            if (design.typography().smallCaps() != null) {
                Map<String, Object> smallCapsMap = new HashMap<>();
                smallCapsMap.put("name", design.typography().smallCaps().name());
                smallCapsMap.put("headline", design.typography().smallCaps().headline());
                smallCapsMap.put("connections", design.typography().smallCaps().connections());
                smallCapsMap.put("sectionTitles", design.typography().smallCaps().sectionTitles());
                typoMap.put("smallCaps", smallCapsMap);
            }
            
            if (design.typography().bold() != null) {
                Map<String, Object> boldMap = new HashMap<>();
                boldMap.put("name", design.typography().bold().name());
                boldMap.put("headline", design.typography().bold().headline());
                boldMap.put("connections", design.typography().bold().connections());
                boldMap.put("sectionTitles", design.typography().bold().sectionTitles());
                typoMap.put("bold", boldMap);
            }
            
            designMap.put("typography", typoMap);
        }
        
        // Links settings
        if (design.links() != null) {
            Map<String, Object> linksMap = new HashMap<>();
            linksMap.put("underline", design.links().underline());
            linksMap.put("showExternalLinkIcon", design.links().showExternalLinkIcon());
            designMap.put("links", linksMap);
        }
        
        // Header settings
        if (design.header() != null) {
            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("alignment", design.header().alignment());
            headerMap.put("photoWidth", design.header().photoWidth());
            headerMap.put("photoPosition", design.header().photoPosition());
            headerMap.put("spaceBelowName", design.header().spaceBelowName());
            headerMap.put("spaceBelowHeadline", design.header().spaceBelowHeadline());
            headerMap.put("spaceBelowConnections", design.header().spaceBelowConnections());
            
            if (design.header().connections() != null) {
                Map<String, Object> connMap = new HashMap<>();
                connMap.put("phoneNumberFormat", design.header().connections().phoneNumberFormat());
                connMap.put("hyperlink", design.header().connections().hyperlink());
                connMap.put("showIcons", design.header().connections().showIcons());
                connMap.put("displayUrlsInsteadOfUsernames", design.header().connections().displayUrlsInsteadOfUsernames());
                connMap.put("separator", design.header().connections().separator());
                connMap.put("spaceBetweenConnections", design.header().connections().spaceBetweenConnections());
                headerMap.put("connections", connMap);
            }
            
            designMap.put("header", headerMap);
        }
        
        // Section titles settings
        if (design.sectionTitles() != null) {
            Map<String, Object> sectTitlesMap = new HashMap<>();
            sectTitlesMap.put("type", design.sectionTitles().type());
            sectTitlesMap.put("lineThickness", design.sectionTitles().lineThickness());
            sectTitlesMap.put("spaceAbove", design.sectionTitles().spaceAbove());
            sectTitlesMap.put("spaceBelow", design.sectionTitles().spaceBelow());
            designMap.put("sectionTitles", sectTitlesMap);
        }
        
        // Sections settings
        if (design.sections() != null) {
            Map<String, Object> sectionsMap = new HashMap<>();
            sectionsMap.put("allowPageBreak", design.sections().allowPageBreak());
            sectionsMap.put("spaceBetweenRegularEntries", design.sections().spaceBetweenRegularEntries());
            sectionsMap.put("spaceBetweenTextBasedEntries", design.sections().spaceBetweenTextBasedEntries());
            designMap.put("sections", sectionsMap);
        }
        
        // Entries settings
        if (design.entries() != null) {
            Map<String, Object> entriesMap = new HashMap<>();
            entriesMap.put("dateAndLocationWidth", design.entries().dateAndLocationWidth());
            entriesMap.put("sideSpace", design.entries().sideSpace());
            entriesMap.put("spaceBetweenColumns", design.entries().spaceBetweenColumns());
            entriesMap.put("allowPageBreak", design.entries().allowPageBreak());
            entriesMap.put("shortSecondRow", design.entries().shortSecondRow());
            
            if (design.entries().summary() != null) {
                Map<String, Object> summaryMap = new HashMap<>();
                summaryMap.put("spaceAbove", design.entries().summary().spaceAbove());
                summaryMap.put("spaceLeft", design.entries().summary().spaceLeft());
                entriesMap.put("summary", summaryMap);
            }
            
            if (design.entries().highlights() != null) {
                Map<String, Object> highlightsMap = new HashMap<>();
                highlightsMap.put("bullet", design.entries().highlights().bullet());
                highlightsMap.put("nestedBullet", design.entries().highlights().nestedBullet());
                highlightsMap.put("spaceLeft", design.entries().highlights().spaceLeft());
                highlightsMap.put("spaceAbove", design.entries().highlights().spaceAbove());
                highlightsMap.put("spaceBetweenItems", design.entries().highlights().spaceBetweenItems());
                highlightsMap.put("spaceBetweenBulletAndText", design.entries().highlights().spaceBetweenBulletAndText());
                entriesMap.put("highlights", highlightsMap);
            }
            
            designMap.put("entries", entriesMap);
        }
        
        return designMap;
    }
    
    /**
     * Escape text for Typst to prevent interpretation as special syntax.
     * This handles:
     * - $ (math mode delimiter)
     * - @ (label syntax)
     * - # (function/keyword syntax - only at start of content blocks)
     * - < and > (can interfere with markup)
     */
    public static String escapeTypst(String text) {
        if (text == null) return null;
        // Escape $ first (math mode delimiter) - this is the most common issue
        // Escape @ symbol which is used for labels in Typst
        return text
            .replace("$", "\\$")
            .replace("@", "\\@");
    }
    
    /**
     * Freemarker method model for escaping Typst special characters in templates.
     */
    public static class EscapeTypstMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(@SuppressWarnings("rawtypes") java.util.List arguments) {
            if (arguments == null || arguments.isEmpty()) {
                return "";
            }
            Object arg = arguments.get(0);
            if (arg == null) {
                return "";
            }
            return escapeTypst(arg.toString());
        }
    }
    
    private String renderPreamble(CV cv) throws IOException, TemplateException {
        Template template = cfg.getTemplate("Preamble.ftl");
        StringWriter writer = new StringWriter();
        
        Map<String, Object> model = createBaseModel(cv);
        template.process(model, writer);
        return writer.toString();
    }
    
    private String renderHeader(CV cv) throws IOException, TemplateException {
        Template template = cfg.getTemplate("Header.ftl");
        StringWriter writer = new StringWriter();
        
        Map<String, Object> model = createBaseModel(cv);
        template.process(model, writer);
        return writer.toString();
    }
    
    private String renderSection(String sectionName, List<Object> entries, CV cv) 
            throws IOException, TemplateException {
        StringBuilder output = new StringBuilder();
        
        // Format section title: "education" -> "Education", "work_experience" -> "Work Experience"
        String sectionTitle = formatSectionTitle(sectionName);
        
        // Render section beginning
        Template beginTemplate = cfg.getTemplate("SectionBeginning.ftl");
        StringWriter beginWriter = new StringWriter();
        Map<String, Object> beginModel = createBaseModel(cv);
        beginModel.put("sectionTitle", sectionTitle);
        beginTemplate.process(beginModel, beginWriter);
        output.append(beginWriter.toString());
        
        // Render each entry
        for (Object entry : entries) {
            output.append(renderEntry(entry, cv));
        }
        
        // Render section ending
        Template endTemplate = cfg.getTemplate("SectionEnding.ftl");
        StringWriter endWriter = new StringWriter();
        Map<String, Object> endModel = createBaseModel(cv);
        endTemplate.process(endModel, endWriter);
        output.append(endWriter.toString());
        
        return output.toString();
    }
    
    private String renderEntry(Object entry, CV cv) throws IOException, TemplateException {
        String templateName = getEntryTemplateName(entry);
        Template template = cfg.getTemplate("entries/" + templateName);
        StringWriter writer = new StringWriter();
        
        Map<String, Object> model = createBaseModel(cv);
        model.put("entry", entry);
        
        template.process(model, writer);
        return writer.toString();
    }
    
    private String getEntryTemplateName(Object entry) {
        if (entry instanceof EducationEntry) {
            return "EducationEntry.ftl";
        } else if (entry instanceof ExperienceEntry) {
            return "ExperienceEntry.ftl";
        } else if (entry instanceof ProjectEntry) {
            return "ProjectEntry.ftl";
        } else if (entry instanceof PublicationEntry) {
            return "PublicationEntry.ftl";
        } else if (entry instanceof OneLineEntry) {
            return "OneLineEntry.ftl";
        } else if (entry instanceof NumberedEntry) {
            return "NumberedEntry.ftl";
        } else if (entry instanceof ReversedNumberedEntry) {
            return "ReversedNumberedEntry.ftl";
        } else if (entry instanceof BulletEntry) {
            return "BulletEntry.ftl";
        } else if (entry instanceof TextEntry) {
            return "TextEntry.ftl";
        }
        return "TextEntry.ftl"; // Default fallback
    }
    
    private String formatSectionTitle(String sectionName) {
        // Convert snake_case or lowercase to Title Case
        // "education" -> "Education"
        // "work_experience" -> "Work Experience"
        String[] words = sectionName.replace("_", " ").split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1).toLowerCase());
                }
            }
        }
        return result.toString();
    }
}
