[![wakatime](https://wakatime.com/badge/user/cd8731d7-2366-4da2-8032-5bb5ad0d3122/project/01444e6f-4980-485c-9960-5a46ecb4e6d5.svg)](https://wakatime.com/badge/user/cd8731d7-2366-4da2-8032-5bb5ad0d3122/project/01444e6f-4980-485c-9960-5a46ecb4e6d5)

# 已启用
由于过度设计和缺乏注释等原因导致该项目过于复杂，故舍弃重新编写
# Qingzhixing Chat Bot

自用的QQ机器人项目

## 配置

将 src/main/resources/settings.xml 中内容填充完整即可  
也可以复制粘贴为settings-private.xml,与settings.xml等价 优先读取settings-private.xml  
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

## 注意事项：

### 目前无法使用中！

### 因腾讯风控原因!

如果出现原来可以登陆但是突然无法登陆的问题，请及时更新仓库，如果更新了仓库无法解决且作者未更新代码请联系作者！

## Todo List:

- [ ] 将配置文件和jar文件分离，优先外部文件读取，其次读取内部，若无外部文件则将内部文件读取输出为外部文件
- [ ] 在关键词回复xml中支持多种回复类型，如at回复和引用回复
- [ ] 测试多账号bot登陆
- [ ] 私聊CommandHandler完成