package com.typecv.model;

/**
 * Text entry for simple text sections.
 * Represented as a plain String in YAML.
 */
public record TextEntry(
    String text
) implements Entry {
}
