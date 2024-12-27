package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.PmsBrandParam;
import com.macro.mall.model.PmsBrand;
import com.macro.mall.service.PmsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品品牌管理控制器
 */
@Controller
@Api(tags = "PmsBrandController")
@Tag(name = "PmsBrandController", description = "商品品牌管理")
@RequestMapping("/brand")
public class PmsBrandController {
    @Autowired
    private PmsBrandService brandService;

    @ApiOperation("获取全部品牌列表")
    @GetMapping("/listAll")
    @ResponseBody
    public CommonResult<List<PmsBrand>> getList() {
        return CommonResult.success(brandService.listAllBrand());
    }

    @ApiOperation("添加品牌")
    @PostMapping("/create")
    @ResponseBody
    public CommonResult create(@Validated @RequestBody PmsBrandParam pmsBrand) {
        int count = brandService.createBrand(pmsBrand);
        return count == 1 ? CommonResult.success(count) : CommonResult.failed();
    }

    @ApiOperation("更新品牌")
    @PostMapping("/update/{id}")
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @Validated @RequestBody PmsBrandParam pmsBrandParam) {
        int count = brandService.updateBrand(id, pmsBrandParam);
        return count == 1 ? CommonResult.success(count) : CommonResult.failed();
    }

    @ApiOperation("删除品牌")
    @GetMapping("/delete/{id}")
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        return brandService.deleteBrand(id) == 1 ? CommonResult.success(null) : CommonResult.failed();
    }

    @ApiOperation("根据品牌名称分页获取品牌列表")
    @GetMapping("/list")
    @ResponseBody
    public CommonResult<CommonPage<PmsBrand>> getList(@RequestParam(value = "keyword", required = false) String keyword,
                                                      @RequestParam(value = "showStatus", required = false) Integer showStatus,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<PmsBrand> brandList = brandService.listBrand(keyword, showStatus, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(brandList));
    }

    @ApiOperation("根据编号查询品牌信息")
    @GetMapping("/{id}")
    @ResponseBody
    public CommonResult<PmsBrand> getItem(@PathVariable Long id) {
        return CommonResult.success(brandService.getBrand(id));
    }

    @ApiOperation("批量删除品牌")
    @PostMapping("/delete/batch")
    @ResponseBody
    public CommonResult deleteBatch(@RequestParam("ids") List<Long> ids) {
        int count = brandService.deleteBrand(ids);
        return count > 0 ? CommonResult.success(count) : CommonResult.failed();
    }

    @ApiOperation("批量更新显示状态")
    @PostMapping("/update/showStatus")
    @ResponseBody
    public CommonResult updateShowStatus(@RequestParam("ids") List<Long> ids,
                                         @RequestParam("showStatus") Integer showStatus) {
        int count = brandService.updateShowStatus(ids, showStatus);
        return count > 0 ? CommonResult.success(count) : CommonResult.failed();
    }

    @ApiOperation("批量更新厂家制造商状态")
    @PostMapping("/update/factoryStatus")
    @ResponseBody
    public CommonResult updateFactoryStatus(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("factoryStatus") Integer factoryStatus) {
        int count = brandService.updateFactoryStatus(ids, factoryStatus);
        return count > 0 ? CommonResult.success(count) : CommonResult.failed();
    }
}
