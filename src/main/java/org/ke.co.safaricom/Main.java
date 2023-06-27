package org.javasparkips;

import org.javasparkips.dao.HeroDao;
import org.javasparkips.dao.HeroSquadDao;
import org.javasparkips.dao.SquadDao;
import org.javasparkips.model.Hero;
import org.javasparkips.model.Squad;
import org.javasparkips.utils.DatabaseConnector;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        // Set the static files location
        staticFileLocation("/");

        // Set up the Handlebars template engine
        HandlebarsTemplateEngine templateEngine = new HandlebarsTemplateEngine("/templates");

        // Set up the database connector
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.init();

        // Set up the HeroDao, SquadDao, and HeroSquadDao
        HeroDao heroDao = new HeroDao(databaseConnector.getSql2o());
        SquadDao squadDao = new SquadDao(databaseConnector.getSql2o());
        HeroSquadDao heroSquadDao = new HeroSquadDao(databaseConnector.getSql2o());

        // Home page route
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("heroes", heroDao.getAllHeroes());
            model.put("squads", squadDao.getActiveSquads());
            return new ModelAndView(model, "index.hbs");
        }, templateEngine);


        get("/squads", (request, response) -> {
            List<Squad> squads = squadDao.getActiveSquads();
            List<Hero> heroes = heroDao.getAllHeroes(); // Add this line
            Map<String, Object> model = new HashMap<>();
            model.put("squads", squads);
            model.put("heroes", heroes); // Add this line
            return new ModelAndView(model, "index.hbs"); // Change the template name if necessary
        }, templateEngine);

        post("/squads/add/", (req, res) -> {
            String name = req.queryParams("name");
            String maxSizeStr = req.queryParams("maxSize");
            String cause = req.queryParams("cause");
            int maxSize;

            try {
                maxSize = Integer.parseInt(maxSizeStr);
            } catch (NumberFormatException e) {
                // Handle the error here
                Map<String, Object> model = new HashMap<>();
                model.put("error", "Invalid max size value");
                return new ModelAndView(model, "error.hbs");
            }

            Squad squad = new Squad(name, maxSize, cause);
            squadDao.addSquad(squad);

            res.redirect("/");
            return null;
        });

        // Edit a squad route
        get("/squads/edit/:id", (req, res) -> {
            int squadId = Integer.parseInt(req.params(":id"));
            Squad squad = squadDao.findSquadById(squadId);

            if (squad != null) {
                Map<String, Object> model = new HashMap<>();
                model.put("squad", squad);
                model.put("id", squadId);
                return new ModelAndView(model, "squads.hbs");
            } else {
                res.redirect("/squads?error=squad_not_found");
                return null;
            }
        }, templateEngine);

        // Handle form submission for editing a squad
        post("/squads/edit/:id", (req, res) -> {
            int squadId = Integer.parseInt(req.params(":id"));
            Squad squad = squadDao.findSquadById(squadId);

            if (squad != null) {
                String name = req.queryParams("name");
                String maxSizeStr = req.queryParams("maxSize");
                String cause = req.queryParams("cause");
                int maxSize;

                try {
                    maxSize = Integer.parseInt(maxSizeStr);
                } catch (NumberFormatException e) {
                    res.redirect("/squads?error=invalid_max_size");
                    return null;
                }

                squad.setName(name);
                squad.setMaxSize(maxSize);
                squad.setCause(cause);
                squadDao.updateSquad(squadId, squad);

                res.redirect("/squads");
            } else {
                res.redirect("/squads?error=squad_not_found");
            }
            return null;
        });

        // Delete a squad route
        post("/squads/delete/:id", (req, res) -> {
            int squadId = Integer.parseInt(req.params(":id"));
            Squad squad = squadDao.findSquadById(squadId);

            if (squad != null) {
                squadDao.deleteSquad(squadId);
            }

            res.redirect("/");
            return null;
        });

        // Add a hero route
        get("/heroes/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("heroes", heroDao.getAllHeroes());
            return new ModelAndView(model, "heroes.hbs");
        }, templateEngine);

        // Handle form submission for adding a hero
        post("/heroes/add/", (req, res) -> {
            String name = req.queryParams("name");
            String ageParam = req.queryParams("age");
            String power = req.queryParams("power");
            String weakness = req.queryParams("weakness");
            String power_scoreParam = req.queryParams("power_score");
            String weakness_scoreParam = req.queryParams("weakness_score");

            int age;
            try {
                age = Integer.parseInt(ageParam);
            } catch (NumberFormatException e) {
                System.out.println("Invalid age: " + ageParam);
                return "Invalid age";
            }

            int power_score = 0; // Default value
            if (power_scoreParam != null) {
                try {
                    power_score = Integer.parseInt(power_scoreParam);
                    if (power_score < 0 || power_score > 100) {
                        power_score = 0; // Set a default value or handle the error differently
                        System.out.println("Invalid power score range: " + power_scoreParam);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid power score: " + power_scoreParam);
                    return "Invalid power score";
                }
            }

            int weakness_score = 0; // Default value
            if (weakness_scoreParam != null) {
                try {
                    weakness_score = Integer.parseInt(weakness_scoreParam);
                    if (weakness_score < 0 || weakness_score > 100) {
                        weakness_score = 0; // Set a default value or handle the error differently
                        System.out.println("Invalid weakness score range: " + weakness_scoreParam);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid weakness score: " + weakness_scoreParam);
                    return "Invalid weakness score";
                }
            }

            Hero hero = new Hero(name, age, power, weakness);
            hero.setPower_score(power_score);
            hero.setWeakness_score(weakness_score);
            heroDao.addHero(hero);

            res.redirect("/");
            return "Hero added successfully";
        });


        // Handle deleting a hero
        post("/heroes/delete/:id", (req, res) -> {
            int heroId = Integer.parseInt(req.params(":id"));
            Hero hero = heroDao.findActiveHeroById(heroId);

            if (hero != null) {
                heroDao.deleteHero(heroId);
            }

            res.redirect("/");
            return null;
        });
        // Route handler for rendering the squads page
        get("/squads", (request, response) -> {
            List<Squad> squads = squadDao.getAllSquads();
            List<Hero> availableHeroes = heroDao.getAllHeroes();
            Map<String, Object> model = new HashMap<>();
            model.put("squads", squads);
            model.put("activeHeroes", availableHeroes); // Change the key to "activeHeroes"
            return new ModelAndView(model, "squads.hbs");
        }, templateEngine);

        // Route handler for rendering the heroes page
        get("/heroes", (request, response) -> {
            List<Hero> heroes = heroDao.getAllHeroes();
            Map<String, Object> model = new HashMap<>();
            model.put("heroes", heroes);
            return new ModelAndView(model, "heroes.hbs");
        }, templateEngine);

        // Route handler for editing a hero
        get("/heroes/edit/:id", (req, res) -> {
            int heroId = Integer.parseInt(req.params(":id"));
            Hero hero = heroDao.findActiveHeroById(heroId);

            if (hero != null) {
                Map<String, Object> model = new HashMap<>();
                model.put("hero", hero);
                model.put("id", heroId);
                return new ModelAndView(model, "heroes.hbs");
            } else {
                res.redirect("/heroes?error=hero_not_found");
                return null;
            }
        }, templateEngine);

        // Handle form submission for editing a hero
        post("/heroes/edit/:id", (req, res) -> {
            int heroId = Integer.parseInt(req.params(":id"));
            Hero hero = heroDao.findActiveHeroById(heroId);

            if (hero != null) {
                String name = req.queryParams("name");
                String ageParam = req.queryParams("age");
                String power = req.queryParams("power");
                String weakness = req.queryParams("weakness");
                String power_scoreParam = req.queryParams("power_score");
                String weakness_scoreParam = req.queryParams("weakness_score");

                int age;
                try {
                    age = Integer.parseInt(ageParam);
                } catch (NumberFormatException e) {
                    res.redirect("/heroes?error=invalid_age");
                    return null;
                }

                int power_score;
                try {
                    power_score = Integer.parseInt(power_scoreParam);
                } catch (NumberFormatException e) {
                    res.redirect("/heroes?error=invalid_power_score");
                    return null;
                }

                int weakness_score;
                try {
                    weakness_score = Integer.parseInt(weakness_scoreParam);
                } catch (NumberFormatException e) {
                    res.redirect("/heroes?error=invalid_weakness_score");
                    return null;
                }

                hero.setName(name);
                hero.setAge(age);
                hero.setPower(power);
                hero.setWeakness(weakness);
                hero.setPower_score(power_score);
                hero.setWeakness_score(weakness_score);
                heroDao.updateHero(hero);

                res.redirect("/heroes");
            } else {
                res.redirect("/heroes?error=hero_not_found");
            }
            res.redirect("/");
            return "Hero Edited Successfully";
        });

        // Assign a hero to a squad
        post("/squads/:squadId/heroes/:heroId/assign", (req, res) -> {
            int squadId = Integer.parseInt(req.params("squadId"));
            int heroId = Integer.parseInt(req.queryParams("selectedHeroId"));

            Squad squad = squadDao.findSquadById(squadId);
            Hero hero = heroDao.findActiveHeroById(heroId);

            if (squad != null && hero != null) {
                // Check if the squad has reached its maximum size
                if (squad.getCurrentSize() >= squad.getMaxSize()) {
                    // Handle the error here (squad is full)
                    Map<String, Object> model = new HashMap<>();
                    model.put("error", "Squad is full");
                    return new ModelAndView(model, "error.hbs");
                }

                // Assign the hero to the squad
                hero.setSquadId(squadId);
                heroDao.assignHeroToSquad(heroId, squadId);

                // Update the current size of the squad
                squad.setCurrentSize(squad.getCurrentSize() + 1);
                squadDao.updateSquad(squadId, squad);

                res.redirect("/squads");
            } else {
                // Handle the error here (squad or hero not found)
                res.redirect("/squads?error=squad_or_hero_not_found");
            }
            return null;
        });

        // Remove a hero from a squad
        post("/squads/:squadId/heroes/:heroId/remove", (req, res) -> {
            int squadId = Integer.parseInt(req.params("squadId"));
            int heroId = Integer.parseInt(req.queryParams("selectedHeroId"));

            Squad squad = squadDao.findSquadById(squadId);
            Hero hero = heroDao.findActiveHeroById(heroId);

            if (squad != null && hero != null) {
                // Remove the hero from the squad
                hero.setSquadId(0);
                heroDao.updateHero(hero);

                // Update the current size of the squad
                squad.setCurrentSize(squad.getCurrentSize() - 1);
                squadDao.updateSquad(squadId, squad);

                res.redirect("/squads");
            } else {
                // Handle the error here (squad or hero not found)
                res.redirect("/squads?error=squad_or_hero_not_found");
            }
            return null;
        });


    }
}
