package org.jmisb.api.klv.st0806;

import org.jmisb.core.klv.PrimitiveConverter;

/**
 * True airspeed (TAS) of platform. (ST 0806 Tag 3).
 *
 * <p>Indicated Airspeed adjusted for temperature and altitude.
 *
 * <p>Units are meters/second. 1 m/s = 1.94384449 knots.
 *
 * <p>Resolution: 1 meter/second.
 */
public class RvtPlatformTrueAirspeed implements IRvtMetadataValue {
    private final int airspeed;
    private static final long MIN_VALUE = 0;
    private static final long MAX_VALUE = 65535;
    private static final int REQUIRED_BYTE_LENGTH = 2;

    /**
     * Create from value.
     *
     * @param speed airspeed in meters/second. Legal values are in [0, 65535].
     */
    public RvtPlatformTrueAirspeed(int speed) {
        if (speed > MAX_VALUE || speed < MIN_VALUE) {
            throw new IllegalArgumentException(getDisplayName() + " must be in range [0, 65535]");
        }
        airspeed = speed;
    }

    /**
     * Construct from encoded bytes.
     *
     * @param bytes two bytes representing unsigned integer value.
     */
    public RvtPlatformTrueAirspeed(byte[] bytes) {
        if (bytes.length != REQUIRED_BYTE_LENGTH) {
            throw new IllegalArgumentException(
                    this.getDisplayName() + " encoding is a two byte unsigned integer");
        }
        airspeed = PrimitiveConverter.toUint16(bytes);
    }

    @Override
    public byte[] getBytes() {
        return PrimitiveConverter.uint16ToBytes(airspeed);
    }

    /**
     * Get the airspeed.
     *
     * @return The airspeed in meters/second
     */
    public int getMetersPerSecond() {
        return airspeed;
    }

    @Override
    public String getDisplayableValue() {
        return String.format("%dm/s", airspeed);
    }

    @Override
    public final String getDisplayName() {
        return "Platform True Airspeed (TAS)";
    }
}
