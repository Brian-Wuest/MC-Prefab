package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Config.ServerModConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Config.ModerateHouseConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureModerateHouse;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import com.wuest.prefab.Tuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.awt.*;

/**
 * @author WuestMan
 */
public class GuiModerateHouse extends GuiStructure {
	protected ModerateHouseConfiguration configuration;
	protected ServerModConfiguration serverConfiguration;
	private ExtendedButton btnHouseStyle;
	private GuiCheckBox btnAddChest;
	private GuiCheckBox btnAddChestContents;
	private GuiCheckBox btnAddMineShaft;
	private ExtendedButton btnBedColor;
	private boolean allowItemsInChestAndFurnace = true;

	public GuiModerateHouse() {
		super("Moderate House");

		this.structureConfiguration = EnumStructureConfiguration.ModerateHouse;
		this.modifiedInitialXAxis = 212;
		this.modifiedInitialYAxis = 83;
	}

	@Override
	protected void Initialize() {
		if (!Minecraft.getInstance().player.isCreative()) {
			this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
		}

		this.serverConfiguration = Prefab.proxy.getServerConfiguration();
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Moderate Houses", ModerateHouseConfiguration.class);
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 212;
		int grayBoxY = this.getCenteredYAxis() - 83;

		// Create the buttons.
		this.btnHouseStyle = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, this.configuration.houseStyle.getDisplayName());

		this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 50, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));

		int x = grayBoxX + 130;
		int y = grayBoxY + 20;

		this.btnBedColor = this.createAndAddButton(x, y, 90, 20, GuiLangKeys.translateDye(this.configuration.bedColor));

		y += 30;

		this.btnAddChest = this.createAndAddCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST), this.configuration.addChests, null);
		y += 15;

		this.btnAddMineShaft = this.createAndAddCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT), this.configuration.addChestContents, null);
		y += 15;

		this.btnAddChestContents = this.createAndAddCheckBox(x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS), this.configuration.addMineshaft, null);
	}

	@Override
	protected void preButtonRender(int x, int y) {
		super.preButtonRender(x, y);

		this.bindTexture(this.configuration.houseStyle.getHousePicture());
		GuiTabScreen.drawModalRectWithCustomSizedTexture(x + 249, y, 1,
				this.configuration.houseStyle.getImageWidth(), this.configuration.houseStyle.getImageHeight(),
				this.configuration.houseStyle.getImageWidth(), this.configuration.houseStyle.getImageHeight());
	}

	@Override
	protected void postButtonRender(int x, int y) {
		this.btnAddChest.visible = this.serverConfiguration.addChests;
		this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
		this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;

		// Draw the text here.
		this.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 10, y + 10, this.textColor);

		this.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 130, y + 10, this.textColor);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	public void buttonClicked(AbstractButton button) {
		this.configuration.addChests = this.btnAddChest.visible && this.btnAddChest.isChecked();
		this.configuration.addChestContents = this.allowItemsInChestAndFurnace && (this.btnAddChestContents.visible && this.btnAddChestContents.isChecked());
		this.configuration.addMineshaft = this.btnAddMineShaft.visible && this.btnAddMineShaft.isChecked();

		this.performCancelOrBuildOrHouseFacing(this.configuration, button);

		if (button == this.btnHouseStyle) {
			int id = this.configuration.houseStyle.getValue() + 1;
			this.configuration.houseStyle = ModerateHouseConfiguration.HouseStyle.ValueOf(id);

			this.btnHouseStyle.setMessage(this.configuration.houseStyle.getDisplayName());
		} else if (button == this.btnVisualize) {
			StructureModerateHouse structure = StructureModerateHouse.CreateInstance(this.configuration.houseStyle.getStructureLocation(), StructureModerateHouse.class);
			StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
			this.closeScreen();
		} else if (button == this.btnBedColor) {
			this.configuration.bedColor = DyeColor.byId(this.configuration.bedColor.getId() + 1);
			this.btnBedColor.setMessage(GuiLangKeys.translateDye(this.configuration.bedColor));
		}
	}
}
