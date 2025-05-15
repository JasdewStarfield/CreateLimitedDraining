package yourscraft.jasdewstarfield.createlimiteddraining.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import yourscraft.jasdewstarfield.createlimiteddraining.Createlimiteddraining;

public class LimitedDrainingBiomeTags {
    public static final TagKey<Biome> INFINITE_DRAINING_BIOMES = create("infinite_draining_biomes");

    private static TagKey<Biome> create(String id) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Createlimiteddraining.MODID, id));
    }
}
