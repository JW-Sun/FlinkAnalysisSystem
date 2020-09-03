#!/bin/bash
day=`date -d "1 hour ago" +"%Y%m%d"`
hour=`date -d "1 hour ago" +"%H"`
echo "$day $hour"
echo "alter table ChannelUserInfo add partition(dayinfo=\"$day\",hourinfo=\"$hour\") location \"/project/FlinkClickHouse/ChannelAnalysis/$day/$hour\";"
/usr/lib/hive/bin/hive -e "alter table ChannelUserInfo add partition(dayinfo=\"$day\",hourinfo=\"$hour\") location \"/project/FlinkClickHouse/ChannelAnalysis/$day/$hour\";"

0 * * * * /root/addchannelparti.sh

crontab -e 中进行添加

