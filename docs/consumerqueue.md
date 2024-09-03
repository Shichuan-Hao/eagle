# 消息分派：ConsumerQueue


## Dispatcher 逻辑的实现介绍

当消息被写入到 CommitLog 文件之后，我们需要将消息在对应哪份 CommitLog 文件的哪个位置给封装成一个对象，然后写入
到 ConsumerQueue（被mmap映射的文件）中。
- 对象的封装（CommitLog文件名，offset位置在哪，消息的长度）
- 写入的操作（单线程 or 多线程 ？由于多线程无法确保写入ConsumerQueue 中数据的先后顺序，所以ConsumerQueue的写入操作需要使用单线程）
- queue 文件中的 mmap 映射


## ConsumeQueue 的文件存储结构设计