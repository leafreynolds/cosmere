package leaf.cosmere.sandmastery.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.blocks.entities.DiemStoneBE;
import leaf.cosmere.sandmastery.common.blocks.entities.DiemStoneBE.Look;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.diem.DiemRank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.Map;

// draws the sand in the stone's basin: a shared look if one is set, otherwise the viewing player's own Diem state
public class DiemStoneRenderer implements BlockEntityRenderer<DiemStoneBE>
{
	public static final Map<Look, ModelResourceLocation> SAND_MODELS = Map.of(
			Look.WHITE, sandModel("white"),
			Look.BLACK, sandModel("black"),
			Look.GLOWING, sandModel("glowing"));

	private final BlockRenderDispatcher blockRenderer;

	public DiemStoneRenderer(BlockEntityRendererProvider.Context context)
	{
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	private static ModelResourceLocation sandModel(String sand)
	{
		return ModelResourceLocation.standalone(Sandmastery.rl("block/diem_stone_sand_" + sand));
	}

	@Override
	public void render(DiemStoneBE stone, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay)
	{
		Look look = stone.getShared(stone.getLevel().getGameTime());
		if (look == null)
		{
			look = lookForLocalPlayer();
		}
		if (look == Look.EMPTY)
		{
			return;
		}

		blockRenderer.getModelRenderer().renderModel(
				poseStack.last(),
				buffers.getBuffer(RenderType.cutout()),
				stone.getBlockState(),
				Minecraft.getInstance().getModelManager().getModel(SAND_MODELS.get(look)),
				1F, 1F, 1F,
				look == Look.GLOWING ? LightTexture.FULL_BRIGHT : packedLight,
				packedOverlay,
				ModelData.EMPTY,
				RenderType.cutout());
	}

	// charged sand waits for a player who can take a trial, spent sand while they're on cooldown, nothing otherwise
	private static Look lookForLocalPlayer()
	{
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null)
		{
			return Look.EMPTY;
		}
		return SpiritwebCapability.get(player).map(spiritweb ->
		{
			SandmasterySpiritwebSubmodule submodule = SandmasterySpiritwebSubmodule.get(spiritweb);
			DiemRank rank = submodule.getDiemRank();
			DiemRank next = rank == null ? null : rank.next();
			if (next == null || SandmasterySpiritwebSubmodule.getRankRibbons(player) < next.getMinRibbons())
			{
				return Look.EMPTY;
			}
			return player.level().getGameTime() < submodule.getTrialCooldownUntil() ? Look.BLACK : Look.WHITE;
		}).orElse(Look.EMPTY);
	}
}
