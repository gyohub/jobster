package com.gyo.recollective.jobster.model;

/**
 * Describes the type of priorities.
 * It relies on the positional order e.g. index 0 (first) highest and N lowest.
 * TODO: Do not rely on the positional order. It is safer to explicitly define a property like
 * <code>Integer level</code> and define <code>HIGH(0)</code>, <code>MEDIUM(1)</code>, <code>LOW(2)</code>
 */
public enum ExecutionPriority {
    HIGH,
    MEDIUM,
    LOW
}
