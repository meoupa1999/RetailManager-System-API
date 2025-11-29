package com.sonnh.retailmanagerv2.controller.admin;

import com.sonnh.retailmanagerv2.dto.request.admin.RankCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.RankUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.RankDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.RankResDto;
import com.sonnh.retailmanagerv2.service.interfaces.RankService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Rank (Admin)")
@RequestMapping("/api/admin")
public class AdminRankController {
    private RankService rankService;

    public AdminRankController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping({"/getAllRank"})
    public ResponseEntity<PageImplResDto<RankResDto>> getAllRank(@RequestParam(required = false) String name, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "100") Integer size) {
        PageImplResDto<RankResDto> result = rankService.getAllRank(name, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/getRankById/{rankId}"})
    public ResponseEntity<RankDetailResDto> getRankById(@PathVariable("rankId") UUID id) {
        RankDetailResDto result = this.rankService.getRankById(id);
        return ResponseEntity.ok(result);
    }
    @PostMapping({"/createRank"})
    public String createRank(@RequestBody RankCreateReqDto dto) {
        return rankService.createRank(dto);
    }

    @PutMapping({"/updateRank/{rankId}"})
    public String updateRank(@PathVariable("rankId") UUID rankId,@RequestBody RankUpdateReqDto dto) {
        return rankService.updateRank(rankId,dto);
    }

    @DeleteMapping({"/deleteRank/{rankId}"})
    public String deleteRank(@PathVariable("rankId") UUID rankId) {
        return rankService.deleteRank(rankId);
    }

}
