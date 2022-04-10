import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.commons.configuration.ConfigurationException;

import java.util.HashMap;

/**
 * 阿里云机器人
 * 参考https://www.alibabacloud.com/zh/product/bot
 */
public class BeeRot {
    private String sessionId = "";
    public static HashMap<String, BeeRot> beeBotMap = new HashMap<>();

    /**
     * @param sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return String
     */
    public  String getSessionId()
    {
        return this.sessionId;
    }

    /**
     * @param clientId
     * @return BeeBot
     */
    public static BeeRot getBeeBot(String clientId) {
        System.out.println("clientId:" + clientId);
        if (beeBotMap.containsKey(clientId)) {
            return beeBotMap.get(clientId);
        } else {
            BeeRot beeRot = new BeeRot();
            beeBotMap.put(clientId, beeRot);
            return beeRot;
        }
    }

    public String  dealRequest(String utterance) throws ClientException, InterruptedException, ConfigurationException {
//        Thread.sleep(2000);
        Logger.info("user input:" + utterance);
        String accountAccessAK = Config.get("aliyun.ak");
        String accountAccessSK = Config.get("aliyun.sk");
        String popRegion = "cn-shanghai";
        String popProduct = "Chatbot";
        String popDomain = "chatbot.cn-shanghai.aliyuncs.com";
        DefaultProfile.addEndpoint(popRegion, popProduct, popDomain);
        IClientProfile profile = DefaultProfile.getProfile(popRegion, accountAccessAK, accountAccessSK);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        //固定入参
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setSysProduct("Chatbot");
        commonRequest.setSysMethod(MethodType.GET);
        //根据API会有变化
        commonRequest.setSysAction("Chat");
        commonRequest.setSysVersion("2017-10-11");
        commonRequest.putQueryParameter("Utterance", utterance);
        commonRequest.putQueryParameter("SenderId", "1");
        commonRequest.putQueryParameter("SenderNick", "许胜斌");
        if (this.getSessionId().length() >0 ) {
            commonRequest.putQueryParameter("SessionId", this.getSessionId());
        }
        //机器人id
        commonRequest.putQueryParameter("InstanceId", "chatbot-cn-NeiNYvayVf");
        CommonResponse commonResponse = client.getCommonResponse(commonRequest);
        JSONObject jsonObject = JSONObject.parseObject(commonResponse.getData());
        String sessionId = jsonObject.getString("SessionId");
        this.setSessionId(sessionId);
        Logger.info("sessionId:" + sessionId);
        String responseTxt = jsonObject.getJSONArray("Messages").getJSONObject(0).getJSONObject("Text").getString("Content");
        Logger.info("response text:" + responseTxt);
        return responseTxt;
    }
}
