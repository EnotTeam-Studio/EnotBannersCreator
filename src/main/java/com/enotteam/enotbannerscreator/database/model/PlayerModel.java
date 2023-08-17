package com.enotteam.enotbannerscreator.database.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@DatabaseTable(tableName = "enotbannerscreator_player_data")
public class PlayerModel {

    public static final String COLUMN_PLAYER_NAME = "player_name";
    public static final String COLUMN_SAVED_BANNERS = "saved_banners";

    @DatabaseField(columnName = COLUMN_PLAYER_NAME, id = true, canBeNull = false)
    private String playerName;
    @DatabaseField(columnName = COLUMN_SAVED_BANNERS, dataType = DataType.SERIALIZABLE)
    private HashSet<String> savedBanners;

    public PlayerModel(String playerName) {
        this.playerName = playerName;
        this.savedBanners = new HashSet<>();
    }

}
