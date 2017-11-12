package com.wuest.prefab.Gui.Structures;

import java.awt.Color;
import java.io.IOException;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Capabilities.IStructureConfigurationCapability;
import com.wuest.prefab.Config.Structures.BasicStructureConfiguration;
import com.wuest.prefab.Config.Structures.VillagerHouseConfiguration;
import com.wuest.prefab.Config.Structures.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Items.Structures.ItemBasicStructure;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Render.StructureRenderHandler;
import com.wuest.prefab.StructureGen.CustomStructures.StructureBasic;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
	
	public GuiBasicStructure(int x, int y, int z)
	{
		super(x, y, z, true);
		this.structureConfiguration = EnumStructureConfiguration.Basic;
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = this.getCenteredXAxis() - this.modifiedInitialXAxis;
		int grayBoxY = this.getCenteredYAxis() - this.modifiedINitialYAxis;
		
		this.drawDefaultBackground();

		if (this.includePicture)
		{
			// Draw the control background.
			this.mc.getTextureManager().bindTexture(this.configuration.basicStructureName.getTopDownPictureLocation());
			
			this.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 
					this.configuration.basicStructureName.getImageWidth(), this.configuration.basicStructureName.getImageHeight(), 
					this.configuration.basicStructureName.getImageWidth(), this.configuration.basicStructureName.getImageHeight());
		}
		
		this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

		// Draw the text here.
		this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_FACING), grayBoxX + 10, grayBoxY + 10, this.textColor);
		
		// Draw the text here.
		this.mc.fontRendererObj.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), grayBoxX + 147, grayBoxY + 10, 95, this.textColor);
		this.mc.fontRendererObj.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_FACING_PLAYER), grayBoxX + 147, grayBoxY + 60, 95, this.textColor);
		
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
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);
		
		if (button == this.btnVisualize)
		{
			StructureBasic structure = StructureBasic.CreateInstance(this.configuration.basicStructureName.getAssetLocation(), StructureBasic.class);
			StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
			this.mc.displayGuiScreen(null);
		}
	}
	
	@Override
	protected void Initialize() 
	{
		// Get the structure configuration for this itemstack.
		ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(this.player);
		
		if (stack != null)
		{
			IStructureConfigurationCapability capability = stack.getCapability(ModRegistry.StructureConfiguration, EnumFacing.NORTH);
			this.configuration = capability.getConfiguration();
			
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
		this.btnHouseFacing = new GuiButtonExt(3, grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateFacing(this.configuration.houseFacing));
		this.buttonList.add(this.btnHouseFacing);

		this.btnVisualize = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 50, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));
		this.buttonList.add(this.btnVisualize);
		
		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
		this.buttonList.add(this.btnCancel);
	}
	
	/**
	 * Determines if the picture for this screen exists in the resources.
	 * @return A value indicating whether the picture exists.
	 */
	protected boolean doesPictureExist()
	{
		try
		{
			this.mc.getResourceManager().getResource(this.configuration.basicStructureName.getTopDownPictureLocation());
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}
}
