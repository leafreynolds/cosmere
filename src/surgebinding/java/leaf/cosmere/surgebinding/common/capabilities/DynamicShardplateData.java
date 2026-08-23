/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.surgebinding.client.render.model.DynamicShardplateModel;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class DynamicShardplateData extends RadiantShardData implements INBTSerializable<CompoundTag>, IShardplateDynamicData
{
	private String headID;
	private String faceplateID;
	private String bodyID;
	private String kamaID;

	private String rightArmID;
	private String rightPaldronsID;
	private String rightLegID;
	private String rightBootOutsideID;
	private String rightBootTipID;

	private String leftArmID;
	private String leftPaldronsID;
	private String leftLegID;
	private String leftBootOutsideID;
	private String leftBootTipID;

	private boolean colored;


	public DynamicShardplateData(ItemStack stack)
	{
		super(stack);

		//set default, so both server/client read the same unseeded plate till it's randomised and set
		setAppearance(1, 1, 1, 0, 1, 0, 1, 1);

		this.colored = true;
	}

	@Override
	protected void randomiseAppearance()
	{
		setAppearance(
				MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_HELMET_IDS),
				MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_FACEPLATE_IDS),
				MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_TORSO_IDS),
				MathHelper.randomInt(0, DynamicShardplateModel.TOTAL_KAMA_IDS),
				MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_ARM_IDS),
				MathHelper.randomInt(0, DynamicShardplateModel.TOTAL_PALDRON_IDS),
				MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_LEG_IDS),
				MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_BOOT_IDS));
	}

	//the left side always mirrors the right, so only the right are ever chosen
	private void setAppearance(int head, int faceplate, int body, int kama, int arm, int paldron, int leg, int boot)
	{
		this.headID = "head" + head;
		this.faceplateID = "faceplate" + faceplate;
		this.bodyID = "body" + body;
		this.kamaID = "kama" + kama;

		this.rightArmID = "right_armmain" + arm;
		this.rightPaldronsID = "right_paldron" + paldron;
		this.rightLegID = "rightleg_top" + leg;
		this.rightBootOutsideID = "rightboot_outside" + boot;
		this.rightBootTipID = rightBootOutsideID.replace("outside", "tip");

		this.leftArmID = rightArmID.replace("right", "left");
		this.leftPaldronsID = rightPaldronsID.replace("right", "left");
		this.leftLegID = rightLegID.replace("right", "left");
		this.leftBootOutsideID = rightBootOutsideID.replace("right", "left");
		this.leftBootTipID = rightBootTipID.replace("right", "left");
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{

		super.serializeNBT(provider);

		super.nbt.putString("headID", this.headID);
		super.nbt.putString("faceplateID", this.faceplateID);
		super.nbt.putString("bodyID", this.bodyID);
		super.nbt.putString("kamaID", this.kamaID);

		this.nbt.putString("rightArmID", this.rightArmID);
		this.nbt.putString("rightPaldronID", this.rightPaldronsID);
		this.nbt.putString("rightLegID", this.rightLegID);
		this.nbt.putString("rightBootOutsideID", this.rightBootOutsideID);
		this.nbt.putString("rightBootTipID", this.rightBootTipID);

		this.nbt.putString("leftArmID", this.leftArmID);
		this.nbt.putString("leftPaldronID", this.leftPaldronsID);
		this.nbt.putString("leftLegID", this.leftLegID);
		this.nbt.putString("leftBootOutsideID", this.leftBootOutsideID);
		this.nbt.putString("leftBootTipID", this.leftBootTipID);

		super.nbt.putBoolean("isColored", colored);

		return this.nbt;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag)
	{
		super.deserializeNBT(provider, compoundTag);

		this.headID = nbt.getString("headID");
		this.faceplateID = nbt.getString("faceplateID");
		this.bodyID = nbt.getString("bodyID");
		this.kamaID = nbt.getString("kamaID");

		this.rightArmID = nbt.getString("rightArmID");
		this.rightPaldronsID = nbt.getString("rightPaldronID");
		this.rightLegID = nbt.getString("rightLegID");
		this.rightBootOutsideID = nbt.getString("rightBootOutsideID");
		this.rightBootTipID = nbt.getString("rightBootTipID");

		this.leftArmID = nbt.getString("leftArmID");
		this.leftPaldronsID = nbt.getString("leftPaldronID");
		this.leftLegID = nbt.getString("leftLegID");
		this.leftBootOutsideID = nbt.getString("leftBootOutsideID");
		this.leftBootTipID = nbt.getString("leftBootTipID");

		this.colored = nbt.getBoolean("isColored");

	}

	public String getHeadID()
	{
		return headID;
	}

	public String getFaceplateID()
	{
		return faceplateID;
	}

	public String getBodyID()
	{
		return bodyID;
	}

	public String getKamaID()
	{
		return kamaID;
	}

	public String getRightArmID()
	{
		return rightArmID;
	}

	public String getRightPaldronsID()
	{
		return rightPaldronsID;
	}

	public String getRightLegID()
	{
		return rightLegID;
	}

	public String getRightBootOutsideID()
	{
		return rightBootOutsideID;
	}

	public String getRightBootTipID()
	{
		return rightBootTipID;
	}

	public String getLeftArmID()
	{
		return leftArmID;
	}

	public String getLeftPaldronsID()
	{
		return leftPaldronsID;
	}

	public String getLeftLegID()
	{
		return leftLegID;
	}

	public String getLeftBootOutsideID()
	{
		return leftBootOutsideID;
	}

	public String getLeftBootTipID()
	{
		return leftBootTipID;
	}

	public boolean isColored()
	{
		return colored;
	}
}
