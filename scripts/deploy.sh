#PROJECT_ROOT="/home/ubuntu/app/dofarming"
#JAR_FILE="$PROJECT_ROOT/dofarming-0.0.1-SNAPSHOT.jar"
#
#APP_LOG="$PROJECT_ROOT/application.log"
#ERROR_LOG="$PROJECT_ROOT/error.log"
#DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
#
#TIME_NOW=$(date +%c)
#
## 현재 실행 중인 애플리케이션 종료
#CURRENT_PID=$(pgrep -f $JAR_FILE)
#
#if [ -z "$CURRENT_PID" ]; then
#  echo "$TIME_NOW : No application running" >> $DEPLOY_LOG
#else
#  echo "$TIME_NOW : Kill process $CURRENT_PID" >> $DEPLOY_LOG
#  kill -9 $CURRENT_PID
#fi
#
#echo "$TIME_NOW : copy $JAR_FILE" >> $DEPLOY_LOG
#cp $PROJECT_ROOT/build/libs/*.jar $PROJECT_ROOT
#
#echo "$TIME_NOW : run $JAR_FILE" >> $DEPLOY_LOG
#chmod +x $JAR_FILE
#nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &
#
#NEW_PID=$(pgrep -f $JAR_FILE)
#echo "$TIME_NOW : New process id is $NEW_PID" >> $DEPLOY_LOG

PROJECT_ROOT="/home/ubuntu/app/dofarming"
BUILD_JAR_FILE="$PROJECT_ROOT/build/libs/dofarming-0.0.1-SNAPSHOT.jar"
COPIED_JAR_FILE="$PROJECT_ROOT/dofarming-0.0.1-SNAPSHOT.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 현재 실행 중인 애플리케이션 종료
CURRENT_PID=$(pgrep -f $COPIED_JAR_FILE)

if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW : No application running" >> $DEPLOY_LOG
else
  echo "$TIME_NOW : Kill process $CURRENT_PID" >> $DEPLOY_LOG
  kill -9 $CURRENT_PID
fi

# 복사
echo "$TIME_NOW : copy $BUILD_JAR_FILE to $COPIED_JAR_FILE" >> $DEPLOY_LOG
cp $BUILD_JAR_FILE $COPIED_JAR_FILE

# 복사 확인
if [ -f "$COPIED_JAR_FILE" ]; then
  echo "$TIME_NOW : $COPIED_JAR_FILE successfully copied" >> $DEPLOY_LOG
else
  echo "$TIME_NOW : Failed to copy $BUILD_JAR_FILE" >> $DEPLOY_LOG
  exit 1
fi

echo "$TIME_NOW : run $COPIED_JAR_FILE" >> $DEPLOY_LOG
chmod +x $COPIED_JAR_FILE
nohup java -jar $COPIED_JAR_FILE > $APP_LOG 2> $ERROR_LOG &

NEW_PID=$(pgrep -f $COPIED_JAR_FILE)
echo "$TIME_NOW : New process id is $NEW_PID" >> $DEPLOY_LOG
