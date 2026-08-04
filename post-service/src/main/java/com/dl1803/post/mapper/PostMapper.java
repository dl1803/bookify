package com.dl1803.post.mapper;

import com.dl1803.post.dto.response.PostResponse;
import com.dl1803.post.entity.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
    List<PostResponse> toListPostResponse(List<Post> list);
}
