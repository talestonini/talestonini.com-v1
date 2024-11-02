#!/bin/bash

echo ">>> Step 1: Clean the repo"
sbt clean

echo ">>> Step 2: Perform full JS link (compile and build Scala.js code)"
sbt fullLinkJS

echo ">>> Step 3: Prepare public assets directory for hosting in Firebase"
./prep_public.sh public

echo ">>> Step 4: Instal firebase-tools (it's a node tool)"
npm install -g firebase-tools

echo ">>> Step 5: Instal all npm dependencies"
npm install

echo ">>> Step 6: Build the website with Vite (vite build)"
npm run build

echo ">>> Step 7: Deploy to Firebase"
firebase deploy --public dist
