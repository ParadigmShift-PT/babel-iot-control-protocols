package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

public class SetMultipleChainableLEDColorHSBRequest extends IoTEventRequest {

    public static final short REQUEST_ID = 8025;

    private final Map<Byte,float[]> values;

    public SetMultipleChainableLEDColorHSBRequest(DeviceHandle h) {
    	super(REQUEST_ID, h);
    	values = new TreeMap<Byte, float[]>();
    }
    
    public SetMultipleChainableLEDColorHSBRequest(DeviceHandle h, byte idx, float hue,
                                           float saturation, float brightness) {
        super(REQUEST_ID, h);
        values = new TreeMap<Byte, float[]>();
        values.put(idx, new float[]{hue, saturation, brightness});
       
    }

    public void addValuesForPosition(byte idx, float[] values) {
    	float[] v = new float[3];
    	System.arraycopy(values, 0, v, 0, Math.min(values.length, v.length));
    	this.values.put(idx, v);
    }
    
    public void addValuesForPosition(byte idx, float hue, float saturation, float brightness) {
    	this.values.put(idx, new float[] {hue, saturation, brightness});
    }
    
    public void setPositionHue(byte idx, float hue) {
    	float[] v = this.values.get(idx);
    	if(v == null) {
    		v = new float[]{0f,0f,0f};
    		this.values.put(idx, v);
    	}
    	v[0] = hue;
    }
    
    public void setPositionSaturation(byte idx, float saturation) {
    	float[] v = this.values.get(idx);
    	if(v == null) {
    		v = new float[]{0f,0f,0f};
    		this.values.put(idx, v);
    	}
    	v[1] = saturation;
    }
    
    public void setPositionBrightness(byte idx, float brightness) {
    	float[] v = this.values.get(idx);
    	if(v == null) {
    		v = new float[]{0f,0f,0f};
    		this.values.put(idx, v);
    	}
    	v[2] = brightness;
    }
    
    public Iterator<Byte> getPositionsIterator() {
    	return values.keySet().iterator();
    }
    
    public float[] getPositionValues(byte idx) {
    	return this.values.get(idx);
    }
    
    public float getPositionHue(byte idx) {
    	return this.values.get(idx)[0];
    }
    
    public float getPositionSaturation(byte idx) {
    	return this.values.get(idx)[1];
    }
    
    public float getPositionBrightness(byte idx) {
    	return this.values.get(idx)[2];
    }
}
