package com.wuest.prefab.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Config.ConfigOption;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Quadruple;
import com.wuest.prefab.Tuple;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IBidiTooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuiPrefab extends GuiBase {
    /**
     * Distance from top of the screen to this GUI's title
     */
    private static final int TITLE_HEIGHT = 8;

    /**
     * Distance from top of the screen to the options row list's top
     */
    private static final int OPTIONS_LIST_TOP_HEIGHT = 55;

    /**
     * Distance from bottom of the screen to the options row list's bottom
     */
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;

    /**
     * Height of each item in the options row list
     */
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    /**
     * Distance from bottom of the screen to the "Done" button's top
     */
    private static final int DONE_BUTTON_TOP_OFFSET = 26;

    private final Screen parentScreen;
    private ExtendedButton doneButton;
    private ExtendedButton resetToDefaultsButton;
    private ExtendedButton generalGroupButton;

    private String currentOption = "General";

    private OptionsRowList currentRowList;
    private ArrayList<Quadruple<String, OptionsRowList, OptionsRowList, String>> optionCollection;
    private OptionsRowList optionsRowList;
    private OptionsRowList chestOptionsRowList;
    private OptionsRowList recipeOptionsRowList;
    private OptionsRowList starterHouseOptionsRowList;

    public GuiPrefab(Minecraft minecraft, Screen parent) {
        super("Prefab Configuration");
        this.parentScreen = parent;
        super.minecraft = minecraft;
    }

    @Nullable
    public static List<IReorderingProcessor> tooltipAt(OptionsRowList optionsRowList, int mouseX, int mouseY) {
        if (optionsRowList.isMouseOver(mouseX, mouseY)) {
            Optional<Widget> optional = optionsRowList.getMouseOver(mouseX, mouseY);

            if (optional.isPresent() && optional.get() instanceof IBidiTooltip) {
                Optional<List<IReorderingProcessor>> tooltip = ((IBidiTooltip) optional.get()).getTooltip();
                return tooltip.orElse(null);
            }
        }

        return null;
    }

    @Override
    protected void Initialize() {
        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int x = adjustedXYValue.getFirst();

        this.optionCollection = new ArrayList<>();

        this.resetToDefaultsButton = this.createAndAddButton(60, this.height - DONE_BUTTON_TOP_OFFSET, 100, 20, "Reset");
        this.doneButton = this.createAndAddButton(this.width - 160, this.height - DONE_BUTTON_TOP_OFFSET, 100, 20, "Done");

        this.generalGroupButton = this.createAndAddButton(this.width / 2, 30, 120, 20, "General");

        OptionsRowList generalOptions = new OptionsRowList(
                this.getMinecraft(),
                this.width,
                this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        OptionsRowList chestOptions = new OptionsRowList(
                this.getMinecraft(),
                this.width,
                this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        OptionsRowList recipeOptions = new OptionsRowList(
                this.getMinecraft(),
                this.width,
                this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        OptionsRowList houseOptions = new OptionsRowList(
                this.getMinecraft(),
                this.width,
                this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        this.optionCollection.add(new Quadruple<>("General", generalOptions, chestOptions, "Chest Options"));
        this.optionCollection.add(new Quadruple<>("Chest Options", chestOptions, recipeOptions, "Recipe Options"));
        this.optionCollection.add(new Quadruple<>("Recipe Options", recipeOptions, houseOptions, "House Options"));
        this.optionCollection.add(new Quadruple<>("House Options", houseOptions, generalOptions, "General"));

        for (ConfigOption<?> configOption : CommonProxy.proxyConfiguration.configOptions) {
            Quadruple<String, OptionsRowList, OptionsRowList, String> rowList = this.getOptionsRowList(configOption.getGroupName());

            if (rowList != null) {
                switch (configOption.getConfigType()) {
                    case "Boolean": {
                        this.addBooleanOption(rowList.getSecond(), configOption);
                        break;
                    }

                    case "String": {
                        this.addStringOption(rowList.getSecond(), configOption);
                        break;
                    }

                    case "Integer": {
                        this.addIntegerOption(rowList.getSecond(), configOption);
                        break;
                    }
                }
            }
        }

        // Only add the general OptionsList when starting the screen.
        // This list will be removed and re-added depending on the group of options chosen.
        this.children.add(this.optionCollection.get(0).getSecond());
    }

    @Override
    public void buttonClicked(AbstractButton button) {
        if (button == this.doneButton) {
            ModConfiguration.UpdateServerConfig();
            this.getMinecraft().setScreen(this.parentScreen);
        } else if (button == this.generalGroupButton) {
            Quadruple<String, OptionsRowList, OptionsRowList, String> option = this.getOptionsRowList(this.currentOption);

            if (option != null) {
                this.children.remove(option.getSecond());
                this.children.add(option.getThird());
                this.generalGroupButton.setMessage(new StringTextComponent(option.getFourth()));
                this.currentOption = option.getFourth();
            }
        } else if (button == this.resetToDefaultsButton) {
            for (ConfigOption<?> configOption : CommonProxy.proxyConfiguration.configOptions) {
                configOption.resetToDefault();
            }
        }
    }

    @Override
    protected Tuple<Integer, Integer> getAdjustedXYValue() {
        return new Tuple<>(0, 0);
    }

    @Override
    protected void preButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.renderDirtBackground(0);

        // Only render the appropriate options row list based on the currently selected option.
        Quadruple<String, OptionsRowList, OptionsRowList, String> rowList = this.getOptionsRowList(this.currentOption);

        if (rowList != null) {
            rowList.getSecond().render(matrixStack, x, y, partialTicks);

            List<IReorderingProcessor> list = GuiPrefab.tooltipAt(rowList.getSecond(), mouseX, mouseY);

            if (list != null) {
                int mousePosition = mouseY > this.height / 2 ? mouseY - 25 : mouseY + 25;
                this.renderTooltip(matrixStack, list, mouseX, mousePosition);
            }
        }

        // Draw the title
        AbstractGui.drawCenteredString(
                matrixStack,
                this.font,
                this.title.getString(),
                this.width / 2,
                TITLE_HEIGHT,
                0xFFFFFF);

        AbstractGui.drawCenteredString(
                matrixStack,
                this.font,
                "Category",
                (this.width / 2) - 50,
                35,
                0xFFFFFF);
    }

    @Override
    protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
    }

    private void addBooleanOption(OptionsRowList rowList, ConfigOption<?> configOption) {
        AbstractOption abstractOption = new BooleanOption(
                configOption.getName(),
                unused -> configOption.getConfigValueAsBoolean().get(),
                (unused, newValue) -> configOption.getConfigValueAsBoolean().set(newValue)
        );

        if (!configOption.getHoverText().isEmpty()) {
            abstractOption.setTooltip(this.getSplitString(configOption.getHoverTextComponent(), 250));
        }

        rowList.addBig(abstractOption);
    }

    private void addIntegerOption(OptionsRowList rowList, ConfigOption<?> configOption) {
        AbstractOption abstractOption = new SliderPercentageOption(
                configOption.getName(),
                // Range
                configOption.getMinRange(),
                configOption.getMaxRange(),
                // This is an integer option, so allow whole steps only
                1.0F,
                // Getter and setter are similar to those in BooleanOption
                unused -> (double) configOption.getConfigValueAsInt().get(),
                (unused, newValue) -> configOption.getConfigValueAsInt().set(newValue.intValue()),
                // BiFunction that returns a string text component
                // in format "<name>: <value>"
                (gs, option) -> new StringTextComponent(
                        // Use I18n.get(String) to get a translation key's value
                        configOption.getName()
                                + ": "
                                + (int) option.get(gs)
                )
        );

        if (!configOption.getHoverText().isEmpty()) {
            abstractOption.setTooltip(this.getSplitString(configOption.getHoverTextComponent(), 250));
        }

        rowList.addBig(abstractOption);
    }

    private void addStringOption(OptionsRowList rowList, ConfigOption<?> configOption) {
        AbstractOption abstractOption = new IteratableOption(
                configOption.getName(),
                (unused, newValue) -> {
                    // 'newValue' is always 1.
                    int nextIndex = configOption.getValidValues().indexOf(configOption.getConfigValueAsString().get()) + newValue;

                    if (nextIndex >= configOption.getValidValues().size()) {
                        nextIndex = 0;
                    }

                    configOption.getConfigValueAsString().set(configOption.getValidValues().get(nextIndex));
                },
                (unused, option) -> new StringTextComponent(
                        configOption.getName()
                                + ": "
                                + configOption.getConfigValueAsString().get())
        );

        if (!configOption.getHoverText().isEmpty()) {
            abstractOption.setTooltip(this.getSplitString(configOption.getHoverTextComponent(), 250));
        }

        rowList.addBig(abstractOption);
    }

    private Quadruple<String, OptionsRowList, OptionsRowList, String> getOptionsRowList(String listName) {
        for (Quadruple<String, OptionsRowList, OptionsRowList, String> option : this.optionCollection) {
            if (option.getFirst().equals(listName)) {
                return option;
            }
        }

        return null;
    }
}
