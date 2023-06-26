#!/bin/bash

sbt clean

# tests only pass with the DEV API key
#sbt test

sbt fullLinkJS

./prep_deploy.sh public
firebase deploy
