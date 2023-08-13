#!/bin/bash

# 从命令行获取文件名
file=$1

# 使用wc命令统计文件的行数
line_count=$(wc -l < $file)

word_count=$(wc -w < $file)

# 输出文件名和行数
string1="文件 $file 有 $line_count 行."
# 去除空格
string1=${string1// /}
echo $string1

# 输出文件名和单词数
string2="文件 $file 有 $word_count 个单词."
# 去除空格
string2=${string2// /}
echo $string2

