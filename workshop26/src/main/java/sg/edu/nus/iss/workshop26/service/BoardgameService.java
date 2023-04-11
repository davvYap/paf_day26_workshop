package sg.edu.nus.iss.workshop26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.workshop26.model.Game;
import sg.edu.nus.iss.workshop26.repository.BoardgameRepository;

@Service
public class BoardgameService {

    @Autowired
    BoardgameRepository boardgameRepository;

    public List<Game> getAllGames(int limit, int offset) {
        return boardgameRepository.getAllGames(limit, offset);
    }

    public List<Game> getGamesByRanking(int limit, int offset) {
        return boardgameRepository.getGamesByRanking(limit, offset);
    }

    public Game getGameById(Integer gameId) {
        return boardgameRepository.getGameById(gameId);
    }
}
