package net.replaceitem.integratedcircuit.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.replaceitem.integratedcircuit.IntegratedCircuit;
import net.replaceitem.integratedcircuit.circuit.Component;
import net.replaceitem.integratedcircuit.circuit.ComponentState;
import net.replaceitem.integratedcircuit.util.ComponentPos;

public record ComponentUpdateS2CPacket(
        ComponentPos pos,
        ComponentState state
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ComponentUpdateS2CPacket> ID = new CustomPacketPayload.Type<>(IntegratedCircuit.id("component_update_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentUpdateS2CPacket> PACKET_CODEC = StreamCodec.composite(
            ComponentPos.STREAM_CODEC, ComponentUpdateS2CPacket::pos,
            ByteBufCodecs.idMapper(Component.STATE_IDS), ComponentUpdateS2CPacket::state,
            ComponentUpdateS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
