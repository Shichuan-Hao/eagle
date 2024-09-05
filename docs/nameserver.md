

- 注册中心启动类：NameServerStartUp
  - main函数，服务正式启动的入口
  
- 基于netty启动nameserver服务:NameServerStarter
  - 真正的服务启动
  - 初始化 port，通过构造函数注入
  - startServer()
    - 构建netty服务
      - 定义两个非常重要的线程组
        - 处理网络io中的accept事件（连接初次建立，三次握手成功之后，它会进入到一个半连接队列，通过accept函数从半连接队列取socket对象）：事件bossGroup(NioEventLoopGroup) 
        - 处理网络io中的read&write事件：workerGroup(NioEventLoopGroup)
      - 注入两个线程组 ServerBootstrap
    - 注入编解码器
    - 注入特定的handler
    - 启动netty服务