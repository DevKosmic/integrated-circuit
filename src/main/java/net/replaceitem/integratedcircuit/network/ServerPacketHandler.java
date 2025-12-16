package net.replaceitem.integratedcircuit.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.replaceitem.integratedcircuit.IntegratedCircuit;
import net.replaceitem.integratedcircuit.IntegratedCircuitBlockEntity;
import net.replaceitem.integratedcircuit.network.packet.*;

import java.util.Objects;

public class ServerPacketHandler {
    public static void receiveComponentInteraction(ComponentInteractionC2SPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        ServerLevel world = player.level();
        if(
                world.getBlockState(packet.blockPos()).is(IntegratedCircuit.Tags.INTEGRATED_CIRCUITS_BLOCK_TAG) &&
                world.getBlockEntity(packet.blockPos()) instanceof IntegratedCircuitBlockEntity integratedCircuitBlockEntity &&
                integratedCircuitBlockEntity.getCircuit() != null
        ) {
            if(!integratedCircuitBlockEntity.isEditing(player)) return;
            integratedCircuitBlockEntity.getCircuit().useComponent(packet.pos(), player);
        }
    }

    public static void receiveFinishEditingPacket(FinishEditingC2SPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        player.resetLastActionTime();
        ServerLevel serverWorld = player.level();
        BlockEntity blockEntity = serverWorld.getBlockEntity(packet.pos());

        if (blockEntity instanceof IntegratedCircuitBlockEntity integratedCircuitBlockEntity) {
            integratedCircuitBlockEntity.removeEditor(player);
        }
    }

    public static void receiveRenameCircuitPacket(RenameCircuitC2SPacket packet, ServerPlayNetworking.Context context) {
        BlockPos pos = packet.pos();
        ServerPlayer renamingPlayer = context.player();
        ServerLevel serverWorld = renamingPlayer.level();
        BlockEntity blockEntity = serverWorld.getBlockEntity(pos);

        renamingPlayer.resetLastActionTime();

        if (blockEntity instanceof IntegratedCircuitBlockEntity integratedCircuitBlockEntity) {

            Component currentName = integratedCircuitBlockEntity.getCustomName();
            Component newName = packet.newName();

            if (!Objects.equals(currentName, newName)) {
                integratedCircuitBlockEntity.setCustomName(newName);

                for (ServerPlayer player : integratedCircuitBlockEntity.getEditingPlayers()) {
                    ServerPlayNetworking.send(
                        player,
                        new CircuitNameUpdateS2CPacket(newName, pos)
                    );
                }
            }
        }
    }

    public static void receivePlaceComponentPacket(PlaceComponentC2SPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        ServerLevel world = player.level();
        if(
                world.getBlockState(packet.blockPos()).is(IntegratedCircuit.Tags.INTEGRATED_CIRCUITS_BLOCK_TAG) && 
                world.getBlockEntity(packet.blockPos()) instanceof IntegratedCircuitBlockEntity integratedCircuitBlockEntity &&
                integratedCircuitBlockEntity.getCircuit() != null
        ) {
            if(!integratedCircuitBlockEntity.isEditing(player)) return;
            integratedCircuitBlockEntity.getCircuit().placeComponentState(packet.pos(), packet.component(), packet.rotation());
        }
    }
}
