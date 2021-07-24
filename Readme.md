# 基于 Springboot 实现的在线教育平台（后端）

## 简介

​	该项目是一个在线教育平台项目，分为前台系统和后台系统，采用前后端分离来搭建项目。
这个项目是学习网上教程的，写这个项目目的是熟悉之前所学过的知识，巩固知识，对于之前一段时间学习的零散知识点进行实战训练，看看自己不足点，并希望能通过这个项目扩展自己的知识点，对知识的深度也有进一步提升。

## 技术栈

这个项目涉及的技术栈包括但不限于：

- 前端
  - html、css、js
  - jquery
  - ajax
  - vue
  - axios
  - ...


- 前端框架
  - 后台：vue + element-ui
  - 前台：NUXT
  
  
  
- 后端
  - Springboot
  - MyBatisPlus
  - SpringCloud
  - SpringSecurity
  - 权限管理
  - Swagger
  - Docker
  - Redis （缓存+超时设置）
  - 阿里云OSS（头像+图片+视频）
  - 阿里云视频点播
  - 阿里云短信服务（验证码注册）
  - 微信扫码登录（OAuth2）
  - EasyExcel
  - nacos 服务注册
  - nacos 配置中心
  - feign 远程调用
  - hystrix 熔断器
  - gateway 网关
  - ...



- 其他
	- nginx
	- nacos
	- token
	- jwt
	- jenkins


​	

## 启动流程

1、启动后端（所需）模块（至少保证Gateway+Edu两个模块启动，才能访问基本的后台管理员页面）

2、启动前端项目（npm run dev）

3、启动nginx（启动 Gateway模块 替代）

4、启动nacos（本地启动 startup.cmd ，注意不是startup.sh）（启动后保持终端窗口开启状态）

5、启动redis（本地 redis-server.exe ）（启动后保持终端窗口开启状态）



## 目录结构说明

### 公共模块

#### 公共配置：

<img src="https://gitee.com/WuZe-wz/blogimg/raw/master/img/20210701004212.png" style="zoom: 67%;" />



#### 基础服务配置：

<img src="https://gitee.com/WuZe-wz/blogimg/raw/master/img/20210701004303.png" style="zoom:67%;" />



#### spring_security模块：

<img src="https://gitee.com/WuZe-wz/blogimg/raw/master/img/20210701003349.png" style="zoom: 50%;" />



### 网关和跨域统一配置：

<img src="https://gitee.com/WuZe-wz/blogimg/raw/master/img/20210701004436.png" style="zoom:50%;" />



### 具体的业务模块:

![](https://gitee.com/WuZe-wz/blogimg/raw/master/img/20210701004532.png)



- `acl`：后端权限管理模块

- `cms`：banner模块（前端）
- `edu`：课程模块（主）
- `msm`：阿里云短信服务模块
- `order`：订单模块
- `oss`：阿里云存储服务模块
- `statistics`：统计服务模块
- `ucenter`：登录注册模块（前端）

- `vod`：阿里云视频点播模块











## 备注

> 记录：
>
> `2021.01.04`
>
> 目前还是项目的起步阶段，今年大三了，年后就要开始着手准备实习的事情了，抓紧时间，fighting！