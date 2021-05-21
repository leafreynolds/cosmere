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
import leaf.cosmere.items.Metalmind;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.CompoundNBTHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
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
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.Optional;
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
                for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
                {
                    CompoundNBTHelper.setBoolean(hemalurgicInfo, manifestation.getRegistryName().getPath(), true);
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

    //todo hemalurgic decay
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
    protected void onEquipStatusChanged(SlotContext slotContext, ItemStack stack, boolean isEquipping)
    {
        //first do normal equip status changed, in case this spike metalmind has nicrosil powers stored
        super.onEquipStatusChanged(slotContext, stack, isEquipping);

        //then do hemalurgy spike logic
        //hurt the user
        //spiritweb attributes are handled in metalmind
        slotContext.getWearer().attackEntityFrom(SPIKED, 4);
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack)
    {
        return true;
    }

    @Override
    public void render(String identifier, int index, MatrixStack matrixStack,
                       IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch, ItemStack stack)
    {
        //todo check if needed
        // ICurio.RenderHelper.translateIfSneaking(matrixStack, livingEntity);
        // ICurio.RenderHelper.rotateIfSneaking(matrixStack, livingEntity);

        if (!(this.model instanceof SpikeModel))
        {
            //todo set spike position?
            this.model = new SpikeModel<>();
        }

        SpikeModel spike = (SpikeModel<?>) this.model;


        //todo change render based on spike placement


        Optional<SlotTypePreset> slotTypePreset = SlotTypePreset.findPreset(identifier);
        if (!slotTypePreset.isPresent())
        {
            return;
        }

        spike.renderMode = identifier;
        spike.renderIndex = index;

        switch (slotTypePreset.get())
        {

            case HEAD:
                //then set up the custom/non biped model stuff
                //this could have been biped I guess, but was a good reference
                ICurio.RenderHelper.followHeadRotations(livingEntity, spike.leftEyeSpike);
                ICurio.RenderHelper.followHeadRotations(livingEntity, spike.rightEyeSpike);
                break;
            case NECKLACE:
                ICurio.RenderHelper.followHeadRotations(livingEntity, spike.neckSpike);
                break;
            case BODY:
            case BACK:
            case BRACELET:
            case HANDS:
            case RING:
            case BELT:
            case CHARM:
            case CURIO:
                //setup biped model stuff
                spike.setLivingAnimations(livingEntity, limbSwing, limbSwingAmount, partialTicks);
                spike.setRotationAngles(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                //and have it follow body rotations
                ICurio.RenderHelper.followBodyRotations(livingEntity, (BipedModel<LivingEntity>) spike);
                break;
        }


        IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(renderTypeBuffer, spike.getRenderType(SPIKE_TEXTURE), false, false);

        Color col = getMetalType().getColor();
        spike.render(matrixStack,
                vertexBuilder,
                light,
                OverlayTexture.NO_OVERLAY,
                col.getRed() / 255f,
                col.getGreen() / 255f,
                col.getBlue() / 255f,
                1.0F);
    }
}
