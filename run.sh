#!/usr/bin/env bash
set -e

JUNIT="$HOME/.m2/repository/junit/junit/4.12/junit-4.12.jar"
HAMCREST="$HOME/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
CP=".:$JUNIT:$HAMCREST"
MARKER=".deps_ready"

if [ ! -f "$MARKER" ] || [ ! -f "$JUNIT" ] || [ ! -f "$HAMCREST" ]; then
  echo "First-time setup: downloading JUnit (this only happens once)..."
  mvn -q dependency:resolve
  touch "$MARKER"
fi

javac -d . -cp "$CP" lab9/*.java

clear
java -cp "$CP" lab9.RunLab
