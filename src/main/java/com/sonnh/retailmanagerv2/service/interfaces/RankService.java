package com.sonnh.retailmanagerv2.service.interfaces;

import com.sonnh.retailmanagerv2.dto.request.admin.RankCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.RankUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.RankDetailResDto;

import java.util.UUID;

public interface RankService {
    public PageImplResDto getAllRank(String name, Integer page, Integer size);

    public RankDetailResDto getRankById(UUID rankId);

    public String createRank(RankCreateReqDto dto);

    public String updateRank(UUID rankId,RankUpdateReqDto dto);

    public String deleteRank(UUID rankId);
}
