#!/bin/bash
# 计算器

# 读取用户输入的两个数字和一个运算符
num1=$1
num2=$2
operator=$3

if [ $# -ne 3 ] 
then
    echo "需要输入三个参数，两个数字和一个运算符！例如：./calculator. sh 10 2 +"
    exit 1
else
  expr $num1 + $num2 &>/dev/null
  if [ $? -eq 0 ]  # shell 传递到脚本的参数等于0，则执行 then 中的语句
  then
    case $operator in
    "+")
        result=$(echo "$num1 + $num2" | bc)
        ;;
    "-")
        result=$(echo "$num1 - $num2" | bc)
        ;;
    "*")
        result=$(echo "$num1 * $num2" | bc)
        ;;
    "/")
        result=$(echo "scale=2;$num1 / $num2" | bc)
        ;;
    *)
        echo "无效的运算符：$operator"
        exit 1
        ;;
    esac
 else
  echo "输入必须是整数"
  exit 1
 fi
fi

# 输出计算结果
echo "计算结果为：$result"

