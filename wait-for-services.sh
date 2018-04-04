#! /bin/bash
set +e

done=false

host=$1
shift
path=$1
shift
ports=$*

while [[ "$done" = false ]]; do
        for port in $ports; do
                curl -q http://${host}:${port}${path} >& /dev/null
                if [[ "$?" -eq "0" ]]; then
                        echo "this happened"
                        done=true
                else
                        echo 'this other loop happened'
                        done=false
			break
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
