package com.wuest.prefab.Structures.Gui;

import java.io.IOException;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Capabilities.IStructureConfigurationCapability;
import com.wuest.prefab.Structures.Capabilities.StructureConfigurationCapability;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration;
import com.wuest.prefab.Structures.Items.ItemBasicStructure;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureBasic;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * This class is used as the gui for all basic structures.
 * @author WuestMan
 *
 */
public class GuiBasicStructure extends GuiStructure
{
	protected BasicStructureConfiguration configuration;
	protected boolean includePicture = true;
	protected int modifiedInitialXAxis = 213;
	protected int modifiedINitialYAxis = 83;
	
	public GuiBasicStructure()
	{
		super("Basic Structure");
		this.structureConfiguration = EnumStructureConfiguration.Basic;
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void render(int x, int y, float f) 
	{
		int grayBoxX = this.getCenteredXAxis() - this.modifiedInitialXAxis;
		int grayBoxY = this.getCenteredYAxis() - this.modifiedINitialYAxis;
		
		this.renderBackground();

		if (this.includePicture)
		{
			// Draw the control background.
			this.getMinecraft().getTextureManager().bindTexture(this.configuration.basicStructureName.getTopDownPictureLocation());
			
			this.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 
					this.configuration.basicStructureName.getImageWidth(), this.configuration.basicStructureName.getImageHeight(), 
					this.configuration.basicStructureName.getImageWidth(), this.configuration.basicStructureName.getImageHeight());
		}
		
		this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);
		
		// Draw the text here.
		this.getMinecraft().fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), grayBoxX + 147, grayBoxY + 10, 95, this.textColor);
		
		if (!Prefab.proxy.proxyConfiguration.serverConfiguration.enableStructurePreview)
		{
			this.btnVisualize.visible = false;
		}
	}
	
	@Override
	protected void Initialize() 
	{
		// Get the structure configuration for this itemstack.
		ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(this.player);
		
		if (stack != null)
		{
			LazyOptional<IStructureConfigurationCapability> optionalCapability = stack.getCapability(ModRegistry.StructureConfiguration, Direction.NORTH);
			
			this.configuration = optionalCapability.orElse(new StructureConfigurationCapability()).getConfiguration();
			
			if (!ClientEventHandler.playerConfig.clientConfigurations.containsKey(this.configuration.basicStructureName.getName()))
			{
				ClientEventHandler.playerConfig.clientConfigurations.put(this.configuration.basicStructureName.getName(), this.configuration);
			}
			else
			{
				this.configuration = ClientEventHandler.playerConfig.getClientConfig(this.configuration.basicStructureName.getName(), BasicStructureConfiguration.class);
			}
			
			this.includePicture = this.doesPictureExist();
		}
		
		this.configuration.pos = this.pos;
		
		if (!this.includePicture)
		{
			this.modifiedInitialXAxis = 125;
		}

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - this.modifiedInitialXAxis;
		int grayBoxY = this.getCenteredYAxis() - this.modifiedINitialYAxis;

		// Create the buttons.
		this.btnVisualize = new GuiButtonExt(grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW), (button) ->  {
			StructureBasic structure = StructureBasic.CreateInstance(this.configuration.basicStructureName.getAssetLocation(), StructureBasic.class);
			StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
			this.getMinecraft().displayGuiScreen(null);
		});
		
		this.buttons.add(this.btnVisualize);
		
		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD), (button) ->  {
			this.performCancelOrBuildOrHouseFacing(this.configuration, button);
		});
		
		this.buttons.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL), (button) ->  {
			this.performCancelOrBuildOrHouseFacing(this.configuration, button);
		});
		
		this.buttons.add(this.btnCancel);
	}
	
	/**
	 * Determines if the picture for this screen exists in the resources.
	 * @return A value indicating whether the picture exists.
	 */
	protected boolean doesPictureExist()
	{
		try
		{
			this.getMinecraft().getResourceManager().getResource(this.configuration.basicStructureName.getTopDownPictureLocation());
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}
}
