#! /bin/bash

txt_number=0
log_number=0

function total_dir() {
	for file in `ls $1`
	do
		if [ -d $1"/"$file ]; # 判断是否是目录，是目录则递归
		then
			total_dir $1"/"$file
		elif [ -f $1"/"$file ]; # 判断是否是文件，输出屏幕
		then
			if [[ $file == *.txt ]]; then
				txt_number=`expr $txt_number + 1`
			elif [[ $file == *.log ]]; then
    		    log_number=`expr $log_number + 1`
			fi
		fi
	done
}

function read_dir() {
	for file in `ls $1`
	do
		if [ -d $1"/"$file ]; # 判断是否是目录，是目录则递归
		then
			read_dir $1"/"$file
		elif [ -f $1"/"$file ]; # 判断是否是文件，输出屏幕
		then
			if [[ $file == *.txt ]]; then
				echo "txt文件：" $1"/"$file
			elif [[ $file == *.log ]]; then
    		    echo "log文件：" $1"/"$file
			fi
		fi
	done
}

total_dir $1
echo "txt文件总数：" $txt_number
echo "log文件总数：" $log_number
read_dir  $1
