package yourscraft.jasdewstarfield.createlimiteddraining.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import yourscraft.jasdewstarfield.createlimiteddraining.Createlimiteddraining;

public class LimitedDrainingFluidTags {
    public static final TagKey<Fluid> INFINITE_DRAINING_FLUIDS = create("infinite_draining_fluids");

    private static TagKey<Fluid> create(String id) {
        return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(Createlimiteddraining.MODID, id));
    }
}
