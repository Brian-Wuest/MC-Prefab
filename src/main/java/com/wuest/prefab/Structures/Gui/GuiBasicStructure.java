package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration;
import com.wuest.prefab.Structures.Items.ItemBasicStructure;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureBasic;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import com.wuest.prefab.Tuple;
import javafx.util.Pair;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;

/**
 * This class is used as the gui for all basic structures.
 *
 * @author WuestMan
 */
@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
public class GuiBasicStructure extends GuiStructure {
	protected BasicStructureConfiguration configuration;
	private boolean includePicture = true;
	private int modifiedInitialXAxis = 213;
	private int modifiedInitialYAxis = 83;
	private GuiButtonExt btnBedColor = null;

	public GuiBasicStructure() {
		super("Basic Structure");
		this.structureConfiguration = EnumStructureConfiguration.Basic;
	}

	@Override
	protected Tuple<Integer, Integer> getAdjustedXYValue() {
		return new Tuple<>(this.getCenteredXAxis() - this.modifiedInitialXAxis, this.getCenteredYAxis() - this.modifiedInitialYAxis);
	}

	@Override
	protected void preButtonRender(int x, int y) {
		super.preButtonRender(x, y);

		if (this.includePicture) {
			// Draw the control background.
			this.getMinecraft().getTextureManager().bindTexture(this.configuration.basicStructureName.getTopDownPictureLocation());

			GuiBasicStructure.drawModalRectWithCustomSizedTexture(x + 250, y, 1,
					this.configuration.basicStructureName.getImageWidth(), this.configuration.basicStructureName.getImageHeight(),
					this.configuration.basicStructureName.getImageWidth(), this.configuration.basicStructureName.getImageHeight());
		}
	}

	@Override
	protected void postButtonRender(int x, int y) {
		// Draw the text here.
		if (this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.MineshaftEntrance
			|| this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.WatchTower
			|| this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.WelcomeCenter) {
			this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 10, y + 10, this.textColor);
		}

		this.getMinecraft().fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), x + 147, y + 10, 95, this.textColor);
	}

	@Override
	protected void Initialize() {
		// Get the structure configuration for this itemstack.
		ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(this.player);

		if (stack != null) {
			ItemBasicStructure item = (ItemBasicStructure) stack.getItem();
			this.configuration = ClientEventHandler.playerConfig.getClientConfig(item.structureType.getName(), BasicStructureConfiguration.class);
			this.configuration.basicStructureName = item.structureType;
			this.includePicture = this.doesPictureExist();
		}

		this.configuration.pos = this.pos;

		if (!this.includePicture) {
			this.modifiedInitialXAxis = 125;
		}

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - this.modifiedInitialXAxis;
		int grayBoxY = this.getCenteredYAxis() - this.modifiedInitialYAxis;

		// Create the buttons.
		int x = grayBoxX + 10;
		int y = grayBoxY + 20;

		this.btnBedColor = this.createAndAddButton(x, y, 90, 20, GuiLangKeys.translateDye(this.configuration.bedColor));

		if (this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.MineshaftEntrance
				|| this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.WatchTower
				|| this.configuration.basicStructureName == BasicStructureConfiguration.EnumBasicStructureName.WelcomeCenter) {
			this.btnBedColor.visible = true;
			y += 40;
		}
		else {
			this.btnBedColor.visible = false;
		}

		this.btnVisualize = this.createAndAddButton(x, y, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
	}

	/**
	 * Determines if the picture for this screen exists in the resources.
	 *
	 * @return A value indicating whether the picture exists.
	 */
	private boolean doesPictureExist() {
		try {
			this.getMinecraft().getResourceManager().getResource(this.configuration.basicStructureName.getTopDownPictureLocation());
			return true;
		} catch (IOException e) {
			return false;
		}
	}


	@Override
	public void buttonClicked(Button button) {
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);

		if (button == this.btnVisualize) {
			StructureBasic structure = StructureBasic.CreateInstance(this.configuration.basicStructureName.getAssetLocation(), StructureBasic.class);
			StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
			this.getMinecraft().displayGuiScreen(null);
		} else if (button == this.btnBedColor) {
			this.configuration.bedColor = DyeColor.byId(this.configuration.bedColor.getId() + 1);
			this.btnBedColor.setMessage(GuiLangKeys.translateDye(this.configuration.bedColor));
		}
	}
}
