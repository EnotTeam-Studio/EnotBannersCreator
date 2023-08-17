package com.enotteam.enotbannerscreator.database;

import com.enotteam.enotbannerscreator.database.model.PlayerModel;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {

    private final Dao<PlayerModel, String> playersDao;

    public DatabaseManager(ConnectionGenerator connectionGenerator) throws SQLException {

        ConnectionSource connection = connectionGenerator.getConnectionSource();

        this.playersDao = DaoManager.createDao(connection, PlayerModel.class);

        TableUtils.createTableIfNotExists(connection, PlayerModel.class);

    }

    /*
     * PlayerModel
     */

    public CompletableFuture<PlayerModel> createPlayerModelIfNotExists(PlayerModel playerModel) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return playersDao.createIfNotExists(playerModel);
            } catch (SQLException e) {
                return null;
            }
        });
    }

    public CompletableFuture<Boolean> createOrUpdatePlayerModel(PlayerModel playerModel) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                playersDao.createOrUpdate(playerModel);
                return true;
            } catch (SQLException e) {
                return false;
            }
        });
    }

    public CompletableFuture<List<PlayerModel>> getAllPlayerModels() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                QueryBuilder<PlayerModel, String> builder = playersDao.queryBuilder();
                return playersDao.query(builder.prepare());
            } catch (SQLException e) {
                return Collections.emptyList();
            }
        });
    }

}
