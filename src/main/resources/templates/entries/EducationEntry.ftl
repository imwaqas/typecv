#entry-header(
  [*${escapeTypst(entry.institution())}*, ${escapeTypst(entry.area())}<#if entry.degree()?? && entry.degree() != ""> -- ${escapeTypst(entry.degree())}</#if>],
  [<#if entry.location()?? && entry.location() != "">${escapeTypst(entry.location())}\ </#if>
${entry.getFormattedDate()}],
)
<#if entry.summary()?? && entry.summary() != "">
#v(${design.entries.summary.spaceAbove})
#h(${design.entries.summary.spaceLeft})${escapeTypst(entry.summary())}
</#if>
<#if entry.highlights()?? && (entry.highlights()?size > 0)>
#v(${design.entries.highlights.spaceAbove})
<#list entry.highlights() as highlight>
#bullet-item[${escapeTypst(highlight)}]
<#if highlight_has_next>#v(${design.entries.highlights.spaceBetweenItems})</#if>
</#list>
</#if>

#v(${design.sections.spaceBetweenRegularEntries})

