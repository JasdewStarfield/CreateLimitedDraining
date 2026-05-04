package yourscraft.jasdewstarfield.createlimiteddraining;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class CreateLimitedDrainingConfig {

    public static final ModConfigSpec SPEC;
    public static final Config INSTANCE;

    static {
        Pair<Config, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Config::new);
        SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    // 定义规则列表
    // 格式说明: "流体ID或Tag;群系ID或Tag"
    // 示例: "#minecraft:lava;#minecraft:is_nether" (岩浆只能在下界无限抽)
    public static class Config {
        public final ModConfigSpec.ConfigValue<List<? extends String>> drainingRules;

        Config(ModConfigSpec.Builder builder) {
            drainingRules = builder
                    .comment("Define infinite draining groups.")
                    .comment("Format: 'Fluid1, Fluid2... ; Biome1, Biome2...'")
                    .comment("Logic: If the fluid is in the left list AND the biome is in the right list, it can be drained infinitely.")
                    .comment("Note that this rule is applied BEFORE the basic Create rule.")
                    .comment("If a fluid bypassed this mod's rule, it still needs to go through Create's rule. (By default, more than 10000 B and has tag #create:bottomless/allow)")
                    .comment("Examples:")
                    .comment("  'minecraft:water, #c:milk ; #minecraft:is_ocean, minecraft:river' (Water and Milk are infinite in Oceans and Rivers)")
                    .comment("  '#minecraft:lava ; #minecraft:is_nether' (Lava is infinite in Nether)")
                    .defineListAllowEmpty("drainingRules",
                            List.of("#minecraft:lava ; #minecraft:is_nether"),
                            () -> "",
                            obj -> obj instanceof String);
        }
    }
}
