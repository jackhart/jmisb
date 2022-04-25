package org.jmisb.st0903.vtarget;

import org.jmisb.core.klv.PrimitiveConverter;
import org.jmisb.st0903.IVmtiMetadataValue;

/**
 * Shared pixel index (row column) implementation.
 *
 * <p>Used by Tag 19 and Tag 20.
 */
public abstract class AbstractPixelIndex implements IVmtiMetadataValue {
    private static final long MIN_VAL = 1;
    private static final long MAX_VAL = 4294967295L;
    private long value;

    /**
     * Create from value.
     *
     * @param index the pixel index (min 1, max 2^32-1)
     */
    public AbstractPixelIndex(long index) {
        if (index < MIN_VAL || index > MAX_VAL) {
            throw new IllegalArgumentException(
                    this.getDisplayName() + " must be in range [1, 4294967295]");
        }
        this.value = index;
    }

    /**
     * Create from encoded bytes.
     *
     * @param bytes Encoded byte array
     */
    public AbstractPixelIndex(byte[] bytes) {
        value = PrimitiveConverter.variableBytesToUint32(bytes);
    }

    @Override
    public byte[] getBytes() {
        return PrimitiveConverter.uint32ToVariableBytes(value);
    }

    @Override
    public String getDisplayableValue() {
        return "" + value;
    }

    /**
     * Get the value.
     *
     * @return the value in pixels (1 base)
     */
    public long getValue() {
        return this.value;
    }
}