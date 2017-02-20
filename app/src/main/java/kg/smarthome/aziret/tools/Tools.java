package kg.smarthome.aziret.tools;

import android.widget.Toast;

import java.io.OutputStream;
import java.io.Serializable;

/**
 * Created by Aziret on 18.02.2017.
 */

public class Tools implements Serializable {
    private OutputStream outputStream;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

}
