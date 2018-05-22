package org.jmisb.api.klv.st0601;

import org.jmisb.api.common.KlvParseException;
import org.jmisb.api.klv.*;
import org.jmisb.core.klv.ArrayUtils;

import java.util.*;

import static org.jmisb.api.klv.KlvConstants.UasDatalinkLocalUl;

/**
 * UAS Datalink Local Set message packet as defined by ST 0601
 * <p>
 * For guidance on which items are mandatory to include in ST 0601 messages, refer to ST 0902 for a list of the
 * minimum set of UAS Datalink LS metadata items.
 */
public class UasDatalinkMessage implements IMisbMessage
{
    // TODO: should we make this class immutable? May have benefits for stability in multi-threaded environments.

    /** Map containing all data elements in the message (except, normally, the checksum) */
    private SortedMap<UasDatalinkTag, UasDatalinkValue> map = new TreeMap<>();

    /**
     * Create the message from the given key/value pairs
     *
     * @param values Tag/value pairs to be included in the message
     */
    public UasDatalinkMessage(SortedMap<UasDatalinkTag, UasDatalinkValue> values)
    {
        this.map = values;
    }

    /**
     * Create the message by parsing the given byte array
     *
     * @param bytes Byte array containing a UAS Datalink message
     *
     * @throws KlvParseException if a parsing error occurs, or checksum is missing/invalid
     */
    public UasDatalinkMessage(byte[] bytes) throws KlvParseException
    {
        // Parse the length field
        LengthField lengthField = BerDecoder.decodeLengthField(bytes, UniversalLabel.LENGTH, false);
        int lengthLength = lengthField.getSizeOfLength();
        int valueLength = lengthField.getSizeOfValue();

        // Parse fields out of the array
        List<LdsField> fields = LdsParser.parseFields(bytes, UniversalLabel.LENGTH + lengthLength, valueLength);

        boolean checksumFound = false;
        for (LdsField field : fields)
        {
            UasDatalinkTag tag = UasDatalinkTag.getTag(field.getLabel());
            if (tag == UasDatalinkTag.Undefined)
            {
                throw new KlvParseException("Invalid UAS Datalink tag: " + field.getLabel());
            }
            else if (tag == UasDatalinkTag.Checksum)
            {
                checksumFound = true;
                byte[] expected = Checksum.compute(bytes, false);
                byte[] actual = Arrays.copyOfRange(bytes, bytes.length-2, bytes.length);
                if (!Arrays.equals(expected, actual))
                {
                    throw new KlvParseException("Bad checksum");
                }
            }
            else
            {
                UasDatalinkValue value = UasDatalinkFactory.createValue(tag, field.getData());
                setField(tag, value);
            }
        }

        // Throw if checksum is missing
        if (!checksumFound)
        {
            throw new KlvParseException("Missing checksum");
        }
    }

    private void setField(UasDatalinkTag tag, UasDatalinkValue value)
    {
        map.put(tag, value);
    }

    public Object getField(UasDatalinkTag tag)
    {
        return map.get(tag);
    }

    @Override
    public UniversalLabel getUniversalLabel()
    {
        return UasDatalinkLocalUl;
    }

    @Override
    public byte[] frameMessage(boolean isNested)
    {
        // List representing all tags and values as primitive byte arrays. Avoids boxing/unboxing
        // individual bytes for efficiency.
        List<byte[]> chunks = new ArrayList<>();

        // Note: we will insert key/length fields into chunks after all value fields have been added

        // Add all values from map
        for (Map.Entry<UasDatalinkTag, UasDatalinkValue> entry : map.entrySet())
        {
            UasDatalinkTag tag = entry.getKey();

            // Ignore checksum if present in the map (should not be), it will be calculated and appended at the very end
            if (tag == UasDatalinkTag.Checksum)
            {
                continue;
            }

            UasDatalinkValue value = entry.getValue();
            byte[] bytes = value.getBytes();
            if (bytes != null && bytes.length > 0)
            {
                // TODO - encode tag with BER-OID if 0601 tags ever get above 127
                chunks.add(new byte[]{(byte) tag.getCode()});
                chunks.add(BerEncoder.encodeLengthField(bytes.length));
                chunks.add(bytes.clone());
            }
        }

        // Add Key and Length of checksum with placeholder for value - Checksum must be final element
        byte[] checksum = new byte[2];
        chunks.add(new byte[]{(byte)UasDatalinkTag.Checksum.getCode()});
        chunks.add(BerEncoder.encodeLengthField(checksum.length, Ber.SHORT_FORM));
        chunks.add(checksum);

        // Figure out value length
        final int keyLength = UniversalLabel.LENGTH;
        int valueLength = 0;
        for (byte[] chunk : chunks)
        {
            valueLength += chunk.length;
        }

        // Determine total length
        int totalLength;
        if (isNested)
        {
            // NOTE: nesting ST 0601 seems unlikely, but we'll support it anyway
            totalLength = valueLength;
        }
        else
        {
            // Prepend length field into front of the list
            byte[] lengthField = BerEncoder.encodeLengthField(valueLength);
            chunks.add(0, lengthField);

            // Prepend key field (UL) into front of the list
            chunks.add(0, UasDatalinkLocalUl.getBytes());

            // Compute full message length
            totalLength = keyLength + lengthField.length + valueLength;
        }

        // Allocate array and write all chunks
        // TODO: optimize
        byte[] array = new byte[totalLength];
        int i = 0;
        for (byte[] chunk : chunks)
        {
            for (byte b : chunk)
            {
                array[i++] = b;
            }
        }

        // Compute the checksum and replace the last two bytes of array
        Checksum.compute(array, true);

        return array;
    }
}
