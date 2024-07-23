DROP TABLE IF EXISTS "business"."t_mer_amount_record" ;

DROP TABLE IF EXISTS "business"."t_mer_info" ;

DROP TABLE IF EXISTS "business"."t_mer_report" ;

DROP TABLE IF EXISTS "business"."t_mer_req_log" ;

DROP TABLE IF EXISTS "business"."t_product" ;

DROP TABLE IF EXISTS "business"."t_product_sell" ;

DROP TABLE IF EXISTS "business"."t_sup_report" ;

DROP TABLE IF EXISTS "business"."t_sup_req_log" ;

DROP TABLE IF EXISTS "business"."t_supplier" ;

DROP TABLE IF EXISTS "business"."t_supplier_product" ;

DROP TABLE IF EXISTS "business"."t_supplier_route" ;








DROP SEQUENCE IF EXISTS "business"."t_mer_amount_record_id_seq" ;

CREATE
SEQUENCE "business"."t_mer_amount_record_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 9223372036854775807
	START
WITH 1000;

DROP SEQUENCE IF EXISTS "business"."t_mer_info_id_seq" ;

CREATE
SEQUENCE "business"."t_mer_info_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 2147483647
	START
WITH 1000;

DROP SEQUENCE IF EXISTS "business"."t_mer_report_id_seq" ;

CREATE
SEQUENCE "business"."t_mer_report_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 9223372036854775807
	START
WITH 1000;

DROP SEQUENCE IF EXISTS "business"."t_mer_req_log_id_seq" ;

CREATE
SEQUENCE "business"."t_mer_req_log_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 9223372036854775807
	START
WITH 1000;

DROP SEQUENCE IF EXISTS "business"."t_product_id_seq" ;

CREATE
SEQUENCE "business"."t_product_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 2147483647
	START
WITH 1000;

DROP SEQUENCE IF EXISTS "business"."t_product_sell_id_seq" ;

CREATE
SEQUENCE "business"."t_product_sell_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 2147483647
	START
WITH 1000;

DROP SEQUENCE IF EXISTS "business"."t_sup_report_id_seq" ;

CREATE
SEQUENCE "business"."t_sup_report_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 9223372036854775807
	START
WITH 1000;

DROP SEQUENCE IF EXISTS "business"."t_sup_req_log_id_seq" ;

CREATE
SEQUENCE "business"."t_sup_req_log_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 9223372036854775807
	START
WITH 1000;

DROP SEQUENCE IF EXISTS "business"."t_supplier_id_seq" ;

CREATE
SEQUENCE "business"."t_supplier_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 32767
	START
WITH 1000;

DROP SEQUENCE IF EXISTS "business"."t_supplier_product_id_seq" ;

CREATE
SEQUENCE "business"."t_supplier_product_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 2147483647
	START
WITH 1000;

DROP SEQUENCE IF EXISTS "business"."t_supplier_route_id_seq" ;

CREATE
SEQUENCE "business"."t_supplier_route_id_seq" INCREMENT BY 1
	MINVALUE 1 MAXVALUE 2147483647
	START
WITH 1000;



