# back
代码在src/main/java目录下
## 启动类
Application,目录为src/main/java/org.exmaple/Application
## 实体类
### Visitor
和数据库中的visitor表对应，用于增删改查
### show
属性为report中上报的key，为了Gson转换方便设置的
## 工具类
### ResultData&&ReturnCode
用于返回json状态码
## Controller类
### report方法
获取前端get请求,将数据存入数据库
### yoydod方法
获取数据本身及数据yoydod
### getStudents方法
返回数据库所有列表

