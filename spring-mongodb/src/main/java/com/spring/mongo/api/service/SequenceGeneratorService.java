package com.spring.mongo.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.spring.mongo.api.model.DbSequence;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import java.util.Objects;

@Service
public class SequenceGeneratorService {

    @Autowired
    public MongoOperations mongo;

    public int getSequenceNum(String seqName) {
        Query query=new Query(Criteria.where("id").is(seqName));
        Update update = new Update().inc("seq", 1);
        DbSequence counter= mongo.findAndModify(query, update, options().returnNew(true).upsert(true), DbSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
