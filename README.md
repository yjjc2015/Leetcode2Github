# 欢迎共同开发

@(工具开发中)[同步工具|leetcode|github]
> **leetcode2github**是一款专为leetcode用户同步AC代码到github上的同步工具。

-	该工具使用**网络爬虫**，爬取**leetcode**用户的提交代码，然后把提交代码更新到**github**网站上。
-	本工具编写的爬虫代码，采用**多线程**方式，在一定程度上提高了爬虫效率。
 
-------------------

[TOC]

## 原理图
```flow
st=>start: MutipleSpider（模拟登录leetcode）
e=>end: 结束
op=>operation: Scheduler
op1=>operation: Downloader
cond=>condition: 是否爬取所有数据？
op2=>operation:	PineLine
op3=>operation:	模拟登录github
op4=>operation:	同步数据

st->op->op1->cond->op2->op3->op4
cond(yes)->op2
cond(no)->op
```


## Spider类说明

