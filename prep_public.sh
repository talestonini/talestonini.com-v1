#!/bin/bash
# Prepares the public directory for both Vite and Firebase.

public_dir=$1
scala_ver=3.5.2

rm -rf $public_dir/css
rm -rf $public_dir/favicom
rm -rf $public_dir/fonts
rm -rf $public_dir/img
rm -rf $public_dir/js

mkdir -p $public_dir/css
mkdir -p $public_dir/favicom
mkdir -p $public_dir/fonts
mkdir -p $public_dir/img
mkdir -p $public_dir/js

cp -R target/scala-$scala_ver/classes/css/* $public_dir/css
cp -R target/scala-$scala_ver/classes/favicom/* $public_dir/favicom
cp -R target/scala-$scala_ver/classes/fonts/* $public_dir/fonts
cp -R target/scala-$scala_ver/classes/img/* $public_dir/img
cp -R target/scala-$scala_ver/classes/js/* $public_dir/js

cp firebase.json $public_dir
cp index.html $public_dir
