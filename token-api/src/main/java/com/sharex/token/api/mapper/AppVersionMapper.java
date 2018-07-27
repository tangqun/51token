package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.AppVersion;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AppVersionMapper {

    List<AppVersion> selectList();
}
