package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.AssetAuth;
import com.sharex.token.api.entity.req.AssetRmAuth;
import com.sharex.token.api.service.AssetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("资产")
@RequestMapping("/asset")
@RestController
public class AssetController {

    @Autowired
    private AssetService assetService;

    // 暂定手动验证token，后期优化 父类验证token问题 / AOP
    @ApiOperation("资产聚合")
    @RequestMapping(value = "/getAuthMapping/{token}", method = RequestMethod.GET)
    public RESTful getAuthMapping(@PathVariable String token) {

        return assetService.getAuthMapping(token);
    }

    @ApiOperation("授权")
    @RequestMapping(value = "/auth/{token}", method = RequestMethod.POST)
    public RESTful auth(@PathVariable String token, @RequestBody AssetAuth assetAuth) {

        return assetService.auth(token, assetAuth);
    }

    @ApiOperation("取消授权")
    @RequestMapping(value = "/rmAuth/{token}", method = RequestMethod.POST)
    public RESTful rmAuth(@PathVariable String token, @RequestBody AssetRmAuth assetRmAuth) {

        return assetService.rmAuth(token, assetRmAuth);
    }
}
