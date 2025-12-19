/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.feruchemy.client.gui.NicrosilMenu;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FeruchemyNicrosil extends FeruchemyManifestation
{
	public FeruchemyNicrosil(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public boolean hasMenu()
	{
		return true;
	}

	@Override
    @OnlyIn(Dist.CLIENT)
	public void openMenu(Minecraft minecraft)
	{
		NicrosilMenu.instance.closeScreen();
		minecraft.setScreen(NicrosilMenu.instance);
	}

	@Override
	public int modeMin(ISpiritweb data)
	{
		return 0;
	}

	@Override
	public int modeMax(ISpiritweb data)
	{
		return 0;
	}
}
