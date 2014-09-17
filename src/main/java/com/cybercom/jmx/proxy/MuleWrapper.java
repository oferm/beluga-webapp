package com.cybercom.jmx.proxy;

import java.io.IOException;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/20/13
 * Time: 4:16 PM
 */
public interface MuleWrapper {

    int getJavaPID() throws IOException;

    String getVersion() throws IOException;

    void restart() throws IOException;

    void stop() throws IOException;
}
