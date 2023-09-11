#!/bin/bash

sbt clean

# tests only pass with the DEV API key
#sbt test

sbt fullLinkJS
npm run build
cp firebase.json dist
firebase deploy --public dist
