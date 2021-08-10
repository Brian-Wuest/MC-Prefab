package com.wuest.prefab.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Quadruple;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.Utils;
import com.wuest.prefab.config.ConfigCategory;
import com.wuest.prefab.config.ConfigOption;
import com.wuest.prefab.config.ModConfiguration;
import com.wuest.prefab.gui.controls.ExtendedButton;
import com.wuest.prefab.proxy.CommonProxy;
import net.minecraft.client.CycleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.TooltipAccessor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.FormattedCharSequence;

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

    private ConfigCategory currentOption = ConfigCategory.General;

    private OptionsList currentRowList;
    private ArrayList<Quadruple<ConfigCategory, OptionsList, OptionsList, ConfigCategory>> optionCollection;
    private OptionsList optionsRowList;
    private OptionsList chestOptionsRowList;
    private OptionsList recipeOptionsRowList;
    private OptionsList starterHouseOptionsRowList;

    public GuiPrefab(Minecraft minecraft, Screen parent) {
        super("Prefab Configuration");
        this.parentScreen = parent;
        super.minecraft = minecraft;
    }

    @Nullable
    public static List<FormattedCharSequence> tooltipAt(OptionsList optionsRowList, int mouseX, int mouseY) {
        if (optionsRowList.isMouseOver(mouseX, mouseY)) {
            Optional<AbstractWidget> optional = optionsRowList.getMouseOver(mouseX, mouseY);

            if (optional.isPresent() && optional.get() instanceof TooltipAccessor) {
                return ((TooltipAccessor) optional.get()).getTooltip();
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

        this.resetToDefaultsButton = this.createAndAddButton(60, this.height - DONE_BUTTON_TOP_OFFSET, 100, 20, "Reset", false);
        this.doneButton = this.createAndAddButton(this.width - 160, this.height - DONE_BUTTON_TOP_OFFSET, 100, 20, "Done", false);

        this.generalGroupButton = this.createAndAddButton(this.width / 2, 30, 120, 20, "General", false);

        for (ConfigCategory category : ConfigCategory.values()) {
            OptionsList nextOptions = new OptionsList(
                    this.getMinecraft(),
                    this.width,
                    this.height,
                    OPTIONS_LIST_TOP_HEIGHT,
                    this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                    OPTIONS_LIST_ITEM_HEIGHT
            );

            OptionsList currentOptions = null;
            int currentLocation = category.ordinal();

            if (currentLocation == 0) {
                currentOptions = new OptionsList(
                        this.getMinecraft(),
                        this.width,
                        this.height,
                        OPTIONS_LIST_TOP_HEIGHT,
                        this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                        OPTIONS_LIST_ITEM_HEIGHT
                );
            } else {
                int currentOptionsIndex = currentLocation - 1;

                if (currentLocation == ConfigCategory.values().length - 1) {
                    // This is the last one; make sure to use the first record as the "next" option row list.
                    nextOptions = this.optionCollection.get(0).getSecond();
                }

                currentOptions = this.optionCollection.get(currentOptionsIndex).getThird();
            }

            ConfigCategory nextCategory = ConfigCategory.getNextCategory(category);

            this.optionCollection.add(new Quadruple<>(category, currentOptions, nextOptions, nextCategory));
        }

        for (ConfigOption<?> configOption : CommonProxy.proxyConfiguration.configOptions) {
            Quadruple<ConfigCategory, OptionsList, OptionsList, ConfigCategory> rowList = this.getOptionsRowList(configOption.getCategory());

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
        this.addWidget(this.optionCollection.get(0).getSecond());
    }

    @Override
    public void buttonClicked(AbstractButton button) {
        if (button == this.doneButton) {
            ModConfiguration.UpdateServerConfig();
            this.getMinecraft().setScreen(this.parentScreen);
        } else if (button == this.generalGroupButton) {
            Quadruple<ConfigCategory, OptionsList, OptionsList, ConfigCategory> option = this.getOptionsRowList(this.currentOption);

            if (option != null) {
                this.removeWidget(option.getSecond());
                this.addWidget(option.getThird());
                GuiUtils.setButtonText(generalGroupButton, option.getFourth().getName());
                this.currentOption = option.getFourth();
            }
        } else if (button == this.resetToDefaultsButton) {
            for (ConfigOption<?> configOption : CommonProxy.proxyConfiguration.configOptions) {
                configOption.resetToDefault();
            }

            this.clearWidgets();
            this.currentOption = ConfigCategory.General;
            this.Initialize();
        }
    }

    @Override
    protected Tuple<Integer, Integer> getAdjustedXYValue() {
        return new Tuple<>(0, 0);
    }

    @Override
    protected void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.renderDirtBackground(0);

        // Only render the appropriate options row list based on the currently selected option.
        Quadruple<ConfigCategory, OptionsList, OptionsList, ConfigCategory> rowList = this.getOptionsRowList(this.currentOption);

        if (rowList != null) {
            rowList.getSecond().render(matrixStack, x, y, partialTicks);

            List<FormattedCharSequence> list = GuiPrefab.tooltipAt(rowList.getSecond(), mouseX, mouseY);

            if (list != null) {
                int mousePosition = mouseY > this.height / 2 ? mouseY - 25 : mouseY + 25;
                this.renderTooltip(matrixStack, list, mouseX, mousePosition);
            }
        }

        // Draw the title
        GuiComponent.drawCenteredString(
                matrixStack,
                this.font,
                this.title.getString(),
                this.width / 2,
                TITLE_HEIGHT,
                0xFFFFFF);

        GuiComponent.drawCenteredString(
                matrixStack,
                this.font,
                "Category",
                (this.width / 2) - 50,
                35,
                0xFFFFFF);
    }

    @Override
    protected void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
    }

    private void addBooleanOption(OptionsList rowList, ConfigOption<?> configOption) {
        CycleOption<Boolean> abstractOption = CycleOption.createOnOff(
                configOption.getName(),
                unused -> configOption.getConfigValueAsBoolean().get(),
                (unused, originalOption, newValue) -> configOption.getConfigValueAsBoolean().set(newValue)
        );

        if (!configOption.getHoverText().isEmpty()) {
            abstractOption.setTooltip((minecraft) -> (supplierValue) -> this.getSplitString(configOption.getHoverTextComponent(), 250));
        }

        rowList.addBig(abstractOption);
    }

    private void addIntegerOption(OptionsList rowList, ConfigOption<?> configOption) {
        Option abstractOption = new ProgressOption(
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
                (gs, option) -> Utils.createTextComponent(
                        // Use I18n.get(String) to get a translation key's value
                        configOption.getName()
                                + ": "
                                + (int) option.get(gs)
                ),
                minecraft1 -> {
                    if (configOption.getHoverText().isEmpty()) {
                        return null;
                    }

                    return this.getSplitString(configOption.getHoverTextComponent(), 250);
                }
        );

        rowList.addBig(abstractOption);
    }

    private void addStringOption(OptionsList rowList, ConfigOption<?> configOption) {
        CycleOption<?> abstractOption = CycleOption.create(
                configOption.getName(),
                configOption.getValidValues().toArray(),
                (selectedOption) -> Utils.createTextComponent(
                        configOption.getName()
                                + ": "
                                + configOption.getConfigValueAsString().get()),
                (gameOption) -> Utils.createTextComponent(
                        configOption.getName()
                                + ": "
                                + configOption.getConfigValueAsString().get()),
                (unused, otherUnused, newValue) -> {
                    // 'newValue' is always 1.
                    int nextIndex = configOption.getValidValues().indexOf(configOption.getConfigValueAsString().get()) + 1;

                    if (nextIndex >= configOption.getValidValues().size()) {
                        nextIndex = 0;
                    }

                    configOption.getConfigValueAsString().set(configOption.getValidValues().get(nextIndex));
                }
        );

        if (!configOption.getHoverText().isEmpty()) {
            abstractOption.setTooltip((minecraft) -> (supplierValue) -> this.getSplitString(configOption.getHoverTextComponent(), 250));
        }

        rowList.addBig(abstractOption);
    }

    private Quadruple<ConfigCategory, OptionsList, OptionsList, ConfigCategory> getOptionsRowList(ConfigCategory listName) {
        for (Quadruple<ConfigCategory, OptionsList, OptionsList, ConfigCategory> option : this.optionCollection) {
            if (option.getFirst() == listName) {
                return option;
            }
        }

        return null;
    }
}
