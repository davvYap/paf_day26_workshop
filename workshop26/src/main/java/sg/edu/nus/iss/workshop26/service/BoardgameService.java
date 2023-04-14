package sg.edu.nus.iss.workshop26.service;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.workshop26.model.Game;
import sg.edu.nus.iss.workshop26.repository.BoardgameRepository;

@Service
public class BoardgameService {

    @Autowired
    BoardgameRepository boardgameRepository;

    public List<Game> getAllGamesByPagination(int page, int size) {
        return boardgameRepository.getAllGamesByPagination(page, size);
    }

    public List<Game> getAllGames(int limit, int offset) {
        return boardgameRepository.getAllGames(limit, offset);
    }

    public List<Game> getGamesByRanking(int limit, int offset, String direction) {
        return boardgameRepository.getGamesByRanking(limit, offset, direction);
    }

    public Game getGameByGid(Integer gameId) {
        return boardgameRepository.getGameByGid(gameId);
    }

    public Game getGameByObjectId(String id) {
        if (boardgameRepository.getGameByObjectId(id).isEmpty()) {
            return null;
        }
        return Game.convertFromDocument(boardgameRepository.getGameByObjectId(id).get());
    }

    public Game getGameById(String id) {
        if (boardgameRepository.getGameById(id).isEmpty()) {
            return null;
        }
        return Game.convertFromDocument(boardgameRepository.getGameById(id).get());
    }
}
