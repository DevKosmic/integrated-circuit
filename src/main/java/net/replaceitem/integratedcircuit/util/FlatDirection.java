package net.replaceitem.integratedcircuit.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.replaceitem.integratedcircuit.IntegratedCircuitBlock;

public enum FlatDirection implements StringRepresentable {
    NORTH(0, "north", 0, -1, Axis.Y, Direction.NORTH),
    EAST(1, "east", 1, 0, Axis.X, Direction.EAST),
    SOUTH(2, "south", 0, 1, Axis.Y, Direction.SOUTH),
    WEST(3, "west", -1, 0, Axis.X, Direction.WEST);

    private final int index;
    private final String name;
    private final Vec3i offset;
    private final Axis axis;
    private final Direction vanillaDirection;

    public static final FlatDirection[] VALUES = FlatDirection.values();
    public static final FlatDirection[] VALUES_X = {EAST, WEST};
    public static final FlatDirection[] VALUES_Y = {NORTH, SOUTH};

    public static final StreamCodec<ByteBuf,FlatDirection> PACKET_CODEC = ByteBufCodecs.idMapper(value -> VALUES[value], FlatDirection::getIndex);

    FlatDirection(int index, String name, int dx, int dy, Axis axis, Direction vanillaDirection) {
        this.index = index;
        this.name = name;
        this.offset = new Vec3i(dx, dy, 0);
        this.axis = axis;
        this.vanillaDirection = vanillaDirection;
    }

    public int getIndex() {
        return this.index;
    }

    public Vec3i getOffset() {
        return offset;
    }

    public Axis getAxis() {
        return axis;
    }

    public FlatDirection rotated(int times) {
        return VALUES[Math.floorMod(this.getIndex()+times,4)];
    }

    public FlatDirection rotatedCounterclockwise(int times) {
        return this.rotated(-times);
    }

    public FlatDirection getOpposite() {
        return this.rotated(2);
    }

    public Direction toVanillaDirection() {
        return vanillaDirection;
    }

    public Direction toVanillaDirection(Direction facing) {
        return this.rotated(FlatDirection.fromVanillaDirection(facing).getIndex()).toVanillaDirection();
    }

    public Direction toVanillaDirection(BlockState circuit) {
        if(circuit.hasProperty(IntegratedCircuitBlock.FACING))
            return this.toVanillaDirection(circuit.getValue(IntegratedCircuitBlock.FACING));
        return this.toVanillaDirection();
    }

    public static FlatDirection fromVanillaDirection(Direction direction) {
        return switch (direction) {
            case DOWN, UP -> throw new IllegalArgumentException("Cannot convert vertical Direction to FlatDirection");
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }

    public static FlatDirection fromVanillaDirection(Direction facing, Direction direction) {
        return FlatDirection.fromVanillaDirection(direction).rotatedCounterclockwise(FlatDirection.fromVanillaDirection(facing).getIndex());
    }

    public static FlatDirection fromVanillaDirection(BlockState circuit, Direction direction) {
        if(circuit.hasProperty(IntegratedCircuitBlock.FACING))
            return FlatDirection.fromVanillaDirection(circuit.getValue(IntegratedCircuitBlock.FACING), direction);
        return FlatDirection.fromVanillaDirection(direction);
    }

    public static FlatDirection[] forAxis(Axis axis) {
        return axis == Axis.X ? VALUES_X : VALUES_Y;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public enum Axis {
        X, Y
    }
}
