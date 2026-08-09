package com.dl1803.file.mapper;

import com.dl1803.file.dto.FileInfo;
import com.dl1803.file.entity.FileMgmt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMgmtMapper {
    @Mapping(target = "id",source = "name")
    FileMgmt toFileMgmt(FileInfo fileInfo);
}
