package com.wuest.prefab.Gui.Controls;

import com.wuest.prefab.Structures.Config.HouseConfiguration;

import net.minecraftforge.fml.client.config.GuiSlider;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiTextSlider extends GuiSlider
{
	protected String configName = "";

	/**
	 * Initializes a new instance of the GuiTextSlider class.
	 * 
	 * @param id         The ID of this slider.
	 * @param xPos       The X-Axis Position of this slider.
	 * @param yPos       The Y-Axis Position of this slider.
	 * @param width      The width of this slider.
	 * @param height     The height of this slider.
	 * @param minVal     The minimum value of this slider.
	 * @param maxVal     The maximum value of this slider.
	 * @param currentVal The default value of this slider.
	 * @param name       The name of this slider.
	 */
	public GuiTextSlider(int id, int xPos, int yPos, int width, int height, double minVal, double maxVal, double currentVal, String name, IPressable handler)
	{
		super(xPos, yPos, width, height, "", "", minVal, maxVal, currentVal, false, true, handler);

		this.configName = name;
		this.updateSlider();
	}

	@Override
	public void updateSlider()
	{
		this.SetSuffix();

		super.updateSlider();
	}

	/**
	 * Fixes an issue where the integer value could be above the maximum value of this slider.
	 */
	@Override
	public int getValueInt()
	{
		int temp = (int) Math.round(sliderValue * (maxValue - minValue) + minValue);

		if (temp > this.maxValue)
		{
			temp = (int) Math.round(this.maxValue);
		}

		return temp;
	}

	public void SetSuffix()
	{
		if (!this.showDecimal)
		{
			int currentValue = this.getValueInt();

			this.suffix = HouseConfiguration.GetIntegerOptionStringValue(this.configName, currentValue);
		}
	}

	public String getName()
	{
		return this.configName;
	}

	public void setName(String name)
	{
		this.configName = name;
	}
}