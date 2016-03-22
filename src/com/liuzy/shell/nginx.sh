#!/bin/bash

echo '##### => 安装nginx'
wget http://nginx.org/packages/centos/6/noarch/RPMS/nginx-release-centos-6-0.el6.ngx.noarch.rpm
rpm -ivh nginx-release-centos-6-0.el6.ngx.noarch.rpm
yum -y install nginx

echo '##### => 启动nginx'
nginx

echo '##### => 关闭防火墙'
service iptables stop
