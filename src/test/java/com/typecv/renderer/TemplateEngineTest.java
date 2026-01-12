package com.typecv.renderer;

import com.typecv.model.*;
import com.typecv.util.YamlParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class TemplateEngineTest {
    
    private TemplateEngine engine;
    private YamlParser parser;
    
    @BeforeEach
    void setUp() {
        engine = new TemplateEngine();
        parser = new YamlParser();
    }
    
    @Test
    void testRenderSampleContent() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("sample_content.yaml");
        assertNotNull(is);
        
        CV cv = parser.parse(is);
        String typst = engine.render(cv);
        
        assertNotNull(typst);
        assertFalse(typst.isEmpty());
        
        // Check that preamble is present
        assertTrue(typst.contains("#set page("));
        assertTrue(typst.contains("#set text("));
        
        // Check that header is present
        assertTrue(typst.contains("John Doe"));
        
        // Check that sections are present
        assertTrue(typst.contains("#section-title[Education]"));
        assertTrue(typst.contains("#section-title[Experience]"));
        assertTrue(typst.contains("#section-title[Skills]"));
        
        // Check that entries are rendered
        assertTrue(typst.contains("Stanford University"));
        assertTrue(typst.contains("Tech Corp"));
    }
    
    @Test
    void testRenderMinimalCV() throws Exception {
        String yaml = """
            cv:
              name: Test User
              email: test@example.com
              sections:
                summary:
                  - A simple summary text.
            """;
        
        CV cv = parser.parseString(yaml);
        String typst = engine.render(cv);
        
        assertNotNull(typst);
        assertTrue(typst.contains("Test User"));
        assertTrue(typst.contains("test@example.com"));
        assertTrue(typst.contains("A simple summary text."));
    }
    
    @Test
    void testSectionTitleFormatting() throws Exception {
        String yaml = """
            cv:
              name: Test User
              sections:
                work_experience:
                  - company: Test Corp
                    position: Developer
                    start_date: 2020-01
                    end_date: present
            """;
        
        CV cv = parser.parseString(yaml);
        String typst = engine.render(cv);
        
        // Should format "work_experience" as "Work Experience"
        assertTrue(typst.contains("#section-title[Work Experience]"));
    }
}
