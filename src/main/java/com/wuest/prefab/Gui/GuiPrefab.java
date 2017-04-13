package com.wuest.prefab.Gui;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.HouseConfiguration;
import com.wuest.prefab.Config.ModConfiguration;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.IConfigElement;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiPrefab extends GuiConfig
{
	public GuiPrefab(GuiScreen parent)
	{
		super(parent,
				new ConfigElement(Prefab.config.getCategory(ModConfiguration.OPTIONS)).getChildElements(),
				Prefab.MODID, null, false, false, GuiConfig.getAbridgedConfigPath(Prefab.config.toString()), null);

		ConfigCategory category = Prefab.config.getCategory(ModConfiguration.OPTIONS);
		String abridgedConfigPath = GuiConfig.getAbridgedConfigPath(Prefab.config.toString());

		this.ReplaceIntegerEntries();
	}

	@Override
	public void initGui()
	{
		if (this.entryList == null || this.needsRefresh)
		{
			this.entryList = new GuiConfigEntries(this, mc);
			this.needsRefresh = false;

			this.ReplaceIntegerEntries();
		}

		super.initGui();
	}

	private void ReplaceIntegerEntries()
	{
		for (int i = 0; i < this.entryList.listEntries.size(); i++)
		{
			IConfigEntry entry = this.entryList.listEntries.get(i);
			IConfigElement element = entry.getConfigElement();

			if (element.getType() == ConfigGuiType.INTEGER)
			{
				TextNumberSliderEntry slider = new TextNumberSliderEntry(this, this.entryList, element);
				slider.updateValueButtonText();
				this.entryList.listEntries.set(i, slider);
				this.initEntries.set(i, slider);
			}
		}
	}

	public class TextNumberSliderEntry extends GuiConfigEntries.ButtonEntry
	{
		protected final double beforeValue;

		public TextNumberSliderEntry(GuiConfig owningScreen,
				GuiConfigEntries owningEntryList, IConfigElement configElement) 
		{
			super(owningScreen, owningEntryList, configElement, new GuiTextSlider(0, owningEntryList.controlX, 0, owningEntryList.controlWidth, 18,
					"", "", Double.valueOf(configElement.getMinValue().toString()), Double.valueOf(configElement.getMaxValue().toString()),
					Double.valueOf(configElement.get().toString()), configElement.getType() == ConfigGuiType.DOUBLE, true));

			((GuiTextSlider)this.btnValue).parentEntry = this;

			if (configElement.getType() == ConfigGuiType.INTEGER)
			{
				this.beforeValue = Integer.valueOf(configElement.get().toString());
			}
			else
			{
				this.beforeValue = Double.valueOf(configElement.get().toString());
			}
		}

		@Override
		public Object getCurrentValue()
		{
			if (configElement.getType() == ConfigGuiType.INTEGER)
			{
				return ((GuiSlider) this.btnValue).getValueInt();
			}
			else
			{
				return ((GuiSlider) this.btnValue).getValue();
			}
		}

		@Override
		public void updateValueButtonText()
		{
			((GuiSlider) this.btnValue).updateSlider();
		}

		@Override
		public void valueButtonPressed(int slotIndex) {}

		@Override
		public boolean isDefault()
		{
			if (configElement.getType() == ConfigGuiType.INTEGER)
				return ((GuiSlider) this.btnValue).getValueInt() == Integer.valueOf(configElement.getDefault().toString());
			else
				return ((GuiSlider) this.btnValue).getValue() == Double.valueOf(configElement.getDefault().toString());
		}

		@Override
		public void setToDefault()
		{
			if (this.enabled())
			{
				((GuiSlider) this.btnValue).setValue(Double.valueOf(configElement.getDefault().toString()));
				((GuiSlider) this.btnValue).updateSlider();
			}
		}

		@Override
		public boolean isChanged()
		{
			if (configElement.getType() == ConfigGuiType.INTEGER)
				return ((GuiSlider) this.btnValue).getValueInt() != (int) Math.round(beforeValue);
			else
				return ((GuiSlider) this.btnValue).getValue() != beforeValue;
		}

		@Override
		public void undoChanges()
		{
			if (this.enabled())
			{
				((GuiSlider) this.btnValue).setValue(beforeValue);
				((GuiSlider) this.btnValue).updateSlider();
			}
		}

		@Override
		public boolean saveConfigElement()
		{
			if (this.enabled() && this.isChanged())
			{
				if (configElement.getType() == ConfigGuiType.INTEGER)
					configElement.set(((GuiSlider) this.btnValue).getValueInt());
				else
					configElement.set(((GuiSlider) this.btnValue).getValue());
				return configElement.requiresMcRestart();
			}
			return false;
		}

		@Override
		public Object[] getCurrentValues()
		{
			return new Object[] { getCurrentValue() };
		}
	}

	public class GuiTextSlider extends GuiSlider
	{
		TextNumberSliderEntry parentEntry = null;

		public GuiTextSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr)
		{
			super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
		}

		@Override
		public void updateSlider()
		{
			if (this.sliderValue < 0.0F)
			{
				this.sliderValue = 0.0F;
			}

			if (this.sliderValue > 1.0F)
			{
				this.sliderValue = 1.0F;
			}

			String val;

			if (showDecimal)
			{
				val = Double.toString(sliderValue * (maxValue - minValue) + minValue);

				if (val.substring(val.indexOf(".") + 1).length() > precision)
				{
					val = val.substring(0, val.indexOf(".") + precision + 1);

					if (val.endsWith("."))
					{
						val = val.substring(0, val.indexOf(".") + precision);
					}
				}
				else
				{
					while (val.substring(val.indexOf(".") + 1).length() < precision)
					{
						val = val + "0";
					}
				}
			}
			else
			{
				val = Integer.toString((int)Math.round(sliderValue * (maxValue - minValue) + minValue));
			}

			if(drawString)
			{
				if (this.parentEntry != null)
				{
					this.SetSuffix();
				}

				displayString = dispString + val + suffix;
			}

			if (parent != null)
			{
				parent.onChangeSliderValue(this);
			}
		}

		public void SetSuffix()
		{
			IConfigElement configElement = this.parentEntry.getConfigElement();

			if (configElement.getType() == ConfigGuiType.INTEGER)
			{
				int currentValue = this.getValueInt();

				this.suffix = HouseConfiguration.GetIntegerOptionStringValue(configElement.getName(), currentValue);
			}
		}
	}
}
