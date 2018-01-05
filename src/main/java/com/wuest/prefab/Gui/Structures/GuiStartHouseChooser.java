package com.wuest.prefab.Gui.Structures;

import java.awt.Color;
import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Config.Structures.HouseConfiguration;
import com.wuest.prefab.Config.Structures.HouseConfiguration.HouseStyle;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
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
	protected GuiButtonExt btnGlassColor;
	protected GuiButtonExt btnVisualize;
	
	// Config:
	protected GuiCheckBox btnAddTorches;
	protected GuiCheckBox btnAddBed;
	protected GuiCheckBox btnAddCraftingTable;
	protected GuiCheckBox btnAddFurnace;
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
	
	protected HouseConfiguration houseConfiguration;
	protected ModConfiguration serverConfiguration;
	protected boolean allowItemsInChestAndFurnace = true;
	
	public GuiStartHouseChooser(int x, int y, int z)
	{
		super();
		this.houseConfiguration = ClientEventHandler.playerConfig.getClientConfig("Starter House", HouseConfiguration.class);
		this.houseConfiguration.pos = new BlockPos(x, y, z);
		this.Tabs.trayWidth = 256;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();

		if (!Minecraft.getMinecraft().player.capabilities.isCreativeMode)
		{
			this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
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
		this.btnAddFurnace.visible = false;
		this.btnAddFarm.visible = false;
		this.btnAddMineShaft.visible = false;
		this.btnIsCeilingFlat.visible = false;
		
		// Update visibility on controls based on the selected tab.
		if (this.getSelectedTab() == this.tabGeneral)
		{
			this.btnHouseStyle.visible = true;
			
			if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.BASIC)
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
			this.btnAddTorches.visible = this.serverConfiguration.addTorches;
			this.btnAddBed.visible = this.serverConfiguration.addBed;
			this.btnAddChest.visible = this.serverConfiguration.addChests;
			this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
			this.btnAddCraftingTable.visible = this.serverConfiguration.addCraftingTable;
			this.btnAddFurnace.visible = this.serverConfiguration.addFurnace;
			this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;
			
			if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.BASIC)
			{
				this.btnAddFarm.visible = this.serverConfiguration.addFarm;
				this.btnIsCeilingFlat.visible = true;
			}
		}
		else if (this.getSelectedTab() == this.tabBlockTypes)
		{
			if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.BASIC)
			{
				this.btnFloorBlock.visible = true;
				this.btnWallWoodType.visible = true;
				this.btnCeilingBlock.visible = true;
				this.btnHouseDepth.visible = true;
				this.btnHouseWidth.visible = true;
			}
			else
			{
				if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.SNOWY
						|| this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.DESERT)
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
			this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), grayBoxX + 10, grayBoxY + 10, color);
			this.mc.fontRenderer.drawSplitString(this.houseConfiguration.houseStyle.getHouseNotes(), grayBoxX + 147, grayBoxY + 10, 95, color);
			
			this.mc.getTextureManager().bindTexture(this.houseConfiguration.houseStyle.getHousePicture());
			GuiTabScreen.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 
					this.houseConfiguration.houseStyle.getImageWidth(), this.houseConfiguration.houseStyle.getImageHeight(), 
					this.houseConfiguration.houseStyle.getImageWidth(), this.houseConfiguration.houseStyle.getImageHeight());
		}
		else if (this.getSelectedTab() == this.tabBlockTypes)
		{
			if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.BASIC)
			{
				// Column 1:
				this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_FLOOR_LABEL), grayBoxX + 10, grayBoxY + 10, color);
				this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_CEILING_LABEL), grayBoxX + 10, grayBoxY + 50, color);
				this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_WALL_LABEL), grayBoxX + 10, grayBoxY + 90, color);
				
				// Column 3:
				this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_DEPTH_LABEL), grayBoxX + 147, grayBoxY + 10, color);
				this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_WIDTH_LABEL), grayBoxX + 147, grayBoxY + 50, color);
			}
			else
			{
				if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.SNOWY
						|| this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.DESERT)
				{
				
				}
				else
				{
					// Column 1:
					this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), grayBoxX + 10, grayBoxY + 10, color);
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
		if (button == this.btnCancel || button == this.btnVisualize
				|| button == this.btnBuild)
		{
			this.houseConfiguration.addBed = this.serverConfiguration.addBed && this.btnAddBed.isChecked();
			this.houseConfiguration.addChest = this.serverConfiguration.addChests && this.btnAddChest.isChecked();
			this.houseConfiguration.addChestContents = this.allowItemsInChestAndFurnace ? this.serverConfiguration.addChestContents && this.btnAddChestContents.isChecked() : false;
			this.houseConfiguration.addCraftingTable = this.serverConfiguration.addCraftingTable && this.btnAddCraftingTable.isChecked();
			this.houseConfiguration.addFarm = this.serverConfiguration.addFarm && this.btnAddFarm.isChecked();
			this.houseConfiguration.addMineShaft = this.serverConfiguration.addMineshaft && this.btnAddMineShaft.isChecked();
			this.houseConfiguration.addTorches = this.serverConfiguration.addTorches && this.btnAddTorches.isChecked();
			this.houseConfiguration.isCeilingFlat = this.btnIsCeilingFlat.isChecked();
			this.houseConfiguration.ceilingBlock = ModConfiguration.CeilingFloorBlockType.ValueOf(this.btnCeilingBlock.getValueInt());
			this.houseConfiguration.floorBlock = ModConfiguration.CeilingFloorBlockType.ValueOf(this.btnFloorBlock.getValueInt());
			this.houseConfiguration.wallWoodType = ModConfiguration.WallBlockType.ValueOf(this.btnWallWoodType.getValueInt());
			this.houseConfiguration.houseDepth = this.btnHouseDepth.getValueInt();
			this.houseConfiguration.houseWidth = this.btnHouseWidth.getValueInt();
			this.houseConfiguration.houseStyle = this.houseConfiguration.houseStyle;
			this.houseConfiguration.houseFacing = this.mc.player.getHorizontalFacing().getOpposite();
		}
		
		if (button == this.btnCancel)
		{
			this.mc.displayGuiScreen(null);
		}
		else if (button == this.btnBuild)
		{			
			Prefab.network.sendToServer(new StructureTagMessage(houseConfiguration.WriteToNBTTagCompound(), EnumStructureConfiguration.StartHouse));
			
			this.mc.displayGuiScreen(null);
		}
		else if (button == this.btnHouseStyle)
		{
			int id = this.houseConfiguration.houseStyle.getValue() + 1;
			this.houseConfiguration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);
			
			// Skip the loft if it's not enabled.
			if (this.houseConfiguration.houseStyle == HouseStyle.LOFT
					&& !this.serverConfiguration.enableLoftHouse)
			{
				id = this.houseConfiguration.houseStyle.getValue() + 1;
				this.houseConfiguration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);
			}
			
			this.btnHouseStyle.displayString = this.houseConfiguration.houseStyle.getDisplayName();
			
			// Set the default glass colors for this style.
			if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.HOBBIT)
			{
				this.houseConfiguration.glassColor = EnumDyeColor.GREEN;
				this.btnGlassColor.displayString = GuiLangKeys.translateDye(EnumDyeColor.GREEN);
			}
			else if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.LOFT)
			{
				this.houseConfiguration.glassColor = EnumDyeColor.BLACK;
				this.btnGlassColor.displayString = GuiLangKeys.translateDye(EnumDyeColor.BLACK);
			}
			else
			{
				this.houseConfiguration.glassColor = EnumDyeColor.CYAN;
				this.btnGlassColor.displayString = GuiLangKeys.translateDye(EnumDyeColor.CYAN);
			}
			
			if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.DESERT
					|| this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.SNOWY)
			{
				this.tabBlockTypes.visible = false;
			}
			else
			{
				this.tabBlockTypes.visible = true;
			}
		}
		else if (button == this.btnGlassColor)
		{
			this.houseConfiguration.glassColor = EnumDyeColor.byMetadata(this.houseConfiguration.glassColor.getMetadata() + 1);
			this.btnGlassColor.displayString = GuiLangKeys.translateDye(this.houseConfiguration.glassColor);
		}
		else if (button == this.btnVisualize)
		{
			StructureAlternateStart structure = StructureAlternateStart.CreateInstance(this.houseConfiguration.houseStyle.getStructureLocation(), StructureAlternateStart.class);
			
			StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.houseConfiguration);
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
		this.btnHouseStyle = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 20, 90, 20, this.houseConfiguration.houseStyle.getDisplayName());
		this.buttonList.add(this.btnHouseStyle);
		
		this.btnVisualize = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));
		this.buttonList.add(this.btnVisualize);
		
		int x = grayBoxX + 10;
		int y = grayBoxY + 10;
		int secondColumnY = y;
		int secondColumnX = x + 137;
		
		this.btnAddFurnace = new GuiCheckBox(5, secondColumnX, secondColumnY, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_FURNACE), this.houseConfiguration.addFurnace);
		this.btnAddFurnace.setStringColor(color);
		this.btnAddFurnace.setWithShadow(false);
		this.btnAddFurnace.visible = false;
		this.buttonList.add(this.btnAddFurnace);
		
		if (this.serverConfiguration.addFurnace)
		{
			secondColumnY += 15;
		}
		
		this.btnAddBed = new GuiCheckBox(2, secondColumnX, secondColumnY, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_BED), this.houseConfiguration.addBed);
		this.btnAddBed.setStringColor(color);
		this.btnAddBed.setWithShadow(false);
		this.btnAddBed.visible = false;
		this.buttonList.add(this.btnAddBed);
		
		if (this.serverConfiguration.addBed)
		{
			secondColumnY += 15;
		}
		
		this.btnAddFarm = new GuiCheckBox(6,  secondColumnX, secondColumnY, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_FARM), this.houseConfiguration.addFarm);
		this.btnAddFarm.setStringColor(color);
		this.btnAddFarm.setWithShadow(false);
		this.btnAddFarm.visible = false;
		this.buttonList.add(this.btnAddFarm);
		
		if (this.serverConfiguration.addFarm)
		{
			secondColumnY += 15;
		}
		
		this.btnAddCraftingTable = new GuiCheckBox(5, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CRAFTING_TABLE), this.houseConfiguration.addCraftingTable);
		this.btnAddCraftingTable.setStringColor(color);
		this.btnAddCraftingTable.setWithShadow(false);
		this.btnAddCraftingTable.visible = false;
		this.buttonList.add(this.btnAddCraftingTable);
		
		if (this.serverConfiguration.addCraftingTable)
		{
			y += 15;
		}
		
		this.btnAddTorches = new GuiCheckBox(1, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_TORCHES), this.houseConfiguration.addTorches);
		this.btnAddTorches.setStringColor(color);
		this.btnAddTorches.setWithShadow(false);
		this.btnAddTorches.visible = false;
		this.buttonList.add(this.btnAddTorches);
		
		if (this.serverConfiguration.addTorches)
		{
			y += 15;
		}

		this.btnAddChest = new GuiCheckBox(3, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST), this.houseConfiguration.addChest);
		this.btnAddChest.setStringColor(color);
		this.btnAddChest.setWithShadow(false);
		this.btnAddChest.visible = false;
		this.buttonList.add(this.btnAddChest);
		
		if (this.serverConfiguration.addChests)
		{
			y += 15;
		}

		this.btnAddMineShaft = new GuiCheckBox(7, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT), this.houseConfiguration.addMineShaft);
		this.btnAddMineShaft.setStringColor(color);
		this.btnAddMineShaft.setWithShadow(false);
		this.btnAddMineShaft.visible = false;
		this.buttonList.add(this.btnAddMineShaft);
		
		if (this.serverConfiguration.addMineshaft)
		{
			y += 15;
		}
		
		this.btnAddChestContents = new GuiCheckBox(4, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS), this.houseConfiguration.addChestContents);
		this.btnAddChestContents.setStringColor(color);
		this.btnAddChestContents.setWithShadow(false);
		this.btnAddChestContents.visible = false;
		this.buttonList.add(this.btnAddChestContents);
		
		if (this.allowItemsInChestAndFurnace)
		{
			y += 15;
		}
		
		this.btnIsCeilingFlat = new GuiCheckBox(8, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_CEILING_FLAT), this.houseConfiguration.isCeilingFlat);
		this.btnIsCeilingFlat.setStringColor(color);
		this.btnIsCeilingFlat.setWithShadow(false);
		this.btnIsCeilingFlat.visible = false;
		this.buttonList.add(this.btnIsCeilingFlat);
		
		this.btnFloorBlock = new GuiTextSlider(11, grayBoxX + 10, grayBoxY + 20, 90, 20, 0, 2, this.houseConfiguration.floorBlock.getValue(), GuiLangKeys.STARTER_HOUSE_FLOOR_STONE);
		this.buttonList.add(this.btnFloorBlock);

		this.btnCeilingBlock = new GuiTextSlider(12, grayBoxX + 10, grayBoxY + 60, 90, 20, 0, 2, this.houseConfiguration.ceilingBlock.getValue(), GuiLangKeys.STARTER_HOUSE_CEILING_TYPE);
		this.buttonList.add(this.btnCeilingBlock);

		this.btnWallWoodType = new GuiTextSlider(13, grayBoxX + 10, grayBoxY + 100, 90, 20, 0, 5, this.houseConfiguration.wallWoodType.getValue(), GuiLangKeys.STARTER_HOUSE_WALL_TYPE);
		this.buttonList.add(this.btnWallWoodType);
		
		this.btnGlassColor = new GuiButtonExt(17, grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateDye(this.houseConfiguration.glassColor));
		this.buttonList.add(this.btnGlassColor);
		
		// Column 2:
		
		// Column 3:
		this.btnHouseDepth = new GuiSlider(15, grayBoxX + 147, grayBoxY + 20, 90, 20, "", "", 5, serverConfiguration.maximumStartingHouseSize, this.houseConfiguration.houseDepth, false, true);
		this.buttonList.add(this.btnHouseDepth);
		
		this.btnHouseWidth = new GuiSlider(16, grayBoxX + 147, grayBoxY + 60, 90, 20, "", "", 5, serverConfiguration.maximumStartingHouseSize, this.houseConfiguration.houseWidth, false, true);
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
