package net.replaceitem.integratedcircuit.circuit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.Strategy;
import net.replaceitem.integratedcircuit.util.ComponentPos;

public class CircuitSection {
    public static final Strategy<ComponentState> COMPONENT_STATE_PALETTE_PROVIDER = new FlatPaletteProvider<>(Component.STATE_IDS, 15);
    
    public static final Codec<PalettedContainer<ComponentState>> PALETTE_CODEC = PalettedContainer.codecRW(
            ComponentState.CODEC, CircuitSection.COMPONENT_STATE_PALETTE_PROVIDER, Components.AIR.getDefaultState()
    );
    
    public static final Codec<CircuitSection> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PALETTE_CODEC.fieldOf("component_states").forGetter(CircuitSection::getComponentStateContainer)
    ).apply(instance, CircuitSection::new));
    
    public static PalettedContainer<ComponentState> createContainer() {
        return new PalettedContainer<>(Components.AIR.getDefaultState(), COMPONENT_STATE_PALETTE_PROVIDER);
    }
    
    private final PalettedContainer<ComponentState> stateContainer;

    public CircuitSection(PalettedContainer<ComponentState> stateContainer) {
        this.stateContainer = stateContainer;
    }
    
    public CircuitSection() {
        this.stateContainer = createContainer();
    }

    public PalettedContainer<ComponentState> getComponentStateContainer() {
        return stateContainer;
    }

    public ComponentState getComponentState(int x, int y) {
        return stateContainer.get(x, y, 0);
    }

    public void setComponentState(ComponentPos pos, ComponentState state) {
        stateContainer.set(pos.getX(), pos.getY(), 0, state);
    }

    public void readPacket(FriendlyByteBuf buf) {
        // TODO use this for sending circuits to clients
        this.stateContainer.read(buf);
    }

    public void writePacket(FriendlyByteBuf buf) {
        this.stateContainer.write(buf);
    }

    public boolean isEmpty() {
        return !stateContainer.maybeHas(componentState -> componentState != Components.AIR_DEFAULT_STATE);
    }
}
