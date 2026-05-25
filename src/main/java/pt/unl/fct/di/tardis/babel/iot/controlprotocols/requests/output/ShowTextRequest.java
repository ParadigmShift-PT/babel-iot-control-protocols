package pt.unl.fct.di.tardis.babel.iot.controlprotocols.requests.output;

import pt.unl.fct.di.tardis.babel.iot.api.DeviceHandle;
import pt.unl.fct.di.tardis.babel.iot.api.requests.IoTEventRequest;

/**
 * Request asking {@code I2COutputControlProtocol} to display a text
 * string on a Grove LCD (or other text-capable display).
 *
 * @author João Brilha (j.brilha@campus.fct.unl.pt)
 * @author João Leitão (jc.leitao@fct.unl.pt)
 */
public class ShowTextRequest extends IoTEventRequest {

    /**
     * Default Babel request id used by this class. <b>ID:</b> {@value}.
     * Handler class: request/reply (shared pool). Owning protocol:
     * {@code I2COutputControlProtocol} (id 2000).
     */
    public static final short REQUEST_ID = 2006;

    private final String text;

    /**
     * @param h    the handle of the display
     * @param text the text to render
     */
    public ShowTextRequest(DeviceHandle h, String text) {
        super(ShowTextRequest.REQUEST_ID, h);
        this.text = text;
    }

    /** @return the text to render. */
    public String getText() { return this.text; }
}
