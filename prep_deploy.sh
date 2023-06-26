#!/bin/bash

serve_dir=$1

rm -rf $serve_dir/css
rm -rf $serve_dir/favicom
rm -rf $serve_dir/fonts
rm -rf $serve_dir/img
rm -rf $serve_dir/js

mkdir -p $serve_dir/css
mkdir -p $serve_dir/favicom
mkdir -p $serve_dir/fonts
mkdir -p $serve_dir/img
mkdir -p $serve_dir/js

cp -r target/scala-3.3.0/classes/css/* $serve_dir/css
cp -r target/scala-3.3.0/classes/favicom/* $serve_dir/favicom
cp -r target/scala-3.3.0/classes/fonts/* $serve_dir/fonts
cp -r target/scala-3.3.0/classes/img/* $serve_dir/img
cp -r target/scala-3.3.0/classes/js/* $serve_dir/js

#cp index.html $serve_dir
#cp main.js $serve_dir
#cp firebase.json $serve_dir
