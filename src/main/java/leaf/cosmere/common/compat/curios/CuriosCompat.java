/*
 * File updated ~ 11 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.common.compat.curios;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

public class CuriosCompat
{

	public static final ResourceLocation EMPTY_SPIKE_SLOT = new ResourceLocation("curios:slot/spike_icon");
	private static boolean curiosModDetected;


	public static boolean CuriosIsPresent()
	{
		return curiosModDetected;
	}

	public static void init()
	{
		curiosModDetected = ModList.get().isLoaded("curios");

		if (!curiosModDetected)
		{
			return;
		}

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(CuriosCompat::onEnqueueIMC);
	}


	private static void onEnqueueIMC(InterModEnqueueEvent event)
	{
		if (!curiosModDetected)
		{
			return;
		}

		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().size(2).build());
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BRACELET.getMessageBuilder().size(6).build());
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().size(8).build());

		//custom slots

		//physical
		//2 eyes, 1 linchpin, 2 top ribs, 2 center chest, 2 upper top leg
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("eyes").priority(0).size(2).icon(InventoryMenu.EMPTY_ARMOR_SLOT_HELMET).build());
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("linchpin").priority(1).size(1).icon(EMPTY_SPIKE_SLOT).build());
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("physical").priority(501).size(8).icon(EMPTY_SPIKE_SLOT).build());

		//mental
		//4 upper arm, 4 upper rib?, 4 middle leg
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("mental").priority(502).size(10).icon(EMPTY_SPIKE_SLOT).build());

		//spiritual
		//4 lower leg, 4 lower arm, 4 lower  ribs
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("spiritual").priority(503).size(4).icon(EMPTY_SPIKE_SLOT).build());

		//temporal
		//4 middle ribs, 4 middle arm, 2 lower middle leg, 2 upper lower leg
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("temporal").priority(504).size(16).icon(EMPTY_SPIKE_SLOT).build());

/*

		// Physical 1
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("back").priority(2).size(4).icon(EMPTY_SPIKE_SLOT).build());
		// Mental 4
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("chest").priority(3).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Physical 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("shoulders_top").priority(4).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Physical 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("shoulders_back").priority(5).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Temporal 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("upper_arms_back").priority(6).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Temporal 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("shoulders_side").priority(7).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Spiritual 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("upper_arms").priority(8).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Mental 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("middle_arms").priority(9).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Temporal 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("lower_arms").priority(10).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Spiritual 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("hands").priority(11).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Temporal 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ribs_top").priority(12).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Physical 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ribs_top_middle").priority(13).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Mental 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ribs_bottom_middle").priority(14).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Temporal 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ribs_bottom").priority(15).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Spiritual 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("upper_legs").priority(16).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Physical 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("upper_middle_legs").priority(17).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Mental 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("lower_middle_legs").priority(18).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Temporal 2
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("lower_legs").priority(19).size(2).icon(EMPTY_SPIKE_SLOT).build());
		// Spiritual 2
*/


	}
}
