#! /usr/bin/bash

FILES=$(ls $1)
for file1 in $FILES
do
if [ -f $file1 ]
then
file $file1
fi
done
exit
