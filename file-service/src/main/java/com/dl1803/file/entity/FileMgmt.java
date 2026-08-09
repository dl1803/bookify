package com.dl1803.file.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "file_mgmt")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FileMgmt {
    @MongoId
    String id;
    String ownerId;
    String contentType;
    long size;
    String path;
    String md5Checksum;
}

