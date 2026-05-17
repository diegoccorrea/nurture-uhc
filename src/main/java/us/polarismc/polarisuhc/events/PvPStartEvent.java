package us.polarismc.polarisuhc.events;

import org.bukkit.event.Cancellable;

public class PvPStartEvent extends UHCEvent implements Cancellable {

    private boolean cancel = false;

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
