hadoop-transfer-tool
====================

hadoop-transfer-tool


<h2>hadoop & ftp file transfer tool</h2>

this tool is used to transfer files to hdfs.

usage: transfer [-D <arg>] [-d <arg>] [--echo <arg>] [-F
       <arg>] [-H <arg>] [-h] [--list <arg>] [-o <arg>] [-P
       <arg>] [-p <arg>] [--shutdown <arg>] [--start <arg>]
       [-t <arg>] [-U <arg>]
       
example 1: java -jar transfer.jar -H 192.168.*.* -F /dir -D
/ttt -U usr -P anonymous -t ftp

example 2: java -jar transfer.jar -H 192.168.*.* -p 9000 -F
/ttt -D /zzz -U usr -t hdfs

 -D,--destination <arg>   destination directory
 
 -d,--delete <arg>        delete files after
                          tansfer.[true|false]
                          
    --echo <arg>          print file name transferring
    
 -F,--filename <arg>      file or directory to transfer
 
 -H,--host <arg>          hostname of the avro source
 
 -h,--help                get help infomation
 
    --list <arg>          list all tasks
    
 -o,--option <arg>        overwrite,append or ignore if file
                          exist on hdfs,o for overwrite,a
                          for append,i for ignore.[a|i|o]
                          
 -P,--password <arg>      password
 
 -p,--port <arg>          port of the avro source
 
    --shutdown <arg>      shutdown a task.[task ID]
    
    --start <arg>         start a task [task ID]
    
 -t,--type <arg>          type of resources.
                          support ftp,hdfs.[ftp|hdfs]
                          
 -U,--user <arg>          user name
 
this tool is used to transfer files to hdfs.

it support ftp & hdfs.

====================== 

shenbaise1001@126.com
