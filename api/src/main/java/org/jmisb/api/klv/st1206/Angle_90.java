package org.jmisb.api.klv.st1206;

import org.jmisb.api.klv.st1201.FpEncoder;
import org.jmisb.api.klv.st1201.OutOfRangeBehaviour;

/**
 * Shared ST1206 implementation of angle between -90 and 90 degrees.
 *
 * <p>This is encoded as IMAPB(-90, 90, 2).
 */
public abstract class Angle_90 extends AbstractAngle implements ISARMIMetadataValue {
    private static final double MIN_VAL = -90.0;
    private static final double MAX_VAL = 90.0;

    /**
     * Create from value.
     *
     * @param angle angle value in degrees.
     */
    public Angle_90(double angle) {
        if (angle < MIN_VAL || angle > MAX_VAL) {
            throw new IllegalArgumentException(
                    String.format(
                            "%s must be in range [%f, %f]",
                            this.getDisplayName(), MIN_VAL, MAX_VAL));
        }
        this.value = angle;
    }

    /**
     * Create from encoded bytes.
     *
     * @param bytes the byte array to decode the value from.
     */
    public Angle_90(byte[] bytes) {
        if (bytes.length != NUM_BYTES) {
            throw new IllegalArgumentException(
                    String.format(
                            "%s encoding is %d byte IMAPB", this.getDisplayName(), NUM_BYTES));
        }
        FpEncoder decoder =
                new FpEncoder(MIN_VAL, MAX_VAL, bytes.length, OutOfRangeBehaviour.Default);
        this.value = decoder.decode(bytes);
    }

    @Override
    public byte[] getBytes() {
        FpEncoder encoder = new FpEncoder(MIN_VAL, MAX_VAL, NUM_BYTES, OutOfRangeBehaviour.Default);
        return encoder.encode(this.value);
    }
}
