package com.wuest.prefab.structures.gui;

import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.structures.config.TreeFarmConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureTreeFarm;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.ResourceLocation;

/**
 * @author WuestMan
 */
public class GuiTreeFarm extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/tree_farm_top_down.png");
    protected TreeFarmConfiguration configuration;

    public GuiTreeFarm() {
        super("Tree Farm");
        this.structureConfiguration = EnumStructureConfiguration.TreeFarm;
    }

    @Override
    protected void Initialize() {
        super.Initialize();
        this.structureImageLocation = structureTopDown;
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Tree Farm", TreeFarmConfiguration.class);
        this.configuration.pos = this.pos;

        this.InitializeStandardButtons();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(AbstractButton button) {
        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnVisualize) {
            StructureTreeFarm structure = StructureTreeFarm.CreateInstance(StructureTreeFarm.ASSETLOCATION, StructureTreeFarm.class);
            this.performPreview(structure, this.configuration);
        }
    }
}
