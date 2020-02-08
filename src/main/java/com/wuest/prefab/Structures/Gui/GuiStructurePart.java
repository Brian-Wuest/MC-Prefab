package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Base.EnumStairsMaterial;
import com.wuest.prefab.Structures.Base.EnumStructureMaterial;
import com.wuest.prefab.Structures.Config.StructurePartConfiguration;
import com.wuest.prefab.Structures.Config.StructurePartConfiguration.EnumStyle;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructurePart;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import javafx.util.Pair;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

/**
 * This class is used as the gui for structure parts.
 *
 * @author WuestMan
 */
@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
public class GuiStructurePart extends GuiStructure {
	protected StructurePartConfiguration configuration;
	private GuiSlider sldrStairWidth;
	private GuiSlider sldrStairHeight;
	private GuiSlider sldrGeneralWidth;
	private GuiSlider sldrGeneralHeight;
	private GuiButtonExt btnPartStyle;
	private GuiButtonExt btnMaterialType;
	private GuiButtonExt btnStairsMaterialType;
	private int modifiedInitialXAxis = 213;
	private int modifiedInitialYAxis = 83;

	public GuiStructurePart() {
		super("Structure Part");
		this.structureConfiguration = EnumStructureConfiguration.Parts;
	}

	@Override
	protected void Initialize() {
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Parts", StructurePartConfiguration.class);
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - this.modifiedInitialXAxis;
		int grayBoxY = this.getCenteredYAxis() - this.modifiedInitialYAxis;

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));

		this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		this.sldrStairHeight = new GuiSlider(grayBoxX + 147, grayBoxY + 100, 90, 20, "", "", 1, 9, this.configuration.stairHeight, false, true, this::buttonClicked);
		this.addButton(this.sldrStairHeight);

		this.sldrStairWidth = new GuiSlider(grayBoxX + 147, grayBoxY + 60, 90, 20, "", "", 1, 9, this.configuration.stairWidth, false, true, this::buttonClicked);
		this.addButton(this.sldrStairWidth);

		this.sldrGeneralHeight = new GuiSlider(grayBoxX + 147, grayBoxY + 100, 90, 20, "", "", 3, 9, this.configuration.generalHeight, false, true, this::buttonClicked);
		this.addButton(this.sldrGeneralHeight);

		this.sldrGeneralWidth = new GuiSlider(grayBoxX + 147, grayBoxY + 60, 90, 20, "", "", 3, 9, this.configuration.generalWidth, false, true, this::buttonClicked);
		this.addButton(this.sldrGeneralWidth);

		this.btnPartStyle = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateString(this.configuration.style.translateKey));

		this.btnMaterialType = this.createAndAddButton(grayBoxX + 147, grayBoxY + 20, 90, 20, this.configuration.partMaterial.getTranslatedName());

		this.btnStairsMaterialType = this.createAndAddButton(grayBoxX + 147, grayBoxY + 20, 90, 20, this.configuration.stairsMaterial.getTranslatedName());
	}

	@Override
	protected Pair<Integer, Integer> getAdjustedXYValue() {
		return new Pair<>(this.getCenteredXAxis() - this.modifiedInitialXAxis, this.getCenteredYAxis() - this.modifiedInitialYAxis);
	}

	@Override
	protected void preButtonRender(int x, int y) {
		super.preButtonRender(x, y);

		this.minecraft.getTextureManager().bindTexture(this.configuration.style.getPictureLocation());

		GuiStructurePart.drawModalRectWithCustomSizedTexture(x + 250, y, 1,
				this.configuration.style.imageWidth, this.configuration.style.imageHeight,
				this.configuration.style.imageWidth, this.configuration.style.imageHeight);
	}

	@Override
	protected void postButtonRender(int x, int y) {
		this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STYLE), x + 10, y + 10, this.textColor);
		this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.MATERIAL), x + 147, y + 10, this.textColor);

		if (this.configuration.style == EnumStyle.Stairs
				|| this.configuration.style == EnumStyle.Roof) {
			this.sldrStairHeight.visible = this.configuration.style != EnumStyle.Roof;
			this.sldrStairWidth.visible = true;
			this.sldrGeneralHeight.visible = false;
			this.sldrGeneralWidth.visible = false;
			this.btnStairsMaterialType.visible = true;
			this.btnMaterialType.visible = false;
		} else {
			this.btnStairsMaterialType.visible = false;
			this.btnMaterialType.visible = true;
			this.sldrStairHeight.visible = false;
			this.sldrStairWidth.visible = false;
			this.sldrGeneralHeight.visible = true;
			this.sldrGeneralWidth.visible = true;
		}

		if (this.configuration.style != EnumStyle.Roof) {
			if (this.configuration.style == EnumStyle.Floor) {
				this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.LENGTH), x + 147, y + 90, this.textColor);
			} else {
				this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.HEIGHT), x + 147, y + 90, this.textColor);
			}
		}

		if (this.configuration.style == EnumStyle.Roof) {
			this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.HEIGHT), x + 147, y + 50, this.textColor);
		} else {
			this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.WIDTH), x + 147, y + 50, this.textColor);
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	public void buttonClicked(Button button) {
		this.configuration.houseFacing = this.minecraft.player.getHorizontalFacing().getOpposite();
		this.configuration.stairHeight = this.sldrStairHeight.getValueInt();
		this.configuration.stairWidth = this.sldrStairWidth.getValueInt();
		this.configuration.generalHeight = this.sldrGeneralHeight.getValueInt();
		this.configuration.generalWidth = this.sldrGeneralWidth.getValueInt();

		this.performCancelOrBuildOrHouseFacing(this.configuration, button);

		if (button == this.btnMaterialType) {
			this.configuration.partMaterial = EnumStructureMaterial.getMaterialByNumber(this.configuration.partMaterial.getNumber() + 1);
			this.btnMaterialType.setMessage(this.configuration.partMaterial.getTranslatedName());
		}
		if (button == this.btnStairsMaterialType) {
			this.configuration.stairsMaterial = EnumStairsMaterial.getByOrdinal(this.configuration.stairsMaterial.ordinal() + 1);
			this.btnStairsMaterialType.setMessage(this.configuration.stairsMaterial.getTranslatedName());
		} else if (button == this.btnPartStyle) {
			this.configuration.style = EnumStyle.getByOrdinal(this.configuration.style.ordinal() + 1);
			this.btnPartStyle.setMessage(GuiLangKeys.translateString(this.configuration.style.translateKey));
		} else if (button == this.btnVisualize) {
			StructurePart structure = new StructurePart();
			structure.getClearSpace().getShape().setDirection(Direction.NORTH);
			structure.setupStructure(this.minecraft.world, this.configuration, this.pos);

			StructureRenderHandler.setStructure(structure, Direction.SOUTH, this.configuration);
			this.minecraft.displayGuiScreen(null);
		}
	}
}
