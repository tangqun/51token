package com.sharex.token.api.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AliSMSUtil {

    //产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    private static final String accessKeyId = "LTAI8Frn7f4AkYmX";
    private static final String accessKeySecret = "bmx4EzUrws34SnUl6JZxroYYnioK3f";

    private static Log logger = LogFactory.getLog(AliSMSUtil.class);

    /**
     *
     * @author 唐群
     * @param mobileNum 手机号
     * @param smsCode 短信验证码
     * @param outId 传入SMSCode表主键用户查询短信状态
     * @return 参考：https://help.aliyun.com/document_detail/55284.html?spm=5176.10629532.106.1.556d1cbeEH5uAc
     */
    public static Boolean send(String mobileNum, String smsCode, String outId) {

        try {

            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(mobileNum);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("阿里云短信测试专用");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_139545093");
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//            request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
            request.setTemplateParam("{\"code\":\"" + smsCode + "\"}");

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId(outId);

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            if ("OK".equals(sendSmsResponse.getCode())) {
                return true;
            }

            return false;
        } catch (ClientException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
