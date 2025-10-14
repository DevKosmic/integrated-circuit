package net.replaceitem.integratedcircuit.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.replaceitem.integratedcircuit.IntegratedCircuitBlock;
import net.replaceitem.integratedcircuit.IntegratedCircuitBlockEntity;
import net.replaceitem.integratedcircuit.client.config.DefaultConfig;
import org.jetbrains.annotations.Nullable;

public class IntegratedCircuitBlockEntityRenderer implements BlockEntityRenderer<IntegratedCircuitBlockEntity, IntegratedCircuitBlockEntityRenderState> {
    private final TextRenderer textRenderer;
    private static final float MAX_WIDTH = 7f/16; // black circuit box is 8 pixels, plus margin
    private static final float MAX_HEIGHT = 5f/16;
    
    public IntegratedCircuitBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.textRenderer = ctx.textRenderer();
    }

    @Override
    public IntegratedCircuitBlockEntityRenderState createRenderState() {
        return new IntegratedCircuitBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(IntegratedCircuitBlockEntity blockEntity, IntegratedCircuitBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.customName = blockEntity.getCustomName();
    }

    @Override
    public void render(IntegratedCircuitBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if(!DefaultConfig.config.getRenderCircuitName()) return;
        Direction facing = state.blockState.get(IntegratedCircuitBlock.FACING);
        Text customName = state.customName;
        if (customName == null) return;

        matrices.push();
        matrices.translate(0.5F, 3f/16+0.001f, 0.5F);
        matrices.multiply(facing.getRotationQuaternion());
        int light = state.lightmapCoordinates;
        int textWidth = textRenderer.getWidth(customName);
        float scale = Math.min(MAX_WIDTH / textWidth, MAX_HEIGHT / textRenderer.fontHeight);
        matrices.scale(-scale, -scale, scale);
        queue.submitText(matrices, (float) -textWidth / 2, (float) -textRenderer.fontHeight /2, customName.asOrderedText(), false, TextRenderer.TextLayerType.POLYGON_OFFSET, light, Colors.WHITE, 0, 0);
        matrices.pop();
    }
}
