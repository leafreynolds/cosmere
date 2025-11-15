package leaf.cosmere.common.items;

import leaf.cosmere.api.Metals.MetalType;

import java.awt.*;
import java.util.ArrayList;

public class AlloyNuggetItem extends MetalNuggetItem
{
	MetalType alloyedMetalType;

	public AlloyNuggetItem(MetalType metalType, MetalType alloyedMetalType)
	{
		super(metalType);
		this.alloyedMetalType = alloyedMetalType;
	}

	public MetalType getAlloyedMetalType()
	{
		return alloyedMetalType;
	}
}
