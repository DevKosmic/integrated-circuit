package net.replaceitem.integratedcircuit.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.replaceitem.integratedcircuit.client.gui.IntegratedCircuitScreen;
import net.replaceitem.integratedcircuit.network.packet.CircuitNameUpdateS2CPacket;
import net.replaceitem.integratedcircuit.network.packet.ComponentUpdateS2CPacket;
import net.replaceitem.integratedcircuit.network.packet.EditIntegratedCircuitS2CPacket;

@SuppressWarnings("resource")
public class ClientPacketHandler {
    public static void receiveEditIntegratedCircuitPacket(EditIntegratedCircuitS2CPacket packet, ClientPlayNetworking.Context context) {
        context.client().setScreen(
            new IntegratedCircuitScreen(
                packet.getClientCircuit(
                    context.client().level,
                    packet.pos()
                ),
                packet.customName()
            )
        );
    }

    public static void receiveCircuitNameUpdatePacket(CircuitNameUpdateS2CPacket packet, ClientPlayNetworking.Context context) {
        if (context.client().screen instanceof IntegratedCircuitScreen integratedCircuitScreen) {
            integratedCircuitScreen.updateCustomNameForExternalChange(packet.newName());
        }
    }

    public static void receiveComponentUpdatePacket(ComponentUpdateS2CPacket packet, ClientPlayNetworking.Context context) {
        if (context.client().screen instanceof IntegratedCircuitScreen integratedCircuitScreen) {
            integratedCircuitScreen.getClientCircuit().onComponentUpdateFromServer(
                packet.state(),
                packet.pos()
            );
        }
    }
}
