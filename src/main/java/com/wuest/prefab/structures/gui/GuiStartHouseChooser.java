package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.ExtendedButton;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.structures.config.HouseConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.predefined.StructureAlternateStart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;

/**
 * @author WuestMan
 */
public class GuiStartHouseChooser extends GuiStructure {
    protected ServerModConfiguration serverConfiguration;

    // General:
    private ExtendedButton btnHouseStyle;
    // Blocks/Size
    private ExtendedButton btnGlassColor;
    private ExtendedButton btnBedColor;
    // Config:
    private GuiCheckBox btnAddChest;
    private GuiCheckBox btnAddChestContents;
    private GuiCheckBox btnAddMineShaft;
    private boolean allowItemsInChestAndFurnace = true;

    private HouseConfiguration specificConfiguration;

    public GuiStartHouseChooser() {
        super("Starter House");
        this.configurationEnum = StructureTagMessage.EnumStructureConfiguration.StartHouse;
    }

    @Override
    public Component getNarrationMessage() {
        return Component.translatable(GuiLangKeys.translateString(GuiLangKeys.TITLE_STARTER_HOUSE));
    }

    @Override
    protected void Initialize() {
        this.modifiedInitialXAxis = 215;
        this.modifiedInitialYAxis = 117;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;

        if (!Minecraft.getInstance().player.isCreative()) {
            this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
        } else {
            this.allowItemsInChestAndFurnace = true;
        }

        this.serverConfiguration = Prefab.proxy.getServerConfiguration();
        this.configuration = this.specificConfiguration = ClientEventHandler.playerConfig.getClientConfig("Starter House", HouseConfiguration.class);
        this.configuration.pos = this.pos;

        this.selectedStructure = StructureAlternateStart.CreateInstance(this.specificConfiguration.houseStyle.getStructureLocation(), StructureAlternateStart.class);

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnHouseStyle = this.createAndAddButton(grayBoxX + 8, grayBoxY + 25, 90, 20, this.specificConfiguration.houseStyle.getDisplayName(), false, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE));
        this.btnBedColor = this.createAndAddDyeButton(grayBoxX + 8, grayBoxY + 60, 90, 20, this.specificConfiguration.bedColor, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR));
        this.btnGlassColor = this.createAndAddFullDyeButton(grayBoxX + 8, grayBoxY + 95, 90, 20, this.specificConfiguration.glassColor, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS));
        this.btnAddChest = this.createAndAddCheckBox(grayBoxX + 8, grayBoxY + 120, GuiLangKeys.STARTER_HOUSE_ADD_CHEST, this.specificConfiguration.addChest, this::buttonClicked);
        this.btnAddMineShaft = this.createAndAddCheckBox(grayBoxX + 8, grayBoxY + 137, GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT, this.specificConfiguration.addMineShaft, this::buttonClicked);
        this.btnAddChestContents = this.createAndAddCheckBox(grayBoxX + 8, grayBoxY + 154, GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS, this.specificConfiguration.addChestContents, this::buttonClicked);

        // Create the standard buttons.
        this.btnVisualize = this.createAndAddCustomButton(grayBoxX + 26, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(grayBoxX + 313, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(grayBoxX + 165, grayBoxY + 177, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int imagePanelUpperLeft = x + 142;
        int imagePanelWidth = 285;
        int imagePanelMiddle = imagePanelWidth / 2;

        this.renderBackground(matrixStack);

        this.drawControlLeftPanel(matrixStack, x + 2, y + 10, 141, 190);
        this.drawControlRightPanel(matrixStack, imagePanelUpperLeft, y + 10, imagePanelWidth, 190);

        int middleOfImage = this.shownImageWidth / 2;
        int imageLocation = imagePanelUpperLeft + (imagePanelMiddle - middleOfImage);

        GuiUtils.bindAndDrawScaledTexture(
                this.specificConfiguration.houseStyle.getHousePicture(),
                matrixStack,
                imageLocation,
                y + 15,
                this.shownImageWidth,
                this.shownImageHeight,
                this.shownImageWidth,
                this.shownImageHeight,
                this.shownImageWidth,
                this.shownImageHeight);

        this.btnAddChest.visible = this.serverConfiguration.addChests;
        this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
        this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;
    }

    @Override
    protected void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Draw the text here.
        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 8, y + 15, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 8, y + 50, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 8, y + 85, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(AbstractButton button) {
        this.specificConfiguration.addBed = this.serverConfiguration.addBed;
        this.specificConfiguration.addChest = this.serverConfiguration.addChests && this.btnAddChest.isChecked();
        this.specificConfiguration.addChestContents = this.allowItemsInChestAndFurnace && (this.serverConfiguration.addChestContents && this.btnAddChestContents.isChecked());
        this.specificConfiguration.addCraftingTable = this.serverConfiguration.addCraftingTable;
        this.specificConfiguration.addFurnace = this.serverConfiguration.addFurnace;
        this.specificConfiguration.addMineShaft = this.serverConfiguration.addMineshaft && this.btnAddMineShaft.isChecked();
        this.specificConfiguration.addTorches = this.serverConfiguration.addTorches;
        this.configuration.houseFacing = this.getMinecraft().player.getDirection().getOpposite();

        this.performCancelOrBuildOrHouseFacing(button);

        if (button == this.btnHouseStyle) {
            int id = this.specificConfiguration.houseStyle.getValue() + 1;
            this.specificConfiguration.houseStyle = HouseConfiguration.HouseStyle.ValueOf(id);
            this.selectedStructure = StructureAlternateStart.CreateInstance(this.specificConfiguration.houseStyle.getStructureLocation(), StructureAlternateStart.class);
            GuiUtils.setButtonText(btnHouseStyle, this.specificConfiguration.houseStyle.getDisplayName());
        } else if (button == this.btnGlassColor) {
            this.specificConfiguration.glassColor = FullDyeColor.ById(this.specificConfiguration.glassColor.getId() + 1);
            GuiUtils.setButtonText(this.btnGlassColor, GuiLangKeys.translateFullDye(this.specificConfiguration.glassColor));
        } else if (button == this.btnBedColor) {
            this.specificConfiguration.bedColor = DyeColor.byId(this.specificConfiguration.bedColor.getId() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.specificConfiguration.bedColor));
        } else if (button == this.btnVisualize) {
            this.performPreview();
        }
    }
}
