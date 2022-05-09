package com.wuest.prefab.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.GuiItemList;
import com.wuest.prefab.gui.controls.GuiListBox;
import com.wuest.prefab.gui.controls.GuiListBox.ListEntry;
import com.wuest.prefab.gui.controls.TextureButton;
import com.wuest.prefab.gui.screens.menus.DraftingTableMenu;
import com.wuest.prefab.proxy.ClientProxy;
import com.wuest.prefab.structures.custom.base.CustomStructureInfo;
import com.wuest.prefab.structures.custom.base.ItemInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

public class GuiDraftingTable extends AbstractContainerScreen<DraftingTableMenu> implements MenuAccess<DraftingTableMenu>, DraftingTableMenu.IStructureMaterialLoader {
    private final ResourceLocation backgroundTexture = new ResourceLocation("prefab", "textures/gui/drafter.png");
    private final ResourceLocation schematicDefault = new ResourceLocation("prefab", "textures/gui/schematics.png");
    private final ResourceLocation schematicSelected = new ResourceLocation("prefab", "textures/gui/schematics_selected.png");
    private final ResourceLocation schematicHover = new ResourceLocation("prefab", "textures/gui/schematics_hovered.png");
    private final ResourceLocation schematicHoverSelected = new ResourceLocation("prefab", "textures/gui/schematics_selected_hovered.png");

    private final ResourceLocation materialDefault = new ResourceLocation("prefab", "textures/gui/materials.png");
    private final ResourceLocation materialSelected = new ResourceLocation("prefab", "textures/gui/materials_selected.png");
    private final ResourceLocation materialHover = new ResourceLocation("prefab", "textures/gui/materials_hovered.png");
    private final ResourceLocation materialHoverSelected = new ResourceLocation("prefab", "textures/gui/materials_selected_hovered.png");

    private int modifiedInitialXAxis = 0;
    private int modifiedInitialYAxis = 0;
    private final int textColor = Color.DARK_GRAY.getRGB();
    private TextureButton schematicsButton;
    private TextureButton materialsButton;
    private GuiListBox schematicsList;
    private GuiItemList materialsList;
    private boolean showingMaterials;
    private CustomStructureInfo selectedStructureInfo;

    public GuiDraftingTable(DraftingTableMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.showingMaterials = false;
    }

    public @Nonnull
    Minecraft getMinecraft() {
        return this.minecraft;
    }

    @Override
    protected void init() {
        this.imageWidth = 176;
        this.imageHeight = 237;

        super.init();

        this.modifiedInitialXAxis = 88;
        this.modifiedInitialYAxis = 120;

        Tuple<Integer, Integer> adjustedXYValues = this.getAdjustedXYValue();
        int adjustedX = adjustedXYValues.getFirst();
        int adjustedY = adjustedXYValues.getSecond();

        // Starting position.
        int bufferColor = new Color(198, 198, 198).getRGB();
        this.schematicsList = new GuiListBox(this.minecraft, 157, 100, adjustedX + 7, adjustedY + 22, 16, bufferColor, this::structureListEntryChanged);

        this.loadCustomStructureEntries();

        if (!this.showingMaterials) {
            this.addRenderableWidget(this.schematicsList);
        }

        this.materialsList = new GuiItemList(this.minecraft, 157, 100, adjustedX + 7, adjustedY + 22, 22, bufferColor, null);
        this.materialsList.setVisible(this.showingMaterials);

        this.loadMaterialEntries();

        if (this.showingMaterials) {
            this.addRenderableWidget(this.materialsList);
        }

        this.schematicsButton = new TextureButton(adjustedX + 7, adjustedY + 130, 18, 18, this::buttonClicked);
        this.schematicsButton
                .setIsToggleButton()
                .setDefaultTexture(this.schematicDefault)
                .setHoverTexture(this.schematicHover)
                .setSelectedTexture(this.schematicSelected)
                .setSelectedHoverTexture(this.schematicHoverSelected)
                .setIsSelected(!this.showingMaterials);
        this.addRenderableWidget(this.schematicsButton);

        this.materialsButton = new TextureButton(adjustedX + 30, adjustedY + 130, 18, 18, this::buttonClicked);
        this.materialsButton
                .setIsToggleButton()
                .setDefaultTexture(this.materialDefault)
                .setHoverTexture(this.materialHover)
                .setSelectedTexture(this.materialSelected)
                .setSelectedHoverTexture(this.materialHoverSelected)
                .setIsSelected(this.showingMaterials);
        this.addRenderableWidget(this.materialsButton);
    }

    @Override
    public void render(@NotNull PoseStack matrixStack, int x, int y, float f) {
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();

        this.preButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);

        super.render(matrixStack, x, y, f);

        this.renderButtons(matrixStack, x, y);

        this.renderTooltip(matrixStack, x, y);

        this.postButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float f, int i, int j) {
    }

    private void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        GuiUtils.bindAndDrawScaledTexture(
                this.backgroundTexture,
                matrixStack,
                x,
                y,
                176,
                237,
                176,
                237,
                176,
                237);

        GuiUtils.drawItemBackground(x + 151, y + 130);
    }

    private void renderButtons(PoseStack matrixStack, int mouseX, int mouseY) {
        for (GuiEventListener button : this.children()) {
            if (button instanceof AbstractWidget currentButton) {
                if (currentButton.visible) {
                    currentButton.renderButton(matrixStack, mouseX, mouseY, this.getMinecraft().getFrameTime());
                }
            } else if (button instanceof Widget currentWidget) {
                currentWidget.render(matrixStack, mouseX, mouseY, this.getMinecraft().getFrameTime());
            }
        }
    }

    private void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Draw Text here.
        GuiUtils.drawString(matrixStack, "Available Structures", x + 10, y + 10, this.textColor);
    }

    private void buttonClicked(AbstractButton button) {
        if (button == this.schematicsButton) {
            this.materialsButton.setIsSelected(false);
            this.showingMaterials = false;
            this.schematicsList.setVisible(true);
            this.materialsList.setVisible(false);
            this.removeWidget(this.materialsList);
            this.removeWidget(this.materialsButton);
            this.removeWidget(this.schematicsButton);
            this.addRenderableWidget(this.schematicsList);
            this.addRenderableWidget(this.materialsButton);
            this.addRenderableWidget(this.schematicsButton);
        } else if (button == this.materialsButton) {
            this.schematicsButton.setIsSelected(false);
            this.showingMaterials = true;
            this.schematicsList.setVisible(false);
            this.materialsList.setVisible(true);
            this.removeWidget(this.schematicsList);
            this.removeWidget(this.materialsButton);
            this.removeWidget(this.schematicsButton);
            this.addRenderableWidget(this.materialsList);
            this.addRenderableWidget(this.materialsButton);
            this.addRenderableWidget(this.schematicsButton);
        }
    }

    public void structureListEntryChanged(ListEntry newEntry) {
        if (newEntry != null && newEntry.getTag() != null) {
            CustomStructureInfo info = (CustomStructureInfo) newEntry.getTag();
            this.selectedStructureInfo = info;
            this.menu.setSelectedStructureInfo(info);
            this.menu.setParent(this);
            this.loadMaterialEntries();
        } else {
            this.menu.setSelectedStructureInfo(null);
        }
    }

    public void loadMaterialEntries() {
        if (this.selectedStructureInfo != null && this.selectedStructureInfo.requiredItems != null && this.selectedStructureInfo.requiredItems.size() > 0) {
            this.materialsList.children().clear();

            for (ItemInfo itemInfo : this.selectedStructureInfo.requiredItems) {
                if (itemInfo.registeredItem != null) {
                    int hasCount = this.getMinecraft().player.getInventory().countItem(itemInfo.registeredItem);

                    this.materialsList.addEntry(itemInfo.registeredItem, itemInfo.count, hasCount);
                }
            }
        }
    }

    private void loadCustomStructureEntries() {
        if (ClientProxy.ServerRegisteredStructures != null && ClientProxy.ServerRegisteredStructures.size() > 0) {
            this.schematicsList.children().clear();

            for (CustomStructureInfo info : ClientProxy.ServerRegisteredStructures) {
                this.schematicsList.addEntry(info.displayName).setTag(info);
            }
        }
    }

    /**
     * Gets the adjusted x/y coordinates for the topleft most part of the screen.
     *
     * @return A new tuple containing the x/y coordinates.
     */
    private Tuple<Integer, Integer> getAdjustedXYValue() {
        return new Tuple<>(this.getCenteredXAxis() - this.modifiedInitialXAxis, this.getCenteredYAxis() - this.modifiedInitialYAxis);
    }

    /**
     * Gets the X-Coordinates of the center of the screen.
     *
     * @return The coordinate value.
     */
    protected int getCenteredXAxis() {
        return this.width / 2;
    }

    /**
     * Gets the Y-Coordinates of the center off the screen.
     *
     * @return The coordinate value.
     */
    protected int getCenteredYAxis() {
        return this.height / 2;
    }
}
