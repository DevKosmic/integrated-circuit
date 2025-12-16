package net.replaceitem.integratedcircuit.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.replaceitem.integratedcircuit.IntegratedCircuit;
import net.replaceitem.integratedcircuit.circuit.Component;
import net.replaceitem.integratedcircuit.util.ComponentPos;
import net.replaceitem.integratedcircuit.util.FlatDirection;

public record PlaceComponentC2SPacket(
        ComponentPos pos,
        BlockPos blockPos,
        Component component,
        FlatDirection rotation
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlaceComponentC2SPacket> ID = new CustomPacketPayload.Type<>(IntegratedCircuit.id("place_component_c2s_packet"));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, PlaceComponentC2SPacket> PACKET_CODEC = StreamCodec.composite(
            ComponentPos.STREAM_CODEC, PlaceComponentC2SPacket::pos,
            BlockPos.STREAM_CODEC, PlaceComponentC2SPacket::blockPos,
            Component.PACKET_CODEC, PlaceComponentC2SPacket::component,
            FlatDirection.PACKET_CODEC, PlaceComponentC2SPacket::rotation,
            PlaceComponentC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
