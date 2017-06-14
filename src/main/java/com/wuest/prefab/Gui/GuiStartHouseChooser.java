package com.wuest.prefab.Gui;

import java.awt.Color;
import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.HouseConfiguration;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Config.HouseConfiguration.HouseStyle;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Gui.Controls.GuiTab;
import com.wuest.prefab.Gui.Controls.GuiTextSlider;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Render.StructureRenderHandler;
import com.wuest.prefab.StructureGen.CustomStructures.StructureAlternateStart;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiStartHouseChooser extends GuiTabScreen
{
	private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnBuild;
	
	// Tabs
	protected GuiTab tabGeneral;
	protected GuiTab tabConfig;
	protected GuiTab tabBlockTypes;
	
	// General:
	protected GuiButtonExt btnHouseStyle;
	protected GuiButtonExt btnHouseFacing;
	protected GuiButtonExt btnGlassColor;
	protected GuiButtonExt btnVisualize;
	
	// Config:
	protected GuiCheckBox btnAddTorches;
	protected GuiCheckBox btnAddBed;
	protected GuiCheckBox btnAddCraftingTable;
	protected GuiCheckBox btnAddChest;
	protected GuiCheckBox btnAddChestContents;
	protected GuiCheckBox btnAddFarm;
	protected GuiCheckBox btnAddMineShaft;
	protected GuiCheckBox btnIsCeilingFlat;

	// Blocks/Size.
	protected GuiSlider btnHouseWidth;
	protected GuiSlider btnHouseDepth;
	protected GuiTextSlider btnFloorBlock;
	protected GuiTextSlider btnCeilingBlock;
	protected GuiTextSlider btnWallWoodType;
	
	protected HouseConfiguration.HouseStyle houseStyle = HouseConfiguration.HouseStyle.BASIC;
	protected EnumFacing houseFacing = EnumFacing.NORTH;
	protected EnumDyeColor houseColor = EnumDyeColor.CYAN;
	public BlockPos pos;
	protected ModConfiguration serverConfiguration;
	protected boolean allowItemsInChestAndFurnace = true;
	
	public GuiStartHouseChooser(int x, int y, int z)
	{
		super();
		this.pos = new BlockPos(x, y, z);
		this.Tabs.trayWidth = 256;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		if (!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
		{
			NBTTagCompound tag = ModEventHandler.getModIsPlayerNewTag(Minecraft.getMinecraft().thePlayer);
			this.allowItemsInChestAndFurnace = !tag.getBoolean(ModEventHandler.Built_Starter_house_Tag);
		}
		
		this.Initialize();
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = (this.width / 2) - 188;
		int grayBoxY = (this.height / 2) - 83;
		this.Tabs.trayX = grayBoxX;
		this.Tabs.trayY = grayBoxY - 21;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(backgroundTextures);
		this.drawTexturedModalRect(grayBoxX, grayBoxY, 0, 0, 256, 256);

		for (GuiButton button : this.buttonList)
		{
			// Make all buttons invisible.
			if (button != this.btnCancel && button != this.btnBuild)
			{
				button.visible = false;
			}
		}
		
		this.btnAddTorches.visible = false;
		this.btnAddBed.visible = false;
		this.btnAddChest.visible = false;
		this.btnAddChestContents.visible = false;
		this.btnAddCraftingTable.visible = false;
		this.btnAddFarm.visible = false;
		this.btnAddMineShaft.visible = false;
		this.btnIsCeilingFlat.visible = false;
		
		// Update visibility on controls based on the selected tab.
		if (this.getSelectedTab() == this.tabGeneral)
		{
			this.btnHouseStyle.visible = true;
			this.btnHouseFacing.visible = true;
			
			if (this.houseStyle == HouseConfiguration.HouseStyle.BASIC)
			{
				this.btnVisualize.visible = false;
			}
			else
			{
				this.btnVisualize.visible = true;
			}
		}
		else if (this.getSelectedTab() == this.tabConfig)
		{
			this.btnAddTorches.visible = true;
			this.btnAddBed.visible = true;
			this.btnAddChest.visible = true;
			this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace;
			this.btnAddCraftingTable.visible = true;
			this.btnAddMineShaft.visible = true;
			
			if (this.houseStyle == HouseConfiguration.HouseStyle.BASIC)
			{
				this.btnAddFarm.visible = true;
				this.btnIsCeilingFlat.visible = true;
			}
		}
		else if (this.getSelectedTab() == this.tabBlockTypes)
		{
			if (this.houseStyle == HouseConfiguration.HouseStyle.BASIC)
			{
				this.btnFloorBlock.visible = true;
				this.btnWallWoodType.visible = true;
				this.btnCeilingBlock.visible = true;
				this.btnHouseDepth.visible = true;
				this.btnHouseWidth.visible = true;
			}
			else
			{
				if (this.houseStyle == HouseConfiguration.HouseStyle.SNOWY
						|| this.houseStyle == HouseConfiguration.HouseStyle.DESERT)
				{
					this.btnGlassColor.visible = false;
				}
				else
				{
					this.btnGlassColor.visible = true;
				}
			}
		}
		
		// Draw the buttons, labels and tabs.
		super.drawScreen(x, y, f);

		// Draw the text here.
		int color = Color.DARK_GRAY.getRGB();
		
		// Draw the appropriate text based on the selected tab.
		if (this.getSelectedTab() == this.tabGeneral)
		{
			this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), grayBoxX + 10, grayBoxY + 10, color);
			this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_FACING), grayBoxX + 10, grayBoxY + 50, color);
			
			this.mc.fontRendererObj.drawSplitString(this.houseStyle.getHouseNotes(), grayBoxX + 147, grayBoxY + 10, 95, color);
			
			this.mc.getTextureManager().bindTexture(this.houseStyle.getHousePicture());
			GuiTabScreen.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 
					this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight(), this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight());
		}
		else if (this.getSelectedTab() == this.tabBlockTypes)
		{
			if (this.houseStyle == HouseConfiguration.HouseStyle.BASIC)
			{
				// Column 1:
				this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_FLOOR_LABEL), grayBoxX + 10, grayBoxY + 10, color);
				this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_CEILING_LABEL), grayBoxX + 10, grayBoxY + 50, color);
				this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_WALL_LABEL), grayBoxX + 10, grayBoxY + 90, color);
				
				// Column 3:
				this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_DEPTH_LABEL), grayBoxX + 147, grayBoxY + 10, color);
				this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_WIDTH_LABEL), grayBoxX + 147, grayBoxY + 50, color);
			}
			else
			{
				if (this.houseStyle == HouseConfiguration.HouseStyle.SNOWY
						|| this.houseStyle == HouseConfiguration.HouseStyle.DESERT)
				{
				
				}
				else
				{
					// Column 1:
					this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), grayBoxX + 10, grayBoxY + 10, color);
				}
			}
		}
		
		if (!Prefab.proxy.proxyConfiguration.enableStructurePreview)
		{
			this.btnVisualize.enabled = false;
		}
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
		else if (button == this.btnBuild)
		{
			HouseConfiguration houseConfiguration = new HouseConfiguration();
			houseConfiguration.pos = this.pos;
			houseConfiguration.addBed = this.btnAddBed.isChecked();
			houseConfiguration.addChest = this.btnAddChest.isChecked();
			houseConfiguration.addChestContents = this.btnAddChestContents.visible ? this.btnAddChestContents.isChecked() : false;
			houseConfiguration.addCraftingTable = this.btnAddCraftingTable.isChecked();
			houseConfiguration.addFarm = this.btnAddFarm.isChecked();
			houseConfiguration.addMineShaft = this.btnAddMineShaft.isChecked();
			houseConfiguration.addTorches = this.btnAddTorches.isChecked();
			houseConfiguration.isCeilingFlat = this.btnIsCeilingFlat.isChecked();
			houseConfiguration.ceilingBlock = ModConfiguration.CeilingFloorBlockType.ValueOf(this.btnCeilingBlock.getValueInt());
			houseConfiguration.floorBlock = ModConfiguration.CeilingFloorBlockType.ValueOf(this.btnFloorBlock.getValueInt());
			houseConfiguration.wallWoodType = ModConfiguration.WallBlockType.ValueOf(this.btnWallWoodType.getValueInt());
			houseConfiguration.houseDepth = this.btnHouseDepth.getValueInt();
			houseConfiguration.houseWidth = this.btnHouseWidth.getValueInt();
			houseConfiguration.houseFacing = this.houseFacing;
			houseConfiguration.houseStyle = this.houseStyle;
			houseConfiguration.glassColor = this.houseColor;
			
			Prefab.network.sendToServer(new StructureTagMessage(houseConfiguration.WriteToNBTTagCompound(), EnumStructureConfiguration.StartHouse));
			
			this.mc.displayGuiScreen(null);
		}
		else if (button == this.btnHouseStyle)
		{
			int id = this.houseStyle.getValue() + 1;
			this.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);
			
			// Skip the loft if it's not enabled.
			if (this.houseStyle == HouseStyle.LOFT
					&& !this.serverConfiguration.enableLoftHouse)
			{
				id = this.houseStyle.getValue() + 1;
				this.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);
			}
			
			this.btnHouseStyle.displayString = this.houseStyle.getDisplayName();
			
			// Set the default glass colors for this style.
			if (this.houseStyle == HouseConfiguration.HouseStyle.HOBBIT)
			{
				this.houseColor = EnumDyeColor.GREEN;
				this.btnGlassColor.displayString = GuiLangKeys.translateDye(EnumDyeColor.GREEN);
			}
			else if (this.houseStyle == HouseConfiguration.HouseStyle.LOFT)
			{
				this.houseColor = EnumDyeColor.BLACK;
				this.btnGlassColor.displayString = GuiLangKeys.translateDye(EnumDyeColor.BLACK);
			}
			else
			{
				this.houseColor = EnumDyeColor.CYAN;
				this.btnGlassColor.displayString = GuiLangKeys.translateDye(EnumDyeColor.CYAN);
			}
			
			if (this.houseStyle == HouseConfiguration.HouseStyle.DESERT
					|| this.houseStyle == HouseConfiguration.HouseStyle.SNOWY)
			{
				this.tabBlockTypes.visible = false;
			}
			else
			{
				this.tabBlockTypes.visible = true;
			}
		}
		else if (button == this.btnHouseFacing)
		{
			this.houseFacing = this.houseFacing.rotateY();
			this.btnHouseFacing.displayString = GuiLangKeys.translateFacing(this.houseFacing);
		}
		else if (button == this.btnGlassColor)
		{
			this.houseColor = EnumDyeColor.byMetadata(this.houseColor.getMetadata() + 1);
			this.btnGlassColor.displayString = GuiLangKeys.translateDye(this.houseColor);
		}
		else if (button == this.btnVisualize)
		{
			HouseConfiguration configuration = new HouseConfiguration();
			configuration.pos = this.pos;
			configuration.glassColor = this.houseColor;
			configuration.houseFacing = this.houseFacing;
			configuration.houseStyle = this.houseStyle;
			StructureAlternateStart structure = StructureAlternateStart.CreateInstance(configuration.houseStyle.getStructureLocation(), StructureAlternateStart.class);
			
			StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, configuration);
			this.mc.displayGuiScreen(null);
		}
	}
	
	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}
	
	private void Initialize() 
	{
		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 188;
		int grayBoxY = (this.height / 2) - 83;
		int color = Color.DARK_GRAY.getRGB();
		this.serverConfiguration = ((ClientProxy)Prefab.proxy).getServerConfiguration();

		// Create the Controls.
		// Column 1:
		this.btnHouseStyle = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 20, 90, 20, this.houseStyle.getDisplayName());
		this.buttonList.add(this.btnHouseStyle);
		
		this.btnHouseFacing = new GuiButtonExt(3, grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.translateFacing(EnumFacing.NORTH));
		this.buttonList.add(this.btnHouseFacing);
		
		this.btnVisualize = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));
		this.buttonList.add(this.btnVisualize);

		
		int x = grayBoxX + 10;
		int y = grayBoxY + 10;
		
		this.btnAddTorches = new GuiCheckBox(1, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_TORCHES), true);
		this.btnAddTorches.setStringColor(color);
		this.btnAddTorches.setWithShadow(false);
		this.btnAddTorches.visible = false;
		this.buttonList.add(this.btnAddTorches);
		y += 15;
		
		this.btnAddBed = new GuiCheckBox(2, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_BED), true);
		this.btnAddBed.setStringColor(color);
		this.btnAddBed.setWithShadow(false);
		this.btnAddBed.visible = false;
		this.buttonList.add(this.btnAddBed);
		y += 15;

		this.btnAddChest = new GuiCheckBox(3, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST), true);
		this.btnAddChest.setStringColor(color);
		this.btnAddChest.setWithShadow(false);
		this.btnAddChest.visible = false;
		this.buttonList.add(this.btnAddChest);
		y += 15;

		this.btnAddCraftingTable = new GuiCheckBox(5, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CRAFTING_TABLE), true);
		this.btnAddCraftingTable.setStringColor(color);
		this.btnAddCraftingTable.setWithShadow(false);
		this.btnAddCraftingTable.visible = false;
		this.buttonList.add(this.btnAddCraftingTable);
		y += 15;

		this.btnAddMineShaft = new GuiCheckBox(7, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT), true);
		this.btnAddMineShaft.setStringColor(color);
		this.btnAddMineShaft.setWithShadow(false);
		this.btnAddMineShaft.visible = false;
		this.buttonList.add(this.btnAddMineShaft);
		y += 15;
		
		this.btnAddChestContents = new GuiCheckBox(4, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS), true);
		this.btnAddChestContents.setStringColor(color);
		this.btnAddChestContents.setWithShadow(false);
		this.btnAddChestContents.visible = false;
		this.buttonList.add(this.btnAddChestContents);
		
		if (this.allowItemsInChestAndFurnace)
		{
			y += 15;
		}

		this.btnAddFarm = new GuiCheckBox(6,  x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_FARM), true);
		this.btnAddFarm.setStringColor(color);
		this.btnAddFarm.setWithShadow(false);
		this.btnAddFarm.visible = false;
		this.buttonList.add(this.btnAddFarm);
		y += 15;
		
		this.btnIsCeilingFlat = new GuiCheckBox(8, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_CEILING_FLAT), false);
		this.btnIsCeilingFlat.setStringColor(color);
		this.btnIsCeilingFlat.setWithShadow(false);
		this.btnIsCeilingFlat.visible = false;
		this.buttonList.add(this.btnIsCeilingFlat);
		
		this.btnFloorBlock = new GuiTextSlider(11, grayBoxX + 10, grayBoxY + 20, 90, 20, 0, 2, 0, GuiLangKeys.STARTER_HOUSE_FLOOR_STONE);
		this.buttonList.add(this.btnFloorBlock);

		this.btnCeilingBlock = new GuiTextSlider(12, grayBoxX + 10, grayBoxY + 60, 90, 20, 0, 2, 0, GuiLangKeys.STARTER_HOUSE_CEILING_TYPE);
		this.buttonList.add(this.btnCeilingBlock);

		this.btnWallWoodType = new GuiTextSlider(13, grayBoxX + 10, grayBoxY + 100, 90, 20, 0, 5, 0, GuiLangKeys.STARTER_HOUSE_WALL_TYPE);
		this.buttonList.add(this.btnWallWoodType);
		
		this.btnGlassColor = new GuiButtonExt(17, grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateDye(EnumDyeColor.CYAN));
		this.buttonList.add(this.btnGlassColor);
		
		// Column 2:
		
		// Column 3:
		this.btnHouseDepth = new GuiSlider(15, grayBoxX + 147, grayBoxY + 20, 90, 20, "", "", 5, serverConfiguration.maximumStartingHouseSize, 9, false, true);
		this.buttonList.add(this.btnHouseDepth);
		
		this.btnHouseWidth = new GuiSlider(16, grayBoxX + 147, grayBoxY + 60, 90, 20, "", "", 5, serverConfiguration.maximumStartingHouseSize, 9, false, true);
		this.buttonList.add(this.btnHouseWidth);		
		
		// Tabs:
		this.tabGeneral = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_GENERAL), grayBoxX + 3, grayBoxY - 20);
		this.Tabs.AddTab(this.tabGeneral);
		
		this.tabConfig = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_CONFIG), grayBoxX + 54, grayBoxY - 20);
		this.Tabs.AddTab(this.tabConfig);
		
		this.tabBlockTypes = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_BLOCK), grayBoxX + 105, grayBoxY - 20);
		this.tabBlockTypes.width = 70;
		this.Tabs.AddTab(this.tabBlockTypes);
		
		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
		this.buttonList.add(this.btnCancel);
	}
}
