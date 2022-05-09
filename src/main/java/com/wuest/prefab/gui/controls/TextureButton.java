package com.wuest.prefab.gui.controls;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class TextureButton extends ExtendedButton {
    private ResourceLocation defaultTexture;
    private ResourceLocation hoverTexture;
    private ResourceLocation selectedTexture;
    private ResourceLocation selectedHoverTexture;
    private boolean isToggleButton;
    private boolean isSelected;

    public TextureButton(int xPos, int yPos, OnPress handler) {
        super(xPos, yPos, 200, 90, new TextComponent(""), handler, null);
        this.isToggleButton = false;
        this.isSelected = false;
    }

    public TextureButton(int xPos, int yPos, int width, int height, OnPress handler) {
        super(xPos, yPos, width, height, new TextComponent(""), handler, null);
        this.isToggleButton = false;
        this.isSelected = false;
    }

    public TextureButton setDefaultTexture(ResourceLocation value) {
        this.defaultTexture = value;
        return this;
    }

    public TextureButton setHoverTexture(ResourceLocation value) {
        this.hoverTexture = value;
        return this;
    }

    public TextureButton setSelectedTexture(ResourceLocation value) {
        this.selectedTexture = value;
        return this;
    }

    public TextureButton setSelectedHoverTexture(ResourceLocation value) {
        this.selectedHoverTexture = value;
        return this;
    }

    public TextureButton setIsToggleButton() {
        this.isToggleButton = true;
        return this;
    }

    public TextureButton setIsSelected(boolean value) {
        this.isSelected = value;
        return this;
    }

    @Override
    public void onPress() {
        if (this.isSelected && this.isToggleButton) {
            // When this is a toggle button and it's already selected; don't allow the click event to trigger.
            return;
        }

        this.onPress.onPress(this);

        if (this.isToggleButton) {
            this.isSelected = true;
        }
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void renderButton(PoseStack mStack, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            Minecraft mc = Minecraft.getInstance();
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            ResourceLocation buttonTexture = this.getButtonTexture();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, buttonTexture);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);

            GuiUtils.bindAndDrawScaledTexture(mStack, this.x, this.y, this.width, this.height, this.width, this.height, this.width, this.height);
        }
    }

    private ResourceLocation getButtonTexture() {
        if (this.isSelected) {
            if (this.isHovered) {
                return this.selectedHoverTexture;
            }

            return this.selectedTexture;
        } else if (this.isHovered) {
            return this.hoverTexture;
        }

        return this.defaultTexture;
    }
}
