package me.lucanius.edge.thread;

import me.lucanius.edge.Edge;
import me.lucanius.edge.player.PlayerTab;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
public class TabThread extends Thread {

    private final long sleepTime;
    private final Edge instance;

    public TabThread() {
        this.sleepTime = 20L * 50L;
        this.instance = Edge.getInstance();

        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                instance.getRegistered().values().forEach(PlayerTab::update);
                sleep(sleepTime);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
