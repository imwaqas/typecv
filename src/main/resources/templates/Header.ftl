// Header Section
#align(center)[
  #text(
    fill: primary-color,
    weight: "bold",
    size: 30pt,
  )[${cv.name}]
]

<#if cv.headline?? && cv.headline != "">
#align(center)[
  #text(fill: primary-color, size: 10pt)[${cv.headline}]
]
</#if>

#v(0.3cm)

#align(center)[
  #text(fill: primary-color, size: 10pt)[
<#if cv.location?? && cv.location != "">    ${cv.location}</#if><#if cv.email?? && cv.email != ""> · #link("mailto:${cv.email}")[${cv.emailEscaped}]</#if><#if cv.phone?? && cv.phone != ""> · ${cv.phone}</#if><#if cv.website?? && cv.website != ""> · #link("${cv.website}")[${cv.website}]</#if>
  ]
]

<#if cv.socialNetworks?? && (cv.socialNetworks?size > 0)>
#align(center)[
  #text(fill: primary-color, size: 10pt)[
<#list cv.socialNetworks as sn>    #link("${sn.getUrl()}")[${sn.network()}: ${sn.username()}]<#if sn_has_next> · </#if>
</#list>
  ]
]
</#if>

#v(0.5cm)

