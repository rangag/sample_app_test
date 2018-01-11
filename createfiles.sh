/usr/bin/curl -i -X PUT -T /opt/tomcat/webapps/webhdfs/$2 "http://34.251.101.50:50075/webhdfs/v1/$1/$2?op=CREATE&namenoderpcaddress=ec2-34-242-108-96.eu-west-1.compute.amazonaws.com:8020&createparent=true&overwrite=false&permission=$3&user.name=hdfs"

