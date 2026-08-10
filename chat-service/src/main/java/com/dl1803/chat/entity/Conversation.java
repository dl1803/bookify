package com.dl1803.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conversation")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Conversation {
    @MongoId
    String id;

    String type; // group / direct

    @Indexed(unique = true)
    String participantsHash;

    List<ParticipantInfo> participants;

    Instant createdDate;
    Instant modifiedDate;
}
