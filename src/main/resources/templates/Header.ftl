// Header Section
<#if design.header.alignment == "center">
#align(center)[
<#elseif design.header.alignment == "left">
#align(left)[
<#else>
#align(right)[
</#if>
  #text(
    fill: name-color,
    weight: <#if design.typography.bold.name>"bold"<#else>"regular"</#if>,
    size: name-size,
  )[<#if design.typography.smallCaps.name>#smallcaps[${cv.name}]<#else>${cv.name}</#if>]
]

<#if cv.headline?? && cv.headline != "">
<#if design.header.alignment == "center">
#align(center)[
<#elseif design.header.alignment == "left">
#align(left)[
<#else>
#align(right)[
</#if>
  #text(
    fill: headline-color,
    weight: <#if design.typography.bold.headline>"bold"<#else>"regular"</#if>,
    size: headline-size,
  )[<#if design.typography.smallCaps.headline>#smallcaps[${cv.headline}]<#else>${cv.headline}</#if>]
]
</#if>

#v(0.3cm)

// Connections (contact info)
<#if design.header.alignment == "center">
#align(center)[
<#elseif design.header.alignment == "left">
#align(left)[
<#else>
#align(right)[
</#if>
  #text(fill: connections-color, size: connections-size)[
<#assign separator = design.header.connections.separator>
<#assign hasPrev = false>
<#if cv.location?? && cv.location != ""><#if hasPrev><#if separator != ""> ${separator} <#else> · </#if></#if>${cv.location}<#assign hasPrev = true></#if><#if cv.email?? && cv.email != ""><#if hasPrev><#if separator != ""> ${separator} <#else> · </#if></#if><#if design.header.connections.hyperlink>#link("mailto:${cv.email}")[${cv.emailEscaped}]<#else>${cv.emailEscaped}</#if><#assign hasPrev = true></#if><#if cv.phone?? && cv.phone != ""><#if hasPrev><#if separator != ""> ${separator} <#else> · </#if></#if>${cv.phone}<#assign hasPrev = true></#if><#if cv.website?? && cv.website != ""><#if hasPrev><#if separator != ""> ${separator} <#else> · </#if></#if><#if design.header.connections.hyperlink>#link("${cv.website}")[<#if design.header.connections.displayUrlsInsteadOfUsernames>${cv.website}<#else>${cv.website?replace("https://", "")?replace("http://", "")}</#if>]<#else>${cv.website}</#if><#assign hasPrev = true></#if>
  ]
]

<#if cv.socialNetworks?? && (cv.socialNetworks?size > 0)>
<#if design.header.alignment == "center">
#align(center)[
<#elseif design.header.alignment == "left">
#align(left)[
<#else>
#align(right)[
</#if>
  #text(fill: connections-color, size: connections-size)[
<#list cv.socialNetworks as sn>    <#if design.header.connections.hyperlink>#link("${sn.getUrl()}")[<#if design.header.connections.displayUrlsInsteadOfUsernames>${sn.getUrl()}<#else><#if design.header.connections.showIcons>${sn.network()}: </#if>${sn.username()}</#if>]<#else><#if design.header.connections.showIcons>${sn.network()}: </#if>${sn.username()}</#if><#if sn_has_next><#if separator != ""> ${separator} <#else> · </#if></#if>
</#list>
  ]
]
</#if>

#v(${design.header.spaceBelowConnections})

