function kill_process(){
        for i do
                pid=`ps -ef | grep $i | grep -v grep | awk '{print $2}'`
                if [ -n "$pid" ]; then
                       kill -9 $pid;
                fi
        done
}

kill_process javatemplatejar

echo "kill success"

nohup java -jar javatemplatejar --spring.profiles.active=prod > log.txt 2>&1 &

echo "start success"