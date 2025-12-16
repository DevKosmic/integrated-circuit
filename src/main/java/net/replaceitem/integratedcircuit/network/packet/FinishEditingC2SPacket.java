package net.replaceitem.integratedcircuit.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.replaceitem.integratedcircuit.IntegratedCircuit;

public record FinishEditingC2SPacket(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<FinishEditingC2SPacket> ID = new CustomPacketPayload.Type<>(IntegratedCircuit.id("finish_editing_c2s_packet"));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, FinishEditingC2SPacket> PACKET_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, FinishEditingC2SPacket::pos, FinishEditingC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
