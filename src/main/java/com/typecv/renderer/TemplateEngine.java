package com.typecv.renderer;

import com.typecv.model.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Template engine for rendering CV data to Typst format using Freemarker.
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
        
        // Create flat model for design
        Design design = cv.design();
        if (design != null) {
            Map<String, Object> designMap = new HashMap<>();
            designMap.put("theme", design.theme());
            if (design.page() != null) {
                Map<String, Object> pageMap = new HashMap<>();
                pageMap.put("size", design.page().size());
                pageMap.put("topMargin", design.page().topMargin());
                pageMap.put("bottomMargin", design.page().bottomMargin());
                pageMap.put("leftMargin", design.page().leftMargin());
                pageMap.put("rightMargin", design.page().rightMargin());
                designMap.put("page", pageMap);
            }
            model.put("design", designMap);
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
     * Escape text for Typst to prevent interpretation as labels or other syntax.
     */
    private String escapeTypst(String text) {
        if (text == null) return null;
        // Escape @ symbol which is used for labels in Typst
        return text.replace("@", "\\@");
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
        Map<String, Object> beginModel = new HashMap<>();
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
        endTemplate.process(new HashMap<>(), endWriter);
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
