package com.typecv.util;

import com.typecv.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class YamlParserTest {
    
    private YamlParser parser;
    
    @BeforeEach
    void setUp() {
        parser = new YamlParser();
    }
    
    @Test
    void testParseSampleContent() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("sample_content.yaml");
        assertNotNull(is, "sample_content.yaml should be in resources");
        
        CV cv = parser.parse(is);
        assertNotNull(cv);
        assertNotNull(cv.cv());
        
        // Test basic CV data
        assertEquals("John Doe", cv.cv().name());
        assertEquals("Software Engineer", cv.cv().headline());
        assertEquals("San Francisco, CA", cv.cv().location());
        assertEquals("john.doe@email.com", cv.cv().email());
        
        // Test social networks
        List<SocialNetwork> networks = cv.cv().socialNetworks();
        assertNotNull(networks);
        assertEquals(2, networks.size());
        assertEquals("LinkedIn", networks.get(0).network());
        assertEquals("johndoe", networks.get(0).username());
        
        // Test sections exist
        assertNotNull(cv.cv().sections());
        assertTrue(cv.cv().sections().containsKey("education"));
        assertTrue(cv.cv().sections().containsKey("experience"));
        assertTrue(cv.cv().sections().containsKey("skills"));
        
        // Test design defaults
        assertNotNull(cv.design());
        assertEquals("classic", cv.design().theme());
        
        // Test locale defaults  
        assertNotNull(cv.locale());
        assertEquals("english", cv.locale().language());
    }
    
    @Test
    void testParseEducationEntry() throws Exception {
        String yaml = """
            cv:
              name: Test User
              sections:
                education:
                  - institution: MIT
                    area: Computer Science
                    degree: PhD
                    start_date: 2020-09
                    end_date: 2024-05
                    location: Cambridge, MA
                    highlights:
                      - Research in AI
                      - Published 5 papers
            """;
        
        CV cv = parser.parseString(yaml);
        List<Object> education = cv.cv().sections().get("education");
        assertNotNull(education);
        assertEquals(1, education.size());
        
        Entry entry = (Entry) education.get(0);
        assertInstanceOf(EducationEntry.class, entry);
        
        EducationEntry edu = (EducationEntry) entry;
        assertEquals("MIT", edu.institution());
        assertEquals("Computer Science", edu.area());
        assertEquals("PhD", edu.degree());
        assertEquals(2, edu.highlights().size());
    }
    
    @Test
    void testParseExperienceEntry() throws Exception {
        String yaml = """
            cv:
              name: Test User
              sections:
                experience:
                  - company: Google
                    position: Software Engineer
                    start_date: 2020-01
                    end_date: present
                    location: Mountain View, CA
                    highlights:
                      - Built scalable systems
            """;
        
        CV cv = parser.parseString(yaml);
        List<Object> experience = cv.cv().sections().get("experience");
        assertNotNull(experience);
        assertEquals(1, experience.size());
        
        Entry entry = (Entry) experience.get(0);
        assertInstanceOf(ExperienceEntry.class, entry);
        
        ExperienceEntry exp = (ExperienceEntry) entry;
        assertEquals("Google", exp.company());
        assertEquals("Software Engineer", exp.position());
    }
    
    @Test
    void testParseBulletEntry() throws Exception {
        String yaml = """
            cv:
              name: Test User
              sections:
                skills:
                  - bullet: Java, Python, Go
                  - bullet: Docker, Kubernetes
            """;
        
        CV cv = parser.parseString(yaml);
        List<Object> skills = cv.cv().sections().get("skills");
        assertNotNull(skills);
        assertEquals(2, skills.size());
        
        assertInstanceOf(BulletEntry.class, skills.get(0));
        BulletEntry bullet = (BulletEntry) skills.get(0);
        assertEquals("Java, Python, Go", bullet.bullet());
    }
    
    @Test
    void testParseTextEntry() throws Exception {
        String yaml = """
            cv:
              name: Test User
              sections:
                summary:
                  - This is a text entry
                  - Another text entry
            """;
        
        CV cv = parser.parseString(yaml);
        List<Object> summary = cv.cv().sections().get("summary");
        assertNotNull(summary);
        assertEquals(2, summary.size());
        
        assertInstanceOf(TextEntry.class, summary.get(0));
        TextEntry text = (TextEntry) summary.get(0);
        assertEquals("This is a text entry", text.text());
    }
}
