package com.avoris.hotel.services;

import com.avoris.hotel.models.ObjectIdGenerator;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class ObjectIdGeneratorService implements ObjectIdGenerator {
    @Override
    public String generate() {
        return new ObjectId().toHexString();
    }
}
