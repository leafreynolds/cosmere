/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Capability/LazyOptional/INBTSerializable removed. State persists directly on the ItemStack
 * via `DataComponents.CUSTOM_DATA` through `StackNBTHelper`. Bind a view with
 * `new DynamicShardplateData(stack)`; on first construction (no NBT yet) random IDs are
 * lazily seeded if the requested field is absent — matches the pre-port behaviour where the
 * constructor pre-populated random pieces.
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.surgebinding.client.render.model.DynamicShardplateModel;
import net.minecraft.world.item.ItemStack;

public class DynamicShardplateData extends RadiantShardData implements IShardplateDynamicData
{
	// NBT keys (kept identical to the pre-port serializeNBT layout for save-game continuity)
	private static final String NBT_HEAD_ID = "headID";
	private static final String NBT_FACEPLATE_ID = "faceplateID";
	private static final String NBT_BODY_ID = "bodyID";
	private static final String NBT_KAMA_ID = "kamaID";

	private static final String NBT_RIGHT_ARM_ID = "rightArmID";
	private static final String NBT_RIGHT_PALDRON_ID = "rightPaldronID";
	private static final String NBT_RIGHT_LEG_ID = "rightLegID";
	private static final String NBT_RIGHT_BOOT_OUTSIDE_ID = "rightBootOutsideID";
	private static final String NBT_RIGHT_BOOT_TIP_ID = "rightBootTipID";

	private static final String NBT_LEFT_ARM_ID = "leftArmID";
	private static final String NBT_LEFT_PALDRON_ID = "leftPaldronID";
	private static final String NBT_LEFT_LEG_ID = "leftLegID";
	private static final String NBT_LEFT_BOOT_OUTSIDE_ID = "leftBootOutsideID";
	private static final String NBT_LEFT_BOOT_TIP_ID = "leftBootTipID";

	private static final String NBT_IS_COLORED = "isColored";

	public DynamicShardplateData(ItemStack stack)
	{
		super(stack);
		// Seed random model IDs if the stack hasn't been initialised yet.
		if (!StackNBTHelper.verifyExistance(stack, NBT_HEAD_ID))
		{
			seedRandomIds();
		}
	}

	private void seedRandomIds()
	{
		String headID = "head" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_HELMET_IDS);
		String faceplateID = "faceplate" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_FACEPLATE_IDS);
		String bodyID = "body" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_TORSO_IDS);
		String kamaID = "kama" + MathHelper.randomInt(0, DynamicShardplateModel.TOTAL_KAMA_IDS);

		String rightArmID = "right_armmain" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_ARM_IDS);
		String rightPaldronsID = "right_paldron" + MathHelper.randomInt(0, DynamicShardplateModel.TOTAL_PALDRON_IDS);
		String rightLegID = "rightleg_top" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_LEG_IDS);
		String rightBootOutsideID = "rightboot_outside" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_BOOT_IDS);
		String rightBootTipID = rightBootOutsideID.replace("outside", "tip");

		String leftArmID = rightArmID.replace("right", "left");
		String leftPaldronsID = rightPaldronsID.replace("right", "left");
		String leftLegID = rightLegID.replace("right", "left");
		String leftBootOutsideID = rightBootOutsideID.replace("right", "left");
		String leftBootTipID = rightBootTipID.replace("right", "left");

		StackNBTHelper.setString(stack, NBT_HEAD_ID, headID);
		StackNBTHelper.setString(stack, NBT_FACEPLATE_ID, faceplateID);
		StackNBTHelper.setString(stack, NBT_BODY_ID, bodyID);
		StackNBTHelper.setString(stack, NBT_KAMA_ID, kamaID);

		StackNBTHelper.setString(stack, NBT_RIGHT_ARM_ID, rightArmID);
		StackNBTHelper.setString(stack, NBT_RIGHT_PALDRON_ID, rightPaldronsID);
		StackNBTHelper.setString(stack, NBT_RIGHT_LEG_ID, rightLegID);
		StackNBTHelper.setString(stack, NBT_RIGHT_BOOT_OUTSIDE_ID, rightBootOutsideID);
		StackNBTHelper.setString(stack, NBT_RIGHT_BOOT_TIP_ID, rightBootTipID);

		StackNBTHelper.setString(stack, NBT_LEFT_ARM_ID, leftArmID);
		StackNBTHelper.setString(stack, NBT_LEFT_PALDRON_ID, leftPaldronsID);
		StackNBTHelper.setString(stack, NBT_LEFT_LEG_ID, leftLegID);
		StackNBTHelper.setString(stack, NBT_LEFT_BOOT_OUTSIDE_ID, leftBootOutsideID);
		StackNBTHelper.setString(stack, NBT_LEFT_BOOT_TIP_ID, leftBootTipID);

		StackNBTHelper.setBoolean(stack, NBT_IS_COLORED, true);
	}

	@Override
	public String getHeadID()
	{
		return StackNBTHelper.getString(stack, NBT_HEAD_ID, "");
	}

	@Override
	public String getFaceplateID()
	{
		return StackNBTHelper.getString(stack, NBT_FACEPLATE_ID, "");
	}

	@Override
	public String getBodyID()
	{
		return StackNBTHelper.getString(stack, NBT_BODY_ID, "");
	}

	@Override
	public String getKamaID()
	{
		return StackNBTHelper.getString(stack, NBT_KAMA_ID, "");
	}

	@Override
	public String getRightArmID()
	{
		return StackNBTHelper.getString(stack, NBT_RIGHT_ARM_ID, "");
	}

	@Override
	public String getRightPaldronsID()
	{
		return StackNBTHelper.getString(stack, NBT_RIGHT_PALDRON_ID, "");
	}

	@Override
	public String getRightLegID()
	{
		return StackNBTHelper.getString(stack, NBT_RIGHT_LEG_ID, "");
	}

	@Override
	public String getRightBootOutsideID()
	{
		return StackNBTHelper.getString(stack, NBT_RIGHT_BOOT_OUTSIDE_ID, "");
	}

	@Override
	public String getRightBootTipID()
	{
		return StackNBTHelper.getString(stack, NBT_RIGHT_BOOT_TIP_ID, "");
	}

	@Override
	public String getLeftArmID()
	{
		return StackNBTHelper.getString(stack, NBT_LEFT_ARM_ID, "");
	}

	@Override
	public String getLeftPaldronsID()
	{
		return StackNBTHelper.getString(stack, NBT_LEFT_PALDRON_ID, "");
	}

	@Override
	public String getLeftLegID()
	{
		return StackNBTHelper.getString(stack, NBT_LEFT_LEG_ID, "");
	}

	@Override
	public String getLeftBootOutsideID()
	{
		return StackNBTHelper.getString(stack, NBT_LEFT_BOOT_OUTSIDE_ID, "");
	}

	@Override
	public String getLeftBootTipID()
	{
		return StackNBTHelper.getString(stack, NBT_LEFT_BOOT_TIP_ID, "");
	}

	@Override
	public boolean isColored()
	{
		return StackNBTHelper.getBoolean(stack, NBT_IS_COLORED, false);
	}
}
