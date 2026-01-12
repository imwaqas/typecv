#entry-header(
  [*${entry.company()}*, ${entry.position()}],
  [<#if entry.location()?? && entry.location() != "">${entry.location()}\ </#if>
${entry.getFormattedDate()}],
)
<#if entry.summary()?? && entry.summary() != "">
${entry.summary()}
</#if>
<#if entry.highlights()?? && (entry.highlights()?size > 0)>
<#list entry.highlights() as highlight>
#bullet-item[${highlight}]
</#list>
</#if>

#v(0.4cm)

