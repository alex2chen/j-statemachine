# 背景
	在日常的开发中，我们有些业务单据有非常复杂流程（它有很多状态），如果靠传统的全局属性然后通过swich和if来判断的话，扩展性差，重复代码多。通过编写一个状态管理库来解决状态变化的优点有：
- 	代码整洁，可读性强
- 	易拓展，可复用
- 	维护成本小

# 有限状态机
有限状态机（Finite-state machine, FSM），又称有限状态自动机，简称状态机，是表示有限个状态以及在这些状态之间的转移和动作等行为的数学模型。
![thread](src/main/resources/images/thread.jpg "thread")
- State：状态。一个标准的状态机最少包含两个状态：初始和终态。初态是状态机初始化后所处的状态，而终态顾名思义就是状态机结束时所处的状态。其他的状态都是一些流转中停留的状态。标准的状态机还会涉及到一些中间态，存在中间态的状态机流程就会比较复杂（用处也不是特别大，而且可以通过其他方式实现），所以在目标实现的状态机里不会引入这个概念。
- Event：事件。还有中描述叫Trigger，表达的意思都一样，就是要执行某个操作的触发器或口令：当状态机处于某个状态时，只有外界告诉状态机要干什么事情的时候，状态机才会去执行具体的行为，来完成外界想要它完成的操作。比如出去吃饭，说“点菜”，服务员才会拿着小本过来记录你要吃的菜，说的那句“点菜”，就相当于Event。
- Action：行为。状态变更索要执行的具体行为。还是拿上面点菜的例子，服务员拿小本记录你定的菜的过程就是Action
- Transition：流转。一个状态接收一个事件执行了某些行为到达了另外一个状态的过程就是一个Transition。定义Transition就是在定义状态机的运转流程。


# 项目结构说明
│  │  └─com
│  │      └─kxtx
│  │          └─fsm
│  │              ├─builder				构建状态机，依赖core,excption,filter,config
│  │              │  ├─chain
│  │              │  │  ├─dto
│  │              │  │  └─support
│  │              │  ├─event
│  │              │  │  ├─annotation
│  │              │  │  ├─dto
│  │              │  │  └─support
│  │              │  ├─exec
│  │              │  │  ├─annotation
│  │              │  │  ├─dto
│  │              │  │  └─support
│  │              │  ├─machine
│  │              │  │  ├─annotation
│  │              │  │  ├─dto
│  │              │  │  ├─factroy
│  │              │  │  └─support
│  │              │  │      └─visit
│  │              │  ├─plugin
│  │              │  │  ├─log
│  │              │  │  │  └─support
│  │              │  │  └─support
│  │              │  ├─selector
│  │              │  └─state
│  │              │      ├─dto
│  │              │      └─support
│  │              │          └─visit
│  │              ├─config					所有配置项，依赖core,filter,exception
│  │              │  ├─annotation
│  │              │  ├─application
│  │              │  └─loader
│  │              │      └─support
│  │              ├─core					基础模块，仅依赖exception
│  │              │  ├─convert
│  │              │  │  └─support
│  │              │  ├─el
│  │              │  │  └─support
│  │              │  ├─extension
│  │              │  │  └─support
│  │              │  ├─factory
│  │              │  ├─types
│  │              │  └─utils
│  │              ├─exception				异常处理模块，不依赖任何模块
│  │              ├─filter					包含校验，后置处理，依赖core,filter,exception
│  │              │  ├─annotation
│  │              │  ├─validator
│  │              │  │  └─support
│  │              │  └─wrapper
│  │              │      └─support
│  │              └─spring					集成spring
│  │                  └─annotation

test/java/com/kxtx/fsm/包含客户端接入实例(QuickStart_test/ProxyStateMachin_test)

## 一、设计编码须知
    1.健壮性（异常处理、灵活没打算依赖spring）           见TODO（持续进行中）
    2.安全性（封装级别、客户端访问、侵入性）             见TODO（持续进行中）
    3.拓展性(可插拔、多变功能柔性设计)                   见TODO（持续进行中）

## 二、代办功能事宜
    Step：链式语法设计                                  √
    Step：添加mvel表达式支持                            √
    Step：大量反射的优化及同异步Action                  √（持续进行中）
    Step：添加注解支持及枚举的兼容性                    √
    Step：客户端那么多泛型好麻烦，优化客户端入口        √
    Step：支持客户端更多后置处理                        √
    Step：支持客户端拓展参数支持,前置处理               √
    Step：添加action,transition的事件支持及多线程驱动    √
    Step：插件化支持(Log记录状态机所有操作...)             √
    Step：parent state支持，transition的正则匹配           ×
    Step：还需要支持XML?Antrl?                             ×
    Step：更多执行策略支持                                 ×
    Step：DependProvider替换SPI的开展                      ×
    Step：关于版本化（设计考虑点）                         ×
    Step：.....

## 三、验收方面
    1.后续编写单元测试
    2.TMS/OMS安排开发进行了解及接入

## 四、交付
    计划晚点提交到ES代码主干中