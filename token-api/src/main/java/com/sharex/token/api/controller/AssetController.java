package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.AssetSyn;
import com.sharex.token.api.entity.req.AssetAuth;
import com.sharex.token.api.entity.req.AssetRmAuth;
import com.sharex.token.api.service.AssetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Api("资产")
@RequestMapping("/asset")
@RestController
@Validated
public class AssetController {

    @Autowired
    private AssetService assetService;

    // 暂定手动验证token，后期优化 父类验证token问题 / AOP
    @ApiOperation("授权列表")
    @RequestMapping(value = "/getAuthMapping", method = RequestMethod.GET)
    public RESTful getAuthMapping(@RequestHeader String token) {

        return assetService.getAuthMapping(token);
    }

    @ApiOperation("授权")
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public RESTful auth(@RequestHeader String token, @RequestBody AssetAuth assetAuth) {

        return assetService.auth(token, assetAuth);
    }

    @ApiOperation("取消授权")
    @RequestMapping(value = "/rmAuth", method = RequestMethod.POST)
    public RESTful rmAuth(@RequestHeader String token, @RequestBody AssetRmAuth assetRmAuth) {

        return assetService.rmAuth(token, assetRmAuth);
    }

    @ApiOperation("远程同步资产")
    @RequestMapping(value = "/syn", method = RequestMethod.POST)
    public RESTful syn(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader String token,
            @Valid
            @RequestBody AssetSyn assetSyn) {

        return assetService.syn(token, assetSyn);
    }

    @ApiOperation("资产聚合")
    @RequestMapping(value = "/getAsset", method = RequestMethod.GET)
    public RESTful getAsset(@RequestHeader String token) {

        return assetService.getAsset(token);
    }

    @ApiOperation("交易所资产聚合")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "exchangeName", value = "交易所", required = true)
    })
    @RequestMapping(value = "/getExchangeAsset", method = RequestMethod.GET)
    public RESTful getExchangeAsset(@RequestHeader String token, String exchangeName) {

        return assetService.getExchangeAsset(token, exchangeName);
    }
}
