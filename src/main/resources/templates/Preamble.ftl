// TypeCV Generated Document
// Theme: ${design.theme}

#set page(
  paper: "${design.page.size}",
  margin: (
    top: ${design.page.topMargin},
    bottom: ${design.page.bottomMargin},
    left: ${design.page.leftMargin},
    right: ${design.page.rightMargin},
  ),
)

#set text(
  font: "${design.typography.fontFamily.body}",
  size: ${design.typography.fontSize.body},
  fill: ${design.colors.body},
)

#set par(
<#if design.typography.alignment == "justified">
  justify: true,
<#elseif design.typography.alignment == "justified-with-no-hyphenation">
  justify: true,
<#else>
  justify: false,
</#if>
  leading: ${design.typography.lineSpacing},
)

<#if design.typography.alignment == "justified-with-no-hyphenation">
#set text(hyphenate: false)
</#if>

// Colors
#let primary-color = ${design.colors.sectionTitles}
#let name-color = ${design.colors.name}
#let headline-color = ${design.colors.headline}
#let connections-color = ${design.colors.connections}
#let body-color = ${design.colors.body}
#let links-color = ${design.colors.links}
#let footer-color = ${design.colors.footer}

// Typography settings
#let name-size = ${design.typography.fontSize.name}
#let headline-size = ${design.typography.fontSize.headline}
#let connections-size = ${design.typography.fontSize.connections}
#let section-title-size = ${design.typography.fontSize.sectionTitles}

// Section title style: ${design.sectionTitles.type}
<#if design.sectionTitles.type == "with_partial_line">
#let section-title(title) = {
  v(${design.sectionTitles.spaceAbove})
  grid(
    columns: (auto, 1fr),
    column-gutter: 0.3cm,
    align: (left, horizon),
    text(
      fill: primary-color,
      weight: <#if design.typography.bold.sectionTitles>"bold"<#else>"regular"</#if>,
      size: section-title-size,
      <#if design.typography.smallCaps.sectionTitles>smallcaps([#title])<#else>[#title]</#if>
    ),
    line(length: 100%, stroke: ${design.sectionTitles.lineThickness} + primary-color),
  )
  v(${design.sectionTitles.spaceBelow})
}
<#elseif design.sectionTitles.type == "with_full_line">
#let section-title(title) = {
  v(${design.sectionTitles.spaceAbove})
  text(
    fill: primary-color,
    weight: <#if design.typography.bold.sectionTitles>"bold"<#else>"regular"</#if>,
    size: section-title-size,
    <#if design.typography.smallCaps.sectionTitles>smallcaps([#title])<#else>[#title]</#if>
  )
  line(length: 100%, stroke: ${design.sectionTitles.lineThickness} + primary-color)
  v(${design.sectionTitles.spaceBelow})
}
<#elseif design.sectionTitles.type == "moderncv">
#let section-title(title) = {
  v(${design.sectionTitles.spaceAbove})
  grid(
    columns: (auto, 1fr),
    column-gutter: 0.3cm,
    align: (left, horizon),
    text(
      fill: primary-color,
      weight: <#if design.typography.bold.sectionTitles>"bold"<#else>"regular"</#if>,
      size: section-title-size,
      <#if design.typography.smallCaps.sectionTitles>smallcaps([#title])<#else>[#title]</#if>
    ),
    rect(width: 100%, height: ${design.sectionTitles.lineThickness}, fill: primary-color),
  )
  v(${design.sectionTitles.spaceBelow})
}
<#else>
// without_line
#let section-title(title) = {
  v(${design.sectionTitles.spaceAbove})
  text(
    fill: primary-color,
    weight: <#if design.typography.bold.sectionTitles>"bold"<#else>"regular"</#if>,
    size: section-title-size,
    <#if design.typography.smallCaps.sectionTitles>smallcaps([#title])<#else>[#title]</#if>
  )
  v(${design.sectionTitles.spaceBelow})
}
</#if>

// Entry header helper
#let entry-header(main, date-location) = {
  grid(
    columns: (1fr, ${design.entries.dateAndLocationWidth}),
    column-gutter: ${design.entries.spaceBetweenColumns},
    align: (left, ${design.typography.dateAndLocationColumnAlignment}),
    main,
    text(size: 0.9em, date-location),
  )
}

// Bullet item helper
#let bullet-item(content) = {
  h(${design.entries.highlights.spaceLeft})
  [${design.entries.highlights.bullet} #content]
}

// Link styling
<#if design.links.underline>
#show link: underline
</#if>

