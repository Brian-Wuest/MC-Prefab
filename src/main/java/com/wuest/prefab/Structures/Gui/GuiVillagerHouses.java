package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
import com.wuest.prefab.Structures.Config.VillagerHouseConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureVillagerHouses;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

/**
 * @author WuestMan
 */
public class GuiVillagerHouses extends GuiStructure {
	protected VillagerHouseConfiguration configuration;
	private ExtendedButton btnHouseStyle;
	private ExtendedButton btnBedColor;
	private VillagerHouseConfiguration.HouseStyle houseStyle;

	public GuiVillagerHouses() {
		super("Villager Houses");
		this.structureConfiguration = EnumStructureConfiguration.VillagerHouses;
	}

	@Override
	public void Initialize() {
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Villager Houses", VillagerHouseConfiguration.class);
		this.configuration.pos = this.pos;
		this.configuration.houseFacing = Direction.NORTH;
		this.houseStyle = this.configuration.houseStyle;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 205;
		int grayBoxY = this.getCenteredYAxis() - 83;

		this.btnHouseStyle = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, this.houseStyle.getDisplayName());

		// Create the buttons.
		this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		int x = grayBoxX + 130;
		int y = grayBoxY + 20;

		this.btnBedColor = this.createAndAddButton(x, y, 90, 20, GuiLangKeys.translateDye(this.configuration.bedColor));

		this.btnBedColor.visible = this.houseStyle == VillagerHouseConfiguration.HouseStyle.LONG_HOUSE;

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
	}

	@Override
	protected Tuple<Integer, Integer> getAdjustedXYValue() {
		return new Tuple<>(this.getCenteredXAxis() - 205, this.getCenteredYAxis() - 83);
	}

	@Override
	protected void preButtonRender(int x, int y) {
		super.preButtonRender(x, y);

		this.minecraft.getTextureManager().bindTexture(this.houseStyle.getHousePicture());
		GuiTabScreen.drawModalRectWithCustomSizedTexture(x + 250, y, 1,
				this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight(),
				this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight());
	}

	@Override
	protected void postButtonRender(int x, int y) {
		this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 10, y + 10, this.textColor);

		if (this.houseStyle == VillagerHouseConfiguration.HouseStyle.LONG_HOUSE) {
			this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 130, y + 10, this.textColor);
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	public void buttonClicked(AbstractButton button) {
		this.configuration.houseStyle = this.houseStyle;

		this.performCancelOrBuildOrHouseFacing(this.configuration, button);

		if (button == this.btnHouseStyle) {
			int id = this.houseStyle.getValue() + 1;
			this.houseStyle = VillagerHouseConfiguration.HouseStyle.ValueOf(id);

			this.btnHouseStyle.setMessage(this.houseStyle.getDisplayName());

			this.btnBedColor.visible = this.houseStyle == VillagerHouseConfiguration.HouseStyle.LONG_HOUSE;
		} else if (button == this.btnVisualize) {
			StructureVillagerHouses structure = StructureVillagerHouses.CreateInstance(this.houseStyle.getStructureLocation(), StructureVillagerHouses.class);
			StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
			assert this.minecraft != null;
			this.minecraft.displayGuiScreen(null);
		} else if (button == this.btnBedColor) {
			this.configuration.bedColor = DyeColor.byId(this.configuration.bedColor.getId() + 1);
			this.btnBedColor.setMessage(GuiLangKeys.translateDye(this.configuration.bedColor));
		}
	}
}