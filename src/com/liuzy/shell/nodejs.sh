#!/bin/bash

echo '##### => 安装nodejs'
cd /opt
wget http://nodejs.org/dist/v5.9.0/node-v5.9.0-linux-x64.tar.gz
tar xvf node-v5.9.0-linux-x64.tar.gz
mv node-v5.9.0-linux-x64 node

echo '' >> /etc/profile
echo 'export PATH=$PATH:/opt/node/bin' >> /etc/profile

source /etc/profile
node -v

echo 'registry = https://registry.npm.taobao.org' >> ~/.npmrc

echo '##### => 安装nodejs组件'
npm install express -g
npm install express-generator -g
npm install nodemon -g
npm install forever -g