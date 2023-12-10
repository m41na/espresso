package works.hop.presso.game.repo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import works.hop.presso.game.repo.pool.PgPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public class Update {

    public static void update(String query, Object[] params, BiConsumer<Throwable, Integer> result) {
        try (Connection conn = PgPool.instance().connection()) {
            update(conn, query, params, result);
        } catch (SQLException e) {
            result.accept(new RuntimeException("Problem executing database update", e), null);
        }
    }

    public static void update(UnitOfWork[] works, BiConsumer<Throwable, Integer> result) throws SQLException {
        try (Connection conn = PgPool.instance().connection()) {
            try {
                conn.setAutoCommit(false);
                for (UnitOfWork work : works) {
                    update(conn, work.query, work.params, result);
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                result.accept(new RuntimeException("Problem executing database update", e), null);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private static void update(Connection conn, String query, Object[] params, BiConsumer<Throwable, Integer> result) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            int affected = ps.executeUpdate();
            result.accept(null, affected);
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class UnitOfWork {
        final String query;
        final Object[] params;
    }
}
