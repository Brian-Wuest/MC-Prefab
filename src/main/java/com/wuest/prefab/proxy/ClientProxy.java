package com.wuest.prefab.proxy;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.config.ModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.structures.events.StructureClientEventHandler;
import com.wuest.prefab.structures.gui.GuiStructure;
import com.wuest.prefab.structures.items.StructureItem;
import com.wuest.prefab.structures.render.ShaderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WuestMan
 */
public class ClientProxy extends CommonProxy {
    public static ClientEventHandler clientEventHandler = new ClientEventHandler();
    public static StructureClientEventHandler structureClientEventHandler = new StructureClientEventHandler();

    /**
     * The hashmap of mod guis.
     */
    public static HashMap<StructureItem, GuiStructure> ModGuis = new HashMap<>();
    public ModConfiguration serverConfiguration = null;

    public ClientProxy() {
        super();

        this.isClient = true;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        // After all items have been registered and all recipes loaded, register any necessary renderer.
        Prefab.proxy.registerRenderers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postinit(FMLPostInitializationEvent event) {
        super.postinit(event);
    }

    @Override
    public void registerRenderers() {
        ShaderHelper.Initialize();
    }

    @Override
    public ModConfiguration getServerConfiguration() {
        if (this.serverConfiguration == null) {
            // Get the server configuration.
            return CommonProxy.proxyConfiguration;
        } else {
            return this.serverConfiguration;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        GuiStructure screen = null;
        for (Map.Entry<StructureItem, GuiStructure> entry : ClientProxy.ModGuis.entrySet()) {
            ItemStack mainHelditem = player.getHeldItem(EnumHand.MAIN_HAND);
            ItemStack otherHelditem = player.getHeldItem(EnumHand.OFF_HAND);


            if (entry.getKey() == otherHelditem.getItem()) {
                screen = entry.getValue();
            } else if (entry.getKey() == mainHelditem.getItem()) {
                screen = entry.getValue();
            }

            if (screen != null) {
                screen.pos = new BlockPos(x, y, z);
                break;
            }
        }

        return screen;
    }
}