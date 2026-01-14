package yourscraft.jasdewstarfield.createlimiteddraining;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Createlimiteddraining.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 定义规则列表
    // 格式说明: "流体ID或Tag;群系ID或Tag"
    // 示例: "#minecraft:lava;#minecraft:is_nether" (岩浆只能在下界无限抽)
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DRAINING_RULES = BUILDER
            .comment("Define infinite draining groups.")
            .comment("Format: 'Fluid1, Fluid2... ; Biome1, Biome2...'")
            .comment("Logic: If the fluid is in the left list AND the biome is in the right list, it can be drained infinitely.")
            .comment("Note that this rule is applied BEFORE the basic Create rule.")
            .comment("If a fluid bypassed this mod's rule, it still needs to go through Create's rule. (By default, more than 10000 B and has tag #create:bottomless/allow)")
            .comment("Examples:")
            .comment("  'minecraft:water, #forge:milk ; #minecraft:is_ocean, minecraft:river' (Water and Milk are infinite in Oceans and Rivers)")
            .comment("  '#minecraft:lava ; #minecraft:is_nether' (Lava is infinite in Nether)")
            .defineList("drainingRules",
                    List.of("#minecraft:lava ; #minecraft:is_nether"),
                    obj -> obj instanceof String);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static List<? extends String> drainingRules = List.of();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        drainingRules = DRAINING_RULES.get();
    }
}
