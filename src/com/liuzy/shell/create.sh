#!/bin/bash

echo '##### => 生成CA私钥'
cd /etc/pki/CA/private
openssl genrsa -out cakey.pem 2048

echo '##### => 生成自签证书 cn sh sh liuzy CA CA'
cd /etc/pki/CA/
openssl req -new -x509 -key private/cakey.pem -out cacert.pem -days 3650


echo '##### => 签发服务器'
cd /etc/pki/CA/
mkdir myssl
cd myssl

echo '##### => 生成nginx私钥'
openssl genrsa -out nginx.pem 2048

echo '##### => 生成nginx证书请求 cn sh sh liuzy nginx *.liuzy.com'
openssl req -new -key nginx.pem -out nginx.csr

echo '签发nginx证书'
openssl ca -in nginx.csr -out nginx.crt -days 3650



echo '##### => 签发客户端'

echo '##### => 生成client私钥'
openssl genrsa -out client.pem 2048

echo '##### => 生成client证书请求 cn sh sh liuzy client client'
openssl req -new -key client.pem -out client.csr

echo '##### => 签发client证书'
openssl ca -in client.csr -out client.crt -days 3650
