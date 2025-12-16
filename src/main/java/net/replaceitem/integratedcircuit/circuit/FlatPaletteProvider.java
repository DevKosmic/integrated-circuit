package net.replaceitem.integratedcircuit.circuit;

import net.minecraft.core.IdMap;
import net.minecraft.world.level.chunk.Configuration;
import net.minecraft.world.level.chunk.HashMapPalette;
import net.minecraft.world.level.chunk.LinearPalette;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.SingleValuePalette;
import net.minecraft.world.level.chunk.Strategy;

public class FlatPaletteProvider<T> extends Strategy<T> {

    private final int edgeSize;

    private static final Palette.Factory SINGULAR = SingleValuePalette::create;
    private static final Palette.Factory ARRAY = LinearPalette::create;
    private static final Palette.Factory BI_MAP = HashMapPalette::create;

    static final Configuration SINGULAR_TYPE = new Configuration.Simple(SINGULAR, 0);
    static final Configuration ARRAY_1_TYPE = new Configuration.Simple(ARRAY, 1);
    static final Configuration ARRAY_2_TYPE = new Configuration.Simple(ARRAY, 2);
    static final Configuration ARRAY_3_TYPE = new Configuration.Simple(ARRAY, 3);
    static final Configuration ARRAY_4_TYPE = new Configuration.Simple(ARRAY, 4);
    static final Configuration BI_MAP_5_TYPE = new Configuration.Simple(BI_MAP, 5);
    static final Configuration BI_MAP_6_TYPE = new Configuration.Simple(BI_MAP, 6);
    static final Configuration BI_MAP_7_TYPE = new Configuration.Simple(BI_MAP, 7);
    static final Configuration BI_MAP_8_TYPE = new Configuration.Simple(BI_MAP, 8);

    public FlatPaletteProvider(IdMap<T> idList, int edgeSize) {
        super(idList, 0);
        this.edgeSize = edgeSize;
    }

    @Override
    public int entryCount() {
        return edgeSize * edgeSize;
    }

    @Override
    public int getIndex(int x, int y, int z) {
        return y * edgeSize + x;
    }

    @Override
    protected Configuration getConfigurationForBitCount(int bitsInStorage) {
        return switch (bitsInStorage) {
            case 0 -> SINGULAR_TYPE;
            case 1, 2, 3, 4 -> ARRAY_4_TYPE;
            case 5 -> BI_MAP_5_TYPE;
            case 6 -> BI_MAP_6_TYPE;
            case 7 -> BI_MAP_7_TYPE;
            case 8 -> BI_MAP_8_TYPE;
            default -> new Configuration.Global(this.globalPaletteBitsInMemory, bitsInStorage);
        };
    }
}
