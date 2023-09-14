**易联车联网数据交换中间件** 

车联网数据交换中间件采用netty/mina作为底层架构，是一个基于JTT-809-2011《道路运输车辆卫星定位系统平台数据交换/JTT-809-2019《道路运输车辆卫星定位系统平台数据交换》的数据交换中间件，采用MQ以json消息（支持RabbitMQ/ActiveMQ）与业务平台进行交互，能够无缝接入各种异构系统，连接企业之间的车辆监控系统，实现数据互通。本网关应用已历经并通过多次交通部部标的检测，性能稳定，适用于车辆监控平台之间的实时数据交换，经测试在普通pc机上，单个网关应用可支持1000wGPS数据/小时的数据处理能力。

**技术特点** 

1、软件绿色免安装      
2、软件架构特点详情请参考：https://www.legaoyi.com/elink-data-exchange.html          
3、软件平台演示地址：https://iov.legaoyi.com        
4、技术支持QQ：78772895 574360187（QQ群）    
5、中间件下载地址：https://www.legaoyi.com/software.html  

**软件运行环境** 

- 操作系统     
操作系统支持window以及linux等，建议使用linux，64位centOS 7以上版本     

- Java环境     
平台的运行依赖java环境，安装jdk1.8以上版本     

- RabbitMQ环境     
软件的运行依赖RabbitMQ环境，RabbitMQ安装最新版本    


**程序打包编译**     
maven环境中运行：mvn package appassembler:assemble    

**配置文件** 

软件conf目前下的所有配置文件都必须保留，不能删除，否则将会导致软件无法运行。使用软件时，请根据实际情况修改conf目录下的配置文件application.properties中的rabbitmq配置部分，如下：

************************************************************     
     
#rabbitmq配置    
spring.rabbitmq.host=localhost    
spring.rabbitmq.port=5672    
#rabbitmq登陆用户名    
spring.rabbitmq.username=admin    
#rabbitmq登陆密码    
spring.rabbitmq.password=123456    
#spring.rabbitmq.virtualHost=test    
    
#下级平台tcp连接端口，注意操作系统配置防火墙允许该端口    
server.port=6039     
     
*************************************************************    

注：server.tcp.port=6039 该配置为车载终端连接的TCP端口，操作系统防火墙需要开放该端口     

**软件模块** 

1、elink-iov-exchange-server 为数据交换中间件服务端（上级平台），下级平台连接该软件程序，可配合elink-iov-exchange-client测试JT/T809协议流程     

2、elink-iov-exchange-server-processor  为上级平台消息处理应用程序，连接elink-iov-exchange-server进行上级平台业务处理，开发者可根据自身业务进行二次开发 

3、elink-iov-exchange-client 为数据交换中间件客户端（下级平台），可配合elink-iov-exchange-server测试JT/T809协议流程     

4、elink-iov-exchange-client-processor  为下级平台消息处理应用程序，连接elink-iov-exchange-client进行下级平台业务处理，开发者可根据自身业务进行二次开发 

5、elink-iov-exchange-client-message-processor  为下级平台消息处理应用程序，连接elink-iov-exchange-client旗舰版进行上级平台业务处理，开发者可根据自身业务进行二次开发 

6、elink-iov-exchange-server-message-processor  为上级平台消息处理应用程序，连接elink-iov-exchange-server旗舰版进行上级平台业务处理，开发者可根据自身业务进行二次开发 

7、elink-iov-jt905-exchange-client-message-processor  为JT905下级平台消息处理应用程序，连接elink-iov-jt905-exchange-client旗舰版进行上级平台业务处理，开发者可根据自身业务进行二次开发 

8、elink-iov-jt905-exchange-server-message-processor  为JT905上级平台消息处理应用程序，连接elink-iov-jt905-exchange-server旗舰版进行上级平台业务处理，开发者可根据自身业务进行二次开发 

**软件运行** 

1、window：双击bin/startup.bat文件即可启动；

2、linux：使用命令启动（注：启动前先赋予该文件startup.sh执行权限，chmod 777 startup.sh）： nohup ./bin/startup.sh & 


本软件为免费版本，觉得好用请支持正版软件，后续会陆续退出更多免费的版本，敬请关注，谢谢！    

