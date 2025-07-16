本教程将采用2025年5月20日正式的GA版，给出如下内容
1. 核心功能模块的快速上手教程
2. 核心功能模块的源码级解读
3. Spring ai alibaba增强的快速上手教程 + 源码级解读

版本：
- JDK21
- SpringBoot3.4.5
- SpringAI 1.0.0
- SpringAI Alibaba(SAA) 1.0.0.2

### 项目目录结构
```text
chat目录                            
    - alibaba-chat                      # 基于alibaba实现chat案例
    - openai-chat                       # 基于openai实现chat案例
    - deepseek-chat                     # 基于deepseek实现chat案例
advisor目录                         
    - advisor-base                  # advisor绑定内存记忆案例
    - advisor-memory-sqlite         # 基于sqlite的advisor绑定内存记忆案例
    - advisor-memory-mysql          # 基于mysql的advisor绑定内存记忆案例
    - advisor-memory-redis          # 基于redis的advisor绑定内存记忆案例
tool-calling                        # 时间、天气两个工具的Method版、Function版实现、internalToolExecutionEnabled、returnDirect设置
structured-output                   # map、list、实例对象类型的格式化输出案例
vector目录                         
    - vector-simple                 # 基于内存的向量数据库案例
    - vecotr-redis                  # 基于redis的向量数据库案例
    - vector-elasticsearch          # 基于ES的向量数据库案例
rag目录                             
    - rag-simple                    # 基于内存的rag效果对比、模块化rag案例
    - rag-etl-pipeline              # 提取文档、转换文档、写出文档的案例   
    - rag-evaluation                # 多模型评估，模型响应结果，结合RAG的案例
    - rag-elasticsearch             # 基于ES的rag案例
mcp目录                             
    - client目录                        
        - mcp-stdio-client                  # MCP的stdio客户端案例
        - mcp-webflux-client                # MCP的webflux客户端案例
        - mcp-nacos2-client                 # MCP基于Nacos2.*实现分布式部署客户端案例
        - mcp-nacos3-client                 # MCP基于Nacos3.*实现分布式部署客户端案例
        - mcp-auth-client                   # MCP基于请求头的授权客户端
        - mcp-recovery-client               # MCP的SSE连接断开，自动重连案例
        - mcp-nacos-parse-swagger-server
    - server目录                     
        - mcp-stdio-server                      # MCP的stdio服务端案例
        - mcp-webflux-server                    # MCP的webflux服务端案例
        - mcp-nacos2-server                     # MCP基于Nacos2.*实现分布式部署服务端案例
        - mcp-nacos3-server                     # MCP基于Nacos3.*实现分布式部署服务端案例
        - mcp-auth-server                       # MCP基于请求头的授权服务端
        - mcp-gateway-server                    # SAA的gateway服务零代码实现存量应用转换MCP案例
        - mcp-nacos-parse-swagger-server(待补充) # MCP基于nacos动态解析swagger的restful服务端案例       
observabilty                                    # ObservationHandler下的client、model、tool、embedding的观测案例
graph目录  # 基于spring ai alibaba graph内核
    - simple                        # 最简单的graph案例
    - stream-node                   # 节点中AI模型的流式输出案例
    - human-node                    # 流式返回结果，中断等待用户反馈，继续执行人类输入之后的工作案例
    - paraller-node                 # 多节点并行的案例
    - mcp-node                      # 配置指定mcp给指定node的案例
other目录
    - restful服务                           # 提供接口调用服务，模拟存量接口
    - nacos-swagger-restful(待补充)         # 基于nacos+swagger的接口
    - nacos-restful                        # 基于nacos3.*的接口    
```
视频讲解地址：[B站-视频讲解](https://www.bilibili.com/video/BV17NMsziEqp?vd_source=8393ba8b4463e2acda959f2ff2c792f6&spm_id_from=333.788.videopod.sections)

微信推文系列：[微信推文版](https://mp.weixin.qq.com/s/9iLebKR8HNwalOVeDz5PXQ)

飞书云文档：[飞书云文档版-最新教程](https://ik3te1knhq.feishu.cn/wiki/WVirwu30Xik0WXks7HGcB6E2nA8)

添加vx，备注交流，可加入交流群

<img src="docx/vx.png" style="width:120px">
