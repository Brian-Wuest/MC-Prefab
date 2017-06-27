package com.wuest.prefab.Gui.Controls;

import com.wuest.prefab.Config.DrafterTileEntityConfig.RoomInfo;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;

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
	public boolean selected;
	
	public GuiRoomInfoButton(int id, int xPos, int yPos, String displayString)
	{
		super(id, xPos, yPos, displayString);
		
		this.selected = false;
	}
	
    public GuiRoomInfoButton(int id, int xPos, int yPos, int width, int height, String displayString)
    {
        super(id, xPos, yPos, width, height, displayString);
        this.selected = false;
    }
    
    public String getHoverText()
    {
    	if (this.roomInfo != null)
    	{
    		return String.format(
    				"Room Name: %1s\nRoom Coordinates: X:%2$d Y:%3$d Z:%4$d", 
    				this.roomInfo.StructureName.getName(),
    				this.roomInfo.RoomCoordinates.getX(),
    				this.roomInfo.RoomCoordinates.getY(),
    				this.roomInfo.RoomCoordinates.getZ());
    	}
    	
    	return "";
    }
    
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial)
    {
        if (this.visible)
        {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int k = this.getHoverState(this.hovered);
            
            // Set the "hover" color if this is a selected button.
            if (this.selected)
            {
            	k = 2;
            }
            
            GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
            this.mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;

            if (packedFGColour != 0)
            {
                color = packedFGColour;
            }
            else if (!this.enabled)
            {
                color = 10526880;
            }
            else if (this.hovered || this.selected)
            {
                color = 16777120;
            }

            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth)
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";

            this.drawCenteredString(mc.fontRenderer, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);
        }
    }

}
