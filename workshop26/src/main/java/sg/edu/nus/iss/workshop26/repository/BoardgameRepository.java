package sg.edu.nus.iss.workshop26.repository;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
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

    public List<Game> getAllGamesByPagination(int page, int size) {
        Query query = new Query();

        Pageable pageable = PageRequest.of(page, size);

        query.with(pageable);

        return mongoTemplate.find(query, Document.class, "games").stream()
                .map(d -> Game.convertFromDocument(d))
                .toList();
    }

    public List<Game> getAllGames(int limits, int offset) {
        Query query = Query.query(Criteria.where("gid").exists(true)).limit(limits).skip(offset);

        return mongoTemplate.find(query, Document.class, "games").stream()
                .map(d -> Game.convertFromDocument(d))
                .toList();
    }

    // sort games based on ascending or descending
    public List<Game> getGamesByRanking(int limit, int offset, String direction) {
        Query query = Query.query(Criteria.where("gid").exists(true)).limit(limit).skip(offset);

        if (direction.equalsIgnoreCase("asc")) {
            query.with(Sort.by(Sort.Direction.ASC, "ranking"));
        } else if (direction.equalsIgnoreCase("desc")) {
            query.with(Sort.by(Sort.Direction.DESC, "ranking"));
        }
        return mongoTemplate.find(query, Document.class, "games").stream()
                .map(d -> Game.convertFromDocument(d))
                .toList();
    }

    // by gid
    public Game getGameByGid(Integer id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gid").is(id));

        // IMPORTANT Since we direct create a game object from query, thus the fields
        // within the class must be
        // similar name with the attributes in database, if not will be null

        Game game = mongoTemplate.findOne(query, Game.class, "games");
        System.out.println("Game from repo >>>>>>>>>>>>>>>>>>>>> " + game.toString());
        return game;
    }

    // by _id
    public Optional<Document> getGameByObjectId(String id) {
        ObjectId objectId = new ObjectId(id);
        return Optional.ofNullable(mongoTemplate.findById(objectId, Document.class, "games"));
    }

    // by either gid or _id
    public Optional<Document> getGameById(String id) {
        if (ObjectId.isValid(id)) {
            return Optional.ofNullable(mongoTemplate.findById(id, Document.class, "games"));
        }
        Query query = Query.query(Criteria.where("gid").is(Integer.parseInt(id)));
        return mongoTemplate.find(query, Document.class, "games").stream().findFirst();

    }

}
