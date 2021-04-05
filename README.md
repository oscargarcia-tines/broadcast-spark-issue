# broadcast-spark-issue
Broadcast in Spark doesn't work when the dataframes are persisted

###### Results when no persisted

```== Physical Plan ==
*BroadcastHashJoin [id#2], [id#9], LeftOuter, BuildRight
:- Scan ExistingRDD[id#2,description#3]
+- BroadcastExchange HashedRelationBroadcastMode(List(cast(input[0, int, true] as bigint)))
+- Scan ExistingRDD[id#9,type#10]
```

###### Results when persisted:

```== Physical Plan ==
SortMergeJoin [id#2], [id#9], LeftOuter
:- *Sort [id#2 ASC NULLS FIRST], false, 0
:  +- Exchange hashpartitioning(id#2, 200)
:     +- Scan ExistingRDD[id#2,description#3]
+- *Sort [id#9 ASC NULLS FIRST], false, 0
+- Exchange hashpartitioning(id#9, 200)
+- InMemoryTableScan [id#9, type#10]
+- InMemoryRelation [id#9, type#10], true, 10000, StorageLevel(disk, memory, deserialized, 1 replicas)
+- Scan ExistingRDD[id#9,type#10]```
