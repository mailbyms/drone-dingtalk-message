package com.gyjian.drone.dingtalk.message.java;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;

import java.util.logging.Logger;

public class DroneDingtalkMessageJavaApplication {

	private static Logger log = Logger.getLogger(DroneDingtalkMessageJavaApplication.class.getName());

	public static void main(String[] args) throws Exception {
		MessageTemplateUtil messageTemplate = new MessageTemplateUtil();
		// 获取所有的环境变量
		messageTemplate.getAllEnvMap();

		// 从资源文件里加载消息模板，并替换其中的值
		String dingMessage = messageTemplate.getMessage();
		//log.info(dingMessage);

		String accessToken = System.getenv("PLUGIN_TOKEN");
		if (accessToken == null || accessToken.isEmpty()){
			throw new Exception("Dingtalk access_token is empty");
		}

		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=" + accessToken);
		OapiRobotSendRequest robotRequest = new OapiRobotSendRequest();

		robotRequest.setMsgtype("markdown");
		OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
		markdown.setTitle(messageTemplate.getTitle());
		markdown.setText(dingMessage);
		robotRequest.setMarkdown(markdown);

		OapiRobotSendResponse response = client.execute(robotRequest);

		log.info("发送结果：" + JSON.toJSON(response));

		if (response.getErrcode() != 0)
			throw new Exception("Invoke dingtalk server api error");
	}

}
