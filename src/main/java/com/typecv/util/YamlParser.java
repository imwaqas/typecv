package com.typecv.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.typecv.model.*;
import com.typecv.theme.ThemeLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * YAML parser for CV files using Jackson.
 * Handles polymorphic deserialization of Entry types.
 */
public class YamlParser {
    
    private final ObjectMapper mapper;
    
    public YamlParser() {
        this.mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // Register custom deserializer for Entry types
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Entry.class, new EntryDeserializer());
        mapper.registerModule(module);
    }
    
    /**
     * Parse a CV from a YAML file.
     */
    public CV parse(File file) throws IOException {
        JsonNode root = mapper.readTree(file);
        return parseFromNode(root);
    }
    
    /**
     * Parse a CV from an InputStream.
     */
    public CV parse(InputStream inputStream) throws IOException {
        JsonNode root = mapper.readTree(inputStream);
        return parseFromNode(root);
    }
    
    /**
     * Parse a CV from a YAML string.
     */
    public CV parseString(String yaml) throws IOException {
        JsonNode root = mapper.readTree(yaml);
        return parseFromNode(root);
    }
    
    private CV parseFromNode(JsonNode root) throws IOException {
        // Parse cv section
        CvData cvData = parseCvData(root.get("cv"));
        
        // Parse design section with theme support
        Design design = parseDesignWithTheme(root);
        
        // Parse locale section (optional)
        Locale locale = null;
        if (root.has("locale")) {
            locale = mapper.treeToValue(root.get("locale"), Locale.class);
        }
        
        return new CV(cvData, design, locale).withDefaults();
    }
    
    /**
     * Parse design section with theme loading support.
     * If a theme is specified, load theme defaults and merge with user overrides.
     */
    private Design parseDesignWithTheme(JsonNode root) throws IOException {
        ThemeLoader themeLoader = new ThemeLoader();
        
        if (!root.has("design")) {
            // No design specified, use classic theme defaults
            return themeLoader.loadThemeDefaults("classic");
        }
        
        JsonNode designNode = root.get("design");
        
        // Get the theme name (default to "classic")
        String themeName = "classic";
        if (designNode.has("theme")) {
            themeName = designNode.get("theme").asText();
        }
        
        // Parse user's design overrides
        Design userDesign = mapper.treeToValue(designNode, Design.class);
        
        // Load theme defaults and merge with user overrides
        return themeLoader.loadTheme(themeName, userDesign);
    }
    
    private CvData parseCvData(JsonNode cvNode) throws IOException {
        String name = getTextOrNull(cvNode, "name");
        String headline = getTextOrNull(cvNode, "headline");
        String location = getTextOrNull(cvNode, "location");
        String email = getTextOrNull(cvNode, "email");
        String photo = getTextOrNull(cvNode, "photo");
        String phone = getTextOrNull(cvNode, "phone");
        String website = getTextOrNull(cvNode, "website");
        
        // Parse social networks
        List<SocialNetwork> socialNetworks = new ArrayList<>();
        if (cvNode.has("social_networks") && cvNode.get("social_networks").isArray()) {
            for (JsonNode snNode : cvNode.get("social_networks")) {
                socialNetworks.add(mapper.treeToValue(snNode, SocialNetwork.class));
            }
        }
        
        // Parse sections with polymorphic entry detection
        Map<String, List<Object>> sections = new LinkedHashMap<>();
        if (cvNode.has("sections")) {
            JsonNode sectionsNode = cvNode.get("sections");
            sectionsNode.fieldNames().forEachRemaining(sectionName -> {
                try {
                    List<Object> entries = parseSectionEntries(sectionsNode.get(sectionName));
                    sections.put(sectionName, entries);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to parse section: " + sectionName, e);
                }
            });
        }
        
        return new CvData(name, headline, location, email, photo, phone, website, socialNetworks, sections);
    }
    
    private List<Object> parseSectionEntries(JsonNode entriesNode) throws IOException {
        List<Object> entries = new ArrayList<>();
        
        if (!entriesNode.isArray()) {
            return entries;
        }
        
        for (JsonNode entryNode : entriesNode) {
            Entry entry = parseEntry(entryNode);
            entries.add(entry);
        }
        
        return entries;
    }
    
    private Entry parseEntry(JsonNode node) throws IOException {
        // Detect entry type based on fields present
        if (node.isTextual()) {
            // Plain string -> TextEntry
            return new TextEntry(node.asText());
        }
        
        // EducationEntry: has "institution" field
        if (node.has("institution")) {
            return mapper.treeToValue(node, EducationEntry.class);
        }
        
        // ExperienceEntry: has "company" field
        if (node.has("company")) {
            return mapper.treeToValue(node, ExperienceEntry.class);
        }
        
        // ProjectEntry: has "name" field with optional summary/highlights (but not "label")
        // Must check before OneLineEntry since both could have "name"
        if (node.has("name") && !node.has("label")) {
            return mapper.treeToValue(node, ProjectEntry.class);
        }
        
        // PublicationEntry: has "title" field (for publications)
        if (node.has("title")) {
            return mapper.treeToValue(node, PublicationEntry.class);
        }
        
        // OneLineEntry: has "label" and "details" fields
        if (node.has("label") && node.has("details")) {
            return mapper.treeToValue(node, OneLineEntry.class);
        }
        
        // BulletEntry: has "bullet" field
        if (node.has("bullet")) {
            return mapper.treeToValue(node, BulletEntry.class);
        }
        
        // NumberedEntry: has "number" field
        if (node.has("number")) {
            return mapper.treeToValue(node, NumberedEntry.class);
        }
        
        // ReversedNumberedEntry: has "reversed_number" field
        if (node.has("reversed_number")) {
            return mapper.treeToValue(node, ReversedNumberedEntry.class);
        }
        
        // Default to TextEntry if it's a simple object with text
        if (node.has("text")) {
            return new TextEntry(node.get("text").asText());
        }
        
        // Fallback: convert to string representation
        return new TextEntry(node.toString());
    }
    
    private String getTextOrNull(JsonNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asText();
        }
        return null;
    }
    
    /**
     * Custom deserializer for Entry interface.
     */
    private static class EntryDeserializer extends JsonDeserializer<Entry> {
        @Override
        public Entry deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            
            if (node.isTextual()) {
                return new TextEntry(node.asText());
            }
            
            if (node.has("institution")) {
                return mapper.treeToValue(node, EducationEntry.class);
            }
            
            if (node.has("company")) {
                return mapper.treeToValue(node, ExperienceEntry.class);
            }
            
            if (node.has("name") && !node.has("label")) {
                return mapper.treeToValue(node, ProjectEntry.class);
            }
            
            if (node.has("title")) {
                return mapper.treeToValue(node, PublicationEntry.class);
            }
            
            if (node.has("label") && node.has("details")) {
                return mapper.treeToValue(node, OneLineEntry.class);
            }
            
            if (node.has("bullet")) {
                return mapper.treeToValue(node, BulletEntry.class);
            }
            
            if (node.has("number")) {
                return mapper.treeToValue(node, NumberedEntry.class);
            }
            
            if (node.has("reversed_number")) {
                return mapper.treeToValue(node, ReversedNumberedEntry.class);
            }
            
            return new TextEntry(node.toString());
        }
    }
    
    /**
     * Get the ObjectMapper for direct use.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
}
