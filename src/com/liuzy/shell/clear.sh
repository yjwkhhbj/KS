#!/bin/bash

echo '清除CA密钥和签发库'
cd /etc/pki/CA
rm -rf ca*
rm -rf index.*
rm -rf serial*
rm -rf private/*
touch index.txt
echo '01' > serial
echo '' >> serial
echo '完成'