package org.jmisb.api.klv.st0601;

import org.jmisb.api.common.KlvParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UasDatalinkLongitudeTest
{
    // Resolution is 84 nano degrees, so error is +/-42 nano degrees
    private final double delta = 42e-9;

    @Test
    public void testSubClasses()
    {
        // Alternate Platform Longitude
        UasDatalinkLongitude longitude = new AlternatePlatformLongitude(0.155527554524842);
        Assert.assertEquals(longitude.getBytes(), new byte[]{(byte)0x00, (byte)0x1c, (byte)0x50, (byte)0x1c});
        Assert.assertEquals(longitude.getDegrees(), 0.155527554524842);
        Assert.assertEquals(longitude.getDisplayableValue(), "0.1555\u00B0");

        longitude = new AlternatePlatformLongitude(new byte[]{(byte)0x00, (byte)0x1c, (byte)0x50, (byte)0x1c});
        Assert.assertEquals(longitude.getDegrees(), 0.155527554524842, delta);
        Assert.assertEquals(longitude.getBytes(), new byte[]{(byte)0x00, (byte)0x1c, (byte)0x50, (byte)0x1c});

        // Frame Center Longitude
        longitude = new FrameCenterLongitude(29.157890122923);
        Assert.assertEquals(longitude.getBytes(), new byte[]{(byte)0x14, (byte)0xbc, (byte)0x08, (byte)0x2b});
        Assert.assertEquals(longitude.getDegrees(), 29.157890122923);
        Assert.assertEquals(longitude.getDisplayableValue(), "29.1579\u00B0");

        longitude = new FrameCenterLongitude(new byte[]{(byte)0x14, (byte)0xbc, (byte)0x08, (byte)0x2b});
        Assert.assertEquals(longitude.getDegrees(), 29.157890122923, delta);
        Assert.assertEquals(longitude.getBytes(), new byte[]{(byte)0x14, (byte)0xbc, (byte)0x08, (byte)0x2b});

        // Target Location Longitude
        longitude = new TargetLocationLongitude(166.400812960416);
        Assert.assertEquals(longitude.getBytes(), new byte[]{(byte)0x76, (byte)0x54, (byte)0x57, (byte)0xf2});
        Assert.assertEquals(longitude.getDegrees(), 166.400812960416);
        Assert.assertEquals(longitude.getDisplayableValue(), "166.4008\u00B0");

        longitude = new TargetLocationLongitude(new byte[]{(byte)0x76, (byte)0x54, (byte)0x57, (byte)0xf2});
        Assert.assertEquals(longitude.getDegrees(), 166.400812960416, delta);
        Assert.assertEquals(longitude.getBytes(), new byte[]{(byte)0x76, (byte)0x54, (byte)0x57, (byte)0xf2});

        // Corner Longitude Point 1
        longitude = new FullCornerLongitude(29.1273677986333);
        Assert.assertEquals(longitude.getBytes(), new byte[]{(byte)0x14, (byte)0xb6, (byte)0x79, (byte)0xb9});
        Assert.assertEquals(longitude.getDegrees(), 29.1273677986333);
        Assert.assertEquals(longitude.getDisplayableValue(), "29.1274\u00B0");

        longitude = new FullCornerLongitude(new byte[]{(byte)0x14, (byte)0xb6, (byte)0x79, (byte)0xb9});
        Assert.assertEquals(longitude.getDegrees(), 29.1273677986333, delta);
        Assert.assertEquals(longitude.getBytes(), new byte[]{(byte)0x14, (byte)0xb6, (byte)0x79, (byte)0xb9});
    }

    @Test
    public void testFactoryAlternatePlatformLongitude() throws KlvParseException {
        byte[] bytes = new byte[]{(byte)0x00, (byte)0x1c, (byte)0x50, (byte)0x1c};
        IUasDatalinkValue v = UasDatalinkFactory.createValue(UasDatalinkTag.AlternatePlatformLongitude, bytes);
        Assert.assertTrue(v instanceof AlternatePlatformLongitude);
        AlternatePlatformLongitude longitude = (AlternatePlatformLongitude)v;
        Assert.assertEquals(longitude.getBytes(),bytes);
        Assert.assertEquals(longitude.getDegrees(), 0.155527554524842, delta);
        Assert.assertEquals(longitude.getDisplayableValue(), "0.1555\u00B0");
    }

    @Test
    public void testFactoryFrameCenterLongitude() throws KlvParseException {
        byte[] bytes = new byte[]{(byte)0x14, (byte)0xbc, (byte)0x08, (byte)0x2b};
        IUasDatalinkValue v = UasDatalinkFactory.createValue(UasDatalinkTag.FrameCenterLongitude, bytes);
        Assert.assertTrue(v instanceof FrameCenterLongitude);
        FrameCenterLongitude longitude = (FrameCenterLongitude)v;
        Assert.assertEquals(longitude.getDegrees(), 29.157890122923, delta);
        Assert.assertEquals(longitude.getDisplayableValue(), "29.1579\u00B0");
    }

    @Test
    public void testFactoryTargetLocationLongitude() throws KlvParseException {
        byte[] bytes = new byte[]{(byte)0x76, (byte)0x54, (byte)0x57, (byte)0xf2};
        IUasDatalinkValue v = UasDatalinkFactory.createValue(UasDatalinkTag.TargetLocationLongitude, bytes);
        Assert.assertTrue(v instanceof TargetLocationLongitude);
        TargetLocationLongitude longitude = (TargetLocationLongitude)v;
        Assert.assertEquals(longitude.getDegrees(), 166.400812960416, delta);
        Assert.assertEquals(longitude.getDisplayableValue(), "166.4008\u00B0");
    }

    @Test
    public void testFactoryFullCornerLatitude() throws KlvParseException {
        byte[] bytes = new byte[]{(byte)0x14, (byte)0xb6, (byte)0x79, (byte)0xb9};
        IUasDatalinkValue v = UasDatalinkFactory.createValue(UasDatalinkTag.CornerLonPt1, bytes);
        Assert.assertTrue(v instanceof FullCornerLongitude);
        FullCornerLongitude longitude = (FullCornerLongitude)v;
        Assert.assertEquals(longitude.getDegrees(), 29.1273677986333, delta);
        Assert.assertEquals(longitude.getDisplayableValue(), "29.1274\u00B0");

        v = UasDatalinkFactory.createValue(UasDatalinkTag.CornerLonPt2, bytes);
        Assert.assertTrue(v instanceof FullCornerLongitude);

        v = UasDatalinkFactory.createValue(UasDatalinkTag.CornerLonPt3, bytes);
        Assert.assertTrue(v instanceof FullCornerLongitude);

        v = UasDatalinkFactory.createValue(UasDatalinkTag.CornerLonPt4, bytes);
        Assert.assertTrue(v instanceof FullCornerLongitude);
    }
}
