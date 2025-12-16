package net.replaceitem.integratedcircuit.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.replaceitem.integratedcircuit.IntegratedCircuit;
import net.replaceitem.integratedcircuit.IntegratedCircuitBlock;
import net.replaceitem.integratedcircuit.client.config.DefaultConfig;
import net.replaceitem.integratedcircuit.network.ClientPacketHandler;
import net.replaceitem.integratedcircuit.network.packet.CircuitNameUpdateS2CPacket;
import net.replaceitem.integratedcircuit.network.packet.ComponentUpdateS2CPacket;
import net.replaceitem.integratedcircuit.network.packet.EditIntegratedCircuitS2CPacket;
import net.replaceitem.integratedcircuit.util.FlatDirection;

public class IntegratedCircuitClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT, IntegratedCircuit.Blocks.CIRCUITS);

        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
            if(view == null || pos == null || !(state.getBlock() instanceof IntegratedCircuitBlock block) || tintIndex > 3)
                return RedStoneWireBlock.getColorForPower(0);

            FlatDirection circuitDirection = FlatDirection.VALUES[tintIndex];

            return RedStoneWireBlock.getColorForPower(
                block.getPortRenderStrength(view, pos, circuitDirection)
            );
        }, IntegratedCircuit.Blocks.CIRCUITS);

        DefaultConfig.initialize();
        
        BlockEntityRenderers.register(IntegratedCircuit.INTEGRATED_CIRCUIT_BLOCK_ENTITY, IntegratedCircuitBlockEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(CircuitNameUpdateS2CPacket.ID, ClientPacketHandler::receiveCircuitNameUpdatePacket);
        ClientPlayNetworking.registerGlobalReceiver(EditIntegratedCircuitS2CPacket.ID, ClientPacketHandler::receiveEditIntegratedCircuitPacket);
        ClientPlayNetworking.registerGlobalReceiver(ComponentUpdateS2CPacket.ID, ClientPacketHandler::receiveComponentUpdatePacket);
    }
}
