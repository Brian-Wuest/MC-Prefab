package com.wuest.prefab.gui.screens;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.BlockStructureScanner;
import com.wuest.prefab.config.block_entities.DraftingTableConfiguration;
import com.wuest.prefab.gui.GuiBase;
import com.wuest.prefab.gui.controls.GuiTextBox;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;

public class GuiDraftingTable extends GuiBase {
    private final BlockPos blockPos;
    private final Level world;
    private DraftingTableConfiguration config;

    public GuiDraftingTable(BlockPos blockPos, Level world, DraftingTableConfiguration config) {
        super("Drafting Table");
        this.blockPos = blockPos;
        this.world = world;
        this.config = config;
        this.config.blockPos = this.blockPos;
    }

    @Override
    protected void Initialize() {
        super.Initialize();
        Tuple<Integer, Integer> adjustedXYValues = this.getAdjustedXYValue();
        int adjustedX = adjustedXYValues.getFirst();
        int adjustedY = adjustedXYValues.getSecond();
        // Starting position.
    }

    @Override
    protected void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawControlBackground(matrixStack, x, y + 15, 350, 250);
    }

    @Override
    protected void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void buttonClicked(AbstractButton button) {
    }
}