package de.zortax.pra.network.messages;//  Created by leo on 26.09.17.

import jdk.internal.jline.internal.Nullable;

import java.util.logging.Logger;

public interface MessageAdapter {

    void initialize(@Nullable  Logger logger);
    String getMessage(String key, String lang);

}
