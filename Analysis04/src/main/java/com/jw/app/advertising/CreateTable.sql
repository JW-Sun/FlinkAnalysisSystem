CREATE EXTERNAL TABLE AdvertisingUserInfo(groupByFieldString String,times Int,deviceType String,timeInfo String,userId String,adId String,productId String) PARTITIONED BY (dayinfo string,hourinfo string) row format serde 'org.openx.data.jsonserde.JsonSerDe'stored as textfile location '/project/FlinkClickHouse/ChannelAnalysis/';

ALTER TABLE AdvertisingUserInfo ADD PARTITION (dayinfo='20200808', dayinfo='08') LOCATION '/project/FlinkClickHouse/ChannelAnalysis/20200808/08';


