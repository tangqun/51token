package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.LoginForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Api("心跳")
@RestController
@Validated
public class HomeController {

    @ApiOperation("心跳脉冲")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {

        return "1";
    }

    @ApiOperation("Header参数验证")
    @RequestMapping(value = "/header", method = RequestMethod.GET)
    public RESTful header(
            @NotBlank(message = "token不能为空")
            @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "token格式错误")
            @RequestHeader
            String token) {

        return RESTful.Success();
    }

    @ApiOperation("Get参数验证")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "id", required = true)
    })
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RESTful get(
            @NotNull(message = "ID不能位空")
            @Min(value = 10, message = "ID最小值为10")
                    Long id) {

//        if (bindingResult.hasErrors()) {
//            for (ObjectError objectError:bindingResult.getAllErrors()) {
//
//                return RESTful.Fail(CodeEnum.ParameterError, objectError.getDefaultMessage());
//            }
//        }

        return RESTful.Success();
    }



    @ApiOperation("Post参数验证")
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public RESTful post(
//            @Validated
            @Valid
            @RequestBody LoginForm loginForm) {

//        if (bindingResult.hasErrors()) {
//            for (ObjectError objectError:bindingResult.getAllErrors()) {
//
//                return RESTful.Fail(CodeEnum.ParameterError, objectError.getDefaultMessage());
//            }
//        }

        return RESTful.Success();
    }
}
