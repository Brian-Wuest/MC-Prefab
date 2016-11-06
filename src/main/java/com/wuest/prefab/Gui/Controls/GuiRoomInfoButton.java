package com.wuest.prefab.Gui.Controls;

import com.wuest.prefab.Config.DrafterTileEntityConfig.RoomInfo;

import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * This custom button is used to contain the hover text for the room information as well as store the room information.
 * @author WuestMan
 *
 */
public class GuiRoomInfoButton extends GuiButtonExt
{

	/**
	 * This is the room information for this button.
	 */
	public RoomInfo roomInfo;
	
	public GuiRoomInfoButton(int id, int xPos, int yPos, String displayString)
	{
		super(id, xPos, yPos, displayString);
		// TODO Auto-generated constructor stub
	}
	
    public GuiRoomInfoButton(int id, int xPos, int yPos, int width, int height, String displayString)
    {
        super(id, xPos, yPos, width, height, displayString);
    }
    
    public String getHoverText()
    {
    	if (this.roomInfo != null)
    	{
    		return String.format(
    				"Room Name: %1s\nRoom Coordinates: X:%2$d Y:%3$d Z:%4$d", 
    				this.roomInfo.StructureName,
    				this.roomInfo.RoomCoordinates.getX(),
    				this.roomInfo.RoomCoordinates.getY(),
    				this.roomInfo.RoomCoordinates.getZ());
    	}
    	
    	return "";
    }

}
