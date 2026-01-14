package yourscraft.jasdewstarfield.createlimiteddraining.mixin;

import com.simibubi.create.content.fluids.transfer.FluidManipulationBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yourscraft.jasdewstarfield.createlimiteddraining.common.HosePulleyAccessor;
import yourscraft.jasdewstarfield.createlimiteddraining.common.RuleHandler;

@Mixin(value = FluidManipulationBehaviour.class, remap = false)
public class FluidManipulationBehaviourMixin {

    @Inject(method = "canDrainInfinitely", at = @At("HEAD"), cancellable = true)
    private void checkRules(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        FluidManipulationBehaviour instance = (FluidManipulationBehaviour) (Object) this;
        Level world = instance.getWorld();
        BlockPos pos = instance.blockEntity.getBlockPos();
        Biome biome = world.getBiome(pos).value();

        // 获取HosePulleyAccessor实例以控制Tooltip
        HosePulleyAccessor pulley = null;
        if (instance.blockEntity instanceof HosePulleyAccessor) {
            pulley = (HosePulleyAccessor) instance.blockEntity;
        }

        // 使用 RuleHandler 进行判断
        // 逻辑：如果在规则列表中找到了该流体，但群系不匹配，返回 false。
        // 如果流体不在规则列表中，或者规则匹配且群系也允许，返回 true。
        boolean allowed = RuleHandler.canDrain(world, fluid, biome);

        if (!allowed) {
            // 被规则禁止了
            if (pulley != null) {
                pulley.createlimiteddraining$setBiomeCheckFailed(true);
            }
            cir.setReturnValue(false); // 强制返回 false，阻止无限抽取
        } else {
            // 允许（或者未定义规则），重置 Tooltip 状态
            if (pulley != null) {
                pulley.createlimiteddraining$setBiomeCheckFailed(false);
            }
            // 走 Create 原版逻辑判定
        }
    }
}