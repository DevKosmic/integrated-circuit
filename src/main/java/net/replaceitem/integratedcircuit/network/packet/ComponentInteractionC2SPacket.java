package net.replaceitem.integratedcircuit.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.replaceitem.integratedcircuit.IntegratedCircuit;
import net.replaceitem.integratedcircuit.util.ComponentPos;

public record ComponentInteractionC2SPacket(
        ComponentPos pos,
        BlockPos blockPos
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ComponentInteractionC2SPacket> ID = new Type<>(IntegratedCircuit.id("component_interaction_c2s_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentInteractionC2SPacket> PACKET_CODEC = StreamCodec.composite(
            ComponentPos.STREAM_CODEC, ComponentInteractionC2SPacket::pos,
            BlockPos.STREAM_CODEC, ComponentInteractionC2SPacket::blockPos,
            ComponentInteractionC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
