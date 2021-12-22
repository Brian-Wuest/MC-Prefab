package com.wuest.prefab.blocks;

/**
 * The flags used when calling world.setBlock
 * Flags can be combined with bitwise OR
 */
public class BlockFlags {
    /**
     * Calls Block.neighborChanged on surrounding blocks (with isMoving as false).
     * Also updates comparator output state.
     */
    public static final int NOTIFY_NEIGHBORS = (1 << 0);
    /**
     * Calls World.notifyBlockUpdate.<br>
     * Server-side, this updates all the path-finding navigators.
     */
    public static final int BLOCK_UPDATE = (1 << 1);
    /**
     * Stops the blocks from being marked for a render update
     */
    public static final int NO_RERENDER = (1 << 2);
    /**
     * Makes the block be re-rendered immediately, on the main thread.
     * If NO_RERENDER is set, then this will be ignored
     */
    public static final int RERENDER_MAIN_THREAD = (1 << 3);
    /**
     * Causes neighbor updates to be sent to all surrounding blocks (including
     * diagonals). This in turn will call Block.updateDiagonalNeighbors
     * on both old and new states, and Block.updateNeighbors on the new state.
     */
    public static final int UPDATE_NEIGHBORS = (1 << 4);

    /**
     * Prevents neighbor changes from spawning item drops, used by Block.ReplaceBlock.
     */
    public static final int NO_NEIGHBOR_DROPS = (1 << 5);

    /**
     * Tell the block being changed that it was moved, rather than removed/replaced,
     * the boolean value is eventually passed to Block.OnReplaced as the last parameter.
     */
    public static final int IS_MOVING = (1 << 6);

    public static final int DEFAULT = NOTIFY_NEIGHBORS | BLOCK_UPDATE;
    public static final int DEFAULT_AND_RERENDER = DEFAULT | RERENDER_MAIN_THREAD;
}
