/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.compat.curios;

import top.theillusivec4.curios.api.type.capability.ICurio;

public class CosmereCurios
{

    public static ICurio createTestCurioProvider()
    {
        return new TestCurio();
    }
}
