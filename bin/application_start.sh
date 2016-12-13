#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

systemctl daemon-reload
systemctl restart myezteam-ws.service
RC=$?
if [ $RC -ne 0 ]; then
    exit 1
fi

echo "Exiting $0 with $RC"
exit ${RC}
