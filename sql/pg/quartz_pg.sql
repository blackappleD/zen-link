DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;


-- ----------------------------
-- Table structure for qrtz_job_details
-- 1、存储每一个已配置的 jobDetail 的详细信息
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_job_details";
CREATE TABLE "qrtz_job_details"
(
    "sched_name"        varchar(120) NOT NULL,
    "job_name"          varchar(200) NOT NULL,
    "job_group"         varchar(200) NOT NULL,
    "description"       varchar(250),
    "job_class_name"    varchar(250) NOT NULL,
    "is_durable"        bool   NOT NULL,
    "is_nonconcurrent"  bool   NOT NULL,
    "is_update_data"    bool   NOT NULL,
    "requests_recovery" bool   NOT NULL,
    "job_data" bytea,
    primary key (sched_name, job_name, job_group)
)
;
COMMENT ON COLUMN "qrtz_job_details"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_job_details"."job_name" IS '任务名称';
COMMENT ON COLUMN "qrtz_job_details"."job_group" IS '任务组名';
COMMENT ON COLUMN "qrtz_job_details"."description" IS '相关介绍';
COMMENT ON COLUMN "qrtz_job_details"."job_class_name" IS '执行任务类名称';
COMMENT ON COLUMN "qrtz_job_details"."is_durable" IS '是否持久化';
COMMENT ON COLUMN "qrtz_job_details"."is_nonconcurrent" IS '是否并发';
COMMENT ON COLUMN "qrtz_job_details"."is_update_data" IS '是否更新数据';
COMMENT ON COLUMN "qrtz_job_details"."requests_recovery" IS '是否接受恢复执行';
COMMENT ON COLUMN "qrtz_job_details"."job_data" IS '存放持久化job对象';
COMMENT ON TABLE "qrtz_job_details" IS '任务详细信息表';




-- ----------------------------
-- Table structure for qrtz_triggers
-- 2、 存储已配置的 Trigger 的信息
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_triggers";
CREATE TABLE "qrtz_triggers"
(
    "sched_name"    varchar(120) NOT NULL,
    "trigger_name"  varchar(200) NOT NULL,
    "trigger_group" varchar(200) NOT NULL,
    "job_name"      varchar(200) NOT NULL,
    "job_group"     varchar(200) NOT NULL,
    "description"   varchar(250),
    "next_fire_time" int8,
    "prev_fire_time" int8,
    "priority" int4,
    "trigger_state" varchar(16)  NOT NULL,
    "trigger_type"  varchar(8)   NOT NULL,
    "start_time" int8 NOT NULL,
    "end_time" int8,
    "calendar_name" varchar(200),
    "misfire_instr" int2,
    "job_data" bytea,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, job_name, job_group) references QRTZ_JOB_DETAILS(sched_name, job_name, job_group)
);
COMMENT ON COLUMN "qrtz_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_triggers"."trigger_name" IS '触发器的名字';
COMMENT ON COLUMN "qrtz_triggers"."trigger_group" IS '触发器所属组的名字';
COMMENT ON COLUMN "qrtz_triggers"."job_name" IS 'qrtz_job_details表job_name的外键';
COMMENT ON COLUMN "qrtz_triggers"."job_group" IS 'qrtz_job_details表job_group的外键';
COMMENT ON COLUMN "qrtz_triggers"."description" IS '相关介绍';
COMMENT ON COLUMN "qrtz_triggers"."next_fire_time" IS '上一次触发时间（毫秒）';
COMMENT ON COLUMN "qrtz_triggers"."prev_fire_time" IS '下一次触发时间（默认为-1表示不触发）';
COMMENT ON COLUMN "qrtz_triggers"."priority" IS '优先级';
COMMENT ON COLUMN "qrtz_triggers"."trigger_state" IS '触发器状态';
COMMENT ON COLUMN "qrtz_triggers"."trigger_type" IS '触发器的类型';
COMMENT ON COLUMN "qrtz_triggers"."start_time" IS '开始时间';
COMMENT ON COLUMN "qrtz_triggers"."end_time" IS '结束时间';
COMMENT ON COLUMN "qrtz_triggers"."calendar_name" IS '日程表名称';
COMMENT ON COLUMN "qrtz_triggers"."misfire_instr" IS '补偿执行的策略';
COMMENT ON COLUMN "qrtz_triggers"."job_data" IS '存放持久化job对象';
COMMENT ON TABLE "qrtz_triggers" IS '触发器详细信息表';





-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- 3、 存储简单的 Trigger，包括重复次数，间隔，以及已触发的次数
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_simple_triggers";
CREATE TABLE "qrtz_simple_triggers"
(
    "sched_name"    varchar(120) NOT NULL,
    "trigger_name"  varchar(200) NOT NULL,
    "trigger_group" varchar(200) NOT NULL,
    "repeat_count" int8 NOT NULL,
    "repeat_interval" int8 NOT NULL,
    "times_triggered" int8 NOT NULL
)
;
COMMENT ON COLUMN "qrtz_simple_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_simple_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT ON COLUMN "qrtz_simple_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON COLUMN "qrtz_simple_triggers"."repeat_count" IS '重复的次数统计';
COMMENT ON COLUMN "qrtz_simple_triggers"."repeat_interval" IS '重复的间隔时间';
COMMENT ON COLUMN "qrtz_simple_triggers"."times_triggered" IS '已经触发的次数';
COMMENT ON TABLE "qrtz_simple_triggers" IS '简单触发器的信息表';




-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- 4、 存储 Cron Trigger，包括 Cron 表达式和时区信息
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_cron_triggers";
CREATE TABLE "qrtz_cron_triggers"
(
    "sched_name"      varchar(120) NOT NULL,
    "trigger_name"    varchar(200) NOT NULL,
    "trigger_group"   varchar(200) NOT NULL,
    "cron_expression" varchar(200) NOT NULL,
    "time_zone_id"    varchar(80),
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
)
;
COMMENT ON COLUMN "qrtz_cron_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_cron_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT ON COLUMN "qrtz_cron_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON COLUMN "qrtz_cron_triggers"."cron_expression" IS 'cron表达式';
COMMENT ON COLUMN "qrtz_cron_triggers"."time_zone_id" IS '时区';
COMMENT ON TABLE "qrtz_cron_triggers" IS 'Cron类型的触发器表';


-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- 5、 Trigger 作为 Blob 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，JobStore 并不知道如何存储实例的时候)
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_blob_triggers";
CREATE TABLE "qrtz_blob_triggers"
(
    "sched_name"    varchar(120) NOT NULL,
    "trigger_name"  varchar(200) NOT NULL,
    "trigger_group" varchar(200) NOT NULL,
    "blob_data" bytea,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
);
COMMENT ON COLUMN "qrtz_blob_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_blob_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT ON COLUMN "qrtz_blob_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON COLUMN "qrtz_blob_triggers"."blob_data" IS '存放持久化Trigger对象';
COMMENT ON TABLE "qrtz_blob_triggers" IS 'Blob类型的触发器表';









-- ----------------------------
-- Table structure for qrtz_calendars
-- 6、 以 Blob 类型存储存放日历信息， quartz可配置一个日历来指定一个时间范围
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_calendars";
CREATE TABLE "qrtz_calendars"
(
    "sched_name"    varchar(120) NOT NULL,
    "calendar_name" varchar(200) NOT NULL,
    "calendar" bytea NOT NULL,
    primary key (sched_name, calendar_name)
)
;
COMMENT ON COLUMN "qrtz_calendars"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_calendars"."calendar_name" IS '日历名称';
COMMENT ON COLUMN "qrtz_calendars"."calendar" IS '存放持久化calendar对象';
COMMENT ON TABLE "qrtz_calendars" IS '日历信息表';


-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- 7、 存储已暂停的 Trigger 组的信息
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_paused_trigger_grps";
CREATE TABLE "qrtz_paused_trigger_grps"
(
    "sched_name"    varchar(120) NOT NULL,
    "trigger_group" varchar(200) NOT NULL,
     primary key (sched_name, trigger_group)
)
;
COMMENT ON COLUMN "qrtz_paused_trigger_grps"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_paused_trigger_grps"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON TABLE "qrtz_paused_trigger_grps" IS '暂停的触发器表';





-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- 存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_fired_triggers";
CREATE TABLE "qrtz_fired_triggers"
(
    "sched_name"        varchar(120) NOT NULL,
    "entry_id"          varchar(95)  NOT NULL,
    "trigger_name"      varchar(200) NOT NULL,
    "trigger_group"     varchar(200) NOT NULL,
    "instance_name"     varchar(200) NOT NULL,
    "fired_time" int8 NOT NULL,
    "sched_time" int8 NOT NULL,
    "priority" int4 NOT NULL,
    "state"             varchar(16)  NOT NULL,
    "job_name"          varchar(200),
    "job_group"         varchar(200),
    "is_nonconcurrent"  bool,
    "requests_recovery" bool,
    primary key (sched_name, entry_id)
)
;
COMMENT ON COLUMN "qrtz_fired_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_fired_triggers"."entry_id" IS '调度器实例id';
COMMENT ON COLUMN "qrtz_fired_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT ON COLUMN "qrtz_fired_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON COLUMN "qrtz_fired_triggers"."instance_name" IS '调度器实例名';
COMMENT ON COLUMN "qrtz_fired_triggers"."fired_time" IS '触发的时间';
COMMENT ON COLUMN "qrtz_fired_triggers"."sched_time" IS '定时器制定的时间';
COMMENT ON COLUMN "qrtz_fired_triggers"."priority" IS '优先级';
COMMENT ON COLUMN "qrtz_fired_triggers"."state" IS '状态';
COMMENT ON COLUMN "qrtz_fired_triggers"."job_name" IS '任务名称';
COMMENT ON COLUMN "qrtz_fired_triggers"."job_group" IS '任务组名';
COMMENT ON COLUMN "qrtz_fired_triggers"."is_nonconcurrent" IS '是否并发';
COMMENT ON COLUMN "qrtz_fired_triggers"."requests_recovery" IS '是否接受恢复执行';
COMMENT ON TABLE "qrtz_fired_triggers" IS '已触发的触发器表';





