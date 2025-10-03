package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.data.domain.Warehouse;
import com.sonnh.retailmanagerv2.data.domain.WarehouseInventory;
import com.sonnh.retailmanagerv2.data.repository.WarehouseInventoryRepository;
import com.sonnh.retailmanagerv2.data.repository.WarehouseRepository;
import com.sonnh.retailmanagerv2.data.specification.ProductOfWarehouseReportSpecification;
import com.sonnh.retailmanagerv2.data.specification.WarehouseInventorySpecification;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ProductOfWarehouseResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseContainProductResDto;
import com.sonnh.retailmanagerv2.mapper.ProductMapper;
import com.sonnh.retailmanagerv2.mapper.WarehouseMapper;
import com.sonnh.retailmanagerv2.service.interfaces.InventoryReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryReportServiceImpl implements InventoryReportService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final WarehouseMapper warehouseMapper;
    private final ProductMapper productMapper;
    @Override
    public PageImplResDto getAllProductOfWarehouseReport(Integer page, Integer size) {
        Specification<Warehouse> spec = Specification.where((null));
        spec = spec.and(ProductOfWarehouseReportSpecification.isActive());
        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"audit.updatedAt"}));
        Page<Warehouse> warehousePage = this.warehouseRepository.findAll(spec, pageable);
        Page<ProductOfWarehouseResDto> dto = warehousePage.map(warehouseMapper::toProductOfWarehouseResDto);
        return PageImplResDto.fromPage(dto);
    }

    @Override
    public PageImplResDto getAllWarehouseOfProductReport(Integer page, Integer size) {
        Specification<WarehouseInventory> spec = Specification.where((null));
        spec = spec.and(WarehouseInventorySpecification.isActive());
        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"audit.updatedAt"}));
        Page<WarehouseInventory> warehousePage = this.warehouseInventoryRepository.findAll(spec, pageable);
        Page<WarehouseContainProductResDto> dto = warehousePage.map(productMapper::toWarehouseContainProductResDto);
        return PageImplResDto.fromPage(dto);
    }
    }

