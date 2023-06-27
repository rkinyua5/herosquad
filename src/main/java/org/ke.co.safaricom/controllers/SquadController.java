package ke.co.safaricom.controllers;
import ke.co.safaricom.dao.HeroDao;
import ke.co.safaricom.dao.SquadDao;
importke.co.safaricom.model.Squad;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class SquadController {
    private final SquadDao squadDao;
    private final HeroDao heroDao;

    public SquadController(SquadDao squadDao, HeroDao heroDao) {
        this.squadDao = squadDao;
        this.heroDao = heroDao;
    }

    public void setupRoutes() {
        // Home page route
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("squads", squadDao.getAllSquads());
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        // Create a new squad route
        post("/squads", (req, res) -> {
            String name = req.queryParams("name");
            int maxSize = Integer.parseInt(req.queryParams("maxSize"));
            String cause = req.queryParams("cause");

            Squad squad = new Squad(name, maxSize, cause);
            squadDao.addSquad(squad);

            res.redirect("/");
            return null;
        });

        // Assign a hero to a squad route
        post("/squads/:squadId/heroes/:heroId", (req, res) -> {
            int squadId = Integer.parseInt(req.params("squadId"));
            int heroId = Integer.parseInt(req.params("heroId"));

            squadDao.assignHeroToSquad(squadId, heroId);

            res.redirect("/");
            return null;
        });

        // Delete a squad route
        post("/squads/:id/delete", (req, res) -> {
            int squadId = Integer.parseInt(req.params("id"));
            squadDao.deleteSquad(squadId);

            res.redirect("/");
            return null;
        });

        // Update a squad route
        post("/squads/:id/update", (req, res) -> {
            int squadId = Integer.parseInt(req.params("id"));
            String name = req.queryParams("name");
            int maxSize = Integer.parseInt(req.queryParams("maxSize"));
            String cause = req.queryParams("cause");

            Squad squad = new Squad(name, maxSize, cause);
            squadDao.updateSquad(squadId, squad);

            res.redirect("/");
            return null;
        });

        // Other routes...

        // Error handling
        exception(Exception.class, (e, req, res) -> {
            e.printStackTrace();
        });
    }
}
