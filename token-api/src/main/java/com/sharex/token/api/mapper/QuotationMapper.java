package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.Quotation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QuotationMapper {

    Quotation selectRandom();
}
