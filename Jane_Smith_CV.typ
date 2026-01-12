// TypeCV Generated Document
// Theme: Classic

#set page(
  paper: "us-letter",
  margin: (
    top: 0.7in,
    bottom: 0.7in,
    left: 0.7in,
    right: 0.7in,
  ),
)

#set text(
  font: "Arial",
  size: 10pt,
  fill: rgb(0, 0, 0),
)

#set par(
  justify: true,
  leading: 0.6em,
)

// Colors
#let primary-color = rgb(0, 79, 144)
#let body-color = rgb(0, 0, 0)
#let gray-color = rgb(128, 128, 128)

// Helper functions
#let section-title(title) = {
  v(0.5cm)
  text(
    fill: primary-color,
    weight: "bold",
    size: 1.4em,
    [#title]
  )
  line(length: 100%, stroke: 0.5pt + primary-color)
  v(0.3cm)
}

#let entry-header(main, date-location) = {
  grid(
    columns: (1fr, auto),
    column-gutter: 0.5cm,
    align: (left, right),
    main,
    text(size: 0.9em, date-location),
  )
}

#let bullet-item(content) = {
  [• #content]
}

// Header Section
#align(center)[
  #text(
    fill: primary-color,
    weight: "bold",
    size: 30pt,
  )[Jane Smith]
]

#align(center)[
  #text(fill: primary-color, size: 10pt)[Software Engineer]
]

#v(0.3cm)

#align(center)[
  #text(fill: primary-color, size: 10pt)[
    San Francisco, CA · #link("mailto:john.doe@email.com")[john.doe\@email.com] · +1 555 123 4567 · #link("https://johndoe.dev")[https://johndoe.dev]
  ]
]

#align(center)[
  #text(fill: primary-color, size: 10pt)[
    #link("https://linkedin.com/in/johndoe")[LinkedIn: johndoe] · 
    #link("https://github.com/johndoe")[GitHub: johndoe]
  ]
]

#v(0.5cm)

#section-title[Summary]

Passionate software engineer with 5+ years of experience building scalable web applications and distributed systems.

Strong background in Java, Python, and cloud technologies.


#section-title[Education]

#entry-header(
  [*Stanford University*, Computer Science -- MS],
  [Stanford, CA\ 
2016-09 -- 2018-06],
)
#bullet-item[GPA: 3.9/4.0]
#bullet-item[Focus on Distributed Systems and Machine Learning]

#v(0.4cm)

#entry-header(
  [*University of California, Berkeley*, Computer Science -- BS],
  [Berkeley, CA\ 
2012-09 -- 2016-05],
)
#bullet-item[GPA: 3.8/4.0]
#bullet-item[Dean's List all semesters]

#v(0.4cm)


#section-title[Experience]

#entry-header(
  [*Tech Corp*, Senior Software Engineer],
  [San Francisco, CA\ 
2020-01 -- present],
)
#bullet-item[Led development of microservices architecture serving 10M+ daily requests]
#bullet-item[Reduced system latency by 40% through optimization initiatives]
#bullet-item[Mentored team of 5 junior engineers]

#v(0.4cm)

#entry-header(
  [*StartupXYZ*, Software Engineer],
  [San Francisco, CA\ 
2018-07 -- 2019-12],
)
#bullet-item[Built RESTful APIs using Java Spring Boot]
#bullet-item[Implemented CI/CD pipelines using Jenkins and Docker]
#bullet-item[Collaborated with product team to deliver features on schedule]

#v(0.4cm)


#section-title[Skills]

#bullet-item[Languages: Java, Python, TypeScript, SQL]

#bullet-item[Frameworks: Spring Boot, React, Node.js]

#bullet-item[Tools: Docker, Kubernetes, AWS, Git]


#section-title[Honors]

#bullet-item[Employee of the Year 2021 - Tech Corp]

#bullet-item[Best Project Award - Stanford CS Department]

#bullet-item[Dean's List - UC Berkeley]


