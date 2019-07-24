package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Gui.GuiStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author WuestMan
 */
public class StructureItem extends Item {

    /**
     * Get's the GuiId to show to the user when this item is used.
     */
    protected int guiId = 0;

    /**
     * Initializes a new instance of the StructureItem class.
     */
    public StructureItem(String name) {
        super(new Item.Properties().group(ItemGroup.MISC));
        this.Initialize(name);
    }

    public StructureItem(String name, Item.Properties properties) {
        super(properties);
        this.Initialize(name);
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getWorld().isRemote) {
            if (context.getFace() == Direction.UP) {
                // Open the client side gui to determine the house options.
                GuiStructure screen = this.getScreen();
                screen.pos = context.getPos();
                Minecraft.getInstance().displayGuiScreen(screen);
                return ActionResultType.PASS;
            }
        }

        return ActionResultType.FAIL;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiStructure getScreen() {
        return null;
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    protected void Initialize(String name) {
        ModRegistry.setItemName(this, name);
    }
}
