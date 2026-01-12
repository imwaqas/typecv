# TypeCV - Java-based CV/Resume Generator

A Java-based CV generator.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Usage Guide](#usage-guide)
- [YAML Format Reference](#yaml-format-reference)
- [Building from Source](#building-from-source)
- [Building Native Executable](#building-native-executable-optional)
- [Troubleshooting](#troubleshooting)
- [License](#license)

## Features

- **Simple YAML input** - Define your CV in a human-readable YAML file
- **Professional PDF output** - Generates beautifully typeset PDFs using Typst
- **Bundled runtime** - Typst is bundled in the JAR (no separate installation needed)
- **Cross-platform** - Works on Windows, macOS, and Linux
- **Fast** - Generates PDFs in seconds
- **Native executable** - Optionally compile to standalone executable with GraalVM

## Prerequisites

### For Running TypeCV (JAR)

| Requirement | Version | Download |
|-------------|---------|----------|
| **Java Runtime (JRE/JDK)** | 17 or higher | [Amazon Corretto 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html) or [Eclipse Temurin 17](https://adoptium.net/temurin/releases/?version=17) |

#### Verify Java Installation

```bash
java -version
```

Expected output (version should be 17 or higher):
```
openjdk version "17.0.x" ...
```

### For Building from Source (Additional)

| Requirement | Version | Notes |
|-------------|---------|-------|
| **Java Development Kit (JDK)** | 17 or higher | Same as above, but JDK not just JRE |
| **Git** | Any recent | [Download Git](https://git-scm.com/downloads) |

### For Building Native Executable (Optional)

| Requirement | Version | Download |
|-------------|---------|----------|
| **GraalVM** | 21 or higher | [GraalVM Downloads](https://www.graalvm.org/downloads/) |
| **Native Image** | Included with GraalVM | Run `gu install native-image` after installing GraalVM |
| **Visual Studio Build Tools** (Windows only) | 2019 or higher | [Visual Studio Build Tools](https://visualstudio.microsoft.com/visual-cpp-build-tools/) |

## Installation

### Option 1: Download Pre-built JAR (Recommended)

1. Download the latest `typecv-1.0.0-all.jar` from the [Releases](https://github.com/yourusername/typecv/releases) page
2. Place it in a convenient directory
3. Run with: `java -jar typecv-1.0.0-all.jar`

### Option 2: Build from Source

See [Building from Source](#building-from-source) section below.

## Quick Start

### Step 1: Create a New CV

```bash
java -jar typecv-1.0.0-all.jar new "John Doe"
```

This creates a file named `John_Doe_CV.yaml` with sample content.

### Step 2: Edit Your CV

Open `John_Doe_CV.yaml` in any text editor and replace the sample content with your information.

### Step 3: Generate PDF

```bash
java -jar typecv-1.0.0-all.jar render John_Doe_CV.yaml
```

This generates `John_Doe_CV.pdf` in the current directory.

## Usage Guide

### Commands

#### `new` - Create a New CV File

```bash
java -jar typecv-1.0.0-all.jar new "Your Full Name"
```

**Arguments:**
- `"Your Full Name"` - Your name in quotes (spaces allowed)

**Output:**
- Creates `Your_Full_Name_CV.yaml` with sample content

**Example:**
```bash
java -jar typecv-1.0.0-all.jar new "Jane Smith"
# Creates: Jane_Smith_CV.yaml
```

#### `render` - Generate PDF from YAML

```bash
java -jar typecv-1.0.0-all.jar render <yaml-file>
```

**Arguments:**
- `<yaml-file>` - Path to your CV YAML file

**Output:**
- Creates a PDF file with the same base name
- Also creates a `.typ` file (Typst source, can be deleted)

**Example:**
```bash
java -jar typecv-1.0.0-all.jar render Jane_Smith_CV.yaml
# Creates: Jane_Smith_CV.pdf
```

#### `--help` - Show Help

```bash
java -jar typecv-1.0.0-all.jar --help
java -jar typecv-1.0.0-all.jar new --help
java -jar typecv-1.0.0-all.jar render --help
```

#### `--version` - Show Version

```bash
java -jar typecv-1.0.0-all.jar --version
```

## YAML Format Reference

### Basic Structure

```yaml
cv:
  name: John Doe
  headline: Software Engineer          # Optional
  location: San Francisco, CA          # Optional
  email: john.doe@email.com            # Optional
  phone: "+1 555 123 4567"             # Optional
  website: https://johndoe.dev         # Optional
  social_networks:                     # Optional
    - network: LinkedIn
      username: johndoe
    - network: GitHub
      username: johndoe
  sections:
    # Your CV sections go here
    
design:
  theme: classic                       # Only "classic" supported in MVP

locale:
  language: english                    # Only "english" supported in MVP
```

### Entry Types

TypeCV supports 4 entry types, automatically detected from the YAML structure:

#### 1. EducationEntry

For education history. Detected when `institution` field is present.

```yaml
education:
  - institution: Stanford University
    area: Computer Science
    degree: MS                           # Optional
    start_date: 2016-09
    end_date: 2018-06
    location: Stanford, CA               # Optional
    summary: Focus on Machine Learning   # Optional
    highlights:                          # Optional
      - "GPA: 3.9/4.0"
      - Dean's List all semesters
```

#### 2. ExperienceEntry

For work experience. Detected when `company` field is present.

```yaml
experience:
  - company: Tech Corp
    position: Senior Software Engineer
    start_date: 2020-01
    end_date: present                    # Use "present" for current job
    location: San Francisco, CA          # Optional
    summary: Led backend development     # Optional
    highlights:                          # Optional
      - Built microservices serving 10M+ requests/day
      - Reduced latency by 40%
```

#### 3. BulletEntry

For simple bullet-point lists. Detected when `bullet` field is present.

```yaml
skills:
  - bullet: "Languages: Java, Python, TypeScript, SQL"
  - bullet: "Frameworks: Spring Boot, React, Node.js"
  - bullet: "Tools: Docker, Kubernetes, AWS, Git"

honors:
  - bullet: Employee of the Year 2021
  - bullet: Best Project Award - Stanford
```

#### 4. TextEntry

For plain text paragraphs. Used when entry is a plain string.

```yaml
summary:
  - Passionate software engineer with 5+ years of experience building scalable web applications.
  - Strong background in Java, Python, and cloud technologies.

about_me:
  - This is a paragraph of text that will be rendered as-is.
```

### Complete Example

```yaml
cv:
  name: Jane Smith
  headline: Full Stack Developer
  location: New York, NY
  email: jane.smith@email.com
  phone: "+1 555 987 6543"
  website: https://janesmith.dev
  social_networks:
    - network: LinkedIn
      username: janesmith
    - network: GitHub
      username: janesmith
  sections:
    summary:
      - Experienced full-stack developer with expertise in React and Node.js.
      - Passionate about building user-friendly applications.
    
    education:
      - institution: MIT
        area: Computer Science
        degree: BS
        start_date: 2012-09
        end_date: 2016-05
        location: Cambridge, MA
        highlights:
          - "GPA: 3.85/4.0"
          - Undergraduate Research Assistant
    
    experience:
      - company: BigTech Inc
        position: Senior Developer
        start_date: 2020-03
        end_date: present
        location: New York, NY
        highlights:
          - Lead frontend architecture for main product
          - Mentored 3 junior developers
      
      - company: StartupXYZ
        position: Software Engineer
        start_date: 2016-06
        end_date: 2020-02
        location: Boston, MA
        highlights:
          - Built REST APIs serving 1M+ users
          - Implemented CI/CD pipeline
    
    skills:
      - bullet: "Frontend: React, TypeScript, Next.js, Tailwind CSS"
      - bullet: "Backend: Node.js, Python, PostgreSQL, Redis"
      - bullet: "DevOps: Docker, Kubernetes, AWS, GitHub Actions"
    
    certifications:
      - bullet: AWS Certified Solutions Architect
      - bullet: Google Cloud Professional Developer

design:
  theme: classic

locale:
  language: english
```

### Date Formats

Dates can be specified in several formats:

```yaml
# Year and month
start_date: 2020-09
end_date: 2024-05

# Just year
date: "2021"

# Current/ongoing
end_date: present

# Single date (for publications, events)
date: 2023-07
```

### Supported Social Networks

- LinkedIn
- GitHub
- Twitter / X
- GitLab
- StackOverflow
- Mastodon
- ORCID
- ResearchGate
- YouTube
- Instagram

## Building from Source

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/typecv.git
cd typecv
```

### Step 2: Build the Project

**Windows:**
```powershell
.\gradlew.bat build
```

**macOS/Linux:**
```bash
./gradlew build
```

### Step 3: Build the Fat JAR

**Windows:**
```powershell
.\gradlew.bat fatJar
```

**macOS/Linux:**
```bash
./gradlew fatJar
```

The JAR will be created at: `build/libs/typecv-1.0.0-all.jar`

### Step 4: Run

```bash
java -jar build/libs/typecv-1.0.0-all.jar --help
```

### Running Tests

**Windows:**
```powershell
.\gradlew.bat test
```

**macOS/Linux:**
```bash
./gradlew test
```

## Building Native Executable (Optional)

Building a native executable creates a standalone `typecv.exe` (Windows) or `typecv` (macOS/Linux) that doesn't require Java to be installed.

### Prerequisites

1. **Install GraalVM 21+**
   - Download from [GraalVM Downloads](https://www.graalvm.org/downloads/)
   - Set `JAVA_HOME` to GraalVM installation directory
   - Add GraalVM `bin` directory to `PATH`

2. **Install Native Image component**
   ```bash
   gu install native-image
   ```

3. **Windows only: Install Visual Studio Build Tools**
   - Download [Visual Studio Build Tools](https://visualstudio.microsoft.com/visual-cpp-build-tools/)
   - Install "Desktop development with C++" workload
   - Run native-image commands from "x64 Native Tools Command Prompt"

### Build Native Executable

**Windows (from x64 Native Tools Command Prompt):**
```powershell
.\gradlew.bat nativeCompile
```

**macOS/Linux:**
```bash
./gradlew nativeCompile
```

The executable will be created at:
- Windows: `build/native/nativeCompile/typecv.exe`
- macOS/Linux: `build/native/nativeCompile/typecv`

### Using the Native Executable

```bash
# No java command needed!
./typecv new "John Doe"
./typecv render John_Doe_CV.yaml
```

## Troubleshooting

### "java is not recognized" or "java: command not found"

**Problem:** Java is not installed or not in PATH.

**Solution:**
1. Install Java 17+ (see [Prerequisites](#prerequisites))
2. Add Java to your PATH:
   - **Windows:** Add `C:\Program Files\Java\jdk-17\bin` to System PATH
   - **macOS/Linux:** Add `export PATH=$JAVA_HOME/bin:$PATH` to `~/.bashrc` or `~/.zshrc`

### "Error: File not found"

**Problem:** The YAML file path is incorrect.

**Solution:**
- Make sure you're in the same directory as the YAML file
- Or provide the full path: `java -jar typecv.jar render "C:\path\to\Your_CV.yaml"`

### "Typst binary not found"

**Problem:** The bundled Typst binary couldn't be extracted or found.

**Solution:**
1. Make sure you have write permissions to your user's AppData/Application Support directory
2. Try installing Typst system-wide: [Typst Installation](https://github.com/typst/typst#installation)

### PDF looks different than expected

**Problem:** Font rendering issues.

**Solution:**
- TypeCV uses Arial font by default (available on most systems)
- If Arial is not available, Typst will use a fallback font
- For best results, ensure standard system fonts are installed

### Build fails with "Could not resolve dependencies"

**Problem:** Network issues or Gradle cache problems.

**Solution:**
```bash
# Clear Gradle cache and rebuild
./gradlew clean build --refresh-dependencies
```

### Native image build fails

**Problem:** Missing build tools or incorrect GraalVM setup.

**Solution:**
1. Verify GraalVM is active: `java -version` should show "GraalVM"
2. Verify native-image is installed: `native-image --version`
3. **Windows:** Run from "x64 Native Tools Command Prompt for VS"

## Project Structure

```
typecv/
├── build.gradle                 # Gradle build configuration
├── settings.gradle              # Gradle settings
├── gradlew, gradlew.bat         # Gradle wrapper scripts
├── README.md                    # This file
├── src/
│   ├── main/
│   │   ├── java/com/typecv/
│   │   │   ├── TypeCV.java           # Main entry point
│   │   │   ├── command/
│   │   │   │   ├── NewCommand.java   # 'new' command
│   │   │   │   └── RenderCommand.java # 'render' command
│   │   │   ├── model/                # Data models
│   │   │   ├── renderer/             # Template & Typst rendering
│   │   │   └── util/                 # YAML parsing
│   │   └── resources/
│   │       ├── sample_content.yaml   # Template for 'new' command
│   │       ├── templates/            # Freemarker templates
│   │       └── typst/                # Bundled Typst binaries
│   └── test/                         # Unit tests
```

## Tech Stack

- **Java 17** - Language
- **Gradle** - Build tool
- **Picocli** - CLI framework
- **Jackson** - YAML parsing
- **Freemarker** - Template engine
- **Typst** - PDF rendering
- **GraalVM Native Image** - Native compilation (optional)

## License

MIT License

## Credits

- Inspired by [RenderCV](https://github.com/rendercv/rendercv)
- PDF rendering powered by [Typst](https://typst.app)
- CLI framework: [Picocli](https://picocli.info)
- Template engine: [Freemarker](https://freemarker.apache.org)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
