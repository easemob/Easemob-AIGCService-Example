# Easemob-AIGCService-Example

## AIGCService-Server

### （一）环境准备

#### 1、部署要求

Windows或者linux 服务器


#### 2、环境要求

JDK 1.8+

Redis

> redis 绑定地址更改为127.0.0.1



### （二）服务配置

#### 1、代码下载
当部署环境准备好后，通过github链接下载服务端代码。


#### 2、信息配置
服务端配置信息全部集中在以下文件中，主要配置包含三个部分：环信IM、miniMax和redis，配置内容如下：
> src/main/resources/application.yml



**（1）环信IM信息配置：**

1.登录[环信即时通讯云console](https://console.easemob.com/user/login)，获取配置信息。
```yaml
logging:
  level:
    com.easemob.im.http: debug  #启动之后可打印的日志级别
server:
  port: 80             #修改启动端口，只能为数字(请确保该端口没有被占用)
easemob:
  appkey: {appkey}     # 创建应用获取到的Appkey，可通过【环信即时通讯云控制台】->【应用详情】页面->【APPKEY】字段获取。
  clientId:{clientId}  #  App 的 client_id，可通过【环信即时通讯云控制台】->【应用详情】页面->【Client ID】字段获取。
  clientSecret:{clientSecret}  #  App 的 client_secret，可通过【环信即时通讯云控制台】->【应用详情】页面->【ClientSecret】字段获取。
```
![image](https://github.com/easemob/Easemob-AIGCService-Example/assets/90945583/1611ff81-592d-4f7a-acf9-cdce93c011d3)



2.设置用户注册的模式为开放注册和好友关系检查关闭
![image](https://github.com/easemob/Easemob-AIGCService-Example/assets/90945583/7e2114ed-ffb5-4346-a51b-62f8f7cf0228)


3.创建机器人的账号
在【应用概览】->【用户认证】下，创建8个机器人的账号
> 注意，这八个基础账号不可以更改用户ID，否则需要调整代码中BotSettingUtil中八个对应机器人的Account

![image](https://github.com/easemob/Easemob-AIGCService-Example/assets/90945583/f0a1dd90-17fe-47ed-8fe2-5ab987f41d89)


4.配置回调规则
> 请确保环信可以通过外网访问到回调地址

![image](https://github.com/easemob/Easemob-AIGCService-Example/assets/90945583/7756e01e-b0ce-431c-8063-0b8e1e061262)


**（2）LLM模型信息配置：**<br/>
本代码示例以miniMax为例[MiniMax 开放平台快速开始](https://api.minimax.chat/document/guides/example?id=6433f36f94878d408fc82947)，若使用其他大语言模型，可按其他语言模型配置要求进行调整。
```yaml
miniMax:
  groupId:{groupId}  # MiniMax基础信息的groupID，可通过【MiniMax账号管理】->【账户信息】页面->【groupID】字段获取。
  appkey: {appkey}   # MiniMax的接口密钥，当需要复制API密钥的时候，可以重新创建一个以完成复制操作。
  url: https://api.minimax.chat/v1/text/chatcompletion_pro?GroupId=
```

**（3）redis配置：** <br/>
redis安装完成以后，设置上redis的密码(也可以设置为空)，确保“host：port" 链接可以访问redis即可
```yaml
  redis:
    host: {host}           #redis地址
    port: {port}           #redis端口
    password: {password}   #redis密码
    # 连接超时时间（毫秒）
    timeout: 30000
    # 连接池中的最大空闲连接
    max-idle: 8
    # 连接池中的最小空闲连接
    min-idle: 10
    # 连接池最大连接数（使用负值表示没有限制）
    max-active: 100
    # 连接池最大阻塞等待时间（使用负值表示没有限制）
    max-wait: -1
```

### （三）启动说明

1.host填写为127.0.0.1<br/>
2.port填写redis所占用的端口<br/>
3.password填写redis的密码(如果没有密码，使用#注释该配置)<br/>
![image](https://github.com/easemob/Easemob-AIGCService-Example/assets/90945583/7b29e95d-976e-4a8a-9208-cb604d934729)

4.使用mvn install 将项目打包为jar。<br/>
![image](https://github.com/easemob/Easemob-AIGCService-Example/assets/90945583/10955848-4180-4298-b3c2-f983719789c9)

5、找到对应位置的jar包，上传到服务器<br/>
使用命令启动即可<br/>
```yaml
nohup java -jar $APP_DIR/chattyai-0.0.1-SNAPSHOT.jar --server.port=$PORT ./chattyai.log 2>&1 &
```
> $APP_DIR 替换为上传jar包存在的根路径<br/>
> $PORT 替换为需要占用的端口

6、查看启动日志<br/>
如果需要额外配置nginx的代理，则将对应的请求拦截，代理到$PORT端口即可<br/>
> tail -f $APP_DIR/chattyai.log 


### （四）Q&A
1.项目启动不起来。

> 确保JDK配置正确，端口没有被占用，redis能被访问到

2.项目启动起来访问不到

> 1.配置nginx的情况下，请确保nginx配置正确<br/>
> 2.未配置nginx的情况下，请确保端口对外开放

3.MiniMax的调用失败，无返回结果
> 请确保MiniMax的有余额，否则可能导致调用MiniMax的调用失败


## AIGCService-AndroidClient
### （一）环境准备
1.开发环境：<br/>
●Tools : Android Studio<br/>
●os : MacOS<br/>
●code : JAVA<br/>


2.运行环境：<br/>
●os : Android 6.0 +<br/>


3.项目包含内容：<br/>
●Android Project<br/>
●安装包<br/>


### （二）参数配置
使用AndroidStudio运行项目，前往com.imchat.chanttyai/base/Constants.java文件，只需要配置APP_KEY及HTTP_HOST两个参数。<br/>
![image](https://github.com/easemob/Easemob-AIGCService-Example/assets/90945583/5c10b274-c0db-4bc0-8dee-f8ba59067d0f)


