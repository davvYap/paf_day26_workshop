package sg.edu.nus.iss.workshop26.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.workshop26.model.Game;

@Repository
public class BoardgameRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<Game> getAllGames(int limit, int offset) {
        Query query = new Query();

        Pageable pageable = PageRequest.of(offset, limit);

        query.with(pageable);

        return mongoTemplate.find(query, Document.class, "games").stream()
                .map(d -> Game.convertFromJSON(d))
                .toList();
    }

    public List<Game> getGamesByRanking(int limit, int offset) {
        Query query = new Query();

        Pageable pageable = PageRequest.of(offset, limit);

        query.with(pageable);
        query.with(Sort.by(Sort.Direction.ASC, "ranking"));
        return mongoTemplate.find(query, Document.class, "games").stream()
                .map(d -> Game.convertFromJSON(d))
                .toList();
    }

    public Game getGameById(Integer id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gid").is(id));

        // IMPORTANT Since we direct create a game object from query, thus the fields
        // within the class must be
        // similar name with the attributes in database, if not will be null

        Game game = mongoTemplate.findOne(query, Game.class, "games");
        System.out.println("Game from repo >>>>>>>>>>>>>>>>>>>>> " + game.toString());
        return game;
    }
}
