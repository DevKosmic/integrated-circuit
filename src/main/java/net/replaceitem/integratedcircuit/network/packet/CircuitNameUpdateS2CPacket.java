package net.replaceitem.integratedcircuit.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.replaceitem.integratedcircuit.IntegratedCircuit;

public record CircuitNameUpdateS2CPacket(
    Component newName,
    BlockPos pos
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CircuitNameUpdateS2CPacket> ID = new CustomPacketPayload.Type<>(IntegratedCircuit.id("circuit_name_update_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CircuitNameUpdateS2CPacket> PACKET_CODEC = StreamCodec.composite(
        ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC, CircuitNameUpdateS2CPacket::newName,
        BlockPos.STREAM_CODEC, CircuitNameUpdateS2CPacket::pos,
        CircuitNameUpdateS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
