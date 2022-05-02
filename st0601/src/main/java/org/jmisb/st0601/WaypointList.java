package org.jmisb.st0601;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jmisb.api.common.KlvParseException;
import org.jmisb.api.klv.ArrayBuilder;
import org.jmisb.api.klv.Ber;
import org.jmisb.api.klv.BerDecoder;
import org.jmisb.api.klv.BerEncoder;
import org.jmisb.api.klv.BerField;
import org.jmisb.api.klv.st1201.FpEncoder;
import org.jmisb.api.klv.st1201.OutOfRangeBehaviour;
import org.jmisb.core.klv.PrimitiveConverter;
import org.jmisb.st0601.dto.Location;
import org.jmisb.st0601.dto.Waypoint;

/**
 * Waypoint List (ST 0601 Item 141).
 *
 * <p>From ST:
 *
 * <blockquote>
 *
 * List of waypoints and their status.
 *
 * <p>Waypoints are a series of aircraft destinations used to navigate the aircraft to certain
 * locations. Waypoints are typically included in a flight plan and known at the beginning of a
 * mission; however, depending on real-time events and information, the plan may change. Several
 * types of changes are possible throughout the lifecycle of a mission: the waypoint order changes;
 * cancellation of a waypoint; and adding ad hoc waypoints.
 *
 * <p>The Waypoint List is a list of Waypoint Records encoded as a Variable Length Pack (VLP) to
 * support waypoint management. A Waypoint Record contains: Waypoint ID, Prosecution Order, Info
 * Value, and Location.
 *
 * </blockquote>
 *
 * <p>See the Waypoint data transfer object documentation for description of the components within a
 * Waypoint.
 */
public class WaypointList implements IUasDatalinkValue {
    private final List<Waypoint> waypoints = new ArrayList<>();
    private static final byte MANUAL_MODE = 0x01;
    private static final byte ADHOC_SOURCE = 0x02;

    private final FpEncoder latDecoder = new FpEncoder(-90, 90, 4, OutOfRangeBehaviour.Default);
    private final FpEncoder lonDecoder = new FpEncoder(-180, 180, 4, OutOfRangeBehaviour.Default);
    private final FpEncoder haeDecoder = new FpEncoder(-900, 9000, 3, OutOfRangeBehaviour.Default);

    /**
     * Create from value.
     *
     * @param waypoints list of Waypoint values
     */
    public WaypointList(List<Waypoint> waypoints) {
        this.waypoints.clear();
        this.waypoints.addAll(waypoints);
    }

    /**
     * Create from encoded bytes.
     *
     * @param bytes Waypoint List, byte array with Variable Length Pack encoding
     * @throws KlvParseException if there is a problem when parsing the structure
     */
    public WaypointList(byte[] bytes) throws KlvParseException {
        int idx = 0;
        while (idx < bytes.length) {
            BerField wpLengthField = BerDecoder.decode(bytes, idx, false);
            idx += wpLengthField.getLength();
            if ((idx + wpLengthField.getValue()) > bytes.length) {
                throw new KlvParseException("Insufficient bytes for Waypoint");
            }
            byte[] waypointBytes = Arrays.copyOfRange(bytes, idx, idx + wpLengthField.getValue());
            // Skip over length we just consumed
            idx += wpLengthField.getValue();
            Waypoint wp = parseWaypoint(waypointBytes);
            this.waypoints.add(wp);
        }
    }

    private Waypoint parseWaypoint(byte[] waypointBytes) {
        int idx = 0; // index into waypointBytes where we'll read the next field
        Waypoint waypoint = new Waypoint();
        BerField waypointIdField = BerDecoder.decode(waypointBytes, 0, false);
        waypoint.setWaypointID(waypointIdField.getValue());
        idx += waypointIdField.getLength();
        waypoint.setProsecutionOrder(PrimitiveConverter.toInt16(waypointBytes, idx));
        idx += Short.BYTES;
        BerField waypointInfoField = BerDecoder.decode(waypointBytes, idx, false);
        boolean infoManual = ((waypointInfoField.getValue() & MANUAL_MODE) == MANUAL_MODE);
        waypoint.setManualMode(infoManual);
        boolean adhocSource = ((waypointInfoField.getValue() & ADHOC_SOURCE) == ADHOC_SOURCE);
        waypoint.setAdhocSource(adhocSource);
        idx += waypointInfoField.getLength();
        Location location = new Location();
        location.setLatitude(latDecoder.decode(waypointBytes, idx));
        idx += Integer.BYTES;
        location.setLongitude(lonDecoder.decode(waypointBytes, idx));
        idx += Integer.BYTES;
        double hae = haeDecoder.decode(waypointBytes, idx);
        location.setHAE(hae);
        waypoint.setLocation(location);
        return waypoint;
    }

    /**
     * Get the waypoints that make up this waypoint list.
     *
     * @return the ordered list of waypoints.
     */
    public List<Waypoint> getWaypoints() {
        return this.waypoints;
    }

    @Override
    public byte[] getBytes() {
        ArrayBuilder array = new ArrayBuilder();
        for (Waypoint wp : getWaypoints()) {
            byte[] idBytes = BerEncoder.encode(wp.getWaypointID(), Ber.OID);
            byte[] prosecutionOrderBytes =
                    PrimitiveConverter.int16ToBytes(wp.getProsecutionOrder());
            byte infoVal = 0;
            if (wp.isAdhocSource()) {
                infoVal += ADHOC_SOURCE;
            }
            if (wp.isManualMode()) {
                infoVal += MANUAL_MODE;
            }
            byte[] infoBytes = BerEncoder.encode(infoVal, Ber.OID);
            byte[] latBytes = latDecoder.encode(wp.getLocation().getLatitude());
            byte[] lonBytes = lonDecoder.encode(wp.getLocation().getLongitude());
            byte[] haeBytes = haeDecoder.encode(wp.getLocation().getHAE());
            int length =
                    idBytes.length
                            + prosecutionOrderBytes.length
                            + infoBytes.length
                            + latBytes.length
                            + lonBytes.length
                            + haeBytes.length;
            array.appendAsBerLength(length);
            array.append(idBytes);
            array.append(prosecutionOrderBytes.clone());
            array.append(infoBytes);
            array.append(latBytes);
            array.append(lonBytes);
            array.append(haeBytes);
        }
        return array.toBytes();
    }

    @Override
    public String getDisplayableValue() {
        return "[Waypoint List]";
    }

    @Override
    public String getDisplayName() {
        return "Waypoint List";
    }
}
