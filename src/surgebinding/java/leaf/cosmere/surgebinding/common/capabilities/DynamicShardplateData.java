
package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.surgebinding.client.render.model.DynamicShardplateModel;
import leaf.cosmere.surgebinding.common.items.ShardplateCurioItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class DynamicShardplateData implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
	private final LazyOptional<DynamicShardplateData> opt = LazyOptional.of(() -> this);

	private CompoundTag nbt;

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


	public DynamicShardplateData()
	{
		this.nbt = new CompoundTag();

		this.headID = "head" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_HELMET_IDS);
		this.faceplateID = "faceplate" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_FACEPLATE_IDS);
		this.bodyID = "body" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_TORSO_IDS);
		this.kamaID = "kama" + MathHelper.randomInt(0, DynamicShardplateModel.TOTAL_KAMA_IDS);

		this.rightArmID = "right_armmain" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_ARM_IDS);
		this.rightPaldronsID = "right_paldron" + MathHelper.randomInt(0, DynamicShardplateModel.TOTAL_PALDRON_IDS);
		this.rightLegID = "rightleg_top" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_LEG_IDS);
		this.rightBootOutsideID = "rightboot_outside" + MathHelper.randomInt(1, DynamicShardplateModel.TOTAL_BOOT_IDS);
		this.rightBootTipID = new String(rightBootOutsideID).replace("outside","tip");

		this.leftArmID = new String(rightArmID).replace("right","left");
		this.leftPaldronsID = new String(rightPaldronsID).replace("right","left");
		this.leftLegID = new String(rightLegID).replace("right","left");
		this.leftBootOutsideID = new String(rightBootOutsideID).replace("right","left");
		this.leftBootTipID = new String(rightBootTipID).replace("right","left");

	}
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction)
	{
		return ShardplateCurioItem.CAPABILITY.orEmpty(capability, opt);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap)
	{
		return ICapabilityProvider.super.getCapability(cap);
	}

	@Override
	public CompoundTag serializeNBT()
	{
		if (this.nbt == null)
		{
			this.nbt = new CompoundTag();
		}

		this.nbt.putString("headID", this.headID);
		this.nbt.putString("faceplateID" , this.faceplateID);
		this.nbt.putString("bodyID", this.bodyID);
		this.nbt.putString("kamaID", this.kamaID);

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


		return this.nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag compoundTag)
	{
		this.nbt = compoundTag;


		this.headID = nbt.getString("headID");
		this.rightArmID = nbt.getString("rightArmID");
		this.bodyID = nbt.getString("bodyID");
		this.rightLegID = nbt.getString("rightLegID");
		this.kamaID = nbt.getString("kamaID");

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
}
