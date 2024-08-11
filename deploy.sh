#!/bin/bash

echo ">>> Installing firebase-tools (it's a node tool)"
npm install -g firebase-tools

echo ">>> Installing all npm dependencies"
npm install

echo ">>> Cleaning the repo"
sbt clean

# tests only pass with the DEV API key
#echo ">>> Unit-testing"
#sbt test

echo ">>> Performing full JS link"
sbt fullLinkJS

echo ">>> Building"
npm run build

echo ">>> Copying firebase.json to the dist folder in prep for deploy"
cp firebase.json dist

echo ">>> Deploying to Firebase"
firebase deploy --public dist
