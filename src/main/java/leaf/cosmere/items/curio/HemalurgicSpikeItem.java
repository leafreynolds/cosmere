/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.client.render.model.SpikeModel;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.helpers.CompoundNBTHelper;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.items.Metalmind;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HemalurgicSpikeItem extends Metalmind implements IHemalurgicInfo
{
    private final float attackDamage;
    /**
     * Modifiers applied when the item is in the mainhand of a user. copied from sword item
     */
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;


    public static final DamageSource SPIKED = (new DamageSource("spiked")).setDamageBypassesArmor().setDamageIsAbsolute();

    //todo move
    private static final ResourceLocation SPIKE_TEXTURE = new ResourceLocation("cosmere", "textures/block/metal_block.png");
    private Object model;

    public HemalurgicSpikeItem(Metals.MetalType metalType)
    {
        super(metalType);

        this.attackDamage = 2f + 1f;//tier.getAttackDamage();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double) -2.4f, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public boolean canUnequip(String identifier, LivingEntity livingEntity, ItemStack stack)
    {
        boolean hasBindingCurse = EnchantmentHelper.hasBindingCurse(stack);
        boolean isPlayer = livingEntity instanceof PlayerEntity;

        return (!hasBindingCurse || (isPlayer && ((PlayerEntity) livingEntity).isCreative()));
    }

    @Override
    public float getMaxChargeModifier()
    {
        return (2f / 9f);
    }

    @Override
    public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> stacks)
    {
        if (isInGroup(tab))
        {
            ItemStack stack = new ItemStack(this);
            stack.addEnchantment(Enchantments.BINDING_CURSE, 1);
            stacks.add(stack);

            if (getMetalType().hasFeruchemicalEffect())
            {
                ItemStack fullPower = new ItemStack(this);
                fullPower.addEnchantment(Enchantments.BINDING_CURSE, 1);
                setCharge(fullPower, getMaxCharge(fullPower));
                stacks.add(fullPower);
            }

            if (this.getMetalType() == Metals.MetalType.LERASIUM)
            {
                ItemStack bound = new ItemStack(this);
                bound.addEnchantment(Enchantments.BINDING_CURSE, 1);
                setHemalurgicIdentity(bound, UUID.randomUUID());
                CompoundNBT hemalurgicInfo = getHemalurgicInfo(bound);
                for (RegistryObject<AManifestation> manifestation : ManifestationRegistry.MANIFESTATIONS.getEntries())
                {
                    CompoundNBTHelper.setBoolean(hemalurgicInfo, manifestation.get().getRegistryName().getPath(), true);
                }
                CompoundNBTHelper.setBoolean(hemalurgicInfo, "hasHemalurgicPower", true);

                stacks.add(bound);
            }
        }
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn)
    {
        stack.addEnchantment(Enchantments.BINDING_CURSE, 1);
    }

    //https://wob.coppermind.net/events/332/#e9534
    private void addDecay(ItemStack stack)
    {

    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        //todo //add decay

        //add decay if not equipped
        {
            // unless its in a jar?
        }
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity)
    {
        //todo //add decay


        return false;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);


        // no extra info if there isn't any
        if (getHemalurgicIdentity(stack) == null)
        {
            return;
        }


        //stolen identities listed?

        //extra investiture powers added
        addInvestitureInformation(stack, tooltip);

        //etc?

        //don't need to do the attributes, since thats covered by curio
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event)
    {
        if (event.getSource().getTrueSource() instanceof PlayerEntity)
        {
            PlayerEntity playerEntity = (PlayerEntity) event.getSource().getTrueSource();
            SpiritwebCapability.get(playerEntity).ifPresent(iSpiritweb ->
            {
                ItemStack itemstack = playerEntity.getHeldItemMainhand();
                if (itemstack.getItem() instanceof HemalurgicSpikeItem)
                {
                    //entity was killed by a spike
                    HemalurgicSpikeItem spikeItem = (HemalurgicSpikeItem) itemstack.getItem();
                    //pass in killed entity for the item to figure out what to do
                    spikeItem.killedEntity(itemstack, event.getEntityLiving());
                }

            });
        }
    }

    public void killedEntity(ItemStack stack, LivingEntity entityKilled)
    {
        //https://wob.coppermind.net/events/332/#e9569

        // do nothing if an identity exists and doesn't match
        if (!matchHemalurgicIdentity(stack, entityKilled.getUniqueID()))
        {
            return;
        }

        // ensure we set the stolen identity
        stealFromSpiritweb(stack, getMetalType(), entityKilled);
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack)
    {
        return super.hasEffect(stack) || hemalurgicIdentityExists(stack);
    }


    /**
     * generate new map of attributes for when used as a curio item.
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
    {
        UUID hemalurgicIdentity = getHemalurgicIdentity(stack);
        return hemalurgicIdentity == null ? super.getAttributeModifiers(slotContext, uuid, stack)
                                          : getHemalurgicAttributes(stack, getMetalType());
    }

    /**
     * Gets a map of item attribute modifiers, used by damage when used as melee weapon.
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot)
    {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifiers
                                                           : super.getAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean canEquip(String identifier, LivingEntity livingEntity, ItemStack stack)
    {
        //has to be a conscious decision to stab yourself
        return true;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack)
    {
        //has to be a conscious decision to stab yourself
        return true;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
    {
        onEquipStatusChanged(slotContext, stack, true);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
    {
        onEquipStatusChanged(slotContext, stack, false);
    }

    private void onEquipStatusChanged(SlotContext slotContext, ItemStack stack, boolean isEquipping)
    {
        //hurt the user
        slotContext.getWearer().attackEntityFrom(SPIKED, 4);

        //then check if we need to change anything about wearers spiritweb
        if (!hemalurgicIdentityExists(stack))
        {
            return;
        }

        SpiritwebCapability.get(slotContext.getWearer()).ifPresent(cap ->
        {
            IForgeRegistry<AManifestation> manifestations = ManifestationRegistry.MANIFESTATION_REGISTRY.get();
            for (AManifestation data : manifestations)
            {
                // if this spike has that power
                if (hasHemalurgicPower(stack, data))
                {
                    //then grant it
                    if (isEquipping)
                    {
                        cap.giveTemporaryManifestation(data.getManifestationType(), data.getPowerID());
                    }
                    else
                    {
                        cap.removeTemporaryManifestation(data.getManifestationType(), data.getPowerID());
                    }
                }
            }
        });
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack)
    {
        return true;
    }

    @Override
    public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, ItemStack stack)
    {
        if (!(this.model instanceof SpikeModel))
        {
            this.model = new SpikeModel();
        }

        //todo change render based on spike placement

        SpikeModel<?> spike = (SpikeModel) this.model;
        ICurio.RenderHelper.followHeadRotations(livingEntity, spike.spike);
        IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(renderTypeBuffer, spike.getRenderType(SPIKE_TEXTURE), false, false);

        Color col = getMetalType().getColor();
        spike.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 1.0F);
    }
}
