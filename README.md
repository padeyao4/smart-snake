# smart-snake

java写的贪食蛇游戏，带简单ai，运行多次后可以出现吃满屏幕的情况。

## 安装运行

电脑需要预先安装jdk1.8

```
git clone https://github.com/guojiank/smart-snake.git

cd smart-snake

./gradlew desktop:dist

```

生成可运行文件在```./desktop/build/libs```目录下

运行游戏

```$xslt

java -jar desktop-1.0.jar

```

## 导入到idea中

打开 idea ,选择 import project , 找到本项目，然后选择 import project from external model 选择 gradle 。

main方法所在目录为 desktop/src/io.github.guojiank.game.desktop/DesktopLauncher 。 启动项目前需要先编辑 Run/Debug configurations 文件。将 working directory 目录修改为 C:\Users\Administrator\Desktop\workspaces\smart-snake\core\assets ,如果是Linux 或者mac 修改为相应目录就可以了