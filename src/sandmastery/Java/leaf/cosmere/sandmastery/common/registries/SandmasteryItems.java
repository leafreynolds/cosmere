package leaf.cosmere.sandmastery.common.registries;


import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.items.QidoItem;
import leaf.cosmere.sandmastery.common.items.SandJarItem;

public class SandmasteryItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Sandmastery.MODID);
    public static final ItemRegistryObject<QidoItem> QIDO_ITEM = ITEMS.register("qido", QidoItem::new);
    public static final ItemRegistryObject<SandJarItem> SAND_JAR_ITEM = ITEMS.register("sand_jar", SandJarItem::new);
}
