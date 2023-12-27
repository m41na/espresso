package works.hop.presso.game.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import works.hop.presso.game.repo.pool.PgPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiConsumer;

public class Select {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static void select(String query, Object[] params, BiConsumer<Throwable, Object> result) {
        try (Connection conn = PgPool.instance().connection()) {
            select(conn, query, params, result);
        } catch (SQLException e) {
            result.accept(new RuntimeException("Problem executing database query", e), null);
        } catch (JsonProcessingException e) {
            result.accept(new RuntimeException("Problem parsing query results", e), null);
        }
    }

    private static void select(Connection conn, String query, Object[] params, BiConsumer<Throwable, Object> result) throws SQLException, JsonProcessingException {
        String queryString = String.format("select json_agg(result.*) as json from (%s) as result", query);
        try (PreparedStatement ps = conn.prepareStatement(queryString)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String json = rs.getString("json");
                    if (json != null) {
                        List<Object> jsonObj = objectMapper.readValue(json, new TypeReference<>() {
                        });
                        result.accept(null, jsonObj);
                    } else {
                        result.accept(new RuntimeException("Entity not found"), null);
                    }
                }
            }
        }
    }
}
