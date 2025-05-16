package yourscraft.jasdewstarfield.createlimiteddraining.mixin;

import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yourscraft.jasdewstarfield.createlimiteddraining.common.HosePulleyAccessor;

import java.util.List;

@Mixin(value = HosePulleyBlockEntity.class, remap = false)
public class HosePulleyBlockEntityMixin implements HosePulleyAccessor {

    @Unique
    private boolean createlimiteddraining$biomeCheckFailed = false; // 添加标志位

    @Unique
    public void createlimiteddraining$setBiomeCheckFailed(boolean failed) {
        this.createlimiteddraining$biomeCheckFailed = failed;
    }

    @Unique
    public boolean createlimiteddraining$isBiomeCheckFailed() {
        return this.createlimiteddraining$biomeCheckFailed; // 提供获取标志位的方法
    }

    @Inject(method = "addToGoggleTooltip", at = @At("RETURN"))
    private void injectAddToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, CallbackInfoReturnable<Boolean> cir) {
        if (createlimiteddraining$isBiomeCheckFailed()) { // 如果生物群系检测失败，添加额外的提示
            TooltipHelper.addHint(tooltip, "hint.hose_pulley.biome_check_failed");
        }
    }

    @Inject(method = "write", at = @At("HEAD"))
    private void injectWrite(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
        if (clientPacket) {
            compound.putBoolean("BiomeCheckFailed", createlimiteddraining$biomeCheckFailed);
        }
    }

    @Inject(method = "read", at = @At("HEAD"))
    private void injectRead(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
        if (clientPacket) {
            createlimiteddraining$biomeCheckFailed = compound.getBoolean("BiomeCheckFailed");
        }
    }
}