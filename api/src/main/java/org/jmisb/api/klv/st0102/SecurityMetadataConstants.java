package org.jmisb.api.klv.st0102;

import org.jmisb.api.klv.UniversalLabel;

/**
 * Constants used by ST 0102
 */
public class SecurityMetadataConstants
{
    /** The currently-supported revision is 0102.12 */
    public static short ST_VERSION_NUMBER = 12;

    private SecurityMetadataConstants() {}

    public static UniversalLabel securityClassificationUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x02, 0x08, 0x02, 0x01, 0x00, 0x00, 0x00, 0x00});
    public static UniversalLabel ccCodingMethodUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x07, 0x01, 0x20, 0x01, 0x02, 0x07, 0x00, 0x00});
    public static UniversalLabel classifyingCountryUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x07, 0x01, 0x20, 0x01, 0x02, 0x08, 0x00, 0x00});
    public static UniversalLabel sciShiInfoUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x01, 0x0e, 0x01, 0x02, 0x03, 0x02, 0x00, 0x00, 0x00});
    public static UniversalLabel caveatsUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x02, 0x08, 0x02, 0x02, 0x00, 0x00, 0x00, 0x00});
    public static UniversalLabel releasingInstructionsUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x07, 0x01, 0x20, 0x01, 0x02, 0x09, 0x00, 0x00});
    public static UniversalLabel classifiedByUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x02, 0x08, 0x02, 0x03, 0x00, 0x00, 0x00, 0x00});
    public static UniversalLabel derivedFromUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x02, 0x08, 0x02, 0x06, 0x00, 0x00, 0x00, 0x00});
    public static UniversalLabel classificationReasonUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x02, 0x08, 0x02, 0x04, 0x00, 0x00, 0x00, 0x00});
    public static UniversalLabel declassificationDateUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x02, 0x08, 0x02, 0x05, 0x00, 0x00, 0x00, 0x00});
    public static UniversalLabel markingSystemUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x02, 0x08, 0x02, 0x08, 0x00, 0x00, 0x00, 0x00});
    public static UniversalLabel ocCodingMethodUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x07, 0x01, 0x20, 0x01, 0x02, 0x06, 0x00, 0x00});
    public static UniversalLabel objectCountryCodesUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x07, 0x01, 0x20, 0x01, 0x02, 0x01, 0x01, 0x00});
    public static UniversalLabel classificationCommentsUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x03, 0x02, 0x08, 0x02, 0x07, 0x00, 0x00, 0x00, 0x00});
    public static UniversalLabel versionUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x01, 0x0e, 0x01, 0x02, 0x05, 0x04, 0x00, 0x00, 0x00});
    public static UniversalLabel ccCodingMethodVersionDateUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x01, 0x0e, 0x01, 0x04, 0x03, 0x03, 0x00, 0x00, 0x00});
    public static UniversalLabel ocCodingMethodVersionDateUl = new UniversalLabel(new byte[] {0x06, 0x0e, 0x2b, 0x34, 0x01, 0x01, 0x01,
            0x01, 0x0e, 0x01, 0x04, 0x03, 0x04, 0x00, 0x00, 0x00});
}
