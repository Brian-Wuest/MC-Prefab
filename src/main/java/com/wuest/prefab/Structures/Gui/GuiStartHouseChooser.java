package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Config.ServerModConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Gui.Controls.GuiTab;
import com.wuest.prefab.Gui.Controls.GuiTextSlider;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Structures.Config.HouseConfiguration;
import com.wuest.prefab.Structures.Config.HouseConfiguration.HouseStyle;
import com.wuest.prefab.Structures.Messages.StructureTagMessage;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureAlternateStart;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.awt.*;

/**
 * @author WuestMan
 */
public class GuiStartHouseChooser extends GuiTabScreen {
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
	protected GuiCheckBox btnAddMineShaft;

	protected ServerModConfiguration serverConfiguration;
	protected boolean allowItemsInChestAndFurnace = true;

	public HouseConfiguration houseConfiguration;

	public GuiStartHouseChooser() {
		super();
		this.Tabs.setWidth(256);
	}

	@Override
	public void init() {
		super.init();

		if (!this.minecraft.player.isCreative()) {
			this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
		}

		this.Initialize();
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void render(int x, int y, float f) {
		int grayBoxX = (this.width / 2) - 198;
		int grayBoxY = (this.height / 2) - 83;
		this.Tabs.x = grayBoxX;
		this.Tabs.y = grayBoxY - 21;

		this.renderBackground();

		// Draw the control background.
		this.minecraft.getTextureManager().bindTexture(backgroundTextures);
		this.blit(grayBoxX, grayBoxY, 0, 0, 256, 256);

		for (Widget button : this.buttons) {
			// Make all buttons invisible.
			if (button != this.btnCancel && button != this.btnBuild) {
				button.visible = false;
			}
		}

		this.btnAddTorches.visible = false;
		this.btnAddBed.visible = false;
		this.btnAddChest.visible = false;
		this.btnAddChestContents.visible = false;
		this.btnAddCraftingTable.visible = false;
		this.btnAddFurnace.visible = false;
		this.btnAddMineShaft.visible = false;

		// Update visibility on controls based on the selected tab.
		if (this.getSelectedTab() == this.tabGeneral) {
			this.btnHouseStyle.visible = true;
			this.btnVisualize.visible = true;
		} else if (this.getSelectedTab() == this.tabConfig) {
			this.btnAddTorches.visible = this.serverConfiguration.addTorches;
			this.btnAddBed.visible = this.serverConfiguration.addBed;
			this.btnAddChest.visible = this.serverConfiguration.addChests;
			this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
			this.btnAddCraftingTable.visible = this.serverConfiguration.addCraftingTable;
			this.btnAddFurnace.visible = this.serverConfiguration.addFurnace;
			this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;

		} else if (this.getSelectedTab() == this.tabBlockTypes) {
			this.btnGlassColor.visible = this.houseConfiguration.houseStyle != HouseStyle.SNOWY
					&& this.houseConfiguration.houseStyle != HouseStyle.DESERT;
		}

		// Draw the buttons, labels and tabs.
		super.render(x, y, f);

		// Draw the text here.
		int color = Color.DARK_GRAY.getRGB();

		// Draw the appropriate text based on the selected tab.
		if (this.getSelectedTab() == this.tabGeneral) {
			this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), grayBoxX + 10, grayBoxY + 10, color);
			this.minecraft.fontRenderer.drawSplitString(this.houseConfiguration.houseStyle.getHouseNotes(), grayBoxX + 147, grayBoxY + 10, 95, color);

			this.minecraft.getTextureManager().bindTexture(this.houseConfiguration.houseStyle.getHousePicture());
			GuiTabScreen.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1,
					this.houseConfiguration.houseStyle.getImageWidth(), this.houseConfiguration.houseStyle.getImageHeight(),
					this.houseConfiguration.houseStyle.getImageWidth(), this.houseConfiguration.houseStyle.getImageHeight());
		} else if (this.getSelectedTab() == this.tabBlockTypes) {
			if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.SNOWY
					|| this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.DESERT) {

			} else {
				// Column 1:
				this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), grayBoxX + 10, grayBoxY + 10, color);
			}
		}

		if (!CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {
			this.btnVisualize.visible = false;
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	public void actionPerformed(Button button) {
		if (button == this.btnCancel || button == this.btnVisualize
				|| button == this.btnBuild) {
			this.houseConfiguration.addBed = this.serverConfiguration.addBed && this.btnAddBed.isChecked();
			this.houseConfiguration.addChest = this.serverConfiguration.addChests && this.btnAddChest.isChecked();
			this.houseConfiguration.addChestContents = this.allowItemsInChestAndFurnace && (this.serverConfiguration.addChestContents && this.btnAddChestContents.isChecked());
			this.houseConfiguration.addCraftingTable = this.serverConfiguration.addCraftingTable && this.btnAddCraftingTable.isChecked();
			this.houseConfiguration.addFurnace = this.serverConfiguration.addFurnace && this.btnAddFurnace.isChecked();
			this.houseConfiguration.addMineShaft = this.serverConfiguration.addMineshaft && this.btnAddMineShaft.isChecked();
			this.houseConfiguration.addTorches = this.serverConfiguration.addTorches && this.btnAddTorches.isChecked();
			this.houseConfiguration.houseFacing = this.minecraft.player.getHorizontalFacing().getOpposite();
		}

		if (button == this.btnCancel) {
			this.minecraft.displayGuiScreen(null);
		} else if (button == this.btnBuild) {
			Prefab.network.sendToServer(new StructureTagMessage(this.houseConfiguration.WriteToCompoundNBT(), EnumStructureConfiguration.StartHouse));

			this.minecraft.displayGuiScreen(null);
		} else if (button == this.btnHouseStyle) {
			int id = this.houseConfiguration.houseStyle.getValue() + 1;
			this.houseConfiguration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);

			// Skip the loft if it's not enabled.
			if (this.houseConfiguration.houseStyle == HouseStyle.LOFT
					&& !this.serverConfiguration.enableLoftHouse) {
				id = this.houseConfiguration.houseStyle.getValue() + 1;
				this.houseConfiguration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);
			}

			this.btnHouseStyle.setMessage(this.houseConfiguration.houseStyle.getDisplayName());

			// Set the default glass colors for this style.
			if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.HOBBIT) {
				this.houseConfiguration.glassColor = DyeColor.GREEN;
				this.btnGlassColor.setMessage(GuiLangKeys.translateDye(DyeColor.GREEN));
			} else if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.LOFT) {
				this.houseConfiguration.glassColor = DyeColor.BLACK;
				this.btnGlassColor.setMessage(GuiLangKeys.translateDye(DyeColor.BLACK));
			} else if (this.houseConfiguration.houseStyle == HouseStyle.BASIC) {
				this.houseConfiguration.glassColor = DyeColor.LIGHT_GRAY;
				this.btnGlassColor.setMessage(GuiLangKeys.translateDye(DyeColor.LIGHT_GRAY));
			} else {
				this.houseConfiguration.glassColor = DyeColor.CYAN;
				this.btnGlassColor.setMessage(GuiLangKeys.translateDye(DyeColor.CYAN));
			}

			this.tabBlockTypes.visible = this.houseConfiguration.houseStyle != HouseStyle.DESERT
					&& this.houseConfiguration.houseStyle != HouseStyle.SNOWY;
		} else if (button == this.btnGlassColor) {
			this.houseConfiguration.glassColor = DyeColor.byId(this.houseConfiguration.glassColor.getId() + 1);
			this.btnGlassColor.setMessage(GuiLangKeys.translateDye(this.houseConfiguration.glassColor));
		} else if (button == this.btnVisualize) {
			StructureAlternateStart structure = StructureAlternateStart.CreateInstance(this.houseConfiguration.houseStyle.getStructureLocation(), StructureAlternateStart.class);

			StructureRenderHandler.setStructure(structure, Direction.NORTH, this.houseConfiguration);
			this.minecraft.displayGuiScreen(null);
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	@Override
	public boolean isPauseScreen() {
		return true;
	}

	@Override
	protected void Initialize() {
		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 198;
		int grayBoxY = (this.height / 2) - 83;
		int color = Color.DARK_GRAY.getRGB();
		this.serverConfiguration = Prefab.proxy.getServerConfiguration();
		this.houseConfiguration = ClientEventHandler.playerConfig.getClientConfig("Starter House", HouseConfiguration.class);
		this.houseConfiguration.pos = this.pos;

		// Create the Controls.
		// Column 1:
		this.btnHouseStyle = new GuiButtonExt(grayBoxX + 10, grayBoxY + 20, 90, 20, this.houseConfiguration.houseStyle.getDisplayName(), (button) ->
		{
			this.actionPerformed(button);
		});

		this.addButton(this.btnHouseStyle);

		this.btnVisualize = new GuiButtonExt(grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW), (button) ->
		{
			this.actionPerformed(button);
		});
		this.addButton(this.btnVisualize);

		int x = grayBoxX + 10;
		int y = grayBoxY + 10;
		int secondColumnY = y;
		int secondColumnX = x + 137;

		this.btnAddFurnace = new GuiCheckBox(secondColumnX, secondColumnY, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_FURNACE), this.houseConfiguration.addFurnace, null);
		this.btnAddFurnace.setFGColor(color);
		this.btnAddFurnace.setWithShadow(false);
		this.btnAddFurnace.visible = false;
		this.addButton(this.btnAddFurnace);

		if (this.serverConfiguration.addFurnace) {
			secondColumnY += 15;
		}

		this.btnAddBed = new GuiCheckBox(secondColumnX, secondColumnY, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_BED), this.houseConfiguration.addBed, null);
		this.btnAddBed.setFGColor(color);
		this.btnAddBed.setWithShadow(false);
		this.btnAddBed.visible = false;
		this.addButton(this.btnAddBed);

		if (this.serverConfiguration.addBed) {
			secondColumnY += 15;
		}
		
		this.btnAddCraftingTable = new GuiCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CRAFTING_TABLE), this.houseConfiguration.addCraftingTable, null);
		this.btnAddCraftingTable.setFGColor(color);
		this.btnAddCraftingTable.setWithShadow(false);
		this.btnAddCraftingTable.visible = false;
		this.addButton(this.btnAddCraftingTable);

		if (this.serverConfiguration.addCraftingTable) {
			y += 15;
		}

		this.btnAddTorches = new GuiCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_TORCHES), this.houseConfiguration.addTorches, null);
		this.btnAddTorches.setFGColor(color);
		this.btnAddTorches.setWithShadow(false);
		this.btnAddTorches.visible = false;
		this.addButton(this.btnAddTorches);

		if (this.serverConfiguration.addTorches) {
			y += 15;
		}

		this.btnAddChest = new GuiCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST), this.houseConfiguration.addChest, null);
		this.btnAddChest.setFGColor(color);
		this.btnAddChest.setWithShadow(false);
		this.btnAddChest.visible = false;
		this.addButton(this.btnAddChest);

		if (this.serverConfiguration.addChests) {
			y += 15;
		}

		this.btnAddMineShaft = new GuiCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT), this.houseConfiguration.addMineShaft, null);
		this.btnAddMineShaft.setFGColor(color);
		this.btnAddMineShaft.setWithShadow(false);
		this.btnAddMineShaft.visible = false;
		this.addButton(this.btnAddMineShaft);

		if (this.serverConfiguration.addMineshaft) {
			y += 15;
		}

		this.btnAddChestContents = new GuiCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS), this.houseConfiguration.addChestContents, null);
		this.btnAddChestContents.setFGColor(color);
		this.btnAddChestContents.setWithShadow(false);
		this.btnAddChestContents.visible = false;
		this.addButton(this.btnAddChestContents);

		if (this.allowItemsInChestAndFurnace) {
			y += 15;
		}

		this.btnGlassColor = new GuiButtonExt(grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateDye(this.houseConfiguration.glassColor), this::actionPerformed);
		this.addButton(this.btnGlassColor);

		// Column 2:

		// Column 3:

		// Tabs:
		this.tabGeneral = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_GENERAL), grayBoxX + 3, grayBoxY - 20);
		this.Tabs.AddTab(this.tabGeneral);

		this.tabConfig = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_CONFIG), grayBoxX + 54, grayBoxY - 20);
		this.Tabs.AddTab(this.tabConfig);

		this.tabBlockTypes = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_BLOCK), grayBoxX + 105, grayBoxY - 20);
		this.tabBlockTypes.setWidth(70);
		this.Tabs.AddTab(this.tabBlockTypes);

		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD), this::actionPerformed);
		this.addButton(this.btnBuild);

		this.btnCancel = new GuiButtonExt(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL), this::actionPerformed);
		this.addButton(this.btnCancel);
	}
}
