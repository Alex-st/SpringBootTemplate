#!/bin/sh

### BEGIN INIT INFO
# Provides:          td-agent
# Required-Start:    $network $local_fs
# Required-Stop:     $network $local_fs
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: data collector for Treasure Data
# Description:       td-agent is a data collector
### END INIT INFO

# Source function library.
#. /etc/init.d/functions


# Define LSB log_* functions.
# Depend on lsb-base (>= 3.0-6) to ensure that this file is present.
. /lib/lsb/init-functions

source ~/.bash_profile
JAVABIN=$JAVA_HOME/bin/java

APP_NAME="SpringBootApp"
APP_JAR="SpringBoot-1.0-SNAPSHOT.jar"
APP_PID_FILE="/tmp/${APP_NAME}.pid"
APP_LOCK_FILE="/tmp/${APP_NAME}.lock"
APP_ARGS="-jar $APP_JAR"
APP_LOG_FILE="/tmp/${APP_NAME}.log"
STOPTIMEOUT=20
ACTION="$1"

#if test $1; then ACTION=$1 ; else ACTION="start"; fi

kill_by_file() {
  local sig="$1"
  shift 1
  local pid="$(cat "$@" 2>/dev/null || true)"
  if [ -n "${pid}" ]; then
    if /bin/kill "${sig}" "${pid}" 1>/dev/null 2>&1; then
      return 0
    else
      return 2
    fi
  else
    return 1
  fi
}

do_start() {
  local RETVAL=0
  daemonize  -p "${APP_PID_FILE}" ${JAVABIN} -jar $(realpath ./SpringBoot-1.0-SNAPSHOT.jar)
  echo $! || RETVAL=$?
  
      [ $RETVAL -eq 0 ] && touch "${APP_LOCK_FILE}"

  echo -n >> "$APP_LOG_FILE"
  exec 2>>"$APP_LOG_FILE"
  exec 1>&2
          return $RETVAL
}

do_stop() {
  # Return
  #   0 if daemon has been stopped
  #   1 if daemon was already stopped
  #   2 if daemon could not be stopped
  #   other if a failure occurred
  if [ -e "${APP_PID_FILE}" ]; then
    # Use own process termination instead of killproc because killproc can't wait SIGTERM
    if kill_by_file -TERM "${APP_PID_FILE}"; then
      local i
      for i in $(seq "${STOPTIMEOUT}"); do
        if kill_by_file -0 "${APP_PID_FILE}"; then
          sleep 1
        else
          break
        fi
      done
      if kill_by_file -0 "${APP_PID_FILE}"; then
        echo -n "Timeout error occurred trying to stop ${APP_NAME}..."
        return 2
      else
        rm -f "${APP_PID_FILE}"
        rm -f "${APP_LOCK_FILE}"
      fi
    else
      return 1
    fi
  else
    if killproc "${APP_PROG_NAME:-${APP_NAME}}"; then
      rm -f "${APP_PID_FILE}"
      rm -f "${APP_LOCK_FILE}"
    else
      return 2
    fi
  fi
}

do_reload() {
  kill_by_file -HUP "${APP_PID_FILE}"
}

do_restart() {
  local val=0
  do_stop || val="$?"
  case "${val}" in
  0 )
    if ! do_start; then
      return 1
    fi
    ;;
  * ) # Failed to stop
    return 1
    ;;
  esac
}


case "$ACTION" in
"start" )
  echo -n "Starting ${APP_NAME}: "
  do_start || RETVAL="$?"
  case "$RETVAL" in
  0 )
    log_success_msg "${APP_NAME}"
    ;;
  * )
    log_failure_msg "${APP_NAME}"
    exit 1
    ;;
  esac
  ;;
"stop" )
  echo -n "Stopping ${APP_NAME}: "
  do_stop || RETVAL="$?"
  case "$RETVAL" in
  0 )
    log_success_msg "${APP_NAME}"
    ;;
  * )
    log_failure_msg "${APP_NAME}"
    exit 1
    ;;
  esac
  ;;
"reload" )
  echo -n "Reloading ${APP_NAME}: "
  if ! do_configtest; then
    log_failure_msg "${APP_NAME}"
    exit 1
  fi
  if do_reload; then
    log_success_msg "${APP_NAME}"
  else
    log_failure_msg "${APP_NAME}"
    exit 1
  fi
  ;;
"restart" )
  echo -n "Restarting ${APP_NAME}: "
  if do_restart; then
    log_success_msg "${APP_NAME}"
  else
    log_failure_msg "${APP_NAME}"
    exit 1
  fi
  ;;
"status" )
  if kill_by_file -0 "${APP_PID_FILE}"; then
    log_success_msg "${APP_NAME} is running"
  else
    log_failure_msg "${APP_NAME} is not running"
    exit 1
  fi
  ;;
"condrestart" )
  if [ -f "${APP_LOCK_FILE}" ]; then
    echo -n "Restarting ${APP_NAME}: "
    if do_restart; then
      log_success_msg "${APP_NAME}"
    else
      log_failure_msg "${APP_NAME}"
      exit 1
    fi
  fi
  ;;
"configtest" )
  if do_configtest; then
    log_success_msg "${APP_NAME}"
  else
    log_failure_msg "${APP_NAME}"
    exit 1
  fi
  ;;
* )
  echo "Usage: $0 {start|stop|reload|restart|status}" >&2
  exit 1
  ;;
esac

