package com.gyjian.drone.dingtalk.message.java;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MessageTemplateUtil {
    private static Logger log = Logger.getLogger(DroneDingtalkMessageJavaApplication.class.getName());

    // 用来保存所有需要使用的变量/环境变量
    private Map<String, Object> allEnvMap = new HashMap<String, Object>();

    public String[] tpl = {    // 消息模板里以下变量，需要获取
            "TPL_STATUS_COLOR",
            "DRONE_REPO",
            "DRONE_REPO_NAME",
            "DRONE_BUILD_STATUS",
            "DRONE_STAGE_STARTED",
            "DRONE_STAGE_FINISHED",  // TPL_BUILD_CONSUMING = DRONE_STAGE_FINISHED - DRONE_STAGE_STARTED
            "TPL_BUILD_CONSUMING",
            "DRONE_BUILD_EVENT",
            "DRONE_TAG",
            "DRONE_COMMIT_BRANCH",
            "DRONE_STAGE_NAME",
            "DRONE_COMMIT_MESSAGE",
            "DRONE_COMMIT_SHA",
            "DRONE_COMMIT_LINK",
            "DRONE_COMMIT_AUTHOR_EMAIL",
            "TPL_STATUS_EMOJI_SKY",
            "TPL_STATUS_EMOJI_LAND",
            "DRONE_BUILD_LINK"};

    public void getAllEnvMap(){
        // 获取系统环境变量
        Map map = System.getenv();

        allEnvMap.putAll(map);

        // 以下变量是要计算得来的
        allEnvMap.put("TPL_STATUS_COLOR", getColor((String) map.get("DRONE_BUILD_STATUS")));
        allEnvMap.put("TPL_BUILD_CONSUMING", Integer.parseInt((String) map.get("DRONE_STAGE_FINISHED")) - Integer.parseInt((String) map.get("DRONE_STAGE_STARTED")));
        allEnvMap.put("TPL_STATUS_EMOJI_SKY", getEmojiSky((String) map.get("DRONE_BUILD_STATUS")));
        allEnvMap.put("TPL_STATUS_EMOJI_LAND", getEmojiLand((String) map.get("DRONE_BUILD_STATUS")));
    }

    // Markdown 消息的标题，显示在钉钉的消息列表
    public String getTitle() throws Exception{
        return allEnvMap.get("DRONE_BUILD_STATUS") + " in " + allEnvMap.get("DRONE_REPO_NAME");
    }

    // 从模板文件里读取，并替换其中的占位符
    public String getMessage() throws Exception {
        Resource resource = new ClassPathResource("markdown.tpl");
        String content = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);

        for(String t:tpl){
            String value = ObjectUtils.nullSafeToString(allEnvMap.get(t));
            if (value.equalsIgnoreCase("null")){
                value = "";
            }

            content = content.replaceAll("\\["+t+"\\]", value);
        }

        return content;
    }

    // get color for message title
    private String getColor(String buildStatus){
        Map colors = new HashMap<String, String>();
        //  success color
        colors.put("success", "#008000");
        //  failure color
        colors.put("failure", "#FF0000");

        return (String)colors.get(buildStatus);

        //return "";
    }

    // get emoji show in the sky
    private String getEmojiSky(String buildStatus){
        Map emojis = new HashMap<String, String>();
        emojis.put("success", "🐬");
        emojis.put("failure", "🐝");

        return (String)emojis.get(buildStatus);

        //return "☀️";
    }

    // get emoji show on land
    private String getEmojiLand(String buildStatus){
        Map emojis = new HashMap<String, String>();
        emojis.put("success", "🌊🌊⛵🌊🌊🌊🌊⁣🌊");
        emojis.put("failure", "🌵🌵🌵🌵🌵🏠🌵🌵");

        return (String)emojis.get(buildStatus);

        //return "👿👿👿👿👿👿👿👿";
    }

}
