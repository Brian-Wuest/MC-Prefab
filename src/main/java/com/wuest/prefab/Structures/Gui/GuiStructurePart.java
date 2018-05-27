package com.wuest.prefab.Structures.Gui;

import java.awt.Color;
import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Base.EnumStructureMaterial;
import com.wuest.prefab.Structures.Config.StructurePartConfiguration;
import com.wuest.prefab.Structures.Config.StructurePartConfiguration.EnumDimensions;
import com.wuest.prefab.Structures.Config.StructurePartConfiguration.EnumStyle;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

/**
 * This class is used as the gui for structure parts.
 * 
 * @author WuestMan
 *
 */
public class GuiStructurePart extends GuiStructure
{
	protected StructurePartConfiguration configuration;
	protected GuiSlider sldrStairWidth;
	protected GuiSlider sldrStairHeight;
	protected GuiButtonExt btnPartDimensions;
	protected GuiButtonExt btnPartStyle;
	protected GuiButtonExt btnMaterialType;
	
	public GuiStructurePart(int x, int y, int z)
	{
		super(x, y, z, true);
		this.structureConfiguration = EnumStructureConfiguration.Parts;
	}

	@Override
	protected void Initialize()
	{
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Parts", StructurePartConfiguration.class);
		this.configuration.pos = this.pos;
		int color = Color.DARK_GRAY.getRGB();

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 125;
		int grayBoxY = this.getCenteredYAxis() - 83;

		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
		this.buttonList.add(this.btnCancel);
		
		this.btnVisualize = new GuiButtonExt(3, grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));
		this.buttonList.add(this.btnVisualize);

		this.sldrStairHeight = new GuiSlider(4, grayBoxX + 147, grayBoxY + 20, 90, 20, "", "", 1, 5, this.configuration.stairHeight, false, true);
		this.buttonList.add(this.sldrStairHeight);

		this.sldrStairWidth = new GuiSlider(5, grayBoxX + 147, grayBoxY + 60, 90, 20, "", "", 1, 5, this.configuration.stairWidth, false, true);
		this.buttonList.add(this.sldrStairWidth);

		this.btnPartDimensions = new GuiButtonExt(6, grayBoxX + 147, grayBoxY + 20, 90, 20, this.configuration.dimensions.diaplayName);
		this.buttonList.add(this.btnPartDimensions);

		this.btnPartStyle = new GuiButtonExt(7, grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateString(this.configuration.style.getTranslateKey()));
		this.buttonList.add(this.btnPartStyle);
		
		this.btnMaterialType = new GuiButtonExt(8, grayBoxX + 10, grayBoxY + 60, 90, 20, this.configuration.partMaterial.getTranslatedName());
		this.buttonList.add(this.btnMaterialType);
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f)
	{
		int grayBoxX = this.getCenteredXAxis() - 125;
		int grayBoxY = this.getCenteredYAxis() - 83;

		this.drawDefaultBackground();

		this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);
		
		this.mc.fontRenderer.drawString("Style", grayBoxX + 10, grayBoxY + 10, this.textColor);
		this.mc.fontRenderer.drawString("Material", grayBoxX + 10, grayBoxY + 50, this.textColor);
		
		if (this.configuration.style == EnumStyle.Stairs)
		{
			this.sldrStairHeight.visible = true;
			this.sldrStairWidth.visible = true;
			this.btnPartDimensions.visible = false;
			
			this.mc.fontRenderer.drawString("Stairs Height", grayBoxX + 147, grayBoxY + 10, this.textColor);
			this.mc.fontRenderer.drawString("Stars Width", grayBoxX + 147, grayBoxY + 50, this.textColor);
		}
		else
		{
			this.sldrStairHeight.visible = false;
			this.sldrStairWidth.visible = false;
			this.btnPartDimensions.visible = true;
			this.mc.fontRenderer.drawString("Dimensions", grayBoxX + 147, grayBoxY + 10, this.textColor);
		}
		
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
		this.configuration.houseFacing = Minecraft.getMinecraft().player.getHorizontalFacing().getOpposite();
		this.configuration.stairHeight = this.sldrStairHeight.getValueInt();
		this.configuration.stairWidth = this.sldrStairWidth.getValueInt();
		
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);
		
		if (button == this.btnMaterialType)
		{
			this.configuration.partMaterial = EnumStructureMaterial.getMaterialByNumber(this.configuration.partMaterial.getNumber() + 1);
			this.btnMaterialType.displayString = this.configuration.partMaterial.getTranslatedName();
		}
		else if (button == this.btnPartDimensions)
		{
			this.configuration.dimensions = EnumDimensions.getByOrdinal(this.configuration.dimensions.ordinal() + 1);
			this.btnPartDimensions.displayString = this.configuration.dimensions.diaplayName;
		}
		else if (button == this.btnPartStyle)
		{
			this.configuration.style = EnumStyle.getByOrdinal(this.configuration.style.ordinal() + 1);
			this.btnPartStyle.displayString = GuiLangKeys.translateString(this.configuration.style.getTranslateKey());
		}
	}
}
