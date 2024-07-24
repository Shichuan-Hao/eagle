# 遇到的报错整理

## 关于 JDK 17 对 Java API 的反射限制

参考：
- https://blog.csdn.net/qq_41611125/article/details/126635762
- https://blog.panpili.com/2023/coding/java/reflection-in-jdk17/

```
--add-opens
java.base/java.lang=ALL-UNNAMED
--add-opens
java.base/java.nio=ALL-UNNAMED
--add-opens
java.base/jdk.internal.ref=ALL-UNNAMED
```


## 