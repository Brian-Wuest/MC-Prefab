package com.wuest.prefab.Gui.Controls;

import com.wuest.prefab.Config.HouseConfiguration;

import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiTextSlider extends GuiSlider
{
	protected String configName = "";

	/**
	 * Initializes a new instance of the GuiTextSlider class.
	 * @param id
	 * @param xPos
	 * @param yPos
	 * @param width
	 * @param height
	 * @param prefix
	 * @param suf
	 * @param minVal
	 * @param maxVal
	 * @param currentVal
	 * @param showDec
	 * @param drawStr
	 */
	public GuiTextSlider(int id, int xPos, int yPos, int width, int height, double minVal, double maxVal,
			double currentVal, String name) 
	{
		super(id, xPos, yPos, width, height, "", "", minVal, maxVal, currentVal,
				false, true);

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
		int temp =(int)Math.round(sliderValue * (maxValue - minValue) + minValue);

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