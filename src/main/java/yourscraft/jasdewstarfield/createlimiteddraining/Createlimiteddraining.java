package yourscraft.jasdewstarfield.createlimiteddraining;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(Createlimiteddraining.MODID)
public class Createlimiteddraining {

    public static final String MODID = "createlimiteddraining";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Createlimiteddraining(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, CreateLimitedDrainingConfig.SPEC);
        LOGGER.info("[CreateLimitedDraining] Mod Registered");
    }
}
