for i in `find ./ -name *.java` ; do echo $i;iconv -f gbk -t utf8 $i -o /mnt/iconv.tmp; mv /mnt/iconv.tmp $i; done
