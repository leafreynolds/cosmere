package leaf.cosmere.sandmastery.common.items;

import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.client.items.AnimatedItemRenderer;
import leaf.cosmere.sandmastery.common.itemgroups.SandmasteryItemGroups;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public class AnimatedItem extends Item implements IAnimatable
{
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public AnimatedItem()
	{
		super(PropTypes.Items.ONE.get().tab(SandmasteryItemGroups.ITEMS));
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new AnimatedItemRenderer();

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}

	private <E extends IAnimatable>PlayState predicate(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory()
	{
		return factory;
	}
}
