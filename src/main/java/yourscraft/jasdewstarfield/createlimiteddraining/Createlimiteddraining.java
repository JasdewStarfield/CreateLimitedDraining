package yourscraft.jasdewstarfield.createlimiteddraining;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Createlimiteddraining.MODID)
public class Createlimiteddraining {

    public static final String MODID = "createlimiteddraining";

    public Createlimiteddraining(FMLJavaModLoadingContext context) {
        MinecraftForge.EVENT_BUS.register(this);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
