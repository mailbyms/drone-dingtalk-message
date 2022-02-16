# drone-dingtalk-message
一个 Drone 插件，配置在 drone 的 pipeline 中，用来发送 pipeline 的结果，效果：
![image](https://user-images.githubusercontent.com/16809751/121153678-d891da00-c878-11eb-9494-da584f43d075.png)

## 源起
原有的 mailbyms/drone-dingtalk-message 项目，只有 Linux 平台的镜像，不能编译到 windows 平台。  
openjdk:8u275-jre 在 Linux 和 Windows 下都有镜像，所以本项目可同时在 Linux 和 Windows 下 

## 流程
项目为 SpringBoot 程序，镜像容器启动后读取系统变量，通过钉钉的接口发送消息

## 编译
- Dockerfile 为 Linux 平台
- Dockerfile.win 为 Windows 平台

### 使用说明
添加一个`step`到你的`.drone.yml`中，下面是例子：

```yaml
steps:
...
  - name: 发送钉钉消息
    image: mailbyms/drone-dingtalk-message
    pull: if-not-exists
    settings:
      token:
        from_secret: dingtalk_token
    when:
      status: [failure, success]
```

## 参数说明
`dingtalk_token`机器的 access_token，必填

- PC 版钉钉，在群聊天界面右上角 -> 群设置 -> 智能群助手 -> 添加机器人， 点击"+"，选择 “自定义（通过 webhook 接入自定义服务）”，点击“添加”
- 填写相关信息，“安全设置” 处勾选自定义关键词，填入 `build`
- 点击完成后，钉钉会提示 webhook 的地址，如：`https://oapi.dingtalk.com/robot/send?access_token=aabbccddeewf`  
其中，此 `access_token` 参数就对应 上面的 `dingtalk_token`  
![image](https://user-images.githubusercontent.com/16809751/121153859-02e39780-c879-11eb-9ae5-ded0ddbd82e5.png)

## 开发注意
钉钉的 markdown 消息，只支持如下[语法](https://developers.dingtalk.com/document/app/develop-enterprise-internal-robots/title-mno-3qd-5f9)：
```
标题
# 一级标题
## 二级标题
### 三级标题
#### 四级标题
##### 五级标题
###### 六级标题
 
引用
> A man who stands for nothing will fall for anything.
 
文字加粗、斜体
**bold**
*italic*
 
链接
[this is a link](https://www.dingtalk.com/)
 
图片
![](http://name.com/pic.jpg)
 
无序列表
- item1
- item2
 
有序列表
1. item1
2. item2
```

## 本地调试
修改本 github 的 docker-compose.yml 文件里面的参数，使用 `docker-compose up` 即可发送消息：  
```
PLUGIN_TOKEN：设置为上面 access_token 的值
DRONE_BUILD_STATUS：设置为 failure 或者 success
```
