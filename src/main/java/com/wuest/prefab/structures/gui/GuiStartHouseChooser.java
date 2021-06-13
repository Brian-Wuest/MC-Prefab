package com.wuest.prefab.structures.gui;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.config.ModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiTabScreen;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.gui.controls.GuiTab;
import com.wuest.prefab.structures.config.HouseConfiguration;
import com.wuest.prefab.structures.config.HouseConfiguration.HouseStyle;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureAlternateStart;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;

/**
 * @author WuestMan
 */
public class GuiStartHouseChooser extends GuiTabScreen {
    private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
    protected GuiButtonExt btnCancel;
    protected GuiButtonExt btnBuild;
    protected GuiButtonExt btnVisualize;
    protected ModConfiguration serverConfiguration;
    // Tabs
    private GuiTab tabGeneral;
    private GuiTab tabConfig;
    private GuiTab tabBlockTypes;
    // General:
    private GuiButtonExt btnHouseStyle;
    // Blocks/Size
    private GuiButtonExt btnGlassColor;
    private GuiButtonExt btnBedColor;
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
        this.Tabs.trayWidth = 256;
        this.modifiedInitialXAxis = 198;
        this.modifiedInitialYAxis = 83;
        this.structureConfiguration = EnumStructureConfiguration.StartHouse;
    }

    @Override
    public void initGui() {
        super.initGui();

        assert this.mc != null;
        if (!this.mc.player.isCreative()) {
            this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
        }

        this.Initialize();
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
    public void drawScreen(int x, int y, float f) {
        Tuple<Integer, Integer> adjustedValueCoords = this.getAdjustedXYValue();
        int grayBoxX = adjustedValueCoords.getFirst();
        int grayBoxY = adjustedValueCoords.getSecond();

        this.Tabs.trayX = adjustedValueCoords.getFirst();
        this.Tabs.trayY = adjustedValueCoords.getSecond() - 21;

        this.drawDefaultBackground();

        // Draw the control background.
        GuiUtils.bindTexture(backgroundTextures);
        this.drawTexturedModalRect(grayBoxX, grayBoxY, 0, 0, 256, 256);

        for (GuiButton button : this.buttonList) {
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
        super.drawScreen(x, y, f);

        // Draw the text here.
        int color = Color.DARK_GRAY.getRGB();

        // Draw the appropriate text based on the selected tab.
        if (this.getSelectedTab() == this.tabGeneral) {
            this.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), grayBoxX + 10, grayBoxY + 10, color);
            this.drawSplitString(this.houseConfiguration.houseStyle.getHouseNotes(), grayBoxX + 147, grayBoxY + 10, 95, color);

            GuiUtils.bindAndDrawModalRectWithCustomSizedTexture(this.houseConfiguration.houseStyle.getHousePicture(), grayBoxX + 250, grayBoxY, 1,
                    this.houseConfiguration.houseStyle.getImageWidth(), this.houseConfiguration.houseStyle.getImageHeight(),
                    this.houseConfiguration.houseStyle.getImageWidth(), this.houseConfiguration.houseStyle.getImageHeight());
        } else if (this.getSelectedTab() == this.tabBlockTypes) {
            if (!(this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.SNOWY
                    || this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.DESERT)) {
                // Column 1:
                this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), grayBoxX + 10, grayBoxY + 10, color);
            }

            // Column 2:
            this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), grayBoxX + 147, grayBoxY + 10, color);
        }

        if (!Prefab.proxy.getServerConfiguration().enableStructurePreview) {
            this.btnVisualize.visible = false;
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void actionPerformed(GuiButton button) {
        if (button == this.btnCancel || button == this.btnVisualize
                || button == this.btnBuild) {
            this.houseConfiguration.addBed = this.serverConfiguration.addBed && this.btnAddBed.isChecked();
            this.houseConfiguration.addChest = this.serverConfiguration.addChests && this.btnAddChest.isChecked();
            this.houseConfiguration.addChestContents = this.allowItemsInChestAndFurnace && (this.serverConfiguration.addChestContents && this.btnAddChestContents.isChecked());
            this.houseConfiguration.addCraftingTable = this.serverConfiguration.addCraftingTable && this.btnAddCraftingTable.isChecked();
            this.houseConfiguration.addFurnace = this.serverConfiguration.addFurnace && this.btnAddFurnace.isChecked();
            this.houseConfiguration.addMineShaft = this.serverConfiguration.addMineshaft && this.btnAddMineShaft.isChecked();
            this.houseConfiguration.addTorches = this.serverConfiguration.addTorches && this.btnAddTorches.isChecked();
            assert this.mc != null;
            this.houseConfiguration.houseFacing = this.mc.player.getHorizontalFacing().getOpposite();
        }

        if (button == this.btnCancel) {
            this.closeScreen();
        } else if (button == this.btnBuild) {
            Prefab.network.sendToServer(new StructureTagMessage(this.houseConfiguration.WriteToNBTTagCompound(), this.structureConfiguration));

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

            GuiUtils.setButtonText(btnHouseStyle, this.houseConfiguration.houseStyle.getDisplayName());

            // Set the default glass colors for this style.
            if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.HOBBIT) {
                this.houseConfiguration.glassColor = FullDyeColor.GREEN;
                GuiUtils.setButtonText(btnGlassColor, GuiLangKeys.translateDye(EnumDyeColor.GREEN));
            } else if (this.houseConfiguration.houseStyle == HouseConfiguration.HouseStyle.LOFT) {
                this.houseConfiguration.glassColor = FullDyeColor.BLACK;
                GuiUtils.setButtonText(btnGlassColor, GuiLangKeys.translateDye(EnumDyeColor.BLACK));
            } else if (this.houseConfiguration.houseStyle == HouseStyle.BASIC) {
                this.houseConfiguration.glassColor = FullDyeColor.LIGHT_GRAY;
                GuiUtils.setButtonText(btnGlassColor, GuiLangKeys.translateDye(EnumDyeColor.SILVER));
            } else {
                this.houseConfiguration.glassColor = FullDyeColor.CYAN;
                GuiUtils.setButtonText(btnGlassColor, GuiLangKeys.translateDye(EnumDyeColor.CYAN));
            }

            this.tabBlockTypes.visible = true;

        } else if (button == this.btnGlassColor) {
            this.houseConfiguration.glassColor = FullDyeColor.ById(this.houseConfiguration.glassColor.getId() + 1);
            GuiUtils.setButtonText(btnGlassColor, GuiLangKeys.translateFullDye(this.houseConfiguration.glassColor));
        } else if (button == this.btnBedColor) {
            this.houseConfiguration.bedColor = EnumDyeColor.byMetadata(this.houseConfiguration.bedColor.getMetadata() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.houseConfiguration.bedColor));
        } else if (button == this.btnVisualize) {
            StructureAlternateStart structure = StructureAlternateStart.CreateInstance(this.houseConfiguration.houseStyle.getStructureLocation(), StructureAlternateStart.class);

            StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.houseConfiguration);
            this.closeScreen();
        }
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
        this.btnHouseStyle = this.createAndAddButton(1, grayBoxX + 10, grayBoxY + 20, 90, 20, this.houseConfiguration.houseStyle.getDisplayName(), false);

        this.btnVisualize = this.createAndAddButton(2, grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);

        int x = grayBoxX + 10;
        int y = grayBoxY + 10;
        int secondColumnY = y;
        int secondColumnX = x + 137;

        this.btnAddFurnace = this.createAndAddCheckBox(3, secondColumnX - 10, secondColumnY, GuiLangKeys.STARTER_HOUSE_ADD_FURNACE, this.houseConfiguration.addFurnace);
        this.btnAddFurnace.visible = false;

        if (this.serverConfiguration.addFurnace) {
            secondColumnY += 20;
        }

        this.btnAddBed = this.createAndAddCheckBox(4, secondColumnX - 10, secondColumnY, GuiLangKeys.STARTER_HOUSE_ADD_BED, this.houseConfiguration.addBed);
        this.btnAddBed.visible = false;

        if (this.serverConfiguration.addBed) {
            secondColumnY += 20;
        }

        this.btnAddCraftingTable = this.createAndAddCheckBox(5, x, y, GuiLangKeys.STARTER_HOUSE_ADD_CRAFTING_TABLE, this.houseConfiguration.addCraftingTable);
        this.btnAddCraftingTable.visible = false;

        if (this.serverConfiguration.addCraftingTable) {
            y += 20;
        }

        this.btnAddTorches = this.createAndAddCheckBox(6, x, y, GuiLangKeys.STARTER_HOUSE_ADD_TORCHES, this.houseConfiguration.addTorches);
        this.btnAddTorches.visible = false;

        if (this.serverConfiguration.addTorches) {
            y += 20;
        }

        this.btnAddChest = this.createAndAddCheckBox(7, x, y, GuiLangKeys.STARTER_HOUSE_ADD_CHEST, this.houseConfiguration.addChest);
        this.btnAddChest.visible = false;

        if (this.serverConfiguration.addChests) {
            y += 20;
        }

        this.btnAddMineShaft = this.createAndAddCheckBox(8, x, y, GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT, this.houseConfiguration.addMineShaft);
        this.btnAddMineShaft.visible = false;

        if (this.serverConfiguration.addMineshaft) {
            y += 20;
        }

        this.btnAddChestContents = this.createAndAddCheckBox(9, x, y, GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS, this.houseConfiguration.addChestContents);
        this.btnAddChestContents.visible = false;

        if (this.allowItemsInChestAndFurnace) {
            y += 20;
        }

        x = grayBoxX + 10;
        y = grayBoxY + 20;

        this.btnGlassColor = this.createAndAddFullDyeButton(10, x, y, 90, 20, this.houseConfiguration.glassColor);

        // Column 2:
        x = secondColumnX;
        this.btnBedColor = this.createAndAddDyeButton(11, x, y, 90, 20, this.houseConfiguration.bedColor);

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
        this.btnBuild = this.createAndAddButton(12, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);

        this.btnCancel = this.createAndAddButton(13, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }
}
