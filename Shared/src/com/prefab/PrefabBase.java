package com.prefab;

import com.prefab.config.ModConfiguration;
import com.prefab.network.INetworkWrapper;
import com.prefab.structures.base.BuildEntity;
import com.prefab.structures.base.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class PrefabBase {
	/**
	 * Contains a hashmap for the structures to build and for whom.
	 */
	public static HashMap<Player, ArrayList<Structure>> structuresToBuild = new HashMap<>();

	/**
	 * This is the ModID
	 */
	public static final String MODID = "prefab";

	public static final BlockBehaviour.StateArgumentPredicate<AABB> NEVER_VIEW_BLOCKING = (state, level, blockPos, nearPlaneBox) -> {
		return false;
	};

	public static Logger logger;
	/**
	 * This is used to determine if the mod is currently being debugged.
	 */
	public static boolean isDebug = false;

	/**
	 * Simulates an air block that blocks movement and cannot be moved.
	 * Basically a GLASS block, but NOT glass.
	 */
	public static final Supplier<BlockBehaviour.Properties> SeeThroughImmovable = ()->BlockBehaviour.Properties.of()
			.noOcclusion().isViewBlocking(NEVER_VIEW_BLOCKING)
			.pushReaction(PushReaction.IGNORE_ENTITY).sound(SoundType.STONE);

	public static INetworkWrapper networkWrapper;

	/**
	 * Used to call mod-loader specific events/methods (such as whether or not a player can break a block)
	 */
	public static IEventCaller eventCaller;

	public static ModConfiguration configuration;
	public static ModConfiguration serverConfiguration;

	/**
	 * Determines if structure items will scan their defined space or show the build gui. Default is false.
	 * Note: this should only be set to true during debug mode.
	 */
	public static boolean useScanningMode = false;

	static {
		PrefabBase.logger = LogManager.getLogger("Prefab");
		PrefabBase.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
	}

	public static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return true;
	}

	public static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return false;
	}

}