package com.prefab.structures.base;

/**
 * This enum contains various ways in which we can return a replacement result and what it means.
 */
public enum ReplacementResultType {
    /**
     * The block replacement is allowed to occur.
     */
    ALLOWED,

    /**
     * The block replacement is NOT allowed to occur as the block is within the Vanilla Spawn protection radius.
     */
    NOT_ALLOWED_SPAWN_PROTECTION,

    /**
     * The block replacement is NOT allowed to occur as the block has been protected by another mod.
     * I.E. The "canBreakBlock" event returned false.
     * Note: Creative mode players can get around this.
     */
    NOT_ALLOWED_MOD_PROTECTED,

    /**
     * The block replacement is NOT allowed to occur as the block is unbreakable in the first place.
     * Note: Creative mode players can get around this.
     */
    NOT_ALLOWED_UNBREAKABLE_BLOCK,

    /**
     * The block replacement is NOT allowed to occur as the block is not within the overwritable blocks list.
     * This result only occurs when Strict Building Mode is enabled from the configuration.
     * Note: Operators can get around this.
     */
    NOT_ALLOWED_STRICT_BUILDING_MODE;
}
