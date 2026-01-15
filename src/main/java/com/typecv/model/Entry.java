package com.typecv.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Sealed interface for CV entry types.
 * Uses Jackson polymorphic deserialization to detect entry type from fields.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.DEDUCTION
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EducationEntry.class),
    @JsonSubTypes.Type(value = ExperienceEntry.class),
    @JsonSubTypes.Type(value = ProjectEntry.class),
    @JsonSubTypes.Type(value = PublicationEntry.class),
    @JsonSubTypes.Type(value = OneLineEntry.class),
    @JsonSubTypes.Type(value = NumberedEntry.class),
    @JsonSubTypes.Type(value = ReversedNumberedEntry.class),
    @JsonSubTypes.Type(value = BulletEntry.class),
    @JsonSubTypes.Type(value = TextEntry.class)
})
public sealed interface Entry permits EducationEntry, ExperienceEntry, ProjectEntry, PublicationEntry, OneLineEntry, NumberedEntry, ReversedNumberedEntry, BulletEntry, TextEntry {
}
