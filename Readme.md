[![wakatime](https://wakatime.com/badge/user/cd8731d7-2366-4da2-8032-5bb5ad0d3122/project/01444e6f-4980-485c-9960-5a46ecb4e6d5.svg)](https://wakatime.com/badge/user/cd8731d7-2366-4da2-8032-5bb5ad0d3122/project/01444e6f-4980-485c-9960-5a46ecb4e6d5)

# Qingzhixing Chat Bot

自用的QQ机器人项目

## 配置

将 src/main/resources/settings.xml 中内容填充完整即可  
也可以复制粘贴为settings-private.xml,与settings.xml等价
优先读取settings-private.xml  
关键词回复文件在同目录下keywords.xml中，也可以写在keywords-private.xml中

## 如何运行

先进入src/main/resources/下填充好 settings.xml 与 keywords.xml
> settings-private.xml , keywords-private.xml 分别等价于 settings.xml 与 keywords.xml

之后打包:

```shell
maven package
```

进入target文件夹下

```shell
java -jar <前面没有'original-'的.jar文件>
```

然后按照Mirai提示账号登陆操作