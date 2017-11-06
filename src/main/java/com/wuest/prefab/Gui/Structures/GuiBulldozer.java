package com.wuest.prefab.Gui.Structures;

import java.awt.Color;
import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.Structures.BulldozerConfiguration;
import com.wuest.prefab.Config.Structures.ModerateHouseConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Render.StructureRenderHandler;
import com.wuest.prefab.StructureGen.CustomStructures.StructureModerateHouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiBulldozer extends GuiStructure
{

	protected BulldozerConfiguration configuration;
	
	/**
	 * Intializes a new instance of the {@link GuiBulldozer} class.
	 * @param x The x-axis location.
	 * @param y The y-axis location.
	 * @param z the z-axis location.
	 */
	public GuiBulldozer(int x, int y, int z)
	{
		super(x, y, z, true);
		
		this.structureConfiguration = EnumStructureConfiguration.Bulldozer;
	}
	
	@Override
	protected void Initialize() 
	{
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Bulldozer", BulldozerConfiguration.class);
		this.configuration.pos = this.pos;
		int color = Color.DARK_GRAY.getRGB();

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 125;
		int grayBoxY = this.getCenteredYAxis() - 83;

		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
		this.buttonList.add(this.btnCancel);
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = this.getCenteredXAxis() - 125;
		int grayBoxY = this.getCenteredYAxis() - 83;
		
		this.drawDefaultBackground();
		
		this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);
		
		this.mc.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BULLDOZER_DESCRIPTION), grayBoxX + 10, grayBoxY + 10, 230, this.textColor);
		
		this.mc.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_CLEARED_AREA), grayBoxX + 10, grayBoxY + 40, 230, this.textColor);
	}
	
	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		this.configuration.houseFacing = Minecraft.getMinecraft().player.getHorizontalFacing().getOpposite();
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);
	}

}
