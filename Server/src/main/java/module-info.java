// Created by leo on 26.09.17
module de.zortax.pra.server {
    requires de.zortax.pra.network;
    requires gson;
    requires java.logging;
    exports de.zortax.pra.server;
    exports de.zortax.pra.server.config;
    exports de.zortax.pra.server.events;
    exports de.zortax.pra.server.net;
    exports de.zortax.pra.server.plugin;
}