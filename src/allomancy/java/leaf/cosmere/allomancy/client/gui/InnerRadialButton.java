package leaf.cosmere.allomancy.client.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class InnerRadialButton extends Button
{
	protected InnerRadialButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration)
	{
		super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
	}
}
