package com.wuest.prefab.structures.gui;

import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.structures.config.FishPondConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureFishPond;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.ResourceLocation;

/**
 * @author WuestMan
 */
public class GuiFishPond extends GuiStructure {
    private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/fish_pond_top_down.png");
    protected FishPondConfiguration configuration;

    public GuiFishPond() {
        super("Fish Pond");
        this.structureConfiguration = EnumStructureConfiguration.FishPond;
    }

    @Override
    protected void Initialize() {
        super.Initialize();
        this.structureImageLocation = structureTopDown;
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Fish Pond", FishPondConfiguration.class);
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
            StructureFishPond structure = StructureFishPond.CreateInstance(StructureFishPond.ASSETLOCATION, StructureFishPond.class);
            this.performPreview(structure, this.configuration);
        }
    }
}
