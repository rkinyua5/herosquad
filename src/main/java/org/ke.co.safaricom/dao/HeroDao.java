package org.javasparkips.dao;

import org.javasparkips.model.Hero;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;

public class HeroDao {
    private final Sql2o sql2o;

    public HeroDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<Hero> getAllHeroes() {
        List<Hero> heroes = new ArrayList<>();
        try (Connection connection = sql2o.open()) {
            String query = "SELECT * FROM heroes WHERE active = true";
            heroes = connection.createQuery(query).executeAndFetch(Hero.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return heroes;
    }

    public void addHero(Hero hero) {
        try (Connection connection = sql2o.open()) {
            String query = "INSERT INTO heroes (name, age, power, weakness, power_score, weakness_score) " +
                    "VALUES (:name, :age, :power, :weakness, :power_score, :weakness_score)";
            connection.createQuery(query)
                    .addParameter("name", hero.getName())
                    .addParameter("age", hero.getAge())
                    .addParameter("power", hero.getPower())
                    .addParameter("weakness", hero.getWeakness())
                    .addParameter("power_score", hero.getPower_score())
                    .addParameter("weakness_score", hero.getWeakness_score())
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateHero(Hero hero) {
        try (Connection connection = sql2o.open()) {
            String query = "UPDATE heroes SET name = :name, age = :age, power = :power, weakness = :weakness, " +
                    "power_score = :power_score, weakness_score = :weakness_score WHERE id = :id";
            connection.createQuery(query)
                    .addParameter("name", hero.getName())
                    .addParameter("age", hero.getAge())
                    .addParameter("power", hero.getPower())
                    .addParameter("weakness", hero.getWeakness())
                    .addParameter("power_score", hero.getPower_score())
                    .addParameter("weakness_score", hero.getWeakness_score())
                    .addParameter("id", hero.getId())
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteHero(int id) {
        try (Connection connection = sql2o.open()) {
            String query = "UPDATE heroes SET active = false WHERE id = :id";
            connection.createQuery(query)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Hero findActiveHeroById(int id) {
        Hero hero = null;
        try (Connection connection = sql2o.open()) {
            String query = "SELECT * FROM heroes WHERE id = :id AND active = true";
            hero = connection.createQuery(query)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Hero.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hero;
    }
    public void assignHeroToSquad(int heroId, int squadId) {
        try (Connection connection = sql2o.open()) {
            String query = "UPDATE heroes SET squadId = :squadId WHERE id = :heroId";
            connection.createQuery(query)
                    .addParameter("squadId", squadId)
                    .addParameter("heroId", heroId)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeHeroFromSquad(int heroId) {
        try (Connection connection = sql2o.open()) {
            String query = "UPDATE heroes SET squadId = NULL WHERE id = :heroId";
            connection.createQuery(query)
                    .addParameter("heroId", heroId)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
