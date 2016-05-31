#! /bin/bash
set +e

done=false

host=$1
shift
path=$1
ports=$*

while [[ "$done" = false ]]; do
        for port in $ports; do
                curl -q http://${host}:${port}${path} >& /dev/null
                if [[ "$?" -eq "0" ]]; then
                        done=true
                else
                        done=false
                fi
        done
        if [[ "$done" = true ]]; then
                echo $path is available
                break;
  fi
  echo -n .
  sleep 1
done

set -e
