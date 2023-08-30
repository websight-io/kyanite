#!/usr/bin/bash

# exits with error code if there are fails in the Backstop CI report file
# exit 1 - no errors detected
# exit 0 - some errors detected

# config
BASEDIR=$(dirname "$0")
NODEDIR="$BASEDIR"/../../../../tests/end-to-end
BACKSTOPDIR="$NODEDIR"/backstop

# read report
BACKSTOP_FAILS=$(sed -n 's/.*failures="\([0-9]*\)".*/\1/p' "${BACKSTOPDIR}/ci_report/xunit.xml");

# result
if ((BACKSTOP_FAILS > 0)); then
  echo "ERROR | Backstop CI report contains failed test cases"
  exit 0;
else
  echo "OK | Backstop CI report contains 0 failed test cases"
  exit 1; # use to break execution of next scripts in chain
fi
