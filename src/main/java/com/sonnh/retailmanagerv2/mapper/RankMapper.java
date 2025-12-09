package com.sonnh.retailmanagerv2.mapper;

import com.sonnh.retailmanagerv2.data.domain.Rank;
import com.sonnh.retailmanagerv2.dto.request.admin.RankCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.RankUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.admin.RankDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.RankResDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RankMapper {
    RankResDto toRankResDto(Rank rank);

    RankDetailResDto toRankDetailResDto(Rank rank);


    Rank toRankEntity(RankCreateReqDto rankCreateReqDto);


//    Rank toRankUpdateEntity(RankUpdateReqDto rankUpdateReqDto);




}