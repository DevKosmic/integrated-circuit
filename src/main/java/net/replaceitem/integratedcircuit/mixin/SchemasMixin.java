package net.replaceitem.integratedcircuit.mixin;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.AddNewChoices;
import net.minecraft.util.datafix.fixes.BlockEntityRenameFix;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import net.replaceitem.integratedcircuit.datafix.Schema3120;
import net.replaceitem.integratedcircuit.datafix.Schema3800_1;
import net.replaceitem.integratedcircuit.datafix.UnflattenCircuitNameFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.UnaryOperator;

@Mixin(DataFixers.class)
public abstract class SchemasMixin {
    @SuppressWarnings("DataFlowIssue")
    @Shadow
    private static UnaryOperator<String> createRenamer(String old, String current) {
        return null;
    }

    @Inject(method = "addFixers", at = @At(value = "CONSTANT", args = "intValue=3201"))
    private static void beforeSchema3201(DataFixerBuilder builder, CallbackInfo ci) {
        Schema schema3120 = builder.addSchema(3120, Schema3120::new);
        builder.addFixer(new AddNewChoices(schema3120, "Added Integrated Circuit", References.BLOCK_ENTITY));
    }

    @Inject(method = "addFixers", at = @At(value = "CONSTANT", args = "intValue=3803"))
    private static void beforeSchema3803(DataFixerBuilder builder, CallbackInfo ci) {
        Schema schema3800_1 = builder.addSchema(3800, 1, Schema3800_1::new);
        builder.addFixer(BlockEntityRenameFix.create(schema3800_1, "Rename integrated_circuit:integrated_circuit_block_entity to integrated_circuit:integrated_circuit", createRenamer("integrated_circuit:integrated_circuit_block_entity", "integrated_circuit:integrated_circuit")));
    }

    // 1.21.5 -> 1.21.6
    @Inject(method = "addFixers", at = @At(value = "CONSTANT", args = "intValue=4424"))
    private static void beforeSchema4424(DataFixerBuilder builder, CallbackInfo ci) {
        Schema schema4421_1 = builder.addSchema(4421, 1, NamespacedSchema::new);
        builder.addFixer(new UnflattenCircuitNameFix(schema4421_1, "integrated_circuit:integrated_circuit"));
    }
}
