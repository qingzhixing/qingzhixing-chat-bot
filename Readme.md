# Qingzhixing Chat Bot

自用的QQ机器人项目

## 配置

将 src/main/resources/settings.xml 中内容填充完整即可  
也可以复制粘贴为settings-private.xml,与settings.xml等价
优先读取settings-private.xml

## 如何运行

```shell
maven package
```

进入target文件夹下

```shell
java -jar <前面没有'original-'的.jar文件>
```

然后按照Mirai提示账号登陆操作