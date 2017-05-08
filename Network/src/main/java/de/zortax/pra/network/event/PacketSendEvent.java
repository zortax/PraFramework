package de.zortax.pra.network.event;//  Created by Leo on 29.04.2017.

import de.zortax.pra.network.PraPacket;
import de.zortax.pra.network.api.Client;

public class PacketSendEvent implements Event, Cancellable {

    private boolean cancelled;
    private PraPacket packet;
    private Client target;

    public PacketSendEvent(PraPacket packet, Client target) {
        this.packet = packet;
        this.cancelled = false;
        this.target = target;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public PraPacket getPacket() {
        return packet;
    }

    public void setPacket(PraPacket packet) {
        this.packet = packet;
    }

    public Client getTargetClient() {
        return target;
    }
}
