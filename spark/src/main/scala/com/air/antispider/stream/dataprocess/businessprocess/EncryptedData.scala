package com.air.antispider.stream.dataprocess.businessprocess

import java.util.regex.Pattern

import com.air.antispider.stream.common.util.decode.MD5

//用于实现数据脱敏的代码（手机号和身份证号码）

object EncryptedData {

  //用于实现手机号码的脱敏实现
  def encryptedPhone(message: String)= {
    //定义临时接受数据的变量
    var encryptedData=message
    //实例MD5
    val md5=new MD5()

    //1 获取手机号的正则表达式
    val phonePattern = Pattern.compile("((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0-9])|(18[0,5-9]))\\d{8}")

    //2 使用正则匹配数据获取出有可能是手机号的数据
    val phones=phonePattern.matcher(encryptedData)

    //遍历每一个可能是手机号的数据
    while(phones.find()){
      //过滤出一个可能手机号数据（不一定是手机号）
      val phone= phones.group()
      //获取到手机号码首位数字  前一位的index
      val befIndex=encryptedData.indexOf(phone)-1
      //获取到手机号码最后一位数字  后面一位的index
      val aftIndex=encryptedData.indexOf(phone)+11
      //获取到手机号码首位数字  前一位的字符
      val befLetter=encryptedData.charAt(befIndex).toString

      //3 判断出一定是手机号的数据
      //3-1手机号码前一个位置不是数字，并且手机号码是一条数据中的最后一个数据，那么表示这个一定是手机号
      if (!befLetter.matches("^[0-9]$")){
        //若手机号的最后一位后面的角标大于数据的总长度  那么表示这个手机号是数据的最后一位
        if (aftIndex>encryptedData.length){
          //确定出是手机号后，将手机号加密，替换原始的数据
          encryptedData=encryptedData.replace(phone,md5.getMD5ofStr(phone))
        }else{//表示数据不是最后一位
          //3-2手机号码前一个位置不是数字，并且后一位也不是数字，那么表示这个一定是手机号
          //获取到手机号码最后一位数字  后面一位的字符
          val aftLetter=encryptedData.charAt(aftIndex).toString
          if(!aftLetter.matches("^[0-9]$")){
            //确定出是手机号后，将手机号加密，替换原始的数据
            encryptedData=encryptedData.replace(phone,md5.getMD5ofStr(phone))
          }
        }
      }
    }
    encryptedData
  }




  //用于实现身份证号码的脱敏实现
  def encryptedId(message: String)= {
    //定义临时接受数据的变量
    var encryptedData=message
    //实例MD5
    val md5=new MD5()

    //1 获取身份证号码的正则表达式
    val idPattern = Pattern.compile("(\\d{18})|(\\d{17}(\\d|X|x))|(\\d{15})")
    //2 使用正则匹配数据获取出有可能是身份证号码的数据
    val ids=idPattern.matcher(encryptedData)

    //遍历每一个可能是身份证号码的数据
    while(ids.find()){
      //过滤出一个可能身份证号码数据（不一定是身份证号码）
      val id= ids.group()
      //获取到身份证号码首位数字  前一位的index
      val befIndex=encryptedData.indexOf(id)-1
      //获取到身份证号码最后一位数字  后面一位的index
      val aftIndex=encryptedData.indexOf(id)+18
      //获取到身份证号码首位数字  前一位的字符
      val befLetter=encryptedData.charAt(befIndex).toString

      //3 判断出一定是身份证号码的数据
      //3-1身份证号码前一个位置不是数字，并且身份证号码是一条数据中的最后一个数据，那么表示这个一定是身份证号码
      if (!befLetter.matches("^[0-9]$")){
        //若身份证号码的最后一位后面的角标大于数据的总长度  那么表示这个身份证号码是数据的最后一位
        if (aftIndex>encryptedData.length){
          //确定出是身份证号码后，将身份证号码加密，替换原始的数据
          encryptedData=encryptedData.replace(id,md5.getMD5ofStr(id))
        }else{//表示数据不是最后一位
        //3-2身份证号码前一个位置不是数字，并且后一位也不是数字，那么表示这个一定是身份证号码
        //获取到身份证号码最后一位数字  后面一位的字符
        val aftLetter=encryptedData.charAt(aftIndex).toString
          if(!aftLetter.matches("^[0-9]$")){
            //确定出是身份证号码后，将身份证号码加密，替换原始的数据
            encryptedData=encryptedData.replace(id,md5.getMD5ofStr(id))
          }
        }
      }
    }
    encryptedData
  }
}
