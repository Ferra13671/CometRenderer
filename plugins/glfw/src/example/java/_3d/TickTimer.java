package _3d;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TickTimer {
    @Getter
    private final int tickDelay = 20;
    private long lastTickTime = System.currentTimeMillis();

    public boolean shouldTick() {
        return System.currentTimeMillis() - this.lastTickTime >= this.tickDelay;
    }

    public void reset() {
        this.lastTickTime = System.currentTimeMillis();
    }
}
