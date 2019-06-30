package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Config.WareHouseConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureWarehouse;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * This class is used to hold the gui options for the warehouse.
 * 
 * @author WuestMan
 *
 */
public class GuiWareHouse extends GuiStructure
{
	private static final ResourceLocation wareHouseTopDown = new ResourceLocation("prefab", "textures/gui/warehouse_top_down.png");
	protected GuiButtonExt btnGlassColor;
	protected WareHouseConfiguration configuration;
	protected String clientGUIIdentifier;

	public GuiWareHouse()
	{
		super("Warehouse");
		this.structureConfiguration = EnumStructureConfiguration.WareHouse;
		this.clientGUIIdentifier = "Warehouse";
	}

	@Override
	public void Initialize()
	{
		this.configuration = ClientEventHandler.playerConfig.getClientConfig(this.clientGUIIdentifier, WareHouseConfiguration.class);
		this.configuration.pos = this.pos;
		this.configuration.houseFacing = Direction.NORTH;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 180;
		int grayBoxY = this.getCenteredYAxis() - 83;

		// Create the buttons.
		this.btnGlassColor = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateDye(this.configuration.dyeColor));

		this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void render(int x, int y, float f)
	{
		int grayBoxX = this.getCenteredXAxis() - 180;
		int grayBoxY = this.getCenteredYAxis() - 83;

		this.renderBackground();

		// Draw the control background.
		this.minecraft.getTextureManager().bindTexture(wareHouseTopDown);
		this.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 132, 153, 132, 153);

		this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

		// Draw the text here.
		this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), grayBoxX + 10, grayBoxY + 10, this.textColor);

		// Draw the text here.
		this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), grayBoxX + 147, grayBoxY + 10, 95, this.textColor);

		this.checkVisualizationSetting();
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	public void buttonClicked(Button button)
	{
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);

		if (button == this.btnGlassColor)
		{
			this.configuration.dyeColor = DyeColor.byId(this.configuration.dyeColor.getId() + 1);
			this.btnGlassColor.setMessage(GuiLangKeys.translateDye(this.configuration.dyeColor));
		}
		else if (button == this.btnVisualize)
		{
			StructureWarehouse structure = StructureWarehouse.CreateInstance(StructureWarehouse.ASSETLOCATION, StructureWarehouse.class);
			StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
			this.minecraft.displayGuiScreen(null);
		}
	}
}