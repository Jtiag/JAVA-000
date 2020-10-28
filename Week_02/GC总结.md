# GC总结

## Minor GC发生的时机

- 当新生代对象的产生速度大于对象的销毁速度时，即新生代内存无法放下新产生的对象时
- 当产生FULL GC时一般会伴随着Minor GC

## FULL GC发生的时机

- 年轻代对象熬过一定的年轮（默认15）后仍不能被回收进入老年代的对像越来越多，达到老年代无法放下新的对象时
- 大对象年轻代无法放下
- 如G1 GC的某些阈值(-XX:InitiatingHeapOccupancyPercent=x)达到时
- -XX:-DisableExplicitGC（默认的）且显示的调用System.gc()方法

## 影响GC性能影响的关键因素

- 高分配速率(High Allocation Rate)
- 过早提升(Premature Promotion)

## 一些默认值

**-Xms和-Xmx实际上是-XX:InitialHeapSize和-XX:MaxHeapSize的缩写**

**任务参数不指定**

Xmx=机器总内存/4

InitialHeapSize=机器总内存/64

**只指定Xmx**

串行GC 

- MaxNewSize=MaxHeapSize/3 

- InitialHeapSize=机器总内存/64

并行GC

- -XX∶ParallelGCThreads=N来指定GC线程数，其默认值为CPU核心数。

- MaxNewSize=MaxHeapSize*60%

- InitialHeapSize=机器总内存/64

CMS GC

- MaxNewSize=`64M(young_gen_per_worker)*机器cpu数*13/10`

- InitialHeapSize=机器总内存/64

G1 GC

- -XX:ParallelGCThreads,垃圾回收暂停期间用于并行工作的最大线程数，当cpu<= 8 时使用cpu数，

- -XX:ConcGCThreads，用于并发工作的最大线程数，其默认值为-XX:ParallelGCThreads/4。

- MaxNewSize=MaxHeapSize*60%

- InitialHeapSize=机器总内存/64

## 串行GC

**适用场景**

串行GC适用于内存不是很大的单核服务器，或者和其他GC配合作为备用GC来使用（如G1 GC当某些情况下发生FULL GC时会使用串行GC作为备用）

**GC 日志**

```shell
java -Xms128m -Xmx128m -XX:+UseSerialGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.serial.log GCLogAnalysis
```

![image-20201027141131438](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201027141131438.png)

如上图所示当串行GC的堆很小时已经发生OOM了，堆内存越大时，分配的对象数还减少了不少

![image-20201027142056033](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201027142056033.png)

如上图所示随着堆内存的增加，FULL GC的次数在不断减少直至没有，需要回收的内存在不断的增加，Minor GC和FULL GC的时间在不断增加，由此可见串行GC适用于那种服务器内存不是太大的场景，由于串行GC中GC线程和业务线程是串行执行了并没有使用多核CPU带来的优势，因为串行GC适用于单核的服务器

**性能压测**

```shell
java -jar -Xms128m -Xmx128m -XX:+UseSerialGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps gateway-server-0.0.1-SNAPSHOT.jar

wrk -t8 -c40 -d60s http://localhost:8088/api/hello
```

![image-20201028163739639](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201028163739639.png)

如上图所示串行GC在适当的内存下其延迟和吞吐量会接近一个临界值，吞吐量并不会随着内存的增加而一直增加，内存的增加导致回收的内存变多延迟会有所增加

## 并行GC

**适用场景**

适用于多核服务器，主要目标是增加吞吐量

```shell
java -Xms128M -Xmx128M -Xloggc:gc.parallel.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
```

![image-20201027150307558](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201027150307558.png)

如上图虽然创建对象时随机的，但当随着堆内存的增加创建的对象的效率并不是越来越大，总会有一个临界值，内存很小时会发生OOM

![image-20201027151125068](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201027151125068.png)

如上图，和串行GC相比不同堆内存下停顿的时间都要低些，并行GC一样是随着堆内存的增加，需要回收的内存慢慢变大，回收的时间也在不断增加

![image-20201027154554109](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201027154554109.png)

从GC日志中可以观察到当年轻代的对象熬过一定的年轮或者年轻代无法放下新创建的对象时，这些对象会不断的向老年代提交，老年代的对象不断增加，当内存不够用时会不断的触发FULL GC,当老年代的内存使用量达到一定的时候会不断做着无效的FULL GC老年代的空间几乎没有被回收，甚至FULL GC后老年代的空间占用还更多（是因为老年代没有回收的对象，还有不断增加的新对象到老年代？）

**性能压测**

```shell
java -jar -Xms128m -Xmx128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps gateway-server-0.0.1-SNAPSHOT.jar

wrk -t8 -c40 -d30s http://localhost:8088/api/hello
```

![image-20201028162119646](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201028162119646.png)

并行GC不管是吞吐量还是延迟时间都比串行GC好很多

## CMS GC

**适用场景**

多核CPU，并且主要调优目标是降低GC停顿导致的系统延迟，常用于互联网或者B/S架构的服务端上

```shell
java -XX:+UseConcMarkSweepGC -Xms128m -Xmx128m -Xloggc:gc.cms.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
```

![image-20201027182830857](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201027182830857.png)

![image-20201027182737836](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201027182737836.png)

使用CMS GC当内存很小时会频繁做着无效的FULL GC直至OOM，逐渐增加内存时FULL GC慢慢减少直到不再频繁的FULL GC，当内存不合理时其停顿时长也会由于FULL GC的原因变得很长，内存增加到一定的时候停顿时间不会再减少，会趋于某个值



**Allocation Failure**-新生代中内存分配失败，导致Minor GC

**Concurrent Mode Failure**-CMS收集器在应用程序线程仍在运行的情况下执行其大部分跟踪和清除工作，因此应用程序线程仅会看到短暂的暂停。但是，如果CMS收集器无法在老年代填满之前完成对无法访问的对象的回收，或者如果老年代中的可用空闲空间块无法满足分配要求，则应用程序将暂停，回收是在所有应用程序线程都会停止的情况下完成的，收集无法和应用程序同时完成的情况称为并发模式故障，表示需要调整CMS收集器参数，如增加Xmx 

**Promotion failed**-在进行Minor GC时，Survivor Space放不下，对象只能放入老年代，而此时老年代也放不下造成的。（promotion failed时老年代CMS还没有机会进行回收，又放不下转移到老年代的对象，因此会先出现concurrent mode failure，需要stop-the-wold 降级为GC-Serail Old或者ParNew

**性能压测**

```shell
java -jar -Xms128m -Xmx128m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps gateway-server-0.0.1-SNAPSHOT.jar

wrk -t8 -c40 -d30s http://localhost:8088/api/hello
```

![image-20201028163816753](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201028163816753.png)

CMS和相较于并行GC来说其吞吐量会稍微差一点，但CMS的延迟时间较并行GC的低的多

## G1 GC

**适用场景**

以响应速度优化，面向服务端应用，将来替换CMS，服务器内存较大时推荐使用G1 GC

```shell
java -XX:+UseG1GC -Xms128m -Xmx128m -Xloggc:gc.g1.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
```

![image-20201028153932387](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201028153932387.png)

产生对象的的效率和CMS差不多

![image-20201028153856730](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201028153856730.png)

**如下两种GC事件都比较耗时，影响性能**

Evacuation Failure  

- 当没有更多的空闲region被提升到老一代或者复制到幸存空间时，并且由于堆已经达到最大值，堆不能扩展，从而发生Evacuation Failure
- a.对于成功复制的对象，G1需要更新引用，并且该region被一直引用。
- b.对于未成功复制的对象，G1将自动转发它们，并保留这些region。

解决方案：

- 不要限制年轻代空间分配，让其自动化适配（不设置-Xmn）
- 加大-Xmx
- 调节-XX:InitiatingHeapOccupancyPercent 如果老年代并未在阈值时开始回收，可以适当增加该阈值
- 如果老年代回收准时开始，但耗时较长，可以适当增加-XX:ConcGCThreads
- 如果出现大量的to-space exhausted(空间耗尽) 或者 to-space overflow (空间溢出)等GC事件则增加-XX:G1ReservePercent 默认为堆空间的10%

Humongous Allocation

- 它是由'G1 Humongous Allocation'造成的。大型对象（Humongous ）是大于G1中region大小50％的对象。频繁大型对象分配会导致性能问题。如果region里面包含大量的大型对象，则该region中最后一个具有巨型对象的区域与区域末端之间的空间将不会使用。如果有多个这样的大型对象，这个未使用的空间可能导致堆碎片化。直到jdk1.8u40之前，这些巨型对象的回收只在full GC期间完成。在较新的JVM中，对这些对象的清理放在了清理阶段。

解决方法

- 可以加大region的大小，设置-XX:G1HeapRegionSize=n，但是这个参数需要设置为2的幂次方，最小值是1M，做大值是32M
- 如果可以增加堆大小

**性能压测**

```shell
java -jar -Xms128m -Xmx128m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps gateway-server-0.0.1-SNAPSHOT.jar

wrk -t8 -c40 -d30s http://localhost:8088/api/hello
```

![image-20201028165117850](https://gitee.com/jasper-hello-world/Img/raw/master/img/image-20201028165117850.png)

总体上G1GC的吞吐量比并行和CMS差了很多，但是其延迟时间比较均衡

