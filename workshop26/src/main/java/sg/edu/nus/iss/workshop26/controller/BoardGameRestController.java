package sg.edu.nus.iss.workshop26.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import sg.edu.nus.iss.workshop26.model.Game;
import sg.edu.nus.iss.workshop26.model.Games;
import sg.edu.nus.iss.workshop26.service.BoardgameService;

@RestController
public class BoardGameRestController {

    @Autowired
    BoardgameService boardgameService;

    @GetMapping(path = "/gamesFromPage", consumes = "application/json")
    public ResponseEntity<String> getAllGamesByPage(@RequestParam int page, @RequestParam int size) {
        List<Game> gameList = boardgameService.getAllGamesByPagination(page, size);
        Games games = new Games(size, page, gameList.size(), LocalDateTime.now(), gameList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(games.toJSONByPage().toString());
    }

    @GetMapping(path = "/games", consumes = "application/json")
    public ResponseEntity<String> getAllGames(@RequestParam int limit, @RequestParam int offset) {
        List<Game> gameList = boardgameService.getAllGames(limit, offset);
        Games games = new Games(limit, offset, gameList.size(), LocalDateTime.now(), gameList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(games.toJSONByOffsetLimit().toString());

    }

    @GetMapping(path = "/games/rank", consumes = "application/json")
    public ResponseEntity<String> getAllGamesByRanking(@RequestParam int limit, @RequestParam int offset,
            @RequestParam(required = true) String direction) {
        List<Game> gameList = boardgameService.getGamesByRanking(limit, offset, direction);
        Games games = new Games(limit, offset, gameList.size(), LocalDateTime.now(), gameList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(games.toJSONByOffsetLimit().toString());

    }

    // by gid
    @GetMapping(path = "/gameGid/{gameId}")
    public ResponseEntity<String> getGameByGid(@PathVariable Integer gameId) {
        Game game = null;
        try {
            game = boardgameService.getGameByGid(gameId);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Error message", "No game found with this id.")
                            .build().toString());

        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(game.toJSON().build().toString());
    }

    // by _id
    @GetMapping(path = "/gameObj/{objectId}")
    public ResponseEntity<String> getGameByObjectId(@PathVariable String objectId) {
        Game game = null;
        try {
            game = boardgameService.getGameByObjectId(objectId);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Error message", "No game found with this id.")
                            .build().toString());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(game.toJSON().build().toString());
    }

    // by either _id or gid
    @GetMapping(path = "/game/{id}")
    public ResponseEntity<String> getGameById(@PathVariable String id) {
        Game game = null;
        try {
            game = boardgameService.getGameById(id);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Error message", "No game found with this id.")
                            .build().toString());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(game.toJSON().build().toString());
    }

    // get by year
    @GetMapping(path = "/games/year")
    public ResponseEntity<String> getGameById(@RequestParam String operator, @RequestParam int year) {
        List<Game> gameList = boardgameService.getGamesByYear(operator, year);
        Games games = new Games();
        games.setYear(year);
        games.setOperator(operator);
        games.setGamesList(gameList);

        if (gameList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Error message",
                                    "No game were found with %s %d"
                                            .formatted(operator, year))
                            .build().toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(games.toJSONByYear().toString());
    }

    // get games based on List of gid
    @PostMapping(path = "/games/listOfYear", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> getGamesByListOfGid(@RequestBody MultiValueMap<String, String> formData) {
        String yearsStr = formData.getFirst("years");
        List<String> years = boardgameService.getListFromString(yearsStr);

        List<Game> gameList = boardgameService
                .getGamesByListofYear(years.stream().map(id -> Integer.valueOf(id)).toList());

        if (gameList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Error message", "No game were found with years of %s".formatted(yearsStr))
                            .build().toString());
        }

        Games games = new Games();
        games.setYearList(years);
        games.setGamesList(gameList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(games.toJSONByYearList().toString());
    }
}