package com.enotteam.enotbannerscreator.database.cache;

import com.enotteam.enotbannerscreator.database.DatabaseManager;
import com.enotteam.enotbannerscreator.database.model.PlayerModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerCache {

    DatabaseManager databaseManager;

    ConcurrentHashMap<String, PlayerModel> models = new ConcurrentHashMap<>();

    public boolean isLoaded(String playerName) {
        return models.containsKey(playerName);
    }

    public PlayerModel get(String playerName) {
        return models.get(playerName);
    }

    public void set(PlayerModel playerModel) {
        models.put(playerModel.getPlayerName(), playerModel);
    }

    public CompletableFuture<PlayerModel> load(String playerName) {
        CompletableFuture<PlayerModel> future = databaseManager.createPlayerModelIfNotExists(new PlayerModel(playerName));
        future.thenAccept(model -> models.put(playerName, model));
        return future;
    }

    public void save(String playerName) {
        databaseManager.createOrUpdatePlayerModel(models.get(playerName));
    }

    public void saveAndUnload(String playerName) {
        databaseManager.createOrUpdatePlayerModel(models.get(playerName)).thenAccept(b -> models.remove(playerName));
    }

}
