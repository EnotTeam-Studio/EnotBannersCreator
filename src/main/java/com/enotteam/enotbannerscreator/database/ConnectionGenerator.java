package com.enotteam.enotbannerscreator.database;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import org.bukkit.configuration.Configuration;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionGenerator {

    private final File dataFolder;
    private final Configuration config;

    public ConnectionGenerator(File dataFolder, Configuration config) {
        this.dataFolder = dataFolder;
        this.config = config;
    }

    public JdbcPooledConnectionSource getConnectionSource() throws SQLException {

        switch (config.getString("database.type").toLowerCase()) {

            case "sqlite":
                return new JdbcPooledConnectionSource(
                        "jdbc:sqlite:" + this.dataFolder + File.separator + config.getString("database.sqlite.file"));

            case "mysql":
                return new JdbcPooledConnectionSource(
                        String.format("jdbc:mysql://%s:%d/%s%s",
                                config.getString("database.mysql.host"),
                                config.getInt("database.mysql.port"),
                                config.getString("database.mysql.name"),
                                formatParameters(config.getStringList("database.mysql.params"))),
                        config.getString("database.mysql.user"),
                        config.getString("database.mysql.password"));

            case "postgresql":
                return new JdbcPooledConnectionSource(String.format("jdbc:postgresql://%s:%d/%s%s",
                        config.getString("database.postgresql.host"),
                        config.getInt("database.postgresql.port"),
                        config.getString("database.postgresql.name"),
                        formatParameters(config.getStringList("database.postgresql.params"))),
                        config.getString("database.postgresql.user"),
                        config.getString("database.postgresql.password"));

        }

        return null;

    }

    private String formatParameters(List<String> parameters) {
        if(parameters == null || parameters.isEmpty())
            return "";

        String joined = parameters.stream()
                .map(this::urlEncode)
                .collect(Collectors.joining("&"));

        return "?" + joined;
    }

    private String urlEncode(String source) {
        try {
            return URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}

