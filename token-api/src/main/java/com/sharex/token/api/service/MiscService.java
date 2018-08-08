package com.sharex.token.api.service;

import com.sharex.token.api.entity.AppEx;
import com.sharex.token.api.entity.AppVersion;
import com.sharex.token.api.entity.Quotation;
import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.MiscRevAppEx;
import com.sharex.token.api.entity.resp.AppVersionResp;
import com.sharex.token.api.entity.resp.QuotationResp;
import com.sharex.token.api.mapper.AppExMapper;
import com.sharex.token.api.mapper.AppVersionMapper;
import com.sharex.token.api.mapper.QuotationMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MiscService {

    private static final Log logger = LogFactory.getLog(MiscService.class);

    @Autowired
    private AppVersionMapper appVersionMapper;

    @Autowired
    private AppExMapper appExMapper;

    @Autowired
    private QuotationMapper quotationMapper;

    public RESTful revAppEx(MiscRevAppEx miscRevAppEx) {
        try {

            // 暂无字段需要验证

            Date date = new Date();

            AppEx appEx = new AppEx();
            appEx.setMobileSeries(miscRevAppEx.getMobileSeries());
            appEx.setOsVersion(miscRevAppEx.getOsVersion());
            appEx.setOccurTime(miscRevAppEx.getOccurTime());
            appEx.setMessage(miscRevAppEx.getMessage());
            appEx.setCreateTime(date);

            appExMapper.insert(appEx);

            return RESTful.Success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }


    public RESTful getAppVersion() {
        try {

            List<AppVersionResp> appVersionRespList = new ArrayList<>();

            List<AppVersion> appVersionList = appVersionMapper.selectList();
            for (AppVersion appVersion: appVersionList) {
                AppVersionResp appVersionResp = new AppVersionResp();
                appVersionResp.setPlatformName(appVersion.getPlatformName());
                appVersionResp.setVersion(appVersion.getVersion());
                appVersionResp.setDownloadUrl(appVersion.getDownloadUrl());

                appVersionRespList.add(appVersionResp);
            }

            return RESTful.Success(appVersionRespList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    public RESTful getQuotation() {
        try {

            QuotationResp quotationResp = new QuotationResp();

            Quotation quotation = quotationMapper.selectRandom();
            if (quotation != null) {
                quotationResp.setContent(quotation.getContent());
                quotationResp.setAuthor(quotation.getAuthor());
            }

            return RESTful.Success(quotationResp);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }
}