CREATE TABLE "business"."t_mer_amount_record"
(
    "id"            bigint         NOT NULL DEFAULT nextval('t_mer_amount_record_id_seq'::regclass),
    "mer_code"      varchar(50)    NOT NULL,
    "mer_name"      varchar(50)    NOT NULL,
    "type"          varchar(5)     NOT NULL,
    "amount"        numeric(10, 4) NOT NULL,
    "order_no"      varchar(50),
    "order_time"    timestamp,
    "create_by"     varchar(50)    NOT NULL,
    "update_by"     varchar(50)    NOT NULL,
    "create_time"   timestamp,
    "update_time"   timestamp,
    "remark"        varchar(200),
    "before_amount" numeric(12, 4) NOT NULL DEFAULT 0,
    "after_amount"  numeric(12, 4) NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "business"."t_mer_amount_record"."id" IS '编号';
COMMENT ON COLUMN "business"."t_mer_amount_record"."mer_code" IS '商户code';
COMMENT ON COLUMN "business"."t_mer_amount_record"."mer_name" IS '商户名称';
COMMENT ON COLUMN "business"."t_mer_amount_record"."type" IS '变更类型 a 充值  d,手动扣减，c 系统扣减';
COMMENT ON COLUMN "business"."t_mer_amount_record"."amount" IS '金额 操作的金额';
COMMENT ON COLUMN "business"."t_mer_amount_record"."order_no" IS '流水号';
COMMENT ON COLUMN "business"."t_mer_amount_record"."order_time" IS '订单时间';
COMMENT ON COLUMN "business"."t_mer_amount_record"."create_by" IS '创建者';
COMMENT ON COLUMN "business"."t_mer_amount_record"."update_by" IS '更新者';
COMMENT ON COLUMN "business"."t_mer_amount_record"."create_time" IS '创建时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_mer_amount_record"."update_time" IS '更新时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_mer_amount_record"."remark" IS '备注';
COMMENT ON COLUMN "business"."t_mer_amount_record"."before_amount" IS '金额 操作的余额';
COMMENT ON COLUMN "business"."t_mer_amount_record"."after_amount" IS '金额 操作后的余额';
COMMENT ON TABLE "business"."t_mer_amount_record" IS '商户余额操作记录表';


CREATE TABLE "business"."t_mer_info"
(
    "id"              integer        NOT NULL DEFAULT nextval('t_mer_info_id_seq'::regclass),
    "mer_code"        varchar(50)    NOT NULL,
    "mer_name"        varchar(100)   NOT NULL,
    "sell_person"     varchar(50)    NOT NULL,
    "invoice_type"    varchar(2)     NOT NULL,
    "bill_type"       varchar(2)     NOT NULL,
    "mer_type"        varchar(50)    NOT NULL,
    "email"           varchar(100)   NOT NULL,
    "settle_type"     varchar(2)     NOT NULL,
    "line_of_credit"  numeric(10, 2) NOT NULL DEFAULT 0,
    "status"          varchar(2)     NOT NULL,
    "sign_key"        varchar(100)   NOT NULL,
    "sign_pwd"        varchar(100)   NOT NULL,
    "balance"         numeric(12, 4) NOT NULL,
    "protocol_status" varchar(2)     NOT NULL,
    "ips"             varchar(500)   NOT NULL,
    "star_time"       timestamp,
    "end_time"        timestamp,
    "create_by"       varchar(50)    NOT NULL,
    "update_by"       varchar(50)    NOT NULL,
    "create_time"     timestamp,
    "update_time"     timestamp,
    "remark"          varchar(200),
    "warn_amount"     numeric(12, 4) NOT NULL DEFAULT 2000,
    CONSTRAINT "pk_business_t_mer_info" PRIMARY KEY ("mer_code")
)
;
COMMENT ON COLUMN "business"."t_mer_info"."id" IS '编号';
COMMENT ON COLUMN "business"."t_mer_info"."mer_code" IS '商户编码';
COMMENT ON COLUMN "business"."t_mer_info"."mer_name" IS '商户名称';
COMMENT ON COLUMN "business"."t_mer_info"."sell_person" IS '销售负责人';
COMMENT ON COLUMN "business"."t_mer_info"."invoice_type" IS '发票类型 1.电子，2.纸质';
COMMENT ON COLUMN "business"."t_mer_info"."bill_type" IS '账单类型 1.每日 , 2.每周 , 3.每月，4.季度';
COMMENT ON COLUMN "business"."t_mer_info"."mer_type" IS '客户类型';
COMMENT ON COLUMN "business"."t_mer_info"."email" IS '商户联系人邮箱';
COMMENT ON COLUMN "business"."t_mer_info"."settle_type" IS '结算方式 y预付费，h后付费';
COMMENT ON COLUMN "business"."t_mer_info"."line_of_credit" IS '后付费授信额度';
COMMENT ON COLUMN "business"."t_mer_info"."status" IS '状态 00 启用，01 停用 ';
COMMENT ON COLUMN "business"."t_mer_info"."sign_key" IS '签名key 令牌';
COMMENT ON COLUMN "business"."t_mer_info"."sign_pwd" IS '秘钥 秘钥';
COMMENT ON COLUMN "business"."t_mer_info"."balance" IS '账户余额';
COMMENT ON COLUMN "business"."t_mer_info"."protocol_status" IS '协议状态 00 已签约, 01 未签约';
COMMENT ON COLUMN "business"."t_mer_info"."ips" IS '商户ip白名单';
COMMENT ON COLUMN "business"."t_mer_info"."star_time" IS '协议开始时间';
COMMENT ON COLUMN "business"."t_mer_info"."end_time" IS '协议结束时间';
COMMENT ON COLUMN "business"."t_mer_info"."create_by" IS '创建者';
COMMENT ON COLUMN "business"."t_mer_info"."update_by" IS '更新者';
COMMENT ON COLUMN "business"."t_mer_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "business"."t_mer_info"."update_time" IS '更新时间';
COMMENT ON COLUMN "business"."t_mer_info"."remark" IS '备注';
COMMENT ON COLUMN "business"."t_mer_info"."warn_amount" IS '账户预警金额（元）';
COMMENT ON TABLE "business"."t_mer_info" IS '商户基本信息表';


CREATE TABLE "business"."t_mer_report"
(
    "id"              bigint      NOT NULL DEFAULT nextval('t_mer_report_id_seq'::regclass),
    "sup_code"        varchar(50),
    "sup_name"        varchar(50),
    "mer_code"        varchar(50) NOT NULL,
    "mer_name"        varchar(50) NOT NULL,
    "product_code"    varchar(50) NOT NULL,
    "product_name"    varchar(50) NOT NULL,
    "cg_code"         varchar(50) NOT NULL,
    "in_price"        numeric     NOT NULL,
    "sell_price"      numeric,
    "total_price"     numeric     NOT NULL,
    "req_date"        timestamp without time zone NOT NULL,
    "status_ok_fit"   integer     NOT NULL,
    "status_ok_unfit" integer     NOT NULL,
    "status_no"       integer     NOT NULL,
    "status_err"      integer     NOT NULL,
    "avg_time"        integer     NOT NULL,
    "times1"          integer     NOT NULL,
    "times3"          integer     NOT NULL,
    "times10"         integer     NOT NULL,
    "times_ge10"      integer     NOT NULL,
    "create_time"     timestamp without time zone DEFAULT now(),
    "update_time"     timestamp without time zone DEFAULT now(),
    "fee_times"       integer,
    CONSTRAINT "pk_business_t_mer_report" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "business"."t_mer_report"."id" IS '编号';
COMMENT ON COLUMN "business"."t_mer_report"."sup_code" IS '供应商编码';
COMMENT ON COLUMN "business"."t_mer_report"."sup_name" IS '供应商名称';
COMMENT ON COLUMN "business"."t_mer_report"."mer_code" IS '商户编码';
COMMENT ON COLUMN "business"."t_mer_report"."mer_name" IS '商户名称';
COMMENT ON COLUMN "business"."t_mer_report"."product_code" IS '产品编码';
COMMENT ON COLUMN "business"."t_mer_report"."product_name" IS '产品名称';
COMMENT ON COLUMN "business"."t_mer_report"."cg_code" IS '产品分类编码';
COMMENT ON COLUMN "business"."t_mer_report"."in_price" IS '总成本价';
COMMENT ON COLUMN "business"."t_mer_report"."sell_price" IS '销售价';
COMMENT ON COLUMN "business"."t_mer_report"."total_price" IS '总价';
COMMENT ON COLUMN "business"."t_mer_report"."req_date" IS '调用日期';
COMMENT ON COLUMN "business"."t_mer_report"."status_ok_fit" IS '查得匹配次数';
COMMENT ON COLUMN "business"."t_mer_report"."status_ok_unfit" IS '查得不匹配次数';
COMMENT ON COLUMN "business"."t_mer_report"."status_no" IS '查无次数';
COMMENT ON COLUMN "business"."t_mer_report"."status_err" IS '异常次数';
COMMENT ON COLUMN "business"."t_mer_report"."avg_time" IS '平均响应时间ms';
COMMENT ON COLUMN "business"."t_mer_report"."times1" IS '响应时间1秒内请求次数 不包含1秒';
COMMENT ON COLUMN "business"."t_mer_report"."times3" IS '响应时间1-3秒内请求次数 不包含3秒';
COMMENT ON COLUMN "business"."t_mer_report"."times10" IS '响应时间3-10秒内请求次数 不包含10秒';
COMMENT ON COLUMN "business"."t_mer_report"."times_ge10" IS '响应时间大于等于10秒请求次数';
COMMENT ON COLUMN "business"."t_mer_report"."create_time" IS '创建时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_mer_report"."update_time" IS '更新时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_mer_report"."fee_times" IS '收费次数';
COMMENT ON TABLE "business"."t_mer_report" IS '商户调用日志统计';


CREATE TABLE "business"."t_mer_req_log"
(
    "id"           bigint         NOT NULL DEFAULT nextval('t_mer_req_log_id_seq'::regclass),
    "order_no"     varchar(50)    NOT NULL,
    "cg_code"      varchar(50)    NOT NULL,
    "product_code" varchar(50)    NOT NULL,
    "product_name" varchar(100)   NOT NULL,
    "mer_code"     varchar(100)   NOT NULL,
    "mer_name"     varchar(100)   NOT NULL,
    "mer_seq"      varchar(50)    NOT NULL,
    "sup_code"     varchar(100),
    "sup_name"     varchar(100),
    "sell_price"   numeric(12, 4) NOT NULL DEFAULT 0,
    "actual_price" numeric(12, 4) NOT NULL DEFAULT 0,
    "in_price"     numeric(12, 4) NOT NULL DEFAULT 0,
    "req_json"     varchar(2000),
    "resp_json"    text,
    "status"       varchar(2),
    "free"         varchar(2)     NOT NULL,
    "req_time"     timestamp,
    "resp_time"    timestamp,
    "total_time"   integer,
    "create_time"  timestamp,
    "ipaddr"       varchar(200),
    "remark"       varchar(200),
    CONSTRAINT "pk_business_t_mer_req_log" PRIMARY KEY ("order_no")
)
;
COMMENT ON COLUMN "business"."t_mer_req_log"."id" IS '编号';
COMMENT ON COLUMN "business"."t_mer_req_log"."order_no" IS '流水号';
COMMENT ON COLUMN "business"."t_mer_req_log"."cg_code" IS '产品分类编码';
COMMENT ON COLUMN "business"."t_mer_req_log"."product_code" IS '产品code';
COMMENT ON COLUMN "business"."t_mer_req_log"."product_name" IS '产品名称';
COMMENT ON COLUMN "business"."t_mer_req_log"."mer_code" IS '商户code';
COMMENT ON COLUMN "business"."t_mer_req_log"."mer_name" IS '商户名';
COMMENT ON COLUMN "business"."t_mer_req_log"."mer_seq" IS '商户请求流水号';
COMMENT ON COLUMN "business"."t_mer_req_log"."sup_code" IS '供应商code';
COMMENT ON COLUMN "business"."t_mer_req_log"."sup_name" IS '供应商code';
COMMENT ON COLUMN "business"."t_mer_req_log"."sell_price" IS '产品售价(元)';
COMMENT ON COLUMN "business"."t_mer_req_log"."actual_price" IS '实际售价(元)';
COMMENT ON COLUMN "business"."t_mer_req_log"."in_price" IS '进价(元)';
COMMENT ON COLUMN "business"."t_mer_req_log"."req_json" IS '商户请求参数';
COMMENT ON COLUMN "business"."t_mer_req_log"."resp_json" IS '响应商户参数';
COMMENT ON COLUMN "business"."t_mer_req_log"."status" IS '查询状态 1 成功或一致，2 查无，3 不一致，4 异常';
COMMENT ON COLUMN "business"."t_mer_req_log"."free" IS '是否收费 0 否，1 是';
COMMENT ON COLUMN "business"."t_mer_req_log"."req_time" IS '请求时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_mer_req_log"."resp_time" IS '响应时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_mer_req_log"."total_time" IS '总耗时(毫秒)';
COMMENT ON COLUMN "business"."t_mer_req_log"."create_time" IS '创建时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_mer_req_log"."ipaddr" IS 'ip地址';
COMMENT ON COLUMN "business"."t_mer_req_log"."remark" IS '备注';
COMMENT ON TABLE "business"."t_mer_req_log" IS '商户调用日志表';

CREATE INDEX "idx_mer_req_log_mctime"
    ON "business"."t_mer_req_log" USING btree ( "mer_code" COLLATE "pg_catalog"."default" ,"req_time" )
;





CREATE TABLE "business"."t_product"
(
    "id"           integer     NOT NULL DEFAULT nextval('t_product_id_seq'::regclass),
    "product_code" varchar(50) NOT NULL,
    "product_name" varchar(50) NOT NULL,
    "cg_code"      varchar(50) NOT NULL,
    "status"       varchar(2)  NOT NULL,
    "create_by"    varchar(50) NOT NULL,
    "update_by"    varchar(50),
    "create_time"  timestamp,
    "update_time"  timestamp,
    "remark"       varchar(200),
    CONSTRAINT "pk_business_t_product" PRIMARY KEY ("product_code")
)
;
COMMENT ON COLUMN "business"."t_product"."id" IS '编号';
COMMENT ON COLUMN "business"."t_product"."product_code" IS '产品code';
COMMENT ON COLUMN "business"."t_product"."product_name" IS '产品名称';
COMMENT ON COLUMN "business"."t_product"."cg_code" IS '产品分类编码';
COMMENT ON COLUMN "business"."t_product"."status" IS '状态 00启用，01停用';
COMMENT ON COLUMN "business"."t_product"."create_by" IS '创建者';
COMMENT ON COLUMN "business"."t_product"."update_by" IS '更新者';
COMMENT ON COLUMN "business"."t_product"."create_time" IS '创建时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_product"."update_time" IS '更新时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_product"."remark" IS '备注';
COMMENT ON TABLE "business"."t_product" IS '产品表';


CREATE TABLE "business"."t_product_sell"
(
    "id"           integer        NOT NULL DEFAULT nextval('t_product_sell_id_seq'::regclass),
    "cg_code"      varchar(50)    NOT NULL,
    "product_code" varchar(50)    NOT NULL,
    "product_name" varchar(50)    NOT NULL,
    "mer_code"     varchar(50)    NOT NULL,
    "status"       varchar(2)     NOT NULL,
    "sell_price"   numeric(10, 4) NOT NULL,
    "create_by"    varchar(50)    NOT NULL,
    "update_by"    varchar(50)    NOT NULL,
    "create_time"  timestamp,
    "update_time"  timestamp,
    "remark"       varchar(200),
    "route_con"    varchar(50)    NOT NULL DEFAULT '2, 4 ',
    CONSTRAINT "pk_business_t_product_sell" PRIMARY KEY ("product_code", "mer_code")
)
;
COMMENT ON COLUMN "business"."t_product_sell"."id" IS '编号';
COMMENT ON COLUMN "business"."t_product_sell"."cg_code" IS '产品分类编码';
COMMENT ON COLUMN "business"."t_product_sell"."product_code" IS '产品code';
COMMENT ON COLUMN "business"."t_product_sell"."product_name" IS '产品名称';
COMMENT ON COLUMN "business"."t_product_sell"."mer_code" IS '商户编码';
COMMENT ON COLUMN "business"."t_product_sell"."status" IS '状态 00 启用，01 停用';
COMMENT ON COLUMN "business"."t_product_sell"."sell_price" IS '售价(元)';
COMMENT ON COLUMN "business"."t_product_sell"."create_by" IS '创建者';
COMMENT ON COLUMN "business"."t_product_sell"."update_by" IS '更新者';
COMMENT ON COLUMN "business"."t_product_sell"."create_time" IS '创建时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_product_sell"."update_time" IS '更新时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_product_sell"."remark" IS '备注';
COMMENT ON COLUMN "business"."t_product_sell"."route_con" IS '供应商路由切换条件2 查无，4查询失败';
COMMENT ON TABLE "business"."t_product_sell" IS '商品销售';


CREATE TABLE "business"."t_sup_report"
(
    "id"              bigint      NOT NULL DEFAULT nextval('t_sup_report_id_seq'::regclass),
    "sup_code"        varchar(50) NOT NULL,
    "sup_name"        varchar(50) NOT NULL,
    "mer_code"        varchar(50) NOT NULL,
    "mer_name"        varchar(50) NOT NULL,
    "product_code"    varchar(50) NOT NULL,
    "product_name"    varchar(50) NOT NULL,
    "cg_code"         varchar(50) NOT NULL,
    "in_price"        numeric     NOT NULL,
    "req_date"        timestamp without time zone NOT NULL,
    "status_ok_fit"   integer     NOT NULL,
    "status_ok_unfit" integer     NOT NULL,
    "status_no"       integer     NOT NULL,
    "status_err"      integer     NOT NULL,
    "avg_time"        integer     NOT NULL,
    "times1"          integer     NOT NULL,
    "times3"          integer     NOT NULL,
    "times10"         integer     NOT NULL,
    "times_ge10"      integer     NOT NULL,
    "create_time"     timestamp without time zone DEFAULT now(),
    "update_time"     timestamp without time zone DEFAULT now(),
    "total_price"     numeric,
    CONSTRAINT "pk_business_t_sup_report" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "business"."t_sup_report"."id" IS '编号';
COMMENT ON COLUMN "business"."t_sup_report"."sup_code" IS '供应商编码';
COMMENT ON COLUMN "business"."t_sup_report"."sup_name" IS '供应商名称';
COMMENT ON COLUMN "business"."t_sup_report"."mer_code" IS '商户编码';
COMMENT ON COLUMN "business"."t_sup_report"."mer_name" IS '商户名称';
COMMENT ON COLUMN "business"."t_sup_report"."product_code" IS '产品编码';
COMMENT ON COLUMN "business"."t_sup_report"."product_name" IS '产品名称';
COMMENT ON COLUMN "business"."t_sup_report"."cg_code" IS '产品分类编码';
COMMENT ON COLUMN "business"."t_sup_report"."in_price" IS '成本价';
COMMENT ON COLUMN "business"."t_sup_report"."req_date" IS '调用日期';
COMMENT ON COLUMN "business"."t_sup_report"."status_ok_fit" IS '查得匹配次数';
COMMENT ON COLUMN "business"."t_sup_report"."status_ok_unfit" IS '查得不匹配次数';
COMMENT ON COLUMN "business"."t_sup_report"."status_no" IS '查无次数';
COMMENT ON COLUMN "business"."t_sup_report"."status_err" IS '异常次数';
COMMENT ON COLUMN "business"."t_sup_report"."avg_time" IS '平均响应时间ms';
COMMENT ON COLUMN "business"."t_sup_report"."times1" IS '响应时间1秒内请求次数 不包含1秒';
COMMENT ON COLUMN "business"."t_sup_report"."times3" IS '响应时间1-3秒内请求次数 不包含3秒';
COMMENT ON COLUMN "business"."t_sup_report"."times10" IS '响应时间3-10秒内请求次数 不包含10秒';
COMMENT ON COLUMN "business"."t_sup_report"."times_ge10" IS '响应时间大于等于10秒请求次数';
COMMENT ON COLUMN "business"."t_sup_report"."create_time" IS '创建时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_sup_report"."update_time" IS '更新时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_sup_report"."total_price" IS '总价';
COMMENT ON TABLE "business"."t_sup_report" IS '供应商调用日志统计';


CREATE TABLE "business"."t_sup_req_log"
(
    "id"            bigint         NOT NULL DEFAULT nextval('t_sup_req_log_id_seq'::regclass),
    "order_no"      varchar(50)    NOT NULL,
    "sup_code"      varchar(50)    NOT NULL,
    "mer_code"      varchar(50)    NOT NULL,
    "sup_name"      varchar(100)   NOT NULL,
    "req_json"      varchar(2000)  NOT NULL,
    "resp_json"     text,
    "sup_product"   varchar(50)    NOT NULL,
    "cg_code"       varchar(50)    NOT NULL,
    "procduct_code" varchar(50)    NOT NULL,
    "in_price"      numeric(10, 4) NOT NULL DEFAULT 0,
    "status"        varchar(2),
    "free"          varchar(2)     NOT NULL,
    "sup_seq"       varchar(100),
    "create_time"   timestamp,
    "req_time"      timestamp,
    "resp_time"     timestamp,
    "total_time"    integer,
    "remark"        varchar(200),
    CONSTRAINT "pk_business_t_sup_req_log" PRIMARY KEY ("order_no", "sup_code")
)
;
COMMENT ON COLUMN "business"."t_sup_req_log"."id" IS '编号';
COMMENT ON COLUMN "business"."t_sup_req_log"."order_no" IS '流水号';
COMMENT ON COLUMN "business"."t_sup_req_log"."sup_code" IS '供应商code';
COMMENT ON COLUMN "business"."t_sup_req_log"."sup_name" IS '供应商名称';
COMMENT ON COLUMN "business"."t_sup_req_log"."req_json" IS '请求参数Json';
COMMENT ON COLUMN "business"."t_sup_req_log"."resp_json" IS '响应参数Json';
COMMENT ON COLUMN "business"."t_sup_req_log"."sup_product" IS '供应产品id';
COMMENT ON COLUMN "business"."t_sup_req_log"."cg_code" IS '产品分类编码';
COMMENT ON COLUMN "business"."t_sup_req_log"."procduct_code" IS '产品code';
COMMENT ON COLUMN "business"."t_sup_req_log"."in_price" IS '成本价';
COMMENT ON COLUMN "business"."t_sup_req_log"."status" IS '查询状态，1 成功或一致，2 查无，3 不一致，4 异常';
COMMENT ON COLUMN "business"."t_sup_req_log"."free" IS '是否收费 0 否，1 是';
COMMENT ON COLUMN "business"."t_sup_req_log"."sup_seq" IS '供应商流水号';
COMMENT ON COLUMN "business"."t_sup_req_log"."create_time" IS '创建时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_sup_req_log"."req_time" IS '请求时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_sup_req_log"."resp_time" IS '响应时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_sup_req_log"."total_time" IS '总耗时(毫秒)';
COMMENT ON COLUMN "business"."t_sup_req_log"."remark" IS '备注';
COMMENT ON TABLE "business"."t_sup_req_log" IS '调用供应商日志表';

CREATE INDEX "idx_sup_req_log_id"
    ON "business"."t_sup_req_log" USING btree ( "id" )
;
CREATE INDEX "idx_sup_req_log_sctime"
    ON "business"."t_sup_req_log" USING btree ( "sup_code" COLLATE "pg_catalog"."default" ,"req_time" )
;


CREATE TABLE "business"."t_supplier"
(
    "id"          smallint     NOT NULL DEFAULT nextval('t_supplier_id_seq'::regclass),
    "code"        varchar(50)  NOT NULL,
    "name"        varchar(100) NOT NULL,
    "acc"         varchar(100),
    "status"      varchar(2)   NOT NULL,
    "url"         varchar(200) NOT NULL,
    "sign_pwd"    varchar(200),
    "sign_key"    varchar(200),
    "create_by"   varchar(50)  NOT NULL,
    "update_by"   varchar(50)  NOT NULL,
    "create_time" timestamp,
    "update_time" timestamp,
    "remark"      varchar(200),
    "processor"   varchar(50)  NOT NULL DEFAULT ' W ':: character varying,
    CONSTRAINT "pk_business_t_supplier" PRIMARY KEY ("code")
)
;
COMMENT ON COLUMN "business"."t_supplier"."id" IS '编号';
COMMENT ON COLUMN "business"."t_supplier"."code" IS 'code';
COMMENT ON COLUMN "business"."t_supplier"."name" IS '名称';
COMMENT ON COLUMN "business"."t_supplier"."acc" IS '账号';
COMMENT ON COLUMN "business"."t_supplier"."status" IS '状态 00 启用，01 停用';
COMMENT ON COLUMN "business"."t_supplier"."url" IS '接口地址 接口地址';
COMMENT ON COLUMN "business"."t_supplier"."sign_pwd" IS '秘钥 加签相关pwd';
COMMENT ON COLUMN "business"."t_supplier"."sign_key" IS '加签相关key';
COMMENT ON COLUMN "business"."t_supplier"."create_by" IS '创建者';
COMMENT ON COLUMN "business"."t_supplier"."update_by" IS '更新者';
COMMENT ON COLUMN "business"."t_supplier"."create_time" IS '创建时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_supplier"."update_time" IS '更新时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_supplier"."remark" IS '备注';
COMMENT ON COLUMN "business"."t_supplier"."processor" IS '供应商处理器';
COMMENT ON TABLE "business"."t_supplier" IS '供应商表';


CREATE TABLE "business"."t_supplier_product"
(
    "id"               integer      NOT NULL DEFAULT nextval('t_supplier_product_id_seq'::regclass),
    "sup_product_name" varchar(100) NOT NULL,
    "sup_product_code" varchar(100) NOT NULL,
    "sup_code"         varchar(50)  NOT NULL,
    "sup_name"         varchar(50)  NOT NULL,
    "status"           varchar(2)   NOT NULL,
    "product_code"     varchar(50),
    "in_price"         numeric(10, 4),
    "time_out"         integer               DEFAULT 4000,
    "create_by"        varchar(50)  NOT NULL,
    "update_by"        varchar(50)  NOT NULL,
    "create_time"      timestamp,
    "update_time"      timestamp,
    "remark"           varchar(200),
    CONSTRAINT "pk_business_t_supplier_product" PRIMARY KEY ("sup_product_code", "sup_code")
)
;
COMMENT ON COLUMN "business"."t_supplier_product"."id" IS '编号';
COMMENT ON COLUMN "business"."t_supplier_product"."sup_product_name" IS '供应商产品名称';
COMMENT ON COLUMN "business"."t_supplier_product"."sup_product_code" IS '供应商产品编码';
COMMENT ON COLUMN "business"."t_supplier_product"."sup_code" IS '供应商code';
COMMENT ON COLUMN "business"."t_supplier_product"."sup_name" IS '供应商名称';
COMMENT ON COLUMN "business"."t_supplier_product"."status" IS '状态 00启用，01停用';
COMMENT ON COLUMN "business"."t_supplier_product"."product_code" IS '产品code';
COMMENT ON COLUMN "business"."t_supplier_product"."in_price" IS '成本价';
COMMENT ON COLUMN "business"."t_supplier_product"."create_by" IS '创建者';
COMMENT ON COLUMN "business"."t_supplier_product"."update_by" IS '更新者';
COMMENT ON COLUMN "business"."t_supplier_product"."create_time" IS '创建时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_supplier_product"."update_time" IS '更新时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_supplier_product"."remark" IS '备注';
COMMENT ON TABLE "business"."t_supplier_product" IS '供应商产品表';


CREATE TABLE "business"."t_supplier_route"
(
    "id"           integer     NOT NULL DEFAULT nextval('t_supplier_route_id_seq'::regclass),
    "cg_code"      varchar(50) NOT NULL,
    "sup_code"     varchar(50) NOT NULL,
    "sup_name"     varchar(100),
    "mer_code"     varchar(50) NOT NULL,
    "product_code" varchar(50) NOT NULL,
    "sort"         integer,
    "create_by"    varchar(50) NOT NULL,
    "update_by"    varchar(50) NOT NULL,
    "create_time"  timestamp,
    "update_time"  timestamp,
    "remark"       varchar(200),
    CONSTRAINT "pk_business_t_supplier_route" PRIMARY KEY ("sup_code", "mer_code", "product_code")
)
;
COMMENT ON COLUMN "business"."t_supplier_route"."id" IS '编号';
COMMENT ON COLUMN "business"."t_supplier_route"."cg_code" IS '产品分类编码';
COMMENT ON COLUMN "business"."t_supplier_route"."sup_code" IS '供应商code';
COMMENT ON COLUMN "business"."t_supplier_route"."sup_name" IS '供应商名';
COMMENT ON COLUMN "business"."t_supplier_route"."mer_code" IS '商户code';
COMMENT ON COLUMN "business"."t_supplier_route"."product_code" IS '产品code';
COMMENT ON COLUMN "business"."t_supplier_route"."sort" IS '排序 1最高';
COMMENT ON COLUMN "business"."t_supplier_route"."create_by" IS '创建者';
COMMENT ON COLUMN "business"."t_supplier_route"."update_by" IS '更新者';
COMMENT ON COLUMN "business"."t_supplier_route"."create_time" IS '创建时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_supplier_route"."update_time" IS '更新时间 默认为当前时间';
COMMENT ON COLUMN "business"."t_supplier_route"."remark" IS '备注';
COMMENT ON TABLE "business"."t_supplier_route" IS '供应商路由表';


INSERT INTO t_product ("id", "product_code", "product_name", "cg_code", "status", "create_by", "update_by", "create_time", "update_time", "remark") VALUES
                                                                                                                                                        (2,'CK_003','个人手机三要素认证','CK','00','admin','admin',to_date('2023-06-21 09:54:36.935','yyyy-mm-dd hh24:mi:ss'),to_date('2023-06-21 09:54:36.935','yyyy-mm-dd hh24:mi:ss'),''),
                                                                                                                                                        (3,'CK_003_10000','个人手机三要素认证-电信','CK','00','admin','admin',to_date('2023-06-21 09:54:48.19','yyyy-mm-dd hh24:mi:ss'),to_date('2023-06-21 09:54:48.19','yyyy-mm-dd hh24:mi:ss'),''),
                                                                                                                                                        (5,'CK_003_10010','个人手机三要素认证-联通','CK','00','admin','admin',to_date('2023-06-21 09:55:11.299','yyyy-mm-dd hh24:mi:ss'),to_date('2023-06-21 09:55:11.299','yyyy-mm-dd hh24:mi:ss'),''),
                                                                                                                                                        (4,'CK_003_10086','个人手机三要素认证-移动','CK','00','admin','admin',to_date('2023-06-21 09:54:59.978','yyyy-mm-dd hh24:mi:ss'),to_date('2023-06-21 09:54:59.978','yyyy-mm-dd hh24:mi:ss'),'');

