package com.wuest.prefab.Structures.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Blocks.FullDyeColor;
import com.wuest.prefab.Config.ServerModConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Gui.Controls.GuiTab;
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
import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.awt.*;

/**
 * @author WuestMan
 */
public class GuiStartHouseChooser extends GuiTabScreen {
	private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
	protected ExtendedButton btnCancel;
	protected ExtendedButton btnBuild;
	protected ExtendedButton btnVisualize;
	protected ServerModConfiguration serverConfiguration;
	// Tabs
	private GuiTab tabGeneral;
	private GuiTab tabConfig;
	private GuiTab tabBlockTypes;
	// General:
	private ExtendedButton btnHouseStyle;
	// Blocks/Size
	private ExtendedButton btnGlassColor;
	private ExtendedButton btnBedColor;
	// Config:
	private GuiCheckBox btnAddTorches;
	private GuiCheckBox btnAddBed;
	private GuiCheckBox btnAddCraftingTable;
	private GuiCheckBox btnAddFurnace;
	private GuiCheckBox btnAddChest;
	private GuiCheckBox btnAddChestContents;
	private GuiCheckBox btnAddMineShaft;
	private boolean allowItemsInChestAndFurnace = true;

	private HouseConfiguration houseConfiguration;

	public GuiStartHouseChooser() {
		super();
		this.Tabs.setWidth(256);
		this.modifiedInitialXAxis = 198;
		this.modifiedInitialYAxis = 83;
		this.structureConfiguration = EnumStructureConfiguration.StartHouse;
	}

