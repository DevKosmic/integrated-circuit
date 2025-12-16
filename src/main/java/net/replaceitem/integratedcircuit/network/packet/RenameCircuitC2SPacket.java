package net.replaceitem.integratedcircuit.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.replaceitem.integratedcircuit.IntegratedCircuit;

public record RenameCircuitC2SPacket(
    Component newName,
    BlockPos pos
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RenameCircuitC2SPacket> ID = new CustomPacketPayload.Type<>(IntegratedCircuit.id("rename_circuit_c2s_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RenameCircuitC2SPacket> PACKET_CODEC = StreamCodec.composite(
        ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC, RenameCircuitC2SPacket::newName,
        BlockPos.STREAM_CODEC, RenameCircuitC2SPacket::pos,
        RenameCircuitC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
