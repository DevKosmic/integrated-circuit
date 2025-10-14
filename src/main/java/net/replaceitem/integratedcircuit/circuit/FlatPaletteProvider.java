package net.replaceitem.integratedcircuit.circuit;

import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.world.chunk.*;

public class FlatPaletteProvider<T> extends PaletteProvider<T> {

    private final int edgeSize;

    private static final Palette.Factory SINGULAR = SingularPalette::create;
    private static final Palette.Factory ARRAY = ArrayPalette::create;
    private static final Palette.Factory BI_MAP = BiMapPalette::create;

    static final PaletteType SINGULAR_TYPE = new PaletteType.Static(SINGULAR, 0);
    static final PaletteType ARRAY_1_TYPE = new PaletteType.Static(ARRAY, 1);
    static final PaletteType ARRAY_2_TYPE = new PaletteType.Static(ARRAY, 2);
    static final PaletteType ARRAY_3_TYPE = new PaletteType.Static(ARRAY, 3);
    static final PaletteType ARRAY_4_TYPE = new PaletteType.Static(ARRAY, 4);
    static final PaletteType BI_MAP_5_TYPE = new PaletteType.Static(BI_MAP, 5);
    static final PaletteType BI_MAP_6_TYPE = new PaletteType.Static(BI_MAP, 6);
    static final PaletteType BI_MAP_7_TYPE = new PaletteType.Static(BI_MAP, 7);
    static final PaletteType BI_MAP_8_TYPE = new PaletteType.Static(BI_MAP, 8);

    public FlatPaletteProvider(IndexedIterable<T> idList, int edgeSize) {
        super(idList, 0);
        this.edgeSize = edgeSize;
    }

    @Override
    public int getSize() {
        return edgeSize * edgeSize;
    }

    @Override
    public int computeIndex(int x, int y, int z) {
        return y * edgeSize + x;
    }

    @Override
    protected PaletteType createType(int bitsInStorage) {
        return switch (bitsInStorage) {
            case 0 -> SINGULAR_TYPE;
            case 1, 2, 3, 4 -> ARRAY_4_TYPE;
            case 5 -> BI_MAP_5_TYPE;
            case 6 -> BI_MAP_6_TYPE;
            case 7 -> BI_MAP_7_TYPE;
            case 8 -> BI_MAP_8_TYPE;
            default -> new PaletteType.Dynamic(this.bitsInMemory, bitsInStorage);
        };
    }
}