	@Override
	public void init() {
		super.init();

		assert this.minecraft != null;
		if (!this.minecraft.player.isCreative()) {
			this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
		}

		this.Initialize();
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void render(MatrixStack matrixStack, int x, int y, float f) {
		Tuple<Integer, Integer> adjustedValueCoords = this.getAdjustedXYValue();
		int grayBoxX = adjustedValueCoords.getFirst();
		int grayBoxY = adjustedValueCoords.getSecond();

		this.Tabs.x = adjustedValueCoords.getFirst();
		this.Tabs.y = adjustedValueCoords.getSecond() - 21;

		this.renderBackground(matrixStack);

		// Draw the control background.
		assert this.minecraft != null;
		this.bindTexture(backgroundTextures);
		this.blit(matrixStack,  grayBoxX, grayBoxY, 0, 0, 256, 256);

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
		this.btnBedColor.visible = false;

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

			this.btnBedColor.visible = true;
		}

		// Draw the buttons, labels and tabs.
		super.render(matrixStack,  x, y, f);

		// Draw the text here.
		int color = Color.DARK_GRAY.getRGB();

		// Draw the appropriate text based on the selected tab.
		if (this.getSelectedTab() == this.tabGeneral) {
			this.drawString(matrixStack,  GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), grayBoxX + 10, grayBoxY + 10, color);
			this.drawSplitString(this.houseConfiguration.houseStyle.getHouseNotes(), grayBoxX + 147, grayBoxY + 10, 95, color);

			this.bindTexture(this.houseConfiguration.houseStyle.getHousePicture());
			GuiTabScreen.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1,
					this.houseConfiguration.houseStyle.getImageWidth(), this.houseConfiguration.houseStyle.getImageHeight(),
					this.houseConfiguration.houseStyle.getImageWidth(), this.houseConfiguration.houseStyle.getImageHeight());
		} else if (this.getSelectedTab() == this.tabBlockTypes) {
			if (!(this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.SNOWY
					|| this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.DESERT)) {
				// Column 1:
				this.drawString(matrixStack,  GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), grayBoxX + 10, grayBoxY + 10, color);
			}

			// Column 2:
			this.drawString(matrixStack,  GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), grayBoxX + 147, grayBoxY + 10, color);
		}

		if (!CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {
			this.btnVisualize.visible = false;
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	public void buttonClicked(AbstractButton button) {
		if (button == this.btnCancel || button == this.btnVisualize
				|| button == this.btnBuild) {
			this.houseConfiguration.addBed = this.serverConfiguration.addBed && this.btnAddBed.selected();
			this.houseConfiguration.addChest = this.serverConfiguration.addChests && this.btnAddChest.selected();
			this.houseConfiguration.addChestContents = this.allowItemsInChestAndFurnace && (this.serverConfiguration.addChestContents && this.btnAddChestContents.selected());
			this.houseConfiguration.addCraftingTable = this.serverConfiguration.addCraftingTable && this.btnAddCraftingTable.selected();
			this.houseConfiguration.addFurnace = this.serverConfiguration.addFurnace && this.btnAddFurnace.selected();
			this.houseConfiguration.addMineShaft = this.serverConfiguration.addMineshaft && this.btnAddMineShaft.selected();
			this.houseConfiguration.addTorches = this.serverConfiguration.addTorches && this.btnAddTorches.selected();
			assert this.minecraft != null;
			this.houseConfiguration.houseFacing = this.minecraft.player.getDirection().getOpposite();
		}

		if (button == this.btnCancel) {
			this.closeScreen();
		} else if (button == this.btnBuild) {
			Prefab.network.sendToServer(new StructureTagMessage(this.houseConfiguration.WriteToCompoundNBT(), this.structureConfiguration));

			this.closeScreen();
		} else if (button == this.btnHouseStyle) {
			int id = this.houseConfiguration.houseStyle.getValue() + 1;
			this.houseConfiguration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);

			// Skip the loft if it's not enabled.
			if (this.houseConfiguration.houseStyle == HouseStyle.LOFT
					&& !this.serverConfiguration.enableLoftHouse) {
				id = this.houseConfiguration.houseStyle.getValue() + 1;
				this.houseConfiguration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);
			}

			this.btnHouseStyle.setMessage(new StringTextComponent( this.houseConfiguration.houseStyle.getDisplayName()));

			// Set the default glass colors for this style.
			if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.HOBBIT) {
				this.houseConfiguration.glassColor = FullDyeColor.GREEN;
				this.btnGlassColor.setMessage(new StringTextComponent(GuiLangKeys.translateDye(DyeColor.GREEN)));
			} else if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.LOFT) {
				this.houseConfiguration.glassColor = FullDyeColor.BLACK;
				this.btnGlassColor.setMessage(new StringTextComponent(GuiLangKeys.translateDye(DyeColor.BLACK)));
			} else if (this.houseConfiguration.houseStyle == HouseStyle.BASIC) {
				this.houseConfiguration.glassColor = FullDyeColor.LIGHT_GRAY;
				this.btnGlassColor.setMessage(new StringTextComponent(GuiLangKeys.translateDye(DyeColor.LIGHT_GRAY)));
			} else if (this.houseConfiguration.houseStyle == HouseStyle.DESERT2) {
				this.houseConfiguration.glassColor = FullDyeColor.RED;
				this.btnGlassColor.setMessage(new StringTextComponent(GuiLangKeys.translateDye(DyeColor.RED)));
			} else {
				this.houseConfiguration.glassColor = FullDyeColor.CYAN;
				this.btnGlassColor.setMessage(new StringTextComponent(GuiLangKeys.translateDye(DyeColor.CYAN)));
			}

			this.tabBlockTypes.visible = true;

		} else if (button == this.btnGlassColor) {
			this.houseConfiguration.glassColor = FullDyeColor.ById(this.houseConfiguration.glassColor.getId() + 1);
			this.btnGlassColor.setMessage(new StringTextComponent(GuiLangKeys.translateFullDye(this.houseConfiguration.glassColor)));
		} else if (button == this.btnBedColor) {
			this.houseConfiguration.bedColor = DyeColor.byId(this.houseConfiguration.bedColor.getId() + 1);
			this.btnBedColor.setMessage(new StringTextComponent(GuiLangKeys.translateDye(this.houseConfiguration.bedColor)));
		} else if (button == this.btnVisualize) {
			StructureAlternateStart structure = StructureAlternateStart.CreateInstance(this.houseConfiguration.houseStyle.getStructureLocation(), StructureAlternateStart.class);

			StructureRenderHandler.setStructure(structure, Direction.NORTH, this.houseConfiguration);
			this.closeScreen();
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
		Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
		int grayBoxX = adjustedXYValue.getFirst();
		int grayBoxY = adjustedXYValue.getSecond();
		int color = Color.DARK_GRAY.getRGB();
		this.serverConfiguration = Prefab.proxy.getServerConfiguration();
		this.houseConfiguration = ClientEventHandler.playerConfig.getClientConfig("Starter House", HouseConfiguration.class);
		this.houseConfiguration.pos = this.pos;

		// Create the Controls.
		// Column 1:
		this.btnHouseStyle = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, this.houseConfiguration.houseStyle.getDisplayName());

		this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		int x = grayBoxX + 10;
		int y = grayBoxY + 10;
		int secondColumnY = y;
		int secondColumnX = x + 137;

		this.btnAddFurnace = this.createAndAddCheckBox(secondColumnX - 10, secondColumnY, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_FURNACE), this.houseConfiguration.addFurnace, null);
		this.btnAddFurnace.visible = false;

		if (this.serverConfiguration.addFurnace) {
			secondColumnY += 20;
		}

		this.btnAddBed = this.createAndAddCheckBox(secondColumnX - 10, secondColumnY, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_BED), this.houseConfiguration.addBed, null);
		this.btnAddBed.visible = false;

		if (this.serverConfiguration.addBed) {
			secondColumnY += 20;
		}

		this.btnAddCraftingTable = this.createAndAddCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CRAFTING_TABLE), this.houseConfiguration.addCraftingTable, null);
		this.btnAddCraftingTable.visible = false;

		if (this.serverConfiguration.addCraftingTable) {
			y += 20;
		}

		this.btnAddTorches = this.createAndAddCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_TORCHES), this.houseConfiguration.addTorches, null);
		this.btnAddTorches.visible = false;

		if (this.serverConfiguration.addTorches) {
			y += 20;
		}

		this.btnAddChest = this.createAndAddCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST), this.houseConfiguration.addChest, null);
		this.btnAddChest.visible = false;

		if (this.serverConfiguration.addChests) {
			y += 20;
		}

		this.btnAddMineShaft = this.createAndAddCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT), this.houseConfiguration.addMineShaft, null);
		this.btnAddMineShaft.visible = false;

		if (this.serverConfiguration.addMineshaft) {
			y += 20;
		}

		this.btnAddChestContents = this.createAndAddCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS), this.houseConfiguration.addChestContents, null);
		this.btnAddChestContents.visible = false;

		if (this.allowItemsInChestAndFurnace) {
			y += 20;
		}

		x = grayBoxX + 10;
		y = grayBoxY + 20;

		this.btnGlassColor = this.createAndAddButton(x, y, 90, 20, GuiLangKeys.translateFullDye(this.houseConfiguration.glassColor));

		// Column 2:
		x = secondColumnX;
		this.btnBedColor = this.createAndAddButton(x, y, 90, 20, GuiLangKeys.translateDye(this.houseConfiguration.bedColor));

		// Column 3:

		// Tabs:
		this.tabGeneral = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_GENERAL), grayBoxX + 3, grayBoxY - 20);
		this.tabGeneral.setWidth(80);
		this.Tabs.AddTab(this.tabGeneral);

		this.tabConfig = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_CONFIG), grayBoxX + 84, grayBoxY - 20);
		this.tabConfig.setWidth(80);
		this.Tabs.AddTab(this.tabConfig);

		this.tabBlockTypes = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_BLOCK), grayBoxX + 165, grayBoxY - 20);
		this.tabBlockTypes.setWidth(80);
		this.Tabs.AddTab(this.tabBlockTypes);

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
	}
}
