package com.wuest.prefab;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * This class contains generic helpers for server side operations.
 * @author WuestMan
 *
 */
public class ServerSideHelpers
{
	/**
	 * Performs a ray trace for a player position
	 * @param world The world which the ray trace is occurring.
	 * @param player The source of the ray trace.
	 * @param distance How far to reach for the ray trace.
	 * @param includeFluids Whether or not fluids should be included.
	 * @return The ray trace result for the world.
	 */
    public static RayTraceResult RayTrace(World world, EntityPlayer player, float distance, boolean includeFluids)
    {
        Vec3d vec3d = new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        Vec3d vec3d1 = player.getLook(1.0F);
        Vec3d vec3d2 = vec3d.add(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);
        
    	try
    	{
	        return world.rayTraceBlocks(vec3d, vec3d2, includeFluids, false, true);
    	}
    	catch(Exception ex)
    	{
    		return new RayTraceResult(RayTraceResult.Type.MISS, vec3d2, player.getHorizontalFacing().getOpposite(), player.getPosition().offset(player.getHorizontalFacing(), (int) distance));
    	}
    }
}
