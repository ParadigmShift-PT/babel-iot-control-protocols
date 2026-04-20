package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class SetMultipleChainableLEDColorRGBRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8026;

    private final Map<Byte,byte[]> values;

    public SetMultipleChainableLEDColorRGBRequest(DeviceHandle h) {
    	 super(REQUEST_ID, h);
         values = new TreeMap<Byte, byte[]>();
    }
    
    public SetMultipleChainableLEDColorRGBRequest(DeviceHandle h, byte idx, byte red,
                                           byte green, byte blue) {
        super(REQUEST_ID, h);
        values = new TreeMap<Byte, byte[]>();
        values.put(idx, new byte[]{red,green,blue});
    }

    public void addValuesForPosition(byte idx, byte[] values) {
    	byte[] v = new byte[3];
    	System.arraycopy(values, 0, v, 0, Math.min(values.length, v.length));
    	this.values.put(idx, v);
    }
    
    public void setValuesForPosition(byte idx, byte red, byte green, byte blue) {
    	this.values.put(idx, new byte[] {red,green,blue});
    }
    
    public void setPositionRed(byte idx, byte red) {
    	byte[] v = this.values.get(idx);
    	if(v == null) {
    		v = new byte[]{0b0,0b0,0b0};
    		this.values.put(idx, v);
    	}
    	v[0] = red;
    }
    
    public void setPositionGreen(byte idx, byte green) {
    	byte[] v = this.values.get(idx);
    	if(v == null) {
    		v = new byte[]{0b0,0b0,0b0};
    		this.values.put(idx, v);
    	}
    	v[1] = green;
    }
    
    public void setPositionBlue(byte idx, byte blue) {
    	byte[] v = this.values.get(idx);
    	if(v == null) {
    		v = new byte[]{0b0,0b0,0b0};
    		this.values.put(idx, v);
    	}
    	v[2] = blue;
    }
    
    public Iterator<Byte> getPositionsIterator() {
    	return values.keySet().iterator();
    }
    
    public byte[] getPositionValues(byte idx) {
    	return this.values.get(idx);
    }
    
    public byte getPositionRed(byte idx) {
    	return this.values.get(idx)[0];
    }
    
    public byte getPositionGreen(byte idx) {
    	return this.values.get(idx)[1];
    }
    
    public byte getPositionBlue(byte idx) {
    	return this.values.get(idx)[2];
    }
}
