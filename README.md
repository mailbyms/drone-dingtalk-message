# drone-dingtalk-message
drone 插件，配置在 drone 的 pipeline 中，用来发送 pipeline 的结果

## 源起
原有的 mailbyms/drone-dingtalk-message 项目，只有 Linux 平台的镜像，不能编译到 windows 平台。  
openjdk:8u275-jre 在 Linux 和 Windows 下都有镜像，所以本项目可同时在 Linux 和 Windows 下 

## 流程
本质为 Spring Boot 程序，镜像容器启动后读取系统变量，通过钉钉的接口发送消息

## 编译
- Dockerfile 为 Linux 平台
- Dockerfile.win 为 Windows 平台