package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.Utils;
import com.wuest.prefab.gui.GuiBase;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.proxy.CommonProxy;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.StructureConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Generic GUI for all structures.
 *
 * @author WuestMan
 */
public abstract class GuiStructure extends GuiBase {
    private static final RandomSource RAND = RandomSource.create();

    public BlockPos pos;
    public StructureTagMessage.EnumStructureConfiguration configurationEnum;
    protected Player player;
    protected Button btnCancel;
    protected Button btnBuild;
    protected Button btnVisualize;
    protected ResourceLocation structureImageLocation;
    protected StructureConfiguration configuration;
    protected Structure selectedStructure;
    protected StructureGuiWorld structureRenderer;
    protected int ticksWithScreenOpen;
    protected Direction houseFacing;

    private boolean isRendererSetup = false;
    private HashMap<Long, BlockState> renderableBlocks;

    public GuiStructure(String title) {
        super(title);
    }

    @Nullable
    public static BlockHitResult rayTrace(Vec3 from, Vec3 to, BlockGetter world) {
        ClipContext rayTraceContext = new ClipContext(from, to, ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE, null);

        ClipBlockStateContext otherContext = new ClipBlockStateContext(from, to, (p_223780_) -> {
            return true;
        });

        BlockHitResult result1 = world.clip(rayTraceContext);

        BlockHitResult result = world.isBlockInLine(otherContext);
        //BlockHitResult result = world.clip(rayTraceContext);

        return result;
    }

    @Override
    public void init() {
        this.player = this.getMinecraft().player;
        this.houseFacing = this.player.getDirection().getOpposite();

        // TODO: Put this back when structure rendering works.
        this.structureRenderer = new StructureGuiWorld();

        this.Initialize();
    }

    @Override
    public void tick() {
        this.ticksWithScreenOpen++;
    }

    /**
     * This method is used to initialize GUI specific items.
     */
    protected void Initialize() {
        super.Initialize();

        this.scheduleRebuild();
    }

    protected void InitializeStandardButtons() {
        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnVisualize = this.createAndAddCustomButton(grayBoxX + 113, grayBoxY + 167, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(grayBoxX + 215, grayBoxY + 167, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(grayBoxX + 10, grayBoxY + 167, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    public void checkVisualizationSetting() {
        if (!CommonProxy.proxyConfiguration.serverConfiguration.enableStructurePreview) {
            this.btnVisualize.visible = false;
        }
    }

    @Override
    public void render(PoseStack matrixStack, int x, int y, float f) {
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();

        this.preButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);

        this.renderButtons(matrixStack, x, y);

        this.postButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);

        if (this.structureRenderer != null) {
            if (this.isRendererSetup) {
                this.renderStructureInScreen(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);
            } else {
                ////this.rebuild();
                this.determineBlocksToRender(matrixStack);
                this.renderStructureInScreen(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);
                this.isRendererSetup = true;
            }
        }

        if (this.btnVisualize != null) {
            this.checkVisualizationSetting();
        }
    }

    @Override
    protected void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.drawStandardControlBoxAndImage(matrixStack, this.structureImageLocation, x, y, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void performCancelOrBuildOrHouseFacing(AbstractButton button) {
        if (button == this.btnCancel) {
            this.closeScreen();
        } else if (button == this.btnBuild) {
            Prefab.network.sendToServer(Utils.createStructureMessage(this.configuration.WriteToCompoundTag(), this.configurationEnum));
            this.closeScreen();
        }
    }

    protected void performPreview() {
        StructureRenderHandler.setStructure(this.selectedStructure, this.configuration);
        this.closeScreen();
    }

    protected void showNoOptionsScreen() {
        this.getMinecraft().setScreen(new GuiNoOptions());
    }

    /**
     * Updates the structure to be rendered.
     */
    protected void updatedRenderedStructure() {
        if (this.structureRenderer != null && this.selectedStructure != null && this.configuration != null) {
            this.structureRenderer.resetStructure();

            this.structureRenderer
                    .setClearShape(this.selectedStructure.getClearSpace())
                    .setStructureConfiguration(this.configuration)
                    .setBlocks(this.selectedStructure.getBlocks())
                    .setupBlocks();

            this.scheduleRebuild();
        }
    }

    public void scheduleRebuild() {
        this.isRendererSetup = false;
        this.renderableBlocks = new HashMap<>();
    }

    /**
     * Renders the current structure on the GUI.
     */
    protected void renderStructureInScreen(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int sizeX = this.structureRenderer.getClearShape().getShape().getWidth();
        int sizeY = this.structureRenderer.getClearShape().getShape().getHeight();
        int sizeZ = this.structureRenderer.getClearShape().getShape().getLength();
        float maxX = 200;
        float maxY = 200;
        float diag = (float) Math.sqrt((sizeX * sizeX) + (sizeZ * sizeZ));
        float scaleX = maxX / diag;
        float scaleY = maxY / sizeY;
        float scale = -Math.min(scaleX, scaleY);

        /*
            TODO: Make these x/y positions based on whether this GUI has configuration options or not.
            May also have to make this structure specific due to how structure dimensions are shown.
        */
        int xPos = x + 166;
        int yPos = 35;

        matrixStack.pushPose();
        matrixStack.translate(xPos, yPos, 100);
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(-(float) sizeX, -(float) sizeY, -(float) sizeZ);

        // Initial eye pos somewhere off in the distance in the -Z direction
        Vector4f eye = new Vector4f(0, 0, -100, 1);
        Matrix4f rotMat = new Matrix4f();
        rotMat.setIdentity();

        // For each GL rotation done, track the opposite to keep the eye pos accurate
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15));
        rotMat.multiply(Vector3f.XP.rotationDegrees(15));

        float offX = sizeX / 4.0f + 1f;
        float offZ = sizeZ / 4.0f;

        matrixStack.translate(-offX, 0, -offZ);

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(45));
        rotMat.multiply(Vector3f.YP.rotationDegrees(-45));
        matrixStack.translate(offX, 0, offZ);

        // Apply the rotations
        eye.transform(rotMat);
        eye.perspectiveDivide();

        // Original Botania style rendering
        this.renderElements(matrixStack, eye);

        matrixStack.popPose();
    }

    private void renderElements(PoseStack matrixStack, Vector4f eye) {
        matrixStack.pushPose();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        matrixStack.translate(0, 0, -1);

        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        this.doWorldRenderPass(matrixStack, buffers, eye);

        buffers.endBatch();
        matrixStack.popPose();
    }

    private void determineBlocksToRender(PoseStack matrixStack) {
        Vec3 eyePos = new Vec3(0, 30, 0);
        this.renderableBlocks = new HashMap<>();
        /*Quaternion quaternion0 = new Quaternion(.5f, .5f, .5f, .5f);
        Quaternion quaternion = new Quaternion(10, 10, 10, 10);

        Frustum frustum = new Frustum(matrixStack.last().pose(), new Matrix4f(quaternion));
        frustum.prepare(eyePos.x, eyePos.y, eyePos.z);*/

        for (Map.Entry<Long, BlockState> entry : this.structureRenderer.getBlocksByPosition().entrySet()) {
            BlockPos position = BlockPos.of(entry.getKey());

            // Don't render the block if it isn't visible (cull)
           /* AABB box = new AABB(position.getX() - 0.5, position.getY() - 0.5, position.getZ() - 0.5,
                    position.getX() + 1.5, position.getY() + 1.5, position.getZ() + 1.5);

            if (!frustum.isVisible(box)) {
                continue;
            }*/

            Vec3 blockPos = new Vec3(position.getX(), position.getY(), position.getZ());
            BlockHitResult blockHitResult = GuiStructure.rayTrace(eyePos, blockPos, this.structureRenderer);

            if (blockHitResult.getType() == HitResult.Type.MISS || position.asLong() != blockHitResult.getBlockPos().asLong()) {
                continue;
            }

            BlockState blockState = entry.getValue();

            if (blockState.getRenderShape() != RenderShape.INVISIBLE && blockState.getRenderShape() == RenderShape.MODEL) {
                // This block can be rendered.
                this.renderableBlocks.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private void doWorldRenderPass(PoseStack matrixStack, final @Nonnull MultiBufferSource.BufferSource buffers, Vector4f eye) {
        this.determineBlocksToRender(matrixStack);

        for (Map.Entry<Long, BlockState> entry : this.renderableBlocks.entrySet()) {
            BlockPos position = BlockPos.of(entry.getKey());
            BlockState blockState = entry.getValue();

            VertexConsumer vertexConsumer = buffers.getBuffer(ItemBlockRenderTypes.getChunkRenderType(blockState));

            matrixStack.pushPose();
            matrixStack.translate(position.getX(), position.getY(), position.getZ());

            int color = minecraft.getBlockColors().getColor(blockState, Minecraft.getInstance().level, position, 50);
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;

            BlockRenderDispatcher renderer = minecraft.getBlockRenderer();
            renderer.getModelRenderer().renderModel(
                    matrixStack.last(),
                    vertexConsumer,
                    blockState,
                    renderer.getBlockModel(blockState),
                    r,
                    g,
                    b,
                    0xF000F0,
                    OverlayTexture.NO_OVERLAY);

            matrixStack.popPose();
        }
    }
}