-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- 9、 存储少量的有关 Scheduler 的状态信息，假如是用于集群中，可以看到其他的 Scheduler 实例
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_scheduler_state";
CREATE TABLE "qrtz_scheduler_state"
(
    "sched_name"    varchar(120) NOT NULL,
    "instance_name" varchar(200) NOT NULL,
    "last_checkin_time" int8 NOT NULL,
    "checkin_interval" int8 NOT NULL,
    primary key (sched_name, instance_name)
)
;
COMMENT ON COLUMN "qrtz_scheduler_state"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_scheduler_state"."instance_name" IS '实例名称';
COMMENT ON COLUMN "qrtz_scheduler_state"."last_checkin_time" IS '上次检查时间';
COMMENT ON COLUMN "qrtz_scheduler_state"."checkin_interval" IS '检查间隔时间';
COMMENT ON TABLE "qrtz_scheduler_state" IS '调度器状态表';



-- ----------------------------
-- Table structure for qrtz_locks
-- 10、 存储程序的悲观锁的信息(假如使用了悲观锁)
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_locks";
CREATE TABLE "qrtz_locks"
(
    "sched_name" varchar(120) NOT NULL,
    "lock_name"  varchar(40)  NOT NULL,
    primary key (sched_name, lock_name)
)
;
COMMENT ON COLUMN "qrtz_locks"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_locks"."lock_name" IS '悲观锁名称';
COMMENT ON TABLE "qrtz_locks" IS '存储的悲观锁信息表';




-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- 11、 Quartz集群实现同步机制的行锁表
-- ----------------------------
DROP TABLE IF EXISTS "qrtz_simprop_triggers";
CREATE TABLE "qrtz_simprop_triggers"
(
    "sched_name"    varchar(120) NOT NULL,
    "trigger_name"  varchar(200) NOT NULL,
    "trigger_group" varchar(200) NOT NULL,
    "str_prop_1"    varchar(512),
    "str_prop_2"    varchar(512),
    "str_prop_3"    varchar(512),
    "int_prop_1" int4,
    "int_prop_2" int4,
    "long_prop_1" int8,
    "long_prop_2" int8,
    "dec_prop_1"    numeric(13, 4),
    "dec_prop_2"    numeric(13, 4),
    "bool_prop_1"   varchar(1),
    "bool_prop_2"   varchar(1),
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references QRTZ_TRIGGERS(sched_name, trigger_name, trigger_group)
)
;
COMMENT ON COLUMN "qrtz_simprop_triggers"."sched_name" IS '调度名称';
COMMENT ON COLUMN "qrtz_simprop_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT ON COLUMN "qrtz_simprop_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT ON COLUMN "qrtz_simprop_triggers"."str_prop_1" IS 'String类型的trigger的第一个参数';
COMMENT ON COLUMN "qrtz_simprop_triggers"."str_prop_2" IS 'String类型的trigger的第二个参数';
COMMENT ON COLUMN "qrtz_simprop_triggers"."str_prop_3" IS 'String类型的trigger的第三个参数';
COMMENT ON COLUMN "qrtz_simprop_triggers"."int_prop_1" IS 'int类型的trigger的第一个参数';
COMMENT ON COLUMN "qrtz_simprop_triggers"."int_prop_2" IS 'int类型的trigger的第二个参数';
COMMENT ON COLUMN "qrtz_simprop_triggers"."long_prop_1" IS 'long类型的trigger的第一个参数';
COMMENT ON COLUMN "qrtz_simprop_triggers"."long_prop_2" IS 'long类型的trigger的第二个参数';
COMMENT ON COLUMN "qrtz_simprop_triggers"."dec_prop_1" IS 'decimal类型的trigger的第一个参数';
COMMENT ON COLUMN "qrtz_simprop_triggers"."dec_prop_2" IS 'decimal类型的trigger的第二个参数';
COMMENT ON COLUMN "qrtz_simprop_triggers"."bool_prop_1" IS 'Boolean类型的trigger的第一个参数';
COMMENT ON COLUMN "qrtz_simprop_triggers"."bool_prop_2" IS 'Boolean类型的trigger的第二个参数';
COMMENT ON TABLE "qrtz_simprop_triggers" IS '同步机制的行锁表';
