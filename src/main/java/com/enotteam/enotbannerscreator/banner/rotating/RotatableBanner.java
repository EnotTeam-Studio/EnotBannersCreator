package com.enotteam.enotbannerscreator.banner.rotating;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.Location;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RotatableBanner {

    BlockState blockState;
    BannerRotateManager bannerRotateManager;

    @NonFinal
    int taskId;

    public RotatableBanner(BlockState blockState, BannerRotateManager bannerRotateManager) {
        this.blockState = blockState;
        this.bannerRotateManager = bannerRotateManager;
    }

    public void startRotating(JavaPlugin plugin) {

       taskId = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {

            if (!isBanner(blockState.getLocation())) {
                cancelRotatingAndRemove(plugin);
                return;
            }

            blockState.setRawData(getNextRotation(blockState.getRawData()));
            blockState.update();

        }, 0, 10L).getTaskId();

    }

    private byte getNextRotation(byte current) {
        if (current > 14) return 0;
        return (byte) (current + 1);
    }

    private void cancelRotatingAndRemove(JavaPlugin plugin) {
        plugin.getServer().getScheduler().cancelTask(taskId);
    }

    private boolean isBanner(Location location) {
        return location.getBlock().getState() instanceof Banner;
    }

}
