package ke.co.safaricom.controllers;

import ke.co.safaricom.dao.HeroDao;
import ke.co.safaricom.dao.SquadDao;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.util.HashMap;
import java.util.Map;

public class HomeController {

    private final HeroDao heroDao;
    private final SquadDao squadDao;

    public HomeController(HeroDao heroDao, SquadDao squadDao) {
        this.heroDao = heroDao;
        this.squadDao = squadDao;
    }

    public Route getHome() {
        return (Request req, Response res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("heroes", heroDao.getAllHeroes());
            model.put("squads", squadDao.getAllSquads());
            return new ModelAndView(model, "index.hbs");
        };
    }
}
