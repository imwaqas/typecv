package com.typecv.model;

/**
 * Bullet entry for simple bullet-point sections like "honors", "awards".
 */
public record BulletEntry(
    String bullet
) implements Entry {
}
