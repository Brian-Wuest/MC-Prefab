package com.wuest.prefab.TileEntities;

import com.wuest.prefab.Base.TileEntityBase;
import com.wuest.prefab.Config.DrafterTileEntityConfig;

/**
 * This is the tile entity for the Drafter block. This block will contain the configuration data.
 * @author WuestMan
 */
public class TileEntityDrafter extends TileEntityBase<DrafterTileEntityConfig>
{
	/**
	 * Initializes a new instance of the TileEntityDrafter class.
	 */
	public TileEntityDrafter()
	{
		super();
		this.config = new DrafterTileEntityConfig();
	}
}