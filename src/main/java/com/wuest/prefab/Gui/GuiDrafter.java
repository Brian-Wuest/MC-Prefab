package com.wuest.prefab.Gui;

import java.awt.Color;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Blocks.BlockDrafter;
import com.wuest.prefab.Config.DrafterTileEntityConfig;
import com.wuest.prefab.Config.DrafterTileEntityConfig.AvailableRoomType;
import com.wuest.prefab.Config.DrafterTileEntityConfig.RoomInfo;
import com.wuest.prefab.Config.DrafterTileEntityConfig.RoomMaterial;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Gui.Controls.GuiRoomInfoButton;
import com.wuest.prefab.Gui.Controls.GuiTab;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.TileEntities.TileEntityDrafter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This screen is used for the modular house to build the additional rooms.
 * @author WuestMan
 *
 */
public class GuiDrafter extends GuiTabScreen
{
	public AvailableRoomType selectedRoomType;
	
	private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/defaultBackground.png");
	
	protected GuiTab tabGeneral;
	protected GuiTab tabDesignRoom;
	protected GuiTab tabPendingChanges;
	
	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnBuild;
	protected GuiButtonExt btnClearPending;
	protected GuiButtonExt btnAddToPending;
	
	protected GuiButtonExt btnBasement2;
	protected GuiButtonExt btnBasement1;
	protected GuiButtonExt btnGroundFloor;
	protected GuiButtonExt btnSecondFloor;
	protected GuiButtonExt btnThirdFloor;
	
	protected RoomList listRooms;
	protected RoomMaterials roomMaterials;
	
	/**
	 * This is the array list for all 49 rooms.
	 */
	protected ArrayList<GuiRoomInfoButton> roomButtons;
	protected ArrayList<HoverChecker> roomHovers;
	
	protected BlockPos pos;
	protected ModConfiguration serverConfiguration;
	protected DrafterTileEntityConfig drafterConfig;
	protected TileEntityDrafter tileEntity;
	
	
	protected GuiRoomInfoButton selectedRoom;
	
	public GuiDrafter(int x, int y, int z)
	{
		super();
		this.pos = new BlockPos(x, y, z);
		this.Tabs.trayWidth = 320;
		this.roomButtons = new ArrayList<GuiRoomInfoButton>();
		this.roomHovers = new ArrayList<HoverChecker>();
		this.selectedRoomType = AvailableRoomType.Field;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.Initialize();
	}
	
	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = (this.width / 2) - 158;
		int grayBoxY = (this.height / 2) - 93;
		this.Tabs.trayX = grayBoxX;
		this.Tabs.trayY = grayBoxY - 21;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(backgroundTextures);
		this.drawModalRectWithCustomSizedTexture(grayBoxX, grayBoxY, 0, 320, 320, 320, 320);

		for (GuiButton button : this.buttonList)
		{
			// Make all buttons invisible.
			if (button != this.btnCancel)
			{
				button.visible = false;
			}
		}
		
		// Update visibility on controls based on the selected tab.
		if (this.getSelectedTab() == this.tabGeneral)
		{
			this.makeGeneralTabControlsVisible();
		}
		else if (this.getSelectedTab() == this.tabDesignRoom)
		{
			this.makeDesignRoomTabControlsVisible(x, y, f);
		}
		else if (this.getSelectedTab() == this.tabPendingChanges)
		{
			this.makePendingChangesTabControlsVisible(x, y, f);
		}
		
		// Draw the buttons, labels and tabs.
		super.drawScreen(x, y, f);
		
		for (int i = 0; i < 49; i++)
		{
			HoverChecker hoverChecker = this.roomHovers.get(i);
			
			if (hoverChecker.checkHover(x, y))
			{
				GuiRoomInfoButton button = this.roomButtons.get(i);
				
				if (button.enabled)
				{
					String hoverText = "Room Name: " + (i + 1) + "\nRoom Coordinates:\n X: BlahX Y: BlahY Z: BlahZ";
					this.drawHoveringText(this.mc.fontRendererObj.listFormattedStringToWidth(button.getHoverText(), 300), x, y);
				}
			}
		}
		
		// Draw the text here.
		int color = Color.DARK_GRAY.getRGB();
		
		// Draw specific text labels here.
		
		if (this.getSelectedTab() == this.tabGeneral)
		{
			this.mc.fontRendererObj.drawString("Level", grayBoxX + 7, grayBoxY + 20, color);
		}
		else if (this.getSelectedTab() == this.tabDesignRoom)
		{
			this.mc.fontRendererObj.drawString("Available Room Styles", grayBoxX + 7, grayBoxY + 5, color);
			this.mc.fontRendererObj.drawString("Room Materials", grayBoxX + 125, grayBoxY + 5, color);
		}
	}
	
	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		this.selectedRoom = null;
		
		if (button == this.btnCancel)
		{
			this.sendConfigToServer();
			this.mc.displayGuiScreen(null);
		}
		else if (button == this.btnBasement2)
		{
			this.setRoomInfosForFloor(-2);
			this.setFloorButtonsEnabled();
			button.enabled = false;
		}
		else if (button == this.btnBasement1)
		{
			this.setRoomInfosForFloor(-1);
			this.setFloorButtonsEnabled();
			button.enabled = false;
		}
		else if (button == this.btnGroundFloor)
		{
			this.setRoomInfosForFloor(0);
			this.setFloorButtonsEnabled();
			button.enabled = false;
		}
		else if (button == this.btnSecondFloor)
		{
			this.setRoomInfosForFloor(1);
			this.setFloorButtonsEnabled();
			button.enabled = false;
		}
		else if (button == this.btnThirdFloor)
		{
			this.setRoomInfosForFloor(2);
			this.setFloorButtonsEnabled();
			button.enabled = false;
		}
		else if (button == this.btnClearPending)
		{
			this.tabPendingChanges.visible = false;
		}
		else if (button instanceof GuiRoomInfoButton)
		{
			// If a room info button was clicked, set the selected button so everything will show up on the details page.
			this.selectedRoom = (GuiRoomInfoButton)button;
			
			if (this.selectedRoom.roomInfo.StructureName.equals("Nothing"))
			{
				this.tabDesignRoom.visible = true;
			}
			else
			{
				this.tabDesignRoom.visible = false;
			}
		}
		else if (button == this.btnAddToPending)
		{
			this.tabPendingChanges.visible = true;
		}
	}
	
	protected void setRoomInfosForFloor(int floorNumber)
	{
		RoomInfo[] roomArray = null;
		
		switch (floorNumber)
		{
			case -2:
			{
				roomArray = this.drafterConfig.Basement2FloorRooms;
				break;
			}
			
			case -1:
			{
				roomArray = this.drafterConfig.BasementFloorRooms;
				break;
			}
			
			case 0:
			{
				roomArray = this.drafterConfig.FirstFloorRooms;
				break;
			}
			
			case 1:
			{
				roomArray = this.drafterConfig.SecondFloorRooms;
				break;
			}
			
			case 2:
			{
				roomArray = this.drafterConfig.ThirdFloorRooms;
				break;
			}
		}
		
		this.enableRoomsForFloor(roomArray, floorNumber);
	}
	
	protected void enableRoomsForFloor(RoomInfo[] roomArray, int floorNumber)
	{
		for (int i = 0; i < 49; i++)
		{
			GuiRoomInfoButton button = this.roomButtons.get(i);
			button.roomInfo = roomArray[i];
			
			// Ground floor room 47 is the foyer and will always exist.
			if (i == 45 && floorNumber == 0)
			{
				button.enabled = true;
				continue;
			}
			else if (i == 45)
			{
				button.enabled = true;
				continue;
			}
			
			// Only set the buttons to enabled if a neighbor is enabled and it has a structure name.
			button.enabled = button.roomInfo.checkNeighbors(roomArray);
			
			if (button.enabled && button.roomInfo.StructureName.equals("Nothing"))
			{
				button.displayString = "+";
			}
			else if (!button.enabled)
			{
				button.displayString = "";
			}
		}
	}
	
	protected void setFloorButtonsEnabled()
	{
		this.btnBasement2.enabled = true;
		this.btnBasement1.enabled = true;
		this.btnGroundFloor.enabled = true;
		this.btnSecondFloor.enabled = true;
		this.btnThirdFloor.enabled = true;
	}
	
	private void Initialize()
	{
		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 158;
		int grayBoxY = (this.height / 2) - 103;
		int color = Color.DARK_GRAY.getRGB();
		int uiComponentID = 1;
		this.serverConfiguration = ((ClientProxy)Prefab.proxy).getServerConfiguration();
		
		// Get the power configuration settings.
		TileEntity entity = this.mc.theWorld.getTileEntity(this.pos);

		if (entity != null && entity.getClass() == TileEntityDrafter.class)
		{
			this.drafterConfig = ((TileEntityDrafter)entity).getConfig();
			this.tileEntity = (TileEntityDrafter)entity;
		}
		else
		{
			this.tileEntity = new TileEntityDrafter();
			this.mc.theWorld.setTileEntity(pos, this.tileEntity);

			this.drafterConfig = this.tileEntity.getConfig();
		}

		this.drafterConfig.pos = this.pos;
		
		// Create the general tab controls.
		uiComponentID = this.createGeneralTabControls(grayBoxX, grayBoxY, uiComponentID);
		
		// Create the design room tab controls.
		uiComponentID = this.createDesignRoomTabControls(grayBoxX, grayBoxY, uiComponentID);
		
		// Create the pending changes tab controls.
		uiComponentID = this.createPendingChangesTabControls(grayBoxX, grayBoxY, uiComponentID);
		
		this.tabGeneral.setIsSelected(true);
	}
	
	private int createGeneralTabControls(int grayBoxX, int grayBoxY, int uiComponentID)
	{
		int roomX = grayBoxX + 40;
		int roomY = grayBoxY + 30;
		
		// Create the 49 room buttons.
		for (int i = 1; i < 50; i++)
		{
			GuiRoomInfoButton roomButton = new GuiRoomInfoButton(uiComponentID++, roomX, roomY, 18, 18, "");
			HoverChecker hoverChecker = new HoverChecker(roomButton, 800);
			this.roomHovers.add(hoverChecker);
			this.buttonList.add(roomButton);
			this.roomButtons.add(roomButton);
			roomX = roomX + 18;
			
			if (i % 7 == 0)
			{
				roomX = grayBoxX + 40;
				roomY = roomY + 18;
			}
		}
		
		this.btnBasement2 = new GuiButtonExt(uiComponentID++, grayBoxX + 10, grayBoxY + 145, 20, 20, "B2");
		this.buttonList.add(this.btnBasement2);
		
		this.btnBasement1 = new GuiButtonExt(uiComponentID++, grayBoxX + 10, grayBoxY + 120, 20, 20, "B1");
		this.buttonList.add(this.btnBasement1);
		
		this.btnGroundFloor = new GuiButtonExt(uiComponentID++, grayBoxX + 10, grayBoxY + 95, 20, 20, "G");
		this.btnGroundFloor.enabled = false;
		
		this.buttonList.add(this.btnGroundFloor);
		
		this.btnSecondFloor = new GuiButtonExt(uiComponentID++, grayBoxX + 10, grayBoxY + 70, 20, 20, "2");
		this.buttonList.add(this.btnSecondFloor);
		
		this.btnThirdFloor = new GuiButtonExt(uiComponentID++, grayBoxX + 10, grayBoxY + 45, 20, 20, "3");
		this.buttonList.add(this.btnThirdFloor);
		
		this.btnClearPending = new GuiButtonExt(uiComponentID++, grayBoxX + 110, grayBoxY + 186, 80, 20, "Clear Pending");
		this.buttonList.add(this.btnClearPending);
		
		this.btnBuild = new GuiButtonExt(uiComponentID++, grayBoxX + 195, grayBoxY + 186, 50, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);
		
		this.btnCancel = new GuiButtonExt(uiComponentID++, grayBoxX + 250, grayBoxY + 186, 50, 20, "Close");
		this.buttonList.add(this.btnCancel);
		
		// Tabs:
		this.tabGeneral = new GuiTab(this.Tabs, GuiLangKeys.translateString(GuiLangKeys.STARTER_TAB_GENERAL), grayBoxX + 5, grayBoxY - 10);
		this.Tabs.AddTab(this.tabGeneral);
		
		this.tabDesignRoom = new GuiTab(this.Tabs, "Design Room", grayBoxX + 58, grayBoxY - 10);
		this.tabDesignRoom.width = 90;
		this.tabDesignRoom.visible = false;
		this.Tabs.AddTab(tabDesignRoom);
		
		this.tabPendingChanges = new GuiTab(this.Tabs, "Pending Changes", grayBoxX + 151, grayBoxY - 10);
		this.tabPendingChanges.width = 100;
		this.tabPendingChanges.visible = false;
		this.Tabs.AddTab(this.tabPendingChanges);
		
		try
		{
			this.actionPerformed(this.btnGroundFloor);
			this.roomButtons.get(45).roomInfo.StructureName = "Foyer";
			this.actionPerformed(this.btnGroundFloor);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return uiComponentID;
	}

	private int createDesignRoomTabControls(int grayBoxX, int grayBoxY, int uiComponentID)
	{
		this.btnAddToPending = new GuiButtonExt(uiComponentID++, grayBoxX + 115, grayBoxY + 186, 130, 20, "Add To Pending Changes");
		this.buttonList.add(this.btnAddToPending);
		
		this.listRooms = new RoomList(this, this.mc, 100, 85, grayBoxY + 25, grayBoxX + 10, 20);
		this.roomMaterials = new RoomMaterials(this, this.mc, 175, 85, grayBoxY + 25, grayBoxX + 125, 20);
		return uiComponentID;
	}
	
	private int createPendingChangesTabControls(int grayBoxX, int grayBoxY, int uiComponentID)
	{
		
		return uiComponentID;
	}
	
	private void makeGeneralTabControlsVisible()
	{
		for (GuiRoomInfoButton roomInfoButton : this.roomButtons)
		{
			roomInfoButton.visible = true;
		}
		
		this.btnBasement2.visible = true;
		this.btnBasement1.visible = true;
		this.btnBuild.visible = true;
		this.btnClearPending.visible = true;
		this.btnGroundFloor.visible = true;
		this.btnSecondFloor.visible = true;
		this.btnThirdFloor.visible = true;
	}
	
	private void makeDesignRoomTabControlsVisible(int mouseX, int mouseY, float renderPartialTicks)
	{
		this.btnAddToPending.visible = true;
		
		// TODO: Make the controls.
		this.listRooms.drawScreen(mouseX, mouseY, renderPartialTicks);
		this.roomMaterials.drawScreen(mouseX, mouseY, renderPartialTicks);
	}
	
	private void makePendingChangesTabControlsVisible(int mouseX, int mouseY, float renderPartialTicks)
	{
		// TODO: Make the controls.
	}
	
	private void sendConfigToServer()
	{
		// TODO: Make the message.
	}

	public class RoomList extends GuiScrollingList
	{
		protected GuiDrafter parent;
		
	    public RoomList(GuiDrafter parent, Minecraft client, int width, int height, int top, int left, int entryHeight)
	    {
	        super(client, width, height, top, top + height, left, entryHeight, parent.width, parent.height);
	        this.parent = parent;
	    }

		@Override
		protected int getSize()
		{
			return AvailableRoomType.values().length;
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick)
		{
			AvailableRoomType roomType = AvailableRoomType.ValueOf(index);
		
			this.parent.selectedRoomType = roomType;
		}

		@Override
		protected boolean isSelected(int index)
		{
			return this.parent.selectedRoomType.getKey() == index;
		}

		@Override
		protected void drawBackground()
		{
		}
		
		@Override
		protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
		{
			FontRenderer font = this.parent.fontRendererObj;
			AvailableRoomType room = AvailableRoomType.ValueOf(slotIdx);
			
            font.drawString(font.trimStringToWidth(room.getName(),    listWidth - 5), this.left + 3 , slotTop +  2, 0xFFFFFF);
		}
	}
	
	public class RoomMaterials extends GuiScrollingList
	{
		protected GuiDrafter parent;
		protected int selectdIndex = 0;
		
	    public RoomMaterials(GuiDrafter parent, Minecraft client, int width, int height, int top, int left, int entryHeight)
	    {
	        super(client, width, height, top, top + height, left, entryHeight, parent.width, parent.height);
	        this.parent = parent;
	    }
	    
		@Override
		protected int getSize()
		{
			return this.parent.selectedRoomType.getRoomMaterials().size();
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick)
		{
			this.selectdIndex = index;
		}

		@Override
		protected boolean isSelected(int index)
		{	
			return this.selectdIndex == index;
		}

		@Override
		protected void drawBackground()
		{
		}
		
		@Override
		protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
		{
			FontRenderer font = this.parent.fontRendererObj;
			AvailableRoomType room = AvailableRoomType.ValueOf(slotIdx);
			
			ArrayList<RoomMaterial> selectedRoomMaterials = this.parent.selectedRoomType.getRoomMaterials();
			
			int i = 0;
			RoomMaterial materialForSlot = selectedRoomMaterials.get(slotIdx);
			
			String itemName = "Not Found!";
			int materialCount = 0;
			ItemStack itemToDraw = null;
			
			Item foundItem = Item.REGISTRY.getObject(materialForSlot.resourceLocation);
			
			if (materialForSlot.metaData >= 0)
			{
				itemToDraw = new ItemStack(foundItem, 1, materialForSlot.metaData);
				itemName = net.minecraft.client.resources.I18n.format(itemToDraw.getUnlocalizedName() + ".name", new Object[0]);
			}
			else
			{
				itemName = net.minecraft.client.resources.I18n.format(foundItem.getUnlocalizedName() + ".name", new Object[0]);
				itemToDraw = new ItemStack(foundItem);
			}
			
			materialCount = materialForSlot.numberRequired;
			
			int foundMaterialCount = this.getFoundMaterialCount(itemToDraw);
			
			int left = this.left + 3;
			
			// Format: [##] [Icon] [Item Name]
            font.drawString(font.trimStringToWidth(materialCount + "", listWidth - 5), left , slotTop +  2, 0xFFFFFF);
            
            if (foundMaterialCount < materialCount)
            {
            	// Draw a string containing the number of materials found in a chest on the it's left.
            	left += 10;
            	
            	if (materialCount > 9)
            	{
            		left += 10;
            	}
            	
            	font.drawString("(" + foundMaterialCount + ")", left, slotTop + 2, Color.RED.getRGB());
            }
            
            this.drawItem(left + 16, slotTop - 1, itemToDraw);
            font.drawString(font.trimStringToWidth(itemName, listWidth - 5), left + 40 , slotTop +  2, 0xFFFFFF);
		}
		
	    /**
	     * Draws an item with a background at the given coordinates. The item and its background are 20 pixels tall/wide
	     * (though only the inner 18x18 is actually drawn on)
	     */
	    private void drawItem(int x, int z, ItemStack itemToDraw)
	    {
	        this.drawItemBackground(x, z, 0, 0);
	        GlStateManager.enableRescaleNormal();

	        if (itemToDraw != null && itemToDraw.getItem() != null)
	        {
	            RenderHelper.enableGUIStandardItemLighting();
	            this.parent.itemRender.renderItemIntoGUI(itemToDraw, x + 1, z + 1);
	            RenderHelper.disableStandardItemLighting();
	        }

	        GlStateManager.disableRescaleNormal();
	    }
	    
        /**
         * Draws the background icon for an item, using a texture from stats.png with the given coords
         */
        private void drawItemBackground(int x, int z, int textureX, int textureY)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.parent.mc.getTextureManager().bindTexture(Gui.STAT_ICONS);
            float f = 0.0078125F;
            float f1 = 0.0078125F;
            int i = 18;
            int j = 18;
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertexbuffer.pos((double)(x + 0), (double)(z + 18), (double)this.parent.zLevel).tex((double)((float)(textureX + 0) * 0.0078125F), (double)((float)(textureY + 18) * 0.0078125F)).endVertex();
            vertexbuffer.pos((double)(x + 18), (double)(z + 18), (double)this.parent.zLevel).tex((double)((float)(textureX + 18) * 0.0078125F), (double)((float)(textureY + 18) * 0.0078125F)).endVertex();
            vertexbuffer.pos((double)(x + 18), (double)(z + 0), (double)this.parent.zLevel).tex((double)((float)(textureX + 18) * 0.0078125F), (double)((float)(textureY + 0) * 0.0078125F)).endVertex();
            vertexbuffer.pos((double)(x + 0), (double)(z + 0), (double)this.parent.zLevel).tex((double)((float)(textureX + 0) * 0.0078125F), (double)((float)(textureY + 0) * 0.0078125F)).endVertex();
            tessellator.draw();
        }
	
        private int getFoundMaterialCount(ItemStack desiredItemStack)
        {
        	int returnValue = 0;
        	
        	for (ItemStack stack : this.parent.tileEntity.getStacks())
        	{
    			if (stack != null && stack.getItem() == desiredItemStack.getItem() && stack.getMetadata() == desiredItemStack.getMetadata())
    			{
    				// This is an exact match, update the return value with how many items are in this stack.
    				returnValue += stack.stackSize;
    			}
        	}
        	
        	/*
        	IBlockState drafterState = this.parent.tileEntity.getBlockType().getStateFromMeta(this.parent.tileEntity.getBlockMetadata());
        	EnumFacing blockFacing = drafterState.getValue(BlockDrafter.FACING).rotateYCCW();
        	
        	BlockPos inventoryPos = this.parent.pos.offset(blockFacing);
        	TileEntity tileEntity = this.parent.mc.theWorld.getTileEntity(inventoryPos);
        	
        	if (tileEntity != null && tileEntity instanceof IInventory)
        	{
        		IInventory inventory = (IInventory)tileEntity;
        		
        		for (int i = 0; i < inventory.getSizeInventory(); i++)
        		{
        			ItemStack slotStack = inventory.getStackInSlot(i);
        			
        			if (slotStack != null && slotStack.getItem() == desiredItemStack.getItem() && slotStack.getMetadata() == desiredItemStack.getMetadata())
        			{
        				// This is an exact match, update the return value with how many items are in this stack.
        				returnValue += slotStack.stackSize;
        			}
        		}
        	}
        	*/
        	
        	return returnValue;
        }
	}
}
