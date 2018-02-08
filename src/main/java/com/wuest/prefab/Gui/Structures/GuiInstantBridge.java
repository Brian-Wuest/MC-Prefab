package com.wuest.prefab.Gui.Structures;

import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.Structures.ChickenCoopConfiguration;
import com.wuest.prefab.Config.Structures.InstantBridgeConfiguration;
import com.wuest.prefab.Config.Structures.InstantBridgeConfiguration.EnumBridgeMaterial;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Render.StructureRenderHandler;
import com.wuest.prefab.StructureGen.CustomStructures.StructureInstantBridge;
import com.wuest.prefab.StructureGen.CustomStructures.StructureProduceFarm;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.EnumDyeColor;
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
	
	public GuiInstantBridge(int x, int y, int z)
	{
		super(x, y, z, true);
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

		this.chckIncludeRoof = new GuiCheckBox(6, grayBoxX + 147, grayBoxY + 60, GuiLangKeys.translateString(GuiLangKeys.INCLUDE_ROOF), true);
		this.buttonList.add(this.chckIncludeRoof);
		
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
		this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.BRIDGE_MATERIAL), grayBoxX + 10, grayBoxY + 10, this.textColor);
		this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.BRIDGE_LENGTH), grayBoxX + 147, grayBoxY + 10, this.textColor);
		
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
		this.configuration.houseFacing = player.getHorizontalFacing().getOpposite();
		this.configuration.pos = this.pos;
		
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);
		
		if (button == this.chckIncludeRoof)
		{
			this.configuration.includeRoof = this.chckIncludeRoof.isChecked();
		}
		if (button == this.btnMaterialType)
		{
			this.configuration.bridgeMaterial = EnumBridgeMaterial.getMaterialByNumber(this.configuration.bridgeMaterial.getNumber() + 1);
			this.btnMaterialType.displayString = this.configuration.bridgeMaterial.getTranslatedName();
		}
		else if (button == this.btnVisualize)
		{
			StructureInstantBridge structure = new StructureInstantBridge();
			structure.getClearSpace().getShape().setDirection(EnumFacing.NORTH);
			structure.setupStructure(this.configuration, this.pos);
			
			StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
			this.mc.displayGuiScreen(null);
		}
	}
}
