package yourscraft.jasdewstarfield.createlimiteddraining.mixin;

import com.simibubi.create.content.fluids.transfer.FluidManipulationBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yourscraft.jasdewstarfield.createlimiteddraining.common.HosePulleyAccessor;
import yourscraft.jasdewstarfield.createlimiteddraining.common.LimitedDrainingBiomeTags;
import yourscraft.jasdewstarfield.createlimiteddraining.common.LimitedDrainingFluidTags;

@Mixin(value = FluidManipulationBehaviour.class, remap = false)
public class FluidManipulationBehaviourMixin {

    @Inject(method = "canDrainInfinitely", at = @At("HEAD"), cancellable = true)
    private void checkBiomeAndDimension(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        FluidManipulationBehaviour instance = (FluidManipulationBehaviour) (Object) this;
        Level world = instance.getWorld();
        BlockPos pos = instance.blockEntity.getBlockPos();
        Biome biome = world.getBiome(pos).value();
        DimensionType dimension = world.dimensionType();

        // 获取HosePulleyAccessor实例
        HosePulleyAccessor pulley = null;
        if (instance.blockEntity instanceof HosePulleyAccessor) {
            pulley = (HosePulleyAccessor) instance.blockEntity;
        }

        // 检测流体，如果是允许的流体，则继续检测Biome和Dimension，否则走Create的默认逻辑
        if (createlimiteddraining$isAllowedFluid(world, fluid)) {
            // 检测Biome
            if (!createlimiteddraining$isAllowedBiome(world, biome)) {
                if (pulley != null) {
                    pulley.createlimiteddraining$setBiomeCheckFailed(true); // 通过接口调用方法
                }
                cir.setReturnValue(false);
                return;
            }

            // 检测Dimension（暂未使用）
            if (!createlimiteddraining$isAllowedDimension(world, dimension)) {
                cir.setReturnValue(false);
            }

            // 所有检查都通过，重置标志位
            if (pulley != null) {
                pulley.createlimiteddraining$setBiomeCheckFailed(false);
            }
        }
        else {
            if (pulley != null) {
                pulley.createlimiteddraining$setBiomeCheckFailed(false); // 通过接口调用方法
            }
        }
    }

    @Unique
    private boolean createlimiteddraining$isAllowedBiome(Level world, Biome biome) {
        if (biome != null) {
            var biomeRegistry = world.registryAccess().registryOrThrow(Registries.BIOME);
            ResourceKey<Biome> biomeKey = biomeRegistry.getResourceKey(biome).orElse(null);
            if (biomeKey != null) {
                return biomeRegistry.getHolderOrThrow(biomeKey).is(LimitedDrainingBiomeTags.INFINITE_DRAINING_BIOMES);
            }
        }
        return false;
    }

    @Unique
    private boolean createlimiteddraining$isAllowedDimension(Level world, DimensionType dimension) {
        return true; // 示例：允许所有Dimension
    }

    @Unique
    private boolean createlimiteddraining$isAllowedFluid(Level world, Fluid fluid) {
        var fluidRegistry = world.registryAccess().registryOrThrow(Registries.FLUID);
        ResourceKey<Fluid> fluidResourceKey = fluidRegistry.getResourceKey(fluid).orElse(null);
        if (fluidResourceKey != null) {
            return fluidRegistry.getHolderOrThrow(fluidResourceKey).is(LimitedDrainingFluidTags.INFINITE_DRAINING_FLUIDS);
        }
        return false;
    }
}