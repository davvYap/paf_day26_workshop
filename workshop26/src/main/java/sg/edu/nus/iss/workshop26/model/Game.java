package sg.edu.nus.iss.workshop26.model;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

public class Game {
    private Integer gid;
    private String name;
    private Integer year;
    private Integer ranking;
    private Integer users_rated;
    private String url;
    private String image;

    public Game() {
    }

    public Game(Integer gameId, String name, Integer year, Integer ranking, Integer userRating, String url,
            String image) {
        this.gid = gameId;
        this.name = name;
        this.year = year;
        this.ranking = ranking;
        this.users_rated = userRating;
        this.url = url;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getUsers_rated() {
        return users_rated;
    }

    public void setUsers_rated(Integer user_rated) {
        this.users_rated = user_rated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Game [gid=" + gid + ", name=" + name + ", year=" + year + ", ranking=" + ranking + ", userRating="
                + users_rated + ", url=" + url + ", image=" + image + "]";
    }

    public JsonObjectBuilder toJSON() {
        return Json.createObjectBuilder()
                .add("game_id", this.gid)
                .add("name", this.name)
                .add("year", this.year)
                .add("ranking", this.ranking)
                .add("user_rated", this.users_rated)
                .add("url", this.url)
                .add("thumbnail", this.image);
    }

    public static Game convertFromJSON(Document d) {
        Game game = new Game();
        game.setGid(d.getInteger("gid"));
        game.setName(d.getString("name"));
        game.setYear(d.getInteger("year"));
        game.setRanking(d.getInteger("ranking"));
        game.setUsers_rated(d.getInteger("users_rated"));
        game.setUrl(d.getString("url"));
        game.setImage(d.getString("image"));
        return game;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

}
