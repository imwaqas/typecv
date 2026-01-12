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

