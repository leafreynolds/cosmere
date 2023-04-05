/*
 * File updated ~ 5 - 4 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.metalScanning;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Largely based on Scannable, so we can use their concept of block clusters
 * Obtained at 5 - 4 - 2023
 * https://github.com/MightyPirates/Scannable/blob/d543d2ee6181d5d61010b64a1824096141c81fcd/src/main/java/li/cil/scannable/client/scanning/ScanResultProviderBlock.java
 */
public final class ScanResult
{
	public final List<Vec3> foundEntities = new ArrayList<>();
	public final List<BlockPos> foundBlocks = new ArrayList<>();

	public final List<BlockScanResult> clusterResults = new ArrayList<>();
	private final Map<BlockPos, BlockScanResult> clusters = new HashMap<>();

	public List<Vec3> clusterCenters = new ArrayList<>();


	public void Clear()
	{
		foundEntities.clear();
		foundBlocks.clear();
		clusterResults.clear();
		clusters.clear();
	}

	public void addBlock(BlockPos blockPos)
	{
		foundBlocks.add(blockPos);

		//Has a cluster been made nearby already?
		if (!tryAddToCluster(clusters, blockPos))
		{
			//if not, make a new cluster
			final BlockScanResult result = new BlockScanResult(blockPos);
			clusters.put(blockPos, result);
			clusterResults.add(result);
		}
	}

	private boolean tryAddToCluster(final Map<BlockPos, BlockScanResult> clusters, final BlockPos pos)
	{
		BlockScanResult root = null;
		root = tryAddToCluster(clusters, pos, pos.east(), root);
		root = tryAddToCluster(clusters, pos, pos.west(), root);
		root = tryAddToCluster(clusters, pos, pos.north(), root);
		root = tryAddToCluster(clusters, pos, pos.south(), root);
		root = tryAddToCluster(clusters, pos, pos.above(), root);
		root = tryAddToCluster(clusters, pos, pos.below(), root);
		return root != null;
	}

	@Nullable
	private BlockScanResult tryAddToCluster(final Map<BlockPos, BlockScanResult> clusters, final BlockPos pos, final BlockPos clusterPos, @Nullable BlockScanResult root)
	{
		final BlockScanResult cluster = clusters.get(clusterPos);
		if (cluster == null)
		{
			return root;
		}

		if (root == null)
		{
			root = cluster.getRoot();
			root.add(pos);
			clusters.put(pos, root);
		}
		else
		{
			cluster.getRoot().setRoot(root);
		}

		return root;
	}

	public void finalizeClusters()
	{
		clusterCenters = clusterResults.stream().map(BlockScanResult::getPosition).toList();
	}
}
