#!/bin/bash
if [ "$1" = 'sushi' ]; then
    exec sushi ${@:2}
elif [ "$1" = 'validator' ]; then
    exec java -Xmx2048m -jar validator_cli.jar ${@:2}
elif [ "$1" = 'publisher' ]; then
    exec java -Xmx2048m -jar publisher.jar ${@:2}
elif [ "$1" = 'test' ]; then
    exec java -Xmx2048m -jar junit_cli.jar -cp engine.jar:validator_cli.jar ${@:2}
else
    echo "ERROR: Unknown argument: $1"
    echo "Valid arguments are [sushi, validator, publisher, test]"
    exit 1
fi