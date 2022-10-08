/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api;

import javax.annotation.Nullable;

public interface IModModule
{
	Version getVersion();

	String getName();

	@Nullable
	ISpiritwebSubmodule makeSubmodule();
}