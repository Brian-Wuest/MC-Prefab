package com.wuest.prefab.Gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.DrafterTileEntityConfig;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Gui.Controls.GuiTab;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.TileEntities.TileEntityDrafter;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;

/**
 * This screen is used for the modular house to build the additional rooms.
 * @author WuestMan
 *
 */
public class GuiDrafter extends GuiTabScreen
{
	private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/defaultBackground.png");
	
	protected GuiTab tabGeneral;
	protected GuiTab tabDetails;
	
	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnBuild;
	protected GuiButtonExt btnClearPending;
	
	protected GuiButtonExt btnBasement2;
	protected GuiButtonExt btnBasement1;
	protected GuiButtonExt btnGroundFloor;
	protected GuiButtonExt btnSecondFloor;
	protected GuiButtonExt btnThirdFloor;
	
	/**
	 * This is the array list for all 49 rooms.
	 */
	protected ArrayList<GuiButtonExt> roomButtons;
	protected ArrayList<HoverChecker> roomHovers;
	
	protected BlockPos pos;
	protected ModConfiguration serverConfiguration;
	protected DrafterTileEntityConfig drafterConfig;
	protected TileEntityDrafter tileEntity;
	
	public GuiDrafter(int x, int y, int z)
	{
		super();
		this.pos = new BlockPos(x, y, z);
		this.Tabs.trayWidth = 320;
		this.roomButtons = new ArrayList<GuiButtonExt>();
		this.roomHovers = new ArrayList<HoverChecker>();
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.Initialize();
	}
	
	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = (this.width / 2) - 158;
		int grayBoxY = (this.height / 2) - 93;
		this.Tabs.trayX = grayBoxX;
		this.Tabs.trayY = grayBoxY - 21;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(backgroundTextures);
		this.drawModalRectWithCustomSizedTexture(grayBoxX, grayBoxY, 0, 320, 320, 320, 320);

		for (GuiButton button : this.buttonList)
		{
			// Make all buttons invisible.
			if (button != this.btnCancel && button != this.btnBuild)
			{
				//button.visible = false;
			}
		}
		
		// Update visibility on controls based on the selected tab.
		
		// Draw the buttons, labels and tabs.
		super.drawScreen(x, y, f);
		
		for (int i = 0; i < 49; i++)
		{
			HoverChecker hoverChecker = this.roomHovers.get(i);
			
			if (hoverChecker.checkHover(x, y))
			{
				String hoverText = "Room: " + (i + 1);
				this.drawHoveringText(this.mc.fontRendererObj.listFormattedStringToWidth(hoverText, 300), x, y);
			}
		}
		
		// Draw the text here.
		int color = Color.DARK_GRAY.getRGB();
		
		// Draw specific text labels here.
		this.mc.fontRendererObj.drawString("Level", grayBoxX + 7, grayBoxY + 20, color);
		
		this.mc.fontRendererObj.drawString("Room Name:", grayBoxX + 186, grayBoxY + 20, color);
		
		this.mc.fontRendererObj.drawString("Room Coordinates:", grayBoxX + 186, grayBoxY + 50, color);
	}
	
	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == this.btnCancel)
		{
			this.mc.displayGuiScreen(null);
		}
	}
	
	private void Initialize() 
	{
		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 158;
		int grayBoxY = (this.height / 2) - 103;
		int color = Color.DARK_GRAY.getRGB();
		int uiComponentID = 1;
		this.serverConfiguration = ((ClientProxy)Prefab.proxy).getServerConfiguration();
		
		// Get the power configuration settings.
		TileEntity entity = this.mc.theWorld.getTileEntity(this.pos);

		if (entity != null && entity.getClass() == TileEntityDrafter.class)
		{
			this.drafterConfig = ((TileEntityDrafter)entity).getConfig();
			this.tileEntity = (TileEntityDrafter)entity;
		}
		else
		{
			this.tileEntity = new TileEntityDrafter();
			this.mc.theWorld.setTileEntity(pos, this.tileEntity);

			this.drafterConfig = this.tileEntity.getConfig();
		}

		this.drafterConfig.pos = this.pos;
		
		int roomX = grayBoxX + 40;
		int roomY = grayBoxY + 30;
		
		// Create the 49 room buttons.
		for (int i = 1; i < 50; i++)
		{
			GuiButtonExt roomButton = new GuiButtonExt(uiComponentID++, roomX, roomY, 20, 20, "");
			HoverChecker hoverChecker = new HoverChecker(roomButton, 800);
			this.roomHovers.add(hoverChecker);
			this.buttonList.add(roomButton);
			this.roomButtons.add(roomButton);
			roomX = roomX + 20;
			
			if (i % 7 == 0)
			{
				roomX = grayBoxX + 40;
				roomY = roomY + 20;
			}
		}
		
		this.btnBasement2 = new GuiButtonExt(uiComponentID++, grayBoxX + 10, grayBoxY + 145, 20, 20, "B2");
		this.buttonList.add(this.btnBasement2);
		
		this.btnBasement1 = new GuiButtonExt(uiComponentID++, grayBoxX + 10, grayBoxY + 120, 20, 20, "B1");
		this.buttonList.add(this.btnBasement1);
		
		this.btnGroundFloor = new GuiButtonExt(uiComponentID++, grayBoxX + 10, grayBoxY + 95, 20, 20, "G");
		this.buttonList.add(this.btnGroundFloor);
		
		this.btnSecondFloor = new GuiButtonExt(uiComponentID++, grayBoxX + 10, grayBoxY + 70, 20, 20, "2");
		this.buttonList.add(this.btnSecondFloor);
		
		this.btnThirdFloor = new GuiButtonExt(uiComponentID++, grayBoxX + 10, grayBoxY + 45, 20, 20, "3");
		this.buttonList.add(this.btnThirdFloor);
		
		this.btnClearPending = new GuiButtonExt(uiComponentID++, grayBoxX + 110, grayBoxY + 186, 80, 20, "Clear Pending");
		this.buttonList.add(this.btnClearPending);
		
		this.btnBuild = new GuiButtonExt(uiComponentID++, grayBoxX + 195, grayBoxY + 186, 50, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);
		
		this.btnCancel = new GuiButtonExt(uiComponentID++, grayBoxX + 250, grayBoxY + 186, 50, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
		this.buttonList.add(this.btnCancel);
		
		// Tabs:
		this.tabGeneral = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_GENERAL), grayBoxX + 5, grayBoxY - 10);
		this.Tabs.AddTab(this.tabGeneral);
		
		this.tabDetails = new GuiTab(this.Tabs, "Details", grayBoxX + 55, grayBoxY - 10);
	}
}
