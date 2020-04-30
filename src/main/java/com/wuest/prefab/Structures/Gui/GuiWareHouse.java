package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Config.WareHouseConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureWarehouse;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * This class is used to hold the gui options for the warehouse.
 *
 * @author WuestMan
 */
public class GuiWareHouse extends GuiStructure {
	private static final ResourceLocation wareHouseTopDown = new ResourceLocation("prefab", "textures/gui/warehouse_top_down.png");
	protected WareHouseConfiguration configuration;
	String clientGUIIdentifier;
	private GuiButtonExt btnGlassColor;

	public GuiWareHouse() {
		super("Warehouse");
		this.structureConfiguration = EnumStructureConfiguration.WareHouse;
		this.clientGUIIdentifier = "Warehouse";
		this.modifiedInitialXAxis = 180;
		this.modifiedInitialYAxis = 83;
	}

	@Override
	public void Initialize() {
		this.configuration = ClientEventHandler.playerConfig.getClientConfig(this.clientGUIIdentifier, WareHouseConfiguration.class);
		this.configuration.pos = this.pos;
		this.configuration.houseFacing = Direction.NORTH;

		// Get the upper left hand corner of the GUI box.
		Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
		int grayBoxX = adjustedXYValue.getFirst();
		int grayBoxY = adjustedXYValue.getSecond();

		// Create the buttons.
		this.btnGlassColor = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateDye(this.configuration.dyeColor));

		this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
	}

	@Override
	protected void preButtonRender(int x, int y) {
		super.preButtonRender(x, y);

		this.bindTexture(wareHouseTopDown);
		GuiWareHouse.drawModalRectWithCustomSizedTexture(x + 250, y, 1, 132, 153, 132, 153);
	}

	@Override
	protected void postButtonRender(int x, int y) {
		this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 10, y + 10, this.textColor);

		// Draw the text here.
		this.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), x + 147, y + 10, 95, this.textColor);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	public void buttonClicked(Button button) {
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);

		if (button == this.btnGlassColor) {
			this.configuration.dyeColor = DyeColor.byId(this.configuration.dyeColor.getId() + 1);
			this.btnGlassColor.setMessage(GuiLangKeys.translateDye(this.configuration.dyeColor));
		} else if (button == this.btnVisualize) {
			if (this.configuration.advanced) {
				StructureWarehouse structure = StructureWarehouse.CreateInstance(StructureWarehouse.ADVANCED_ASSET_LOCATION, StructureWarehouse.class);
				StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
			} else {
				StructureWarehouse structure = StructureWarehouse.CreateInstance(StructureWarehouse.ASSETLOCATION, StructureWarehouse.class);
				StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
			}

			this.closeScreen();
		}
	}
}