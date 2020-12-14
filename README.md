# EPMC-POMDP
EPMC-POMDP Project daily progress

### 11.17
1) Setting up ePMC in Eclipse done;

2) Talked about the POMDP format in PRISM with LiYong and Andrea;

3) Read PRISM Manual to learn PRISM Language;

4) Thought about how to represent the UAV Collision Avoidance Model as PRISM Model;

5) Investigeted the C++ implementation of PBVI algorithem.

#### Conclusion
目前希望先实现离散状态空间的PBVI算法， 主要分两步，一是实现PRISM的POMDP Model格式支持；二是在PRISM里面添加一个Plugin，在其中实现PBVI算法，读入POMDP Model，生成policy。

### 11.22
目前希望在plugin下添加一个pomdp，对prism-format这个plugin进行一些扩展，能读入pomdp-prism格式的文件（[UAVpomdp](https://github.com/fengwz17/EPMC-POMDP/blob/main/UAVpomdp.prism)）：

1、修改parser: 支持observables关键字;

2、状态转移支持observation distribution.

#### 一些困难
1、读prism-forma代码，代码中很多地方弄不清楚在做什么....没办法准确定位自己要进行扩展的部分，对那些地方的名字、参数、调用进行修改；

2、不知道如何方便的进行调试：

目前感觉应该在distribution下添加一个pomdp/build.sh，编译带有pomdp扩展的epmc-pomdp.jar，然后用其跑pomdp-prism格式的文件，但是感觉每次运行build.sh都需要很长时间进行编译，比较麻烦..

### 12.14 ePMC-POMDP计划

主要任务：和Andrea一起在ePMC上把POMDP的plugin开发出来。

主要分这几步走：

1、	fix一些编译的问题，让POMDPParser能够编译成功，从而读入POMDP-PRISM文件。

2、	获得POMDP-PRISM文件中的信息：通过打印state、action、observable、transition command等方式测试读入信息的获取。

3、	通过读入的模型信息，实现PBVI算法，生成policy。
