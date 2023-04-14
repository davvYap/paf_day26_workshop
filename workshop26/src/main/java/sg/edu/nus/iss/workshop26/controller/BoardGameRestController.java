package sg.edu.nus.iss.workshop26.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
