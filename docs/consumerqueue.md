# 消息分派：ConsumerQueue


## Dispatcher 逻辑的实现介绍

当消息被写入到 CommitLog 文件之后，我们需要将消息在对应哪份 CommitLog 文件的哪个位置给封装成一个对象，然后写入
到 ConsumerQueue（被mmap映射的文件）中。
- 对象的封装（CommitLog文件名，offset位置在哪，消息的长度）
- 写入的操作（单线程 or 多线程 ？由于多线程无法确保写入ConsumerQueue 中数据的先后顺序，所以ConsumerQueue的写入操作需要使用单线程）
- queue 文件中的 mmap 映射


## ConsumeQueue 的文件存储结构设计

最终要做mmap映射存储consumerQueue内容的文件

消息的长度 消息的index commitLog名称的标识，都需要封装成一个对象，然后写入到 consumerqueue 的文件中存储

int

int

int

12byte

没有必要映射1GB? mmap 映射内存大小不需要太大，rocketmq, kafka， 5-10mb左右即可

commitlog 1gb
byte[] (200~500byte?)

eaglemq-topic.json 消息发送方使用
```json
[
  {
    "commitLogModel": {
      "fileName": "00000000",
      "offset": 0,
      "offsetLimit": 1048576
    },
    "createTime": 1701918231281,
    "queueList": [
      {
        "id": 1,
        "fileName": "00000000",
        "offsetLimit": 6291456,
        "latestOffset": 101,  // 100 byte数据进来需要 100——1 maxOff
        "lastOffset": 0   // 当前consumerQueue保存的最早的数据记录offset，索引地址，消费过一段时间的数据做删除
      }
    ],
    "topic": "order_cancel_topic",
    "updateTime": 1701918231280
  }
]
```

consumequeue-offset.json 消息消费者

```json
{
  "offsetTable": {
    "order_cancel_topic": {
      "order_service_group": {
        "0": 455,
        "1": 1001,
        "2": 8917
      },
      "msg_service_group": {
        "0": 455,
        "1": 1001,
        "2": 8917
      }
    }
  }
}
```