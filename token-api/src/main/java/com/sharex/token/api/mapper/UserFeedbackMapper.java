package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.UserFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserFeedbackMapper {

    void insert(UserFeedback userFeedback);
}
