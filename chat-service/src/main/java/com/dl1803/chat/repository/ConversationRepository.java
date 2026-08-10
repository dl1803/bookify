package com.dl1803.chat.repository;

import com.dl1803.chat.entity.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Optional<Conversation> findByParticipantsHash(String hash);

    @Query("{'participants.userId': ?0}") //báo cho Spring biết hãy query data bằng câu lệnh tìm kiếm bên trong "{}"
    // tìm userId bên trong List participants trong Conversation, ?0(biến hứng input data) đại diện cho tham số đầu tiên truyền vào hàm
    List<Conversation> findAllByParticipantIdsContains(String userId);

}
