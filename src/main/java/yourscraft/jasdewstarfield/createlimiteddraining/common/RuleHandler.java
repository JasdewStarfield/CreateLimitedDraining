package yourscraft.jasdewstarfield.createlimiteddraining.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import yourscraft.jasdewstarfield.createlimiteddraining.Config;

import java.util.Objects;

public class RuleHandler {
    /**
     * 检查是否允许无限抽取
     * @return true: 允许（或没有规则限制）; false: 被规则禁止
     */
    public static boolean canDrain(Level level, Fluid fluid, Biome biome) {
        // 遍历配置文件中的每一条规则
        for (String rule : Config.drainingRules) {
            String[] parts = rule.split(";");
            if (parts.length != 2) continue; // 格式错误跳过

            String fluidPart = parts[0].trim();
            String biomePart = parts[1].trim();

            // 1. 检查流体是否属于这一组
            if (matchAny(level, fluid, fluidPart, true)) {
                // 2. 如果流体属于这一组，那么群系也必须属于这一组
                return matchAny(level, biome, biomePart, false);
            }
        }

        // 如果不匹配，走 Create 原版逻辑
        return true;
    }

    /**
     * 通用的匹配方法，支持逗号分隔的多个 ID 或 Tag
     * @param isFluid true表示检查流体，false表示检查群系
     */
    private static boolean matchAny(Level level, Object target, String patternStr, boolean isFluid) {
        // 用逗号分割多个条目
        String[] patterns = patternStr.split(",");

        for (String pattern : patterns) {
            pattern = pattern.trim();
            if (pattern.isEmpty()) continue;

            boolean matched;
            if (isFluid) {
                matched = checkFluid(level, (Fluid) target, pattern);
            } else {
                matched = checkBiome(level, (Biome) target, pattern);
            }

            if (matched) return true; // 只要匹配到一个，就通过
        }
        return false;
    }

    private static boolean checkFluid(Level level, Fluid fluid, String matcher) {
        if (matcher.startsWith("#")) {
            // 是标签 (Tag)
            ResourceLocation loc = ResourceLocation.tryParse(matcher.substring(1));
            if (loc == null) return false;
            TagKey<Fluid> tag = TagKey.create(Registries.FLUID, loc);
            return ForgeRegistries.FLUIDS.getHolder(fluid).map(h -> h.is(tag)).orElse(false);
        } else {
            // 是具体 ID
            ResourceLocation loc = ResourceLocation.tryParse(matcher);
            if (loc == null) return false;
            ResourceLocation key = ForgeRegistries.FLUIDS.getKey(fluid);
            return key != null && key.equals(loc);
        }
    }

    private static boolean checkBiome(Level level, Biome biome, String matcher) {
        if (biome == null) return false;

        if (matcher.startsWith("#")) {
            // 是标签 (Tag)
            ResourceLocation loc = ResourceLocation.tryParse(matcher.substring(1));
            if (loc == null) return false;
            TagKey<Biome> tag = TagKey.create(Registries.BIOME, loc);
            var registry = level.registryAccess().registryOrThrow(Registries.BIOME);
            return registry.getHolder(registry.getId(biome)).map(h -> h.is(tag)).orElse(false);
        } else {
            // 是具体 ID
            ResourceLocation loc = ResourceLocation.tryParse(matcher);
            if (loc == null) return false;
            var registry = level.registryAccess().registryOrThrow(Registries.BIOME);
            ResourceLocation key = registry.getKey(biome);
            return key != null && key.equals(loc);
        }
    }
}
