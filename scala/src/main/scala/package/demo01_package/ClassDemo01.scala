

//案例: 演示包的3种写法.

/*
  注意事项:
    1. 编写Scala源代码时, 包名和源码所在的目录结构可以不一致.
    2. 编译后, 字节码文件和包名路径会保持一致(由编译器自动完成).
    3. 包名由数字, 大小写英文字母, _(下划线), $(美元符)组成, 多级包之间用.隔开, 一般是公司域名反写.
 */

//格式1: 文件顶部标记法, 合并版.
//package com.itheima.scala

//格式2: 文件顶部标记法, 分解版
/*package cn.itcast
package scala*/

//格式3: 串联式包语句, 建议不超过3层.
package `package`.demo01_package

//人类
    class Person
    //测试类
    object ClassDemo01 {
      def main(args: Array[String]): Unit = {

      }
    }


