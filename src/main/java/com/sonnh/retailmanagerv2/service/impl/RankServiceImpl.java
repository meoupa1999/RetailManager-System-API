package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.data.domain.Rank;
import com.sonnh.retailmanagerv2.data.domain.Store;
import com.sonnh.retailmanagerv2.data.repository.RankRepository;
import com.sonnh.retailmanagerv2.data.specification.RankSpecification;
import com.sonnh.retailmanagerv2.data.specification.StoreSpecification;
import com.sonnh.retailmanagerv2.dto.request.admin.RankCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.RankUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.RankDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.RankResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import com.sonnh.retailmanagerv2.mapper.RankMapper;
import com.sonnh.retailmanagerv2.service.interfaces.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {
    private final RankRepository rankRepository;
    private final RankMapper rankMapper;
    @Override
    public PageImplResDto getAllRank(String name, Integer page, Integer size) {
        Specification<Rank> spec = Specification.where((null));
        if (StringUtils.hasText(name)) {
            spec = spec.and(RankSpecification.nameContains(name));
        }
        spec = spec.and(RankSpecification.isActive());
        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"audit.updatedAt"}));
        Page<Rank> rankPage = this.rankRepository.findAll(spec, pageable);
        Page<RankResDto> dto = rankPage.map(rankMapper::toRankResDto);
        return PageImplResDto.fromPage(dto);
    }

    @Override
    public RankDetailResDto getRankById(UUID rankId) {
        Rank rank = rankRepository.findById(rankId).get();
        return Optional.ofNullable(rank).map(rankMapper::toRankDetailResDto).get();
    }

    @Override
    @Transactional
    public String createRank(RankCreateReqDto dto) {
        Rank rank = Optional.ofNullable(dto).map(rankMapper::toRankEntity).get();
        rankRepository.save(rank);
        return "Create Success";
    }

    @Override
    @Transactional
    public String updateRank(UUID rankId,RankUpdateReqDto dto) {
        Rank rank = rankRepository.findById(rankId).get();
        if (!Objects.isNull(dto.getName()))
            rank.setName(dto.getName());
        if (!Objects.isNull(dto.getDiscountPercent()))
            rank.setDiscountPercent(dto.getDiscountPercent());
        if (!Objects.isNull(dto.getSpendingThreshold()))
            rank.setSpendingThreshold(dto.getSpendingThreshold());
        return "Update Success";
    }

    @Override
    @Transactional
    public String deleteRank(UUID rankId) {
        Rank rank = rankRepository.findById(rankId).get();
        rank.getAudit().setIsActive(false);
        return "Delete Successful";
    }
}
