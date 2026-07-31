package com.prefab.gui.controls;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class GuiSlider extends AbstractSliderButton {
    protected double minValue;
    protected double maxValue;
    protected double sliderValue;

    public GuiSlider(int xPos, int yPos, int width, int height, Component component, double minValue, double maxValue, double value) {
        super(xPos, yPos, width, height, component, value / (maxValue != 0 ? maxValue : 1));
        this.sliderValue = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.updateMessage();
    }

    public double getDoubleValue() {
        return this.sliderValue;
    }

    public int getIntValue() {
        return (int)this.sliderValue;
    }


    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(Integer.toString((int)this.sliderValue)));
    }

    @Override
    protected void applyValue() {
        this.sliderValue = Mth.floor(Mth.lerp(Mth.clamp(this.value, 0.0, 1.0), this.minValue, this.maxValue));
    }

}
