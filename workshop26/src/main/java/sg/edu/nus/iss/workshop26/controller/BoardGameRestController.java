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

    @GetMapping(path = "/games", consumes = "application/json")
    public ResponseEntity<String> getAllGames(@RequestParam int limit, @RequestParam int offset) {
        List<Game> gameList = boardgameService.getAllGames(limit, offset);
        Games games = new Games(limit, offset, gameList.size(), LocalDateTime.now(), gameList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(games.toJSON().toString());

    }

    @GetMapping(path = "/games/rank", consumes = "application/json")
    public ResponseEntity<String> getAllGamesByRanking(@RequestParam int limit, @RequestParam int offset) {
        List<Game> gameList = boardgameService.getGamesByRanking(limit, offset);
        Games games = new Games(limit, offset, gameList.size(), LocalDateTime.now(), gameList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(games.toJSON().toString());

    }

    @GetMapping(path = "/game/{gameId}")
    public ResponseEntity<String> getGameById(@PathVariable Integer gameId) {
        Game game = null;
        try {
            game = boardgameService.getGameById(gameId);

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
