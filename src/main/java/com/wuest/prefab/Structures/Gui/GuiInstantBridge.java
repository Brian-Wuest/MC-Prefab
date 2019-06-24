package com.wuest.prefab.Structures.Gui;

import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Structures.Base.EnumStructureMaterial;
import com.wuest.prefab.Structures.Config.InstantBridgeConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureInstantBridge;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiInstantBridge extends GuiStructure
{
	private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/instant_bridge_top_down.png");
	protected InstantBridgeConfiguration configuration;
	protected GuiButtonExt btnMaterialType;
	protected GuiSlider sldrBridgeLength;
	protected GuiCheckBox chckIncludeRoof;
	protected GuiSlider sldrInteriorHeight;
	
	public GuiInstantBridge()
	{
		super("Instant Bridge");
		this.structureConfiguration = EnumStructureConfiguration.InstantBridge;
	}

	@Override
	protected void Initialize() 
	{
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("InstantBridge", InstantBridgeConfiguration.class);
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 213;
		int grayBoxY = this.getCenteredYAxis() - 83;

		// Create the buttons.
		this.btnMaterialType = new GuiButtonExt(3, grayBoxX + 10, grayBoxY + 20, 90, 20, this.configuration.bridgeMaterial.getTranslatedName());
		this.buttonList.add(this.btnMaterialType);
		
		this.sldrBridgeLength = new GuiSlider(5, grayBoxX + 147, grayBoxY + 20, 90, 20, "", "", 25, 75, this.configuration.bridgeLength, false, true);
		this.buttonList.add(this.sldrBridgeLength);
		
		this.chckIncludeRoof = new GuiCheckBox(6, grayBoxX + 147, grayBoxY + 55, GuiLangKeys.translateString(GuiLangKeys.INCLUDE_ROOF), this.configuration.includeRoof);
		this.buttonList.add(this.chckIncludeRoof);
		
		this.sldrInteriorHeight = new GuiSlider(7, grayBoxX + 147, grayBoxY + 90, 90, 20, "", "", 3, 8, this.configuration.interiorHeight, false, true);
		this.buttonList.add(this.sldrInteriorHeight);
		
		this.sldrInteriorHeight.enabled = this.chckIncludeRoof.isChecked();
		
		this.btnVisualize = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));
		this.buttonList.add(this.btnVisualize);
		
		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
		this.buttonList.add(this.btnCancel);
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = this.getCenteredXAxis() - 210;
		int grayBoxY = this.getCenteredYAxis() - 83;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(structureTopDown);
		this.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 165, 58, 165, 58);
		
		this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

		// Draw the text here.
		this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.BRIDGE_MATERIAL), grayBoxX + 10, grayBoxY + 10, this.textColor);
		this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.INTERIOR_HEIGHT), grayBoxX + 147, grayBoxY + 80, this.textColor);
		this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.BRIDGE_LENGTH), grayBoxX + 147, grayBoxY + 10, this.textColor);
		
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
		int sliderValue = this.sldrBridgeLength.getValueInt();
		
		if (sliderValue > 75)
		{
			sliderValue = 75;
		}
		else if (sliderValue < 25)
		{
			sliderValue = 25;
		}
		
		this.configuration.bridgeLength = sliderValue;
		
		sliderValue = this.sldrInteriorHeight.getValueInt();
		if (sliderValue > 8)
		{
			sliderValue = 8;
		}
		else if (sliderValue < 3)
		{
			sliderValue = 3;
		}
		
		this.configuration.interiorHeight = sliderValue;
		
		this.configuration.houseFacing = player.getHorizontalFacing().getOpposite();
		this.configuration.pos = this.pos;
		
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);
		
		if (button == this.chckIncludeRoof)
		{
			this.configuration.includeRoof = this.chckIncludeRoof.isChecked();
			
			this.sldrInteriorHeight.enabled = this.configuration.includeRoof;
		}
		if (button == this.btnMaterialType)
		{
			this.configuration.bridgeMaterial = EnumStructureMaterial.getMaterialByNumber(this.configuration.bridgeMaterial.getNumber() + 1);
			this.btnMaterialType.displayString = this.configuration.bridgeMaterial.getTranslatedName();
		}
		else if (button == this.btnVisualize)
		{
			StructureInstantBridge structure = new StructureInstantBridge();
			structure.getClearSpace().getShape().setDirection(EnumFacing.SOUTH);
			structure.setupStructure(this.configuration, this.pos);
			
			StructureRenderHandler.setStructure(structure, EnumFacing.SOUTH, this.configuration);
			this.mc.displayGuiScreen(null);
		}
	}
}
