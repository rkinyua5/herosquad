package org.javasparkips.dao;

import org.javasparkips.model.Hero;
import org.javasparkips.model.Squad;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class SquadDao {
    private final Sql2o sql2o;
    private final HeroSquadDao heroSquadDao;

    public SquadDao(Sql2o sql2o) {
        this.sql2o = sql2o;
        this.heroSquadDao = new HeroSquadDao(sql2o);
    }

    public void addSquad(Squad squad) {
        String sql = "INSERT INTO squads (name, maxSize, cause) VALUES (:name, :maxSize, :cause)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("name", squad.getName())
                    .addParameter("maxSize", squad.getMaxSize())
                    .addParameter("cause", squad.getCause())
                    .executeUpdate();
        }
    }

    public List<Squad> getAllSquads() {
        String sql = "SELECT * FROM squads";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Squad.class);
        }
    }

    public void deleteSquad(int id) {
        String sql = "UPDATE squads SET active = false WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
    public List<Squad> getActiveSquads() {
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT * FROM squads WHERE active = true";
            return conn.createQuery(sql).executeAndFetch(Squad.class);
        }
    }
    public void updateSquad(int squadId, Squad squad) {
        try (Connection conn = sql2o.open()) {
            String sql = "UPDATE squads SET name = :name, maxSize = :maxSize, cause = :cause, active = :active WHERE id = :id";
            conn.createQuery(sql)
                    .addParameter("id", squadId)
                    .addParameter("name", squad.getName())
                    .addParameter("maxSize", squad.getMaxSize())
                    .addParameter("cause", squad.getCause())
                    .addParameter("active", squad.isActive()) // Set the active field based on squad's active status
                    .executeUpdate();
        }
    }



    public Squad findSquadById(int id) {
        String sql = "SELECT * FROM squads WHERE id = :id AND active = true";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Squad.class);
        }
    }

    public void assignHeroToSquad(int squadId, int heroId) {
        heroSquadDao.addAssignment(squadId, heroId);
    }

    public void removeHeroFromSquad(int squadId, int heroId) {
        heroSquadDao.removeAssignment(squadId, heroId);
    }

    public List<Hero> getAssignedHeroes(int squadId) {
        return heroSquadDao.getAssignedHeroes(squadId);
    }
}
