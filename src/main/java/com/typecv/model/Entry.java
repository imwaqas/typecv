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
    @JsonSubTypes.Type(value = BulletEntry.class),
    @JsonSubTypes.Type(value = TextEntry.class)
})
public sealed interface Entry permits EducationEntry, ExperienceEntry, BulletEntry, TextEntry {
}
