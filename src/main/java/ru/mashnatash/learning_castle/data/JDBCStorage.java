package ru.mashnatash.learning_castle.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCStorage {
    private final Connection connection;

    public JDBCStorage() {
        final Settings settings = Settings.getInstance();
        try {
            connection = DriverManager.getConnection(settings.value("jdbc.url"), "postgres", "gtxfkm");
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<UserU> values() {
        final List<UserU> users = new ArrayList<UserU>();
        try (final Statement statement = this.connection.createStatement();
             final ResultSet rs = statement.executeQuery("select * from users2")) {
            while(rs.next()) {
                users.add(new UserU(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public int add(UserU user) {
        try (final PreparedStatement statement = this.connection.prepareStatement("insert into users2 (name) values(?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1,user.getName());
            statement.executeUpdate();
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Could not create new user");
    }

    public UserU get(int id) {
        try(final PreparedStatement statement = this.connection.prepareStatement("select * from users2 where id = ?")) {
            statement.setInt(1,id);
            try (final ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    return new UserU(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("User does not exists");
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
