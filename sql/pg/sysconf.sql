-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS "sys_config";
CREATE TABLE "sys_config"
(
    "config_id"    SERIAL NOT NULL,
    "config_name"  varchar(100),
    "config_key"   varchar(100),
    "config_value" varchar(500),
    "config_type"  char(1),
    "create_by"    varchar(64),
    "create_time"  timestamp(6),
    "update_by"    varchar(64),
    "update_time"  timestamp(6),
    "remark"       varchar(500)
);




SELECT SETVAL(pg_get_serial_sequence('sys_config', 'config_id'), 20000, FALSE);

COMMENT ON COLUMN "sys_config"."config_id" IS '参数主键';
COMMENT ON COLUMN "sys_config"."config_name" IS '参数名称';
COMMENT ON COLUMN "sys_config"."config_key" IS '参数键名';
COMMENT ON COLUMN "sys_config"."config_value" IS '参数键值';
COMMENT ON COLUMN "sys_config"."config_type" IS '系统内置（Y是 N否）';
COMMENT ON COLUMN "sys_config"."create_by" IS '创建者';
COMMENT ON COLUMN "sys_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_config"."update_by" IS '更新者';
COMMENT ON COLUMN "sys_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_config"."remark" IS '备注';
COMMENT ON TABLE "sys_config" IS '参数配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO "sys_config"
VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2023-04-07 09:47:29', '', NULL,
        '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO "sys_config"
VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2023-04-07 09:47:30', '', NULL,
        '初始化密码 123456');
INSERT INTO "sys_config"
VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2023-04-07 09:47:30', '', NULL,
        '深黑主题theme-dark，浅色主题theme-light，深蓝主题theme-blue');
INSERT INTO "sys_config"
VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2023-04-07 09:47:30', '', NULL,
        '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO "sys_config"
VALUES (5, '用户管理-密码字符范围', 'sys.account.chrtype', '0', 'Y', 'admin', '2023-04-07 09:47:30', '', NULL,
        '默认任意字符范围，0任意（密码可以输入任意字符），1数字（密码只能为0-9数字），2英文字母（密码只能为a-z和A-Z字母），3字母和数字（密码必须包含字母，数字）,4字母数字和特殊字符（目前支持的特殊字符包括：~!@#$%^&*()-=_+）');
INSERT INTO "sys_config"
VALUES (6, '用户管理-初始密码修改策略', 'sys.account.initPasswordModify', '0', 'Y', 'admin', '2023-04-07 09:47:30', '', NULL,
        '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
INSERT INTO "sys_config"
VALUES (7, '用户管理-账号密码更新周期', 'sys.account.passwordValidateDays', '0', 'Y', 'admin', '2023-04-07 09:47:30', '', NULL,
        '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
INSERT INTO "sys_config"
VALUES (8, '主框架页-菜单导航显示风格', 'sys.index.menuStyle', 'default', 'Y', 'admin', '2023-04-07 09:47:30', '', NULL,
        '菜单导航显示风格（default为左侧导航菜单，topnav为顶部导航菜单）');
INSERT INTO "sys_config"
VALUES (9, '主框架页-是否开启页脚', 'sys.index.footer', 'true', 'Y', 'admin', '2023-04-07 09:47:30', '', NULL,
        '是否开启底部页脚显示（true显示，false隐藏）');
INSERT INTO "sys_config"
VALUES (10, '主框架页-是否开启页签', 'sys.index.tagsView', 'true', 'Y', 'admin', '2023-04-07 09:47:30', '', NULL,
        '是否开启菜单多页签显示（true显示，false隐藏）');
INSERT INTO "sys_config"
VALUES (11, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2023-04-07 09:47:31', '', NULL,
        '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "sys_dept";
CREATE TABLE "sys_dept"
(
    "dept_id"     SERIAL NOT NULL,
    "parent_id" int4 default 0,
    "ancestors"   varchar(50) default '' ,
    "dept_name"   varchar(30) default '' ,
    "order_num" int4 default 0,
    "leader"      varchar(20),
    "phone"       varchar(11),
    "email"       varchar(50),
    "status"      char(1) default '0',
    "del_flag"    char(1) default '0',
    "create_by"   varchar(64),
    "create_time" timestamp(6),
    "update_by"   varchar(64),
    "update_time" timestamp(6),
    primary key (dept_id)
);

SELECT SETVAL(pg_get_serial_sequence('sys_dept', 'dept_id'), 10000, FALSE);

COMMENT ON COLUMN "sys_dept"."dept_id" IS '部门id';
COMMENT ON COLUMN "sys_dept"."parent_id" IS '父部门id';
COMMENT ON COLUMN "sys_dept"."ancestors" IS '祖级列表';
COMMENT ON COLUMN "sys_dept"."dept_name" IS '部门名称';
COMMENT ON COLUMN "sys_dept"."order_num" IS '显示顺序';
COMMENT ON COLUMN "sys_dept"."leader" IS '负责人';
COMMENT ON COLUMN "sys_dept"."phone" IS '联系电话';
COMMENT ON COLUMN "sys_dept"."email" IS '邮箱';
COMMENT ON COLUMN "sys_dept"."status" IS '部门状态（0正常 1停用）';
COMMENT ON COLUMN "sys_dept"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN "sys_dept"."create_by" IS '创建者';
COMMENT ON COLUMN "sys_dept"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_dept"."update_by" IS '更新者';
COMMENT ON COLUMN "sys_dept"."update_time" IS '更新时间';
COMMENT ON TABLE "sys_dept" IS '部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
insert into sys_dept values(100,  0,   '0',          '旗天科技',   0, '领导', '15888888888', 'test@qt300061.com', '0', '0', 'admin', now(), '', null);
insert into sys_dept values(101,  100, '0,100',      '百行数科', 1, '领导', '15888888888', 'test@qt300061.com', '0', '0', 'admin', now(), '', null);
insert into sys_dept values(103,  101, '0,100,101',  '科技部门',   1, '领导', '15888888888', 'test@qt300061.com', '0', '0', 'admin', now(), '', null);
insert into sys_dept values(104,  101, '0,100,101',  '市场部门',   2, '领导', '15888888888', 'test@qt300061.com', '0', '0', 'admin', now(), '', null);
insert into sys_dept values(105,  101, '0,100,101',  '测试部门',   3, '领导', '15888888888', 'test@qt300061.com', '0', '0', 'admin', now(), '', null);
insert into sys_dept values(106,  101, '0,100,101',  '财务部门',   4, '领导', '15888888888', 'test@qt300061.com', '0', '0', 'admin', now(), '', null);
insert into sys_dept values(107,  101, '0,100,101',  '运维部门',   5, '领导', '15888888888', 'test@qt300061.com', '0', '0', 'admin', now(), '', null);


-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS "sys_dict_data";
CREATE TABLE "sys_dict_data"
(
    "dict_code"   SERIAL NOT NULL,
    "dict_sort"   int4,
    "dict_label"  varchar(100),
    "dict_value"  varchar(100),
    "dict_type"   varchar(100),
    "css_class"   varchar(100),
    "list_class"  varchar(100),
    "is_default"  char(1),
    "status"      char(1),
    "create_by"   varchar(64),
    "create_time" timestamp(6),
    "update_by"   varchar(64),
    "update_time" timestamp(6),
    "remark"      varchar(500),
    primary key (dict_code)
);

SELECT SETVAL(pg_get_serial_sequence('sys_dict_data', 'dict_code'), 10000, FALSE);


COMMENT ON COLUMN "sys_dict_data"."dict_code" IS '字典编码';
COMMENT ON COLUMN "sys_dict_data"."dict_sort" IS '字典排序';
COMMENT ON COLUMN "sys_dict_data"."dict_label" IS '字典标签';
COMMENT ON COLUMN "sys_dict_data"."dict_value" IS '字典键值';
COMMENT ON COLUMN "sys_dict_data"."dict_type" IS '字典类型';
COMMENT ON COLUMN "sys_dict_data"."css_class" IS '样式属性（其他样式扩展）';
COMMENT ON COLUMN "sys_dict_data"."list_class" IS '表格回显样式';
COMMENT ON COLUMN "sys_dict_data"."is_default" IS '是否默认（Y是 N否）';
COMMENT ON COLUMN "sys_dict_data"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "sys_dict_data"."create_by" IS '创建者';
COMMENT ON COLUMN "sys_dict_data"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_dict_data"."update_by" IS '更新者';
COMMENT ON COLUMN "sys_dict_data"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_dict_data"."remark" IS '备注';
COMMENT ON TABLE "sys_dict_data" IS '字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO "sys_dict_data"
VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2023-04-07 09:47:26', '', NULL, '性别男');
INSERT INTO "sys_dict_data"
VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2023-04-07 09:47:26', '', NULL, '性别女');
INSERT INTO "sys_dict_data"
VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2023-04-07 09:47:26', '', NULL, '性别未知');
INSERT INTO "sys_dict_data"
VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2023-04-07 09:47:26', '', NULL, '显示菜单');
INSERT INTO "sys_dict_data"
VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2023-04-07 09:47:26', '', NULL, '隐藏菜单');
INSERT INTO "sys_dict_data"
VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2023-04-07 09:47:26', '', NULL,
        '正常状态');
INSERT INTO "sys_dict_data"
VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2023-04-07 09:47:26', '', NULL,
        '停用状态');
INSERT INTO "sys_dict_data"
VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2023-04-07 09:47:26', '', NULL, '正常状态');
INSERT INTO "sys_dict_data"
VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2023-04-07 09:47:26', '', NULL, '停用状态');
INSERT INTO "sys_dict_data"
VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2023-04-07 09:47:27', '', NULL, '默认分组');
INSERT INTO "sys_dict_data"
VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2023-04-07 09:47:27', '', NULL, '系统分组');
INSERT INTO "sys_dict_data"
VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2023-04-07 09:47:27', '', NULL, '系统默认是');
INSERT INTO "sys_dict_data"
VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2023-04-07 09:47:27', '', NULL, '系统默认否');
INSERT INTO "sys_dict_data"
VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2023-04-07 09:47:27', '', NULL, '通知');
INSERT INTO "sys_dict_data"
VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2023-04-07 09:47:27', '', NULL, '公告');
INSERT INTO "sys_dict_data"
VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2023-04-07 09:47:27', '', NULL,
        '正常状态');
INSERT INTO "sys_dict_data"
VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2023-04-07 09:47:28', '', NULL,
        '关闭状态');
INSERT INTO "sys_dict_data"
VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2023-04-07 09:47:28', '', NULL, '其他操作');
INSERT INTO "sys_dict_data"
VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2023-04-07 09:47:28', '', NULL, '新增操作');
INSERT INTO "sys_dict_data"
VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2023-04-07 09:47:28', '', NULL, '修改操作');
INSERT INTO "sys_dict_data"
VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2023-04-07 09:47:28', '', NULL, '删除操作');
INSERT INTO "sys_dict_data"
VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2023-04-07 09:47:28', '', NULL, '授权操作');
INSERT INTO "sys_dict_data"
VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2023-04-07 09:47:28', '', NULL, '导出操作');
INSERT INTO "sys_dict_data"
VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2023-04-07 09:47:28', '', NULL, '导入操作');
INSERT INTO "sys_dict_data"
VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2023-04-07 09:47:28', '', NULL, '强退操作');
INSERT INTO "sys_dict_data"
VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2023-04-07 09:47:28', '', NULL, '生成操作');
INSERT INTO "sys_dict_data"
VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2023-04-07 09:47:29', '', NULL, '清空操作');
INSERT INTO "sys_dict_data"
VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2023-04-07 09:47:29', '', NULL,
        '正常状态');
INSERT INTO "sys_dict_data"
VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2023-04-07 09:47:29', '', NULL,
        '停用状态');
INSERT INTO "sys_dict_data"
VALUES (101, 2, '姓名_身份证_手机号', 'nsm', 'xhapi_input_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-20 08:58:59', '', NULL,
        NULL);
INSERT INTO "sys_dict_data"
VALUES (102, 4, '姓名_身份证_手机号_银行卡', 'nsmb', 'xhapi_input_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-20 08:59:33', '',
        NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (103, 3, '姓名_身份证_银行卡', 'nsb', 'xhapi_input_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-20 08:59:59', '', NULL,
        NULL);
INSERT INTO "sys_dict_data"
VALUES (104, 1, '预付费', 'y', 'xh_mer_settle', '', 'default', 'Y', '0', 'admin', '2023-04-20 09:07:51', 'admin',
        '2023-04-20 09:08:30', '');
INSERT INTO "sys_dict_data"
VALUES (105, 2, '后付费', 'h', 'xh_mer_settle', '', 'default', 'Y', '0', 'admin', '2023-04-20 09:08:10', 'admin',
        '2023-04-20 09:08:47', '');
INSERT INTO "sys_dict_data"
VALUES (106, 1, '启用', '00', 'xh_off_on_state', '', 'default', 'Y', '0', 'admin', '2023-04-23 14:47:32', 'admin',
        '2023-04-23 14:47:49', '');
INSERT INTO "sys_dict_data"
VALUES (107, 2, '停用', '01', 'xh_off_on_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 14:48:18', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (108, 1, '成功', '1', 'xh_mer_req_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 15:12:19', '', NULL,
        '包含 一致，查询成功');
INSERT INTO "sys_dict_data"
VALUES (109, 2, '不一致', '2', 'xh_mer_req_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 15:13:06', '', NULL,
        '信息不一致');
INSERT INTO "sys_dict_data"
VALUES (110, 3, '查无', '3', 'xh_mer_req_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 15:13:49', '', NULL, '查无');
INSERT INTO "sys_dict_data"
VALUES (111, 4, '查询异常', '4', 'xh_mer_req_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 15:14:16', '', NULL,
        '查询异常');
INSERT INTO "sys_dict_data"
VALUES (112, 1, '信息核验', 'CK', 'xh_product_category', '', '', 'Y', '0', 'admin', '2023-04-23 15:25:14', 'admin',
        '2023-04-23 15:26:29', '');
INSERT INTO "sys_dict_data"
VALUES (113, 2, '工商查询', 'GS', 'xh_product_category', '', '', 'Y', '0', 'admin', '2023-04-23 15:25:50', 'admin',
        '2023-04-23 15:26:24', '');
INSERT INTO "sys_dict_data"
VALUES (114, 3, '司法查询', 'SF', 'xh_product_category', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 15:26:19', '', NULL,
        NULL);
INSERT INTO "sys_dict_data"
VALUES (115, 1, '渠道客户', 'QD', 'xh_mer_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 15:58:05', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (116, 9, '其它', 'QT', 'xh_mer_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 15:58:41', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (117, 1, '每日', '1', 'xh_mer_bill_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 15:59:05', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (118, 2, '每月', '2', 'xh_mer_bill_type', '', '', 'Y', '0', 'admin', '2023-04-23 15:59:26', 'admin',
        '2023-04-23 15:59:45', '');
INSERT INTO "sys_dict_data"
VALUES (119, 3, '季度', '3', 'xh_mer_bill_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 15:59:59', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (120, 4, '每年', '4', 'xh_mer_bill_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 16:00:08', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (121, 1, '充值', 'a', 'xh_mer_amount_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 17:21:50', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (122, 2, '手动扣减', 'b', 'xh_mer_amount_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 17:22:18', '', NULL,
        NULL);
INSERT INTO "sys_dict_data"
VALUES (123, 3, '查询扣款', 'c', 'xh_mer_amount_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 17:22:42', '', NULL,
        NULL);
INSERT INTO "sys_dict_data"
VALUES (124, 1, '成功', '1', 'xh_sup_req_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 17:25:18', '', NULL,
        '包含成功，一致');
INSERT INTO "sys_dict_data"
VALUES (125, 2, '不一致', '2', 'xh_sup_req_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 17:25:29', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (126, 3, '查无', '3', 'xh_sup_req_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 17:25:40', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (127, 4, '查询异常', '4', 'xh_sup_req_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 17:26:00', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (128, 5, '超时', '5', 'xh_sup_req_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-23 17:26:12', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (129, 1, '电子发票', '1', 'xh_mer_invoice_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-24 09:20:21', '', NULL,
        NULL);
INSERT INTO "sys_dict_data"
VALUES (130, 2, '纸质发票', '2', 'xh_mer_invoice_type', NULL, NULL, 'Y', '0', 'admin', '2023-04-24 09:20:33', '', NULL,
        NULL);
INSERT INTO "sys_dict_data"
VALUES (131, 1, '开通', '00', 'xh_mer_protocol_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-24 15:15:09', '', NULL,
        NULL);
INSERT INTO "sys_dict_data"
VALUES (132, 2, '未开通', '01', 'xh_mer_protocol_state', NULL, NULL, 'Y', '0', 'admin', '2023-04-24 15:15:18', '', NULL,
        NULL);
INSERT INTO "sys_dict_data"
VALUES (133, 4, '交通信息', 'CAR', 'xh_product_category', NULL, NULL, 'Y', '0', 'admin', '2023-05-24 15:32:35', '',
        NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (134, 5, '运营商', 'YYS', 'xh_product_category', NULL, NULL, 'Y', '0', 'admin', '2023-05-24 15:32:35', '', NULL, NULL);

INSERT INTO "sys_dict_data"
VALUES (135, 1, '供应商', 'supName', 'xh_mer_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (136, 2, '商户', 'merName', 'xh_mer_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (137, 3, '产品', 'productName', 'xh_mer_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (138, 4, '产品分类', 'cgCode', 'xh_mer_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (139, 5, '单价', 'sellPrice', 'xh_mer_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (140, 6, '日期', 'reqDate', 'xh_mer_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);

INSERT INTO "sys_dict_data"
VALUES (141, 1, '供应商', 'supName', 'xh_sup_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (142, 2, '商户', 'merName', 'xh_sup_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (143, 3, '产品', 'productName', 'xh_sup_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (144, 4, '产品分类', 'cgCode', 'xh_sup_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (145, 5, '成本价', 'inPrice', 'xh_sup_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (146, 6, '日期', 'reqDate', 'xh_sup_report_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);

INSERT INTO "sys_dict_data"
VALUES (147, 1, '商户', 'merName', 'xh_mer_cost_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (148, 2, '产品分类', 'cgCode', 'xh_mer_cost_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (149, 3, '产品', 'productName', 'xh_mer_cost_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (150, 4, '日期', 'reqDate', 'xh_mer_cost_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);

INSERT INTO "sys_dict_data"
VALUES (151, 1, '供应商', 'supName', 'xh_mer_profit_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (152, 2, '商户', 'merName', 'xh_mer_profit_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (153, 3, '产品', 'productName', 'xh_mer_profit_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (154, 4, '产品分类', 'cgCode', 'xh_mer_profit_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (155, 5, '日期', 'reqDate', 'xh_mer_profit_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);

INSERT INTO "sys_dict_data"
VALUES (156, 1, '供应商', 'supName', 'xh_sup_cost_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (157, 2, '产品分类', 'cgCode', 'xh_sup_cost_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (158, 3, '产品', 'productName', 'xh_sup_cost_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);
INSERT INTO "sys_dict_data"
VALUES (159, 4, '日期', 'reqDate', 'xh_sup_cost_dim', NULL, NULL, 'N', '0', 'admin', '2023-06-17 15:32:35', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS "sys_dict_type";
CREATE TABLE "sys_dict_type"
(
    "dict_id"     SERIAL NOT NULL,
    "dict_name"   varchar(100),
    "dict_type"   varchar(100),
    "status"      char(1),
    "create_by"   varchar(64),
    "create_time" timestamp(6),
    "update_by"   varchar(64),
    "update_time" timestamp(6),
    "remark"      varchar(500),
    primary key (dict_id),
    unique (dict_type)
);

SELECT SETVAL(pg_get_serial_sequence('sys_dict_type', 'dict_id'), 1000, FALSE);


COMMENT ON COLUMN "sys_dict_type"."dict_id" IS '字典主键';
COMMENT ON COLUMN "sys_dict_type"."dict_name" IS '字典名称';
COMMENT ON COLUMN "sys_dict_type"."dict_type" IS '字典类型';
COMMENT ON COLUMN "sys_dict_type"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "sys_dict_type"."create_by" IS '创建者';
COMMENT ON COLUMN "sys_dict_type"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_dict_type"."update_by" IS '更新者';
COMMENT ON COLUMN "sys_dict_type"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_dict_type"."remark" IS '备注';
COMMENT ON TABLE "sys_dict_type" IS '字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO "sys_dict_type"
VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2023-04-07 09:47:23', '', NULL, '用户性别列表');
INSERT INTO "sys_dict_type"
VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2023-04-07 09:47:23', '', NULL, '菜单状态列表');
INSERT INTO "sys_dict_type"
VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2023-04-07 09:47:24', '', NULL, '系统开关列表');
INSERT INTO "sys_dict_type"
VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2023-04-07 09:47:24', '', NULL, '任务状态列表');
INSERT INTO "sys_dict_type"
VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2023-04-07 09:47:24', '', NULL, '任务分组列表');
INSERT INTO "sys_dict_type"
VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2023-04-07 09:47:24', '', NULL, '系统是否列表');
INSERT INTO "sys_dict_type"
VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2023-04-07 09:47:24', '', NULL, '通知类型列表');
INSERT INTO "sys_dict_type"
VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2023-04-07 09:47:24', '', NULL, '通知状态列表');
INSERT INTO "sys_dict_type"
VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2023-04-07 09:47:24', '', NULL, '操作类型列表');
INSERT INTO "sys_dict_type"
VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2023-04-07 09:47:24', '', NULL, '登录状态列表');
INSERT INTO "sys_dict_type"
VALUES (100, '产品API接口入参类型', 'xhapi_input_type', '0', 'admin', '2023-04-20 08:56:34', 'admin', '2023-04-20 08:57:11',
        '用于产品 api 接口 入参校验');
INSERT INTO "sys_dict_type"
VALUES (101, '商户结算方式', 'xh_mer_settle', '0', 'admin', '2023-04-20 09:03:20', 'admin', '2023-04-23 14:43:40',
        '商户结算方式   预付费，后付费');
INSERT INTO "sys_dict_type"
VALUES (102, '停启用状态', 'xh_off_on_state', '0', 'admin', '2023-04-23 14:47:07', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (103, '商户调用返回状态码', 'xh_mer_req_state', '0', 'admin', '2023-04-23 15:11:18', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (104, '供应商调用返回状态码', 'xh_sup_req_state', '0', 'admin', '2023-04-23 15:14:59', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (105, '产品分类', 'xh_product_category', '0', 'admin', '2023-04-23 15:24:49', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (106, '商户账单类型', 'xh_mer_bill_type', '0', 'admin', '2023-04-23 15:52:29', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (107, '客户类型', 'xh_mer_type', '0', 'admin', '2023-04-23 15:54:01', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (108, '商户协议状态', 'xh_mer_protocol_state', '0', 'admin', '2023-04-23 16:01:05', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (109, '商户金额操作类型', 'xh_mer_amount_type', '0', 'admin', '2023-04-23 17:20:17', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (110, '商户开票类型', 'xh_mer_invoice_type', '0', 'admin', '2023-04-24 09:20:00', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (111, '商户日志统计维度', 'xh_mer_report_dim', '0', 'admin', '22023-06-17 09:20:00', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (112, '供应商日志统计维度', 'xh_sup_report_dim', '0', 'admin', '22023-06-17 09:20:00', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (113, '商户结算统计', 'xh_mer_cost_dim', '0', 'admin', '22023-06-17 09:20:00', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (114, '商户利润统计', 'xh_mer_profit_dim', '0', 'admin', '22023-06-17 09:20:00', '', NULL, NULL);
INSERT INTO "sys_dict_type"
VALUES (115, '供应商结算统计', 'xh_sup_cost_dim', '0', 'admin', '22023-06-17 09:20:00', '', NULL, NULL);


-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "sys_job";
CREATE TABLE "sys_job"
(
    "job_id"          SERIAL       NOT NULL,
    "job_name"        varchar(64)  NOT NULL,
    "job_group"       varchar(64)  NOT NULL,
    "invoke_target"   varchar(500) NOT NULL,
    "cron_expression" varchar(255),
    "misfire_policy"  varchar(20),
    "concurrent"      char(1),
    "status"          char(1),
    "create_by"       varchar(64),
    "create_time"     timestamp(6),
    "update_by"       varchar(64),
    "update_time"     timestamp(6),
    "remark"          varchar(500),
    primary key (job_id, job_name, job_group)
);

SELECT SETVAL(pg_get_serial_sequence('sys_job', 'job_id'), 1000, FALSE);


COMMENT ON COLUMN "sys_job"."job_id" IS '任务ID';
COMMENT ON COLUMN "sys_job"."job_name" IS '任务名称';
COMMENT ON COLUMN "sys_job"."job_group" IS '任务组名';
COMMENT ON COLUMN "sys_job"."invoke_target" IS '调用目标字符串';
COMMENT ON COLUMN "sys_job"."cron_expression" IS 'cron执行表达式';
COMMENT ON COLUMN "sys_job"."misfire_policy" IS '计划执行错误策略（1立即执行 2执行一次 3放弃执行）';
COMMENT ON COLUMN "sys_job"."concurrent" IS '是否并发执行（0允许 1禁止）';
COMMENT ON COLUMN "sys_job"."status" IS '状态（0正常 1暂停）';
COMMENT ON COLUMN "sys_job"."create_by" IS '创建者';
COMMENT ON COLUMN "sys_job"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_job"."update_by" IS '更新者';
COMMENT ON COLUMN "sys_job"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_job"."remark" IS '备注信息';
COMMENT ON TABLE "sys_job" IS '定时任务调度表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO "sys_job"
VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2023-04-07 09:47:33',
        '', NULL, '');
INSERT INTO "sys_job"
VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(''ry'')', '0/15 * * * * ?', '3', '1', '1', 'admin',
        '2023-04-07 09:47:33', '', NULL, '');
INSERT INTO "sys_job"
VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(''ry'', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3',
        '1', '1', 'admin', '2023-04-07 09:47:33', '', NULL, '');

insert into sys_job(job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time)
values('商户日志统计(当日)', 'DEFAULT', 'statTask.merReportNow', '0 0 * * * ?', 2, 1, 1, 'admin', now());
insert into sys_job(job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time)
values('商户日志统计(昨日)', 'DEFAULT', 'statTask.merReportLast', '0 0 1 * * ?', 2, 1, 1, 'admin', now());

insert into sys_job(job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time)
values('供应商日志统计(当日)', 'DEFAULT', 'statTask.supReportNow', '10 0 * * * ?', 2, 1, 1, 'admin', now());
insert into sys_job(job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time)
values('供应商日志统计(昨日)', 'DEFAULT', 'statTask.supReportLast', '10 0 1 * * ?', 2, 1, 1, 'admin', now());

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS "sys_job_log";
CREATE TABLE "sys_job_log"
(
    "job_log_id"     SERIAL       NOT NULL,
    "job_name"       varchar(64)  NOT NULL,
    "job_group"      varchar(64)  NOT NULL,
    "invoke_target"  varchar(500) NOT NULL,
    "job_message"    varchar(500),
    "status"         char(1),
    "exception_info" varchar(2000),
    "create_time"    timestamp(6),
    primary key (job_log_id)
);

SELECT SETVAL(pg_get_serial_sequence('sys_job_log', 'job_log_id'), 1000, FALSE);


COMMENT ON COLUMN "sys_job_log"."job_log_id" IS '任务日志ID';
COMMENT ON COLUMN "sys_job_log"."job_name" IS '任务名称';
COMMENT ON COLUMN "sys_job_log"."job_group" IS '任务组名';
COMMENT ON COLUMN "sys_job_log"."invoke_target" IS '调用目标字符串';
COMMENT ON COLUMN "sys_job_log"."job_message" IS '日志信息';
COMMENT ON COLUMN "sys_job_log"."status" IS '执行状态（0正常 1失败）';
COMMENT ON COLUMN "sys_job_log"."exception_info" IS '异常信息';
COMMENT ON COLUMN "sys_job_log"."create_time" IS '创建时间';
COMMENT ON TABLE "sys_job_log" IS '定时任务调度日志表';

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS "sys_logininfor";
CREATE TABLE "sys_logininfor"
(
    "info_id"        SERIAL NOT NULL,
    "login_name"     varchar(50),
    "ipaddr"         varchar(128),
    "login_location" varchar(255),
    "browser"        varchar(50),
    "os"             varchar(50),
    "status"         char(1),
    "msg"            varchar(255),
    "login_time"     timestamp(6),
    primary key (info_id)
);

SELECT SETVAL(pg_get_serial_sequence('sys_logininfor', 'info_id'), 1000, FALSE);


COMMENT ON COLUMN "sys_logininfor"."info_id" IS '访问ID';
COMMENT ON COLUMN "sys_logininfor"."login_name" IS '登录账号';
COMMENT ON COLUMN "sys_logininfor"."ipaddr" IS '登录IP地址';
COMMENT ON COLUMN "sys_logininfor"."login_location" IS '登录地点';
COMMENT ON COLUMN "sys_logininfor"."browser" IS '浏览器类型';
COMMENT ON COLUMN "sys_logininfor"."os" IS '操作系统';
COMMENT ON COLUMN "sys_logininfor"."status" IS '登录状态（0成功 1失败）';
COMMENT ON COLUMN "sys_logininfor"."msg" IS '提示消息';
COMMENT ON COLUMN "sys_logininfor"."login_time" IS '访问时间';
COMMENT ON TABLE "sys_logininfor" IS '系统访问记录';




-- ----------------------------
-- Table structure for sys_menu
 -- 5、菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS "sys_menu";
CREATE TABLE "sys_menu"
(
    "menu_id"     SERIAL      NOT NULL,
    "menu_name"   varchar(50) NOT NULL,
    "parent_id" int4 default 0,
    "order_num" int4 default 0,
    "url"         varchar(200) default '#',
    "target"      varchar(20) default '',
    "menu_type"   char(1) default '',
    "visible"     char(1) default '0',
    "is_refresh"  char(1) default '1',
    "perms"       varchar(100) default '' ,
    "icon"        varchar(100) default '#' ,
    "create_by"   varchar(64) default '',
    "create_time" timestamp(6),
    "update_by"   varchar(64) default '',
    "update_time" timestamp(6),
    "remark"      varchar(500),
    primary key (menu_id)
);
SELECT SETVAL(pg_get_serial_sequence('sys_menu', 'menu_id'), 10000, FALSE);


COMMENT ON COLUMN "sys_menu"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "sys_menu"."menu_name" IS '菜单名称';
COMMENT ON COLUMN "sys_menu"."parent_id" IS '父菜单ID';
COMMENT ON COLUMN "sys_menu"."order_num" IS '显示顺序';
COMMENT ON COLUMN "sys_menu"."url" IS '请求地址';
COMMENT ON COLUMN "sys_menu"."target" IS '打开方式（menuItem页签 menuBlank新窗口）';
COMMENT ON COLUMN "sys_menu"."menu_type" IS '菜单类型（M目录 C菜单 F按钮）';
COMMENT ON COLUMN "sys_menu"."visible" IS '菜单状态（0显示 1隐藏）';
COMMENT ON COLUMN "sys_menu"."is_refresh" IS '是否刷新（0刷新 1不刷新）';
COMMENT ON COLUMN "sys_menu"."perms" IS '权限标识';
COMMENT ON COLUMN "sys_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "sys_menu"."create_by" IS '创建者';
COMMENT ON COLUMN "sys_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_menu"."update_by" IS '更新者';
COMMENT ON COLUMN "sys_menu"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_menu"."remark" IS '备注';
COMMENT ON TABLE "sys_menu" IS '菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "sys_menu"
VALUES (1, '系统管理', 0, 1, '#', '', 'M', '0', '1', '', 'fa fa-gear', 'admin', '2023-04-07 09:46:57', '', NULL, '系统管理目录');
INSERT INTO "sys_menu"
VALUES (2, '系统监控', 0, 2, '#', '', 'M', '0', '1', '', 'fa fa-video-camera', 'admin', '2023-04-07 09:46:57', '', NULL,
        '系统监控目录');

INSERT INTO "sys_menu"
VALUES (100, '用户管理', 1, 1, '/system/user', '', 'C', '0', '0', 'system:user:view', 'fa fa-user-o', 'admin',
        '2023-04-07 09:46:58', '', NULL, '用户管理菜单');
INSERT INTO "sys_menu"
VALUES (101, '角色管理', 1, 2, '/system/role', '', 'C', '0', '1', 'system:role:view', 'fa fa-user-secret', 'admin',
        '2023-04-07 09:46:58', '', NULL, '角色管理菜单');
INSERT INTO "sys_menu"
VALUES (102, '菜单管理', 1, 3, '/system/menu', '', 'C', '0', '1', 'system:menu:view', 'fa fa-th-list', 'admin',
        '2023-04-07 09:46:58', '', NULL, '菜单管理菜单');
INSERT INTO "sys_menu"
VALUES (103, '部门管理', 1, 4, '/system/dept', '', 'C', '0', '1', 'system:dept:view', 'fa fa-outdent', 'admin',
        '2023-04-07 09:46:58', '', NULL, '部门管理菜单');
INSERT INTO "sys_menu"
VALUES (104, '岗位管理', 1, 5, '/system/post', '', 'C', '0', '1', 'system:post:view', 'fa fa-address-card-o', 'admin',
        '2023-04-07 09:46:58', '', NULL, '岗位管理菜单');
INSERT INTO "sys_menu"
VALUES (105, '字典管理', 1, 6, '/system/dict', '', 'C', '0', '1', 'system:dict:view', 'fa fa-bookmark-o', 'admin',
        '2023-04-07 09:46:58', '', NULL, '字典管理菜单');
INSERT INTO "sys_menu"
VALUES (106, '参数设置', 1, 7, '/system/config', '', 'C', '0', '1', 'system:config:view', 'fa fa-sun-o', 'admin',
        '2023-04-07 09:46:58', '', NULL, '参数设置菜单');
INSERT INTO "sys_menu"
VALUES (107, '通知公告', 1, 8, '/system/notice', '', 'C', '0', '1', 'system:notice:view', 'fa fa-bullhorn', 'admin',
        '2023-04-07 09:46:58', '', NULL, '通知公告菜单');
INSERT INTO "sys_menu"
VALUES (108, '日志管理', 1, 9, '#', '', 'M', '0', '1', '', 'fa fa-pencil-square-o', 'admin', '2023-04-07 09:46:59', '',
        NULL, '日志管理菜单');

INSERT INTO "sys_menu"
VALUES (109, '在线用户', 2, 1, '/monitor/online', '', 'C', '0', '1', 'monitor:online:view', 'fa fa-user-circle', 'admin',
        '2023-04-07 09:46:59', '', NULL, '在线用户菜单');
INSERT INTO "sys_menu"
VALUES (110, '定时任务', 2, 2, '/monitor/job', '', 'C', '0', '1', 'monitor:job:view', 'fa fa-tasks', 'admin',
        '2023-04-07 09:46:59', '', NULL, '定时任务菜单');
INSERT INTO "sys_menu"
VALUES (111, '数据监控', 2, 3, '/monitor/data', '', 'C', '0', '1', 'monitor:data:view', 'fa fa-bug', 'admin',
        '2023-04-07 09:46:59', '', NULL, '数据监控菜单');
INSERT INTO "sys_menu"
VALUES (112, '服务监控', 2, 4, '/monitor/server', '', 'C', '0', '1', 'monitor:server:view', 'fa fa-server', 'admin',
        '2023-04-07 09:46:59', '', NULL, '服务监控菜单');
INSERT INTO "sys_menu"
VALUES (113, '缓存监控', 2, 5, '/monitor/cache', '', 'C', '0', '1', 'monitor:cache:view', 'fa fa-cube', 'admin',
        '2023-04-07 09:46:59', '', NULL, '缓存监控菜单');

INSERT INTO "sys_menu"
VALUES (500, '操作日志', 108, 1, '/monitor/operlog', '', 'C', '0', '1', 'monitor:operlog:view', 'fa fa-address-book',
        'admin', '2023-04-07 09:46:59', '', NULL, '操作日志菜单');
INSERT INTO "sys_menu"
VALUES (501, '登录日志', 108, 2, '/monitor/logininfor', '', 'C', '0', '1', 'monitor:logininfor:view', 'fa fa-file-image-o',
        'admin', '2023-04-07 09:47:00', '', NULL, '登录日志菜单');

INSERT INTO "sys_menu"
VALUES (1000, '用户查询', 100, 1, '#', '', 'F', '0', '1', 'system:user:list', '#', 'admin', '2023-04-07 09:47:00', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1001, '用户新增', 100, 2, '#', '', 'F', '0', '1', 'system:user:add', '#', 'admin', '2023-04-07 09:47:00', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1002, '用户修改', 100, 3, '#', '', 'F', '0', '1', 'system:user:edit', '#', 'admin', '2023-04-07 09:47:00', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1003, '用户删除', 100, 4, '#', '', 'F', '0', '1', 'system:user:remove', '#', 'admin', '2023-04-07 09:47:00', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1004, '用户导出', 100, 5, '#', '', 'F', '0', '1', 'system:user:export', '#', 'admin', '2023-04-07 09:47:00', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1005, '用户导入', 100, 6, '#', '', 'F', '0', '1', 'system:user:import', '#', 'admin', '2023-04-07 09:47:00', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1006, '重置密码', 100, 7, '#', '', 'F', '0', '1', 'system:user:resetPwd', '#', 'admin', '2023-04-07 09:47:00', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1007, '角色查询', 101, 1, '#', '', 'F', '0', '1', 'system:role:list', '#', 'admin', '2023-04-07 09:47:00', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1008, '角色新增', 101, 2, '#', '', 'F', '0', '1', 'system:role:add', '#', 'admin', '2023-04-07 09:47:01', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1009, '角色修改', 101, 3, '#', '', 'F', '0', '1', 'system:role:edit', '#', 'admin', '2023-04-07 09:47:01', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1010, '角色删除', 101, 4, '#', '', 'F', '0', '1', 'system:role:remove', '#', 'admin', '2023-04-07 09:47:01', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1011, '角色导出', 101, 5, '#', '', 'F', '0', '1', 'system:role:export', '#', 'admin', '2023-04-07 09:47:01', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1012, '菜单查询', 102, 1, '#', '', 'F', '0', '1', 'system:menu:list', '#', 'admin', '2023-04-07 09:47:01', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1013, '菜单新增', 102, 2, '#', '', 'F', '0', '1', 'system:menu:add', '#', 'admin', '2023-04-07 09:47:01', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1014, '菜单修改', 102, 3, '#', '', 'F', '0', '1', 'system:menu:edit', '#', 'admin', '2023-04-07 09:47:01', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1015, '菜单删除', 102, 4, '#', '', 'F', '0', '1', 'system:menu:remove', '#', 'admin', '2023-04-07 09:47:01', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1016, '部门查询', 103, 1, '#', '', 'F', '0', '1', 'system:dept:list', '#', 'admin', '2023-04-07 09:47:02', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1017, '部门新增', 103, 2, '#', '', 'F', '0', '1', 'system:dept:add', '#', 'admin', '2023-04-07 09:47:02', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1018, '部门修改', 103, 3, '#', '', 'F', '0', '1', 'system:dept:edit', '#', 'admin', '2023-04-07 09:47:02', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1019, '部门删除', 103, 4, '#', '', 'F', '0', '1', 'system:dept:remove', '#', 'admin', '2023-04-07 09:47:02', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1020, '岗位查询', 104, 1, '#', '', 'F', '0', '1', 'system:post:list', '#', 'admin', '2023-04-07 09:47:02', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1021, '岗位新增', 104, 2, '#', '', 'F', '0', '1', 'system:post:add', '#', 'admin', '2023-04-07 09:47:02', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1022, '岗位修改', 104, 3, '#', '', 'F', '0', '1', 'system:post:edit', '#', 'admin', '2023-04-07 09:47:02', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1023, '岗位删除', 104, 4, '#', '', 'F', '0', '1', 'system:post:remove', '#', 'admin', '2023-04-07 09:47:02', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1024, '岗位导出', 104, 5, '#', '', 'F', '0', '1', 'system:post:export', '#', 'admin', '2023-04-07 09:47:02', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1025, '字典查询', 105, 1, '#', '', 'F', '0', '1', 'system:dict:list', '#', 'admin', '2023-04-07 09:47:03', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1026, '字典新增', 105, 2, '#', '', 'F', '0', '1', 'system:dict:add', '#', 'admin', '2023-04-07 09:47:03', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1027, '字典修改', 105, 3, '#', '', 'F', '0', '1', 'system:dict:edit', '#', 'admin', '2023-04-07 09:47:03', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1028, '字典删除', 105, 4, '#', '', 'F', '0', '1', 'system:dict:remove', '#', 'admin', '2023-04-07 09:47:03', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1029, '字典导出', 105, 5, '#', '', 'F', '0', '1', 'system:dict:export', '#', 'admin', '2023-04-07 09:47:03', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1030, '参数查询', 106, 1, '#', '', 'F', '0', '1', 'system:config:list', '#', 'admin', '2023-04-07 09:47:03', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1031, '参数新增', 106, 2, '#', '', 'F', '0', '1', 'system:config:add', '#', 'admin', '2023-04-07 09:47:03', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1032, '参数修改', 106, 3, '#', '', 'F', '0', '1', 'system:config:edit', '#', 'admin', '2023-04-07 09:47:03', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1033, '参数删除', 106, 4, '#', '', 'F', '0', '1', 'system:config:remove', '#', 'admin', '2023-04-07 09:47:04', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1034, '参数导出', 106, 5, '#', '', 'F', '0', '1', 'system:config:export', '#', 'admin', '2023-04-07 09:47:04', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1035, '公告查询', 107, 1, '#', '', 'F', '0', '1', 'system:notice:list', '#', 'admin', '2023-04-07 09:47:04', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1036, '公告新增', 107, 2, '#', '', 'F', '0', '1', 'system:notice:add', '#', 'admin', '2023-04-07 09:47:04', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1037, '公告修改', 107, 3, '#', '', 'F', '0', '1', 'system:notice:edit', '#', 'admin', '2023-04-07 09:47:04', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1038, '公告删除', 107, 4, '#', '', 'F', '0', '1', 'system:notice:remove', '#', 'admin', '2023-04-07 09:47:04', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1039, '操作查询', 500, 1, '#', '', 'F', '0', '1', 'monitor:operlog:list', '#', 'admin', '2023-04-07 09:47:04', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1040, '操作删除', 500, 2, '#', '', 'F', '0', '1', 'monitor:operlog:remove', '#', 'admin', '2023-04-07 09:47:04', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1041, '详细信息', 500, 3, '#', '', 'F', '0', '1', 'monitor:operlog:detail', '#', 'admin', '2023-04-07 09:47:05', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1042, '日志导出', 500, 4, '#', '', 'F', '0', '1', 'monitor:operlog:export', '#', 'admin', '2023-04-07 09:47:05', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1043, '登录查询', 501, 1, '#', '', 'F', '0', '1', 'monitor:logininfor:list', '#', 'admin', '2023-04-07 09:47:06',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1044, '登录删除', 501, 2, '#', '', 'F', '0', '1', 'monitor:logininfor:remove', '#', 'admin', '2023-04-07 09:47:06',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1045, '日志导出', 501, 3, '#', '', 'F', '0', '1', 'monitor:logininfor:export', '#', 'admin', '2023-04-07 09:47:06',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1046, '账户解锁', 501, 4, '#', '', 'F', '0', '1', 'monitor:logininfor:unlock', '#', 'admin', '2023-04-07 09:47:06',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1047, '在线查询', 109, 1, '#', '', 'F', '0', '1', 'monitor:online:list', '#', 'admin', '2023-04-07 09:47:06', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1048, '批量强退', 109, 2, '#', '', 'F', '0', '1', 'monitor:online:batchForceLogout', '#', 'admin',
        '2023-04-07 09:47:06', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1049, '单条强退', 109, 3, '#', '', 'F', '0', '1', 'monitor:online:forceLogout', '#', 'admin', '2023-04-07 09:47:06',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1050, '任务查询', 110, 1, '#', '', 'F', '0', '1', 'monitor:job:list', '#', 'admin', '2023-04-07 09:47:06', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1051, '任务新增', 110, 2, '#', '', 'F', '0', '1', 'monitor:job:add', '#', 'admin', '2023-04-07 09:47:07', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1052, '任务修改', 110, 3, '#', '', 'F', '0', '1', 'monitor:job:edit', '#', 'admin', '2023-04-07 09:47:07', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1053, '任务删除', 110, 4, '#', '', 'F', '0', '1', 'monitor:job:remove', '#', 'admin', '2023-04-07 09:47:07', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1054, '状态修改', 110, 5, '#', '', 'F', '0', '1', 'monitor:job:changeStatus', '#', 'admin', '2023-04-07 09:47:07',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1055, '任务详细', 110, 6, '#', '', 'F', '0', '1', 'monitor:job:detail', '#', 'admin', '2023-04-07 09:47:08', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1056, '任务导出', 110, 7, '#', '', 'F', '0', '1', 'monitor:job:export', '#', 'admin', '2023-04-07 09:47:08', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2000, '商户管理', 0, 4, '#', 'menuItem', 'M', '0', '1', '', 'fa fa-address-book', 'admin', '2023-04-18 17:18:30',
        'admin', '2023-04-20 11:42:46', '');
INSERT INTO "sys_menu"
VALUES (2007, '产品管理', 0, 5, '#', 'menuItem', 'M', '0', '1', '', 'fa fa-file-powerpoint-o', 'admin',
        '2023-04-20 11:37:44', 'admin', '2023-04-20 11:38:31', '');
INSERT INTO "sys_menu"
VALUES (2008, '供应商管理', 0, 6, '#', 'menuItem', 'M', '0', '1', '', 'fa fa-upload', 'admin', '2023-04-20 11:42:04',
        'admin', '2023-04-21 14:58:02', '');
INSERT INTO "sys_menu"
VALUES (2009, '报表', 0, 7, '#', 'menuItem', 'M', '0', '1', NULL, 'fa fa-pie-chart', 'admin', '2023-04-21 14:58:55', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2058, '产品供应商路由', 2008, 3, '/xh/supRoute', 'menuItem', 'C', '0', '1', 'xh:supRoute:view', '#', 'admin',
        '2023-04-24 15:00:46', 'admin', '2023-04-25 10:20:36', '供应商路由菜单');
INSERT INTO "sys_menu"
VALUES (2059, '供应商路由查询', 2058, 1, '#', '', 'F', '0', '1', 'xh:supRoute:list', '#', 'admin', '2023-04-24 15:00:47', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2060, '供应商路由新增', 2058, 2, '#', '', 'F', '0', '1', 'xh:supRoute:add', '#', 'admin', '2023-04-24 15:00:47', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2061, '供应商路由修改', 2058, 3, '#', '', 'F', '0', '1', 'xh:supRoute:edit', '#', 'admin', '2023-04-24 15:00:47', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2062, '供应商路由删除', 2058, 4, '#', '', 'F', '0', '1', 'xh:supRoute:remove', '#', 'admin', '2023-04-24 15:00:47', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2063, '供应商路由导出', 2058, 5, '#', '', 'F', '0', '1', 'xh:supRoute:export', '#', 'admin', '2023-04-24 15:00:47', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2064, '供应商产品管理', 2008, 2, '/xh/supProduct', 'menuItem', 'C', '0', '1', 'xh:supProduct:view', '#', 'admin',
        '2023-04-24 15:01:02', 'admin', '2023-04-27 14:49:42', '供应商产品信息管理菜单');
INSERT INTO "sys_menu"
VALUES (2065, '供应商产品信息管理查询', 2064, 1, '#', '', 'F', '0', '1', 'xh:supProduct:list', '#', 'admin', '2023-04-24 15:01:02',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2066, '供应商产品信息管理新增', 2064, 2, '#', '', 'F', '0', '1', 'xh:supProduct:add', '#', 'admin', '2023-04-24 15:01:03',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2067, '供应商产品信息管理修改', 2064, 3, '#', '', 'F', '0', '1', 'xh:supProduct:edit', '#', 'admin', '2023-04-24 15:01:03',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2068, '供应商产品信息管理删除', 2064, 4, '#', '', 'F', '0', '1', 'xh:supProduct:remove', '#', 'admin',
        '2023-04-24 15:01:03', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2069, '供应商产品信息管理导出', 2064, 5, '#', '', 'F', '0', '1', 'xh:supProduct:export', '#', 'admin',
        '2023-04-24 15:01:03', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2070, '供应商信息管理', 2008, 1, '/xh/supplier', '', 'C', '0', '1', 'xh:supplier:view', '#', 'admin',
        '2023-04-24 15:01:10', '', NULL, '供应商信息管理菜单');
INSERT INTO "sys_menu"
VALUES (2071, '供应商信息管理查询', 2070, 1, '#', '', 'F', '0', '1', 'xh:supplier:list', '#', 'admin', '2023-04-24 15:01:10', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2072, '供应商信息管理新增', 2070, 2, '#', '', 'F', '0', '1', 'xh:supplier:add', '#', 'admin', '2023-04-24 15:01:10', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2073, '供应商信息管理修改', 2070, 3, '#', '', 'F', '0', '1', 'xh:supplier:edit', '#', 'admin', '2023-04-24 15:01:10', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2074, '供应商信息管理删除', 2070, 4, '#', '', 'F', '0', '1', 'xh:supplier:remove', '#', 'admin', '2023-04-24 15:01:10',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2075, '供应商信息管理导出', 2070, 5, '#', '', 'F', '0', '1', 'xh:supplier:export', '#', 'admin', '2023-04-24 15:01:10',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2076, '调用供应商日志', 2008, 5, '/xh/supLog', 'menuItem', 'C', '0', '1', 'xh:supLog:view', '#', 'admin',
        '2023-04-24 15:01:17', 'admin', '2023-04-25 10:20:47', '调用供应商日志菜单');
INSERT INTO "sys_menu"
VALUES (2077, '调用供应商日志查询', 2076, 1, '#', '', 'F', '0', '1', 'xh:supLog:list', '#', 'admin', '2023-04-24 15:01:17', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2078, '调用供应商日志新增', 2076, 2, '#', '', 'F', '0', '1', 'xh:supLog:add', '#', 'admin', '2023-04-24 15:01:17', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2079, '调用供应商日志修改', 2076, 3, '#', '', 'F', '0', '1', 'xh:supLog:edit', '#', 'admin', '2023-04-24 15:01:17', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2080, '调用供应商日志删除', 2076, 4, '#', '', 'F', '0', '1', 'xh:supLog:remove', '#', 'admin', '2023-04-24 15:01:17', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2081, '调用供应商日志导出', 2076, 5, '#', '', 'F', '0', '1', 'xh:supLog:export', '#', 'admin', '2023-04-24 15:01:18', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2082, '产品销售管理', 2007, 2, '/xh/productSell', 'menuItem', 'C', '0', '1', 'xh:productSell:view', '#', 'admin',
        '2023-04-24 15:02:00', 'admin', '2023-04-27 11:06:34', '商品销售菜单');
INSERT INTO "sys_menu"
VALUES (2083, '产品销售查询', 2082, 1, '#', '', 'F', '0', '1', 'xh:productSell:list', '#', 'admin', '2023-04-24 15:02:00', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2084, '产品销售新增', 2082, 2, '#', '', 'F', '0', '1', 'xh:productSell:add', '#', 'admin', '2023-04-24 15:02:00', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2085, '产品销售修改', 2082, 3, '#', '', 'F', '0', '1', 'xh:productSell:edit', '#', 'admin', '2023-04-24 15:02:00', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2086, '产品销售删除', 2082, 4, '#', '', 'F', '0', '1', 'xh:productSell:remove', '#', 'admin', '2023-04-24 15:02:00',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2087, '产品销售导出', 2082, 5, '#', '', 'F', '0', '1', 'xh:productSell:export', '#', 'admin', '2023-04-24 15:02:00',
        '', NULL, '');

INSERT INTO "sys_menu"
VALUES (  nextval ('sys_menu_menu_id_seq'), '产品销售批量路由', 2082, 5, '#', '', 'F', '0', '1', 'xh:productSell:batchRoute', '#', 'admin', '2023-04-24 15:02:00',
          '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2112, '产品供应商路由', 2082, 6, '#', '', 'F', '0', '1', 'xh:productSell:route', '#', 'admin', '2023-04-28 14:43:15',
        '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2113, '产品供应商批量路由', 2082, 7, '#', '', 'F', '0', '1', 'xh:productSell:batchRoute', '#', 'admin', '2023-04-28 14:43:15',
        '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2088, '产品信息管理', 2007, 1, '/xh/product', 'menuItem', 'C', '0', '1', 'xh:product:view', '#', 'admin',
        '2023-04-24 15:02:07', 'admin', '2023-04-25 10:19:34', '产品菜单');
INSERT INTO "sys_menu"
VALUES (2089, '产品查询', 2088, 1, '#', '', 'F', '0', '1', 'xh:product:list', '#', 'admin', '2023-04-24 15:02:07', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (2090, '产品新增', 2088, 2, '#', '', 'F', '0', '1', 'xh:product:add', '#', 'admin', '2023-04-24 15:02:07', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (2091, '产品修改', 2088, 3, '#', '', 'F', '0', '1', 'xh:product:edit', '#', 'admin', '2023-04-24 15:02:07', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (2092, '产品删除', 2088, 4, '#', '', 'F', '0', '1', 'xh:product:remove', '#', 'admin', '2023-04-24 15:02:07', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2093, '产品导出', 2088, 5, '#', '', 'F', '0', '1', 'xh:product:export', '#', 'admin', '2023-04-24 15:02:08', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2094, '商户调用日志', 2000, 3, '/xh/merLog', 'menuItem', 'C', '0', '1', 'xh:merLog:view', '#', 'admin',
        '2023-04-24 15:02:14', 'admin', '2023-04-25 10:19:14', '商户调用日志菜单');
INSERT INTO "sys_menu"
VALUES (2095, '商户调用日志查询', 2094, 1, '#', '', 'F', '0', '1', 'xh:merLog:list', '#', 'admin', '2023-04-24 15:02:15', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2096, '商户调用日志新增', 2094, 2, '#', '', 'F', '0', '1', 'xh:merLog:add', '#', 'admin', '2023-04-24 15:02:15', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2097, '商户调用日志修改', 2094, 3, '#', '', 'F', '0', '1', 'xh:merLog:edit', '#', 'admin', '2023-04-24 15:02:15', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2098, '商户调用日志删除', 2094, 4, '#', '', 'F', '0', '1', 'xh:merLog:remove', '#', 'admin', '2023-04-24 15:02:15', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2099, '商户调用日志导出', 2094, 5, '#', '', 'F', '0', '1', 'xh:merLog:export', '#', 'admin', '2023-04-24 15:02:15', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2100, '商户信息管理', 2000, 1, '/xh/merInfo', '', 'C', '0', '1', 'xh:merInfo:view', '#', 'admin',
        '2023-04-24 15:02:21', '', NULL, '商户信息管理菜单');
INSERT INTO "sys_menu"
VALUES (2101, '商户信息管理查询', 2100, 1, '#', '', 'F', '0', '1', 'xh:merInfo:list', '#', 'admin', '2023-04-24 15:02:21', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2102, '商户信息管理新增', 2100, 2, '#', '', 'F', '0', '1', 'xh:merInfo:add', '#', 'admin', '2023-04-24 15:02:21', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2103, '商户信息管理修改', 2100, 3, '#', '', 'F', '0', '1', 'xh:merInfo:edit', '#', 'admin', '2023-04-24 15:02:22', '',
        NULL, '');

INSERT INTO "sys_menu"
VALUES (2104, '商户信息管理删除', 2100, 4, '#', '', 'F', '0', '1', 'xh:merInfo:remove', '#', 'admin', '2023-04-24 15:02:22', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (2105, '商户信息管理导出', 2100, 5, '#', '', 'F', '0', '1', 'xh:merInfo:export', '#', 'admin', '2023-04-24 15:02:22', '',
        NULL, '');

INSERT INTO "sys_menu"
VALUES (nextval ('sys_menu_menu_id_seq'), '商户信息管理导出', 2100, 6, '#', '', 'F', '0', '1', 'xh:merInfo:sendMail', '#', 'admin', '2023-04-24 15:02:22', '',
        NULL, '');




insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商户账户余额更新', 2100, 6,  '#',  'F', '0', 'xh:merInfo:topup',    '#', 'admin', now(), '', null, '');


INSERT INTO "sys_menu"
VALUES (2106, '商户余额操作记录', 2000, 2, '/xh/merAmountRecord', 'menuItem', 'C', '0', '1', 'xh:merAmountRecord:view', '#',
        'admin', '2023-04-24 15:02:28', 'admin', '2023-04-25 10:19:00', '商户余额操作记录菜单');
INSERT INTO "sys_menu"
VALUES (2107, '商户余额操作记录查询', 2106, 1, '#', '', 'F', '0', '1', 'xh:merAmountRecord:list', '#', 'admin',
        '2023-04-24 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2108, '商户余额操作记录新增', 2106, 2, '#', '', 'F', '0', '1', 'xh:merAmountRecord:add', '#', 'admin',
        '2023-04-24 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2109, '商户余额操作记录修改', 2106, 3, '#', '', 'F', '0', '1', 'xh:merAmountRecord:edit', '#', 'admin',
        '2023-04-24 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2110, '商户余额操作记录删除', 2106, 4, '#', '', 'F', '0', '1', 'xh:merAmountRecord:remove', '#', 'admin',
        '2023-04-24 15:02:29', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2111, '商户余额操作记录导出', 2106, 5, '#', '', 'F', '0', '1', 'xh:merAmountRecord:export', '#', 'admin',
        '2023-04-24 15:02:29', '', NULL, '');


INSERT INTO "sys_menu"
VALUES (2200, '商户日志统计', 2000, 4, '/xh/merReport', 'menuItem', 'C', '0', '1', 'xh:merReport:view', '#',
        'admin', '2023-06-18 15:02:28', 'admin', '2023-06-18 10:19:00', '商户日志统计菜单');
INSERT INTO "sys_menu"
VALUES (2201, '商户日志统计查询', 2200, 1, '#', '', 'F', '0', '1', 'xh:merReport:list', '#', 'admin',
        '2023-06-18 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2202, '商户日志统计更新', 2200, 2, '#', '', 'F', '0', '1', 'xh:merReport:stat', '#', 'admin',
        '2023-06-18 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2203, '商户日志统计导出', 2200, 3, '#', '', 'F', '0', '1', 'xh:merReport:export', '#', 'admin',
        '2023-06-18 15:02:29', '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2240, '商户日志统计-销售岗', 2000, 4, '/xh/merReport/sell', 'menuItem', 'C', '0', '1', 'xh:merReport:viewSell', '#',
        'admin', '2023-06-18 15:02:28', 'admin', '2023-06-18 10:19:00', '商户日志统计-销售岗菜单');
INSERT INTO "sys_menu"
VALUES (2241, '商户日志统计-销售岗查询', 2240, 1, '#', '', 'F', '0', '1', 'xh:merReport:listSell', '#', 'admin', '2023-06-18 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2242, '商户日志统计-销售岗导出', 2240, 2, '#', '', 'F', '0', '1', 'xh:merReport:exportSell', '#', 'admin', '2023-06-18 15:02:29', '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2210, '商户结算报表', 2009, 1, '/xh/merReport/cost', 'menuItem', 'C', '0', '1', 'xh:merReport:cost', '#',
        'admin', '2023-06-18 15:02:28', 'admin', '2023-06-18 10:19:00', '商户结算报表菜单');
INSERT INTO "sys_menu"
VALUES (2211, '商户结算报表查询', 2210, 1, '#', '', 'F', '0', '1', 'xh:merReport:list', '#', 'admin', '2023-06-18 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2212, '商户结算报表导出', 2210, 2, '#', '', 'F', '0', '1', 'xh:merSettleReport:export', '#', 'admin', '2023-06-18 15:02:29', '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2220, '商户利润报表', 2009, 2, '/xh/merReport/profit', 'menuItem', 'C', '0', '1', 'xh:merReport:profit', '#',
		'admin', '2023-06-18 15:02:28', 'admin', '2023-06-18 10:19:00', '商户利润报表菜单');
INSERT INTO "sys_menu"
VALUES (2221, '商户利润报表查询', 2220, 1, '#', '', 'F', '0', '1', 'xh:merReport:list', '#', 'admin', '2023-06-18 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2222, '商户利润报表导出', 2220, 2, '#', '', 'F', '0', '1', 'xh:merProfit:export', '#', 'admin','2023-06-18 15:02:29', '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2300, '供应商日志统计', 2008, 5, '/xh/supReport', 'menuItem', 'C', '0', '1', 'xh:supReport:view', '#',
        'admin', '2023-06-18 15:02:28', 'admin', '2023-06-18 10:19:00', '供应商日志统计菜单');
INSERT INTO "sys_menu"
VALUES (2301, '供应商日志统计查询', 2300, 1, '#', '', 'F', '0', '1', 'xh:supReport:list', '#', 'admin',
        '2023-06-18 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2302, '供应商日志统计更新', 2300, 2, '#', '', 'F', '0', '1', 'xh:supReport:stat', '#', 'admin',
        '2023-06-18 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2303, '供应商日志统计导出', 2300, 3, '#', '', 'F', '0', '1', 'xh:supReport:export', '#', 'admin',
        '2023-06-18 15:02:29', '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2204, '数据工具', 0, 8, '#', 'menuItem', 'M', '0', '1', '', 'fa fa-unlock-alt', 'admin',
		'2023-06-21 10:53:59.308501', '', NULL, NULL);
INSERT INTO "sys_menu"
VALUES (2205, '压缩加密', 2204, 1, '/xh/dataUtil', 'menuItem', 'C', '0', '1', 'xh:dataUtil:view', 'fa fa-wrench', 'admin',
		'2023-06-21 13:26:24.221437', 'admin', '2023-06-21 14:08:29.392434', '');
INSERT INTO "sys_menu"
VALUES (2206, '加密', 2205, 1, '#', '', 'F', '0', '1', 'xh:dataUtil:enc', '#', 'admin', '2023-06-18 15:02:29', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2207, '解密', 2205, 2, '#', '', 'F', '0', '1', 'xh:dataUtil:dec', '#', 'admin', '2023-06-18 15:02:29', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2208, '加压', 2205, 3, '#', '', 'F', '0', '1', 'xh:dataUtil:zip', '#', 'admin', '2023-06-18 15:02:29', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2209, '解压', 2205, 4, '#', '', 'F', '0', '1', 'xh:dataUtil:unzip', '#', 'admin', '2023-06-18 15:02:29', '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2310, '供应商结算报表', 2009, 3, '/xh/supReport/cost', 'menuItem', 'C', '0', '1', 'xh:supReport:cost', '#',
        'admin', '2023-06-18 15:02:28', 'admin', '2023-06-18 10:19:00', '供应商结算报表菜单');
INSERT INTO "sys_menu"
VALUES (2311, '供应商结算报表查询', 2310, 1, '#', '', 'F', '0', '1', 'xh:supReport:list', '#', 'admin', '2023-06-18 15:02:28', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (2312, '供应商结算报表导出', 2310, 2, '#', '', 'F', '0', '1', 'xh:supSettleReport:export', '#', 'admin', '2023-06-18 15:02:29', '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2230, '商户图表', 2009, 4, '/xh/merReport/chartMea', 'menuItem', 'C', '0', '1', 'xh:merReport:chartMea', '#', 'admin', '2023-06-18 15:02:28', 'admin', '2023-06-18 10:19:00', '商户图表菜单');
INSERT INTO "sys_menu"
VALUES (2231, '商户图表查询', 2230, 1, '#', '', 'F', '0', '1', 'xh:merReport:list', '#', 'admin', '2023-06-18 15:02:28', '', NULL, '');

INSERT INTO "sys_menu"
VALUES (2330, '供应商图表', 2009, 5, '/xh/supReport/chartMea', 'menuItem', 'C', '0', '1', 'xh:supReport:chartMea', '#', 'admin', '2023-06-18 15:02:28', 'admin', '2023-06-18 10:19:00', '供应商图表菜单');
INSERT INTO "sys_menu"
VALUES (2331, '供应商图表查询', 2330, 1, '#', '', 'F', '0', '1', 'xh:supReport:list', '#', 'admin', '2023-06-18 15:02:28', '', NULL, '');


-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS "sys_notice";
CREATE TABLE "sys_notice"
(
    "notice_id" int4 NOT NULL,
    "notice_title"   varchar(50) NOT NULL,
    "notice_type"    char(1)     NOT NULL,
    "notice_content" varchar(2000),
    "status"         char(1),
    "create_by"      varchar(64),
    "create_time"    timestamp(6),
    "update_by"      varchar(64),
    "update_time"    timestamp(6),
    "remark"         varchar(255),
    primary key (notice_id)
);

SELECT SETVAL(pg_get_serial_sequence('sys_notice', 'notice_id'), 1000, FALSE);


COMMENT ON COLUMN "sys_notice"."notice_id" IS '公告ID';
COMMENT ON COLUMN "sys_notice"."notice_title" IS '公告标题';
COMMENT ON COLUMN "sys_notice"."notice_type" IS '公告类型（1通知 2公告）';
COMMENT ON COLUMN "sys_notice"."notice_content" IS '公告内容';
COMMENT ON COLUMN "sys_notice"."status" IS '公告状态（0正常 1关闭）';
COMMENT ON COLUMN "sys_notice"."create_by" IS '创建者';
COMMENT ON COLUMN "sys_notice"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_notice"."update_by" IS '更新者';
COMMENT ON COLUMN "sys_notice"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_notice"."remark" IS '备注';
COMMENT ON TABLE "sys_notice" IS '通知公告表';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO "sys_notice"
VALUES (1, '温馨提醒：聚合营销云上线了', '2', '新版本内容', '0', 'admin', '2023-07-05 09:47:36', '', NULL, '管理员');


-- ----------------------------
-- Table structure for sys_oper_log
-- 10、操作日志记录
-- ----------------------------
DROP TABLE IF EXISTS "sys_oper_log";
CREATE TABLE "sys_oper_log"
(
    "oper_id"        SERIAL NOT NULL,
    "title"          varchar(50) default '',
    "business_type"  int2 default 0,
    "method"         varchar(100) default '',
    "request_method" varchar(10) default '',
    "operator_type" int4  default 0,
    "oper_name"      varchar(50) default '',
    "dept_name"      varchar(50) default '',
    "oper_url"       varchar(255) default '',
    "oper_ip"        varchar(128) default '',
    "oper_location"  varchar(255) default '',
    "oper_param"     varchar(2000) default '',
    "json_result"    varchar(2000) default '',
    "status" int2  default 0,
    "error_msg"      varchar(2000)  default '',
    "oper_time"      timestamp(6) ,
    "cost_time" int4  default 0,
    primary key (oper_id)
);

SELECT SETVAL(pg_get_serial_sequence('sys_oper_log', 'oper_id'), 1000, FALSE);


COMMENT ON COLUMN "sys_oper_log"."oper_id" IS '日志主键';
COMMENT ON COLUMN "sys_oper_log"."title" IS '模块标题';
COMMENT ON COLUMN "sys_oper_log"."business_type" IS '业务类型（0其它 1新增 2修改 3删除）';
COMMENT ON COLUMN "sys_oper_log"."method" IS '方法名称';
COMMENT ON COLUMN "sys_oper_log"."request_method" IS '请求方式';
COMMENT ON COLUMN "sys_oper_log"."operator_type" IS '操作类别（0其它 1后台用户 2手机端用户）';
COMMENT ON COLUMN "sys_oper_log"."oper_name" IS '操作人员';
COMMENT ON COLUMN "sys_oper_log"."dept_name" IS '部门名称';
COMMENT ON COLUMN "sys_oper_log"."oper_url" IS '请求URL';
COMMENT ON COLUMN "sys_oper_log"."oper_ip" IS '主机地址';
COMMENT ON COLUMN "sys_oper_log"."oper_location" IS '操作地点';
COMMENT ON COLUMN "sys_oper_log"."oper_param" IS '请求参数';
COMMENT ON COLUMN "sys_oper_log"."json_result" IS '返回参数';
COMMENT ON COLUMN "sys_oper_log"."status" IS '操作状态（0正常 1异常）';
COMMENT ON COLUMN "sys_oper_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "sys_oper_log"."oper_time" IS '操作时间';
COMMENT ON COLUMN "sys_oper_log"."cost_time" IS '消耗时间';
COMMENT ON TABLE "sys_oper_log" IS '操作日志记录';



-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS "sys_post";
CREATE TABLE "sys_post"
(
    "post_id"     SERIAL      NOT NULL,
    "post_code"   varchar(64) NOT NULL,
    "post_name"   varchar(50) NOT NULL,
    "post_sort" int4 NOT NULL,
    "status"      char(1)     NOT NULL,
    "create_by"   varchar(64),
    "create_time" timestamp(6),
    "update_by"   varchar(64),
    "update_time" timestamp(6),
    "remark"      varchar(500),
    primary key (post_id)
);

SELECT SETVAL(pg_get_serial_sequence('sys_post', 'post_id'), 1000, FALSE);


COMMENT ON COLUMN "sys_post"."post_id" IS '岗位ID';
COMMENT ON COLUMN "sys_post"."post_code" IS '岗位编码';
COMMENT ON COLUMN "sys_post"."post_name" IS '岗位名称';
COMMENT ON COLUMN "sys_post"."post_sort" IS '显示顺序';
COMMENT ON COLUMN "sys_post"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "sys_post"."create_by" IS '创建者';
COMMENT ON COLUMN "sys_post"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_post"."update_by" IS '更新者';
COMMENT ON COLUMN "sys_post"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_post"."remark" IS '备注';
COMMENT ON TABLE "sys_post" IS '岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO "sys_post"
VALUES (1, 'ceo', '管理', 1, '0', 'admin', '2023-04-07 09:46:55', '', NULL, '');
INSERT INTO "sys_post"
VALUES (2, 'IT', '技术', 2, '0', 'admin', '2023-04-07 09:46:55', '', NULL, '');
INSERT INTO "sys_post"
VALUES (3, 'CW', '财务', 3, '0', 'admin', '2023-04-07 09:46:56', '', NULL, '');
INSERT INTO "sys_post"
VALUES (4, 'sell', '销售', 4, '0', 'admin', '2023-04-07 09:46:56', '', NULL, '');
INSERT INTO "sys_post"
VALUES (5, 'cp', '产品', 4, '0', 'admin', '2023-04-07 09:46:56', '', NULL, '');
INSERT INTO "sys_post"
VALUES (6, 'user', '普通员工', 4, '0', 'admin', '2023-04-07 09:46:56', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- 4、角色信息表
-- ----------------------------
DROP TABLE IF EXISTS "sys_role";
CREATE TABLE "sys_role"
(
    "role_id"     SERIAL       NOT NULL,
    "role_name"   varchar(30)  NOT NULL,
    "role_key"    varchar(100) NOT NULL,
    "role_sort" int4 NOT NULL,
    "data_scope"  char(1),
    "status"      char(1)      NOT NULL,
    "del_flag"    char(1)  default  '0',
    "create_by"   varchar(64),
    "create_time" timestamp(6),
    "update_by"   varchar(64),
    "update_time" timestamp(6),
    "remark"      varchar(500),
    primary key (role_id)
);

SELECT SETVAL(pg_get_serial_sequence('sys_role', 'role_id'), 1000, FALSE);


COMMENT ON COLUMN "sys_role"."role_id" IS '角色ID';
COMMENT ON COLUMN "sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "sys_role"."role_key" IS '角色权限字符串';
COMMENT ON COLUMN "sys_role"."role_sort" IS '显示顺序';
COMMENT ON COLUMN "sys_role"."data_scope" IS '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';
COMMENT ON COLUMN "sys_role"."status" IS '角色状态（0正常 1停用）';
COMMENT ON COLUMN "sys_role"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN "sys_role"."create_by" IS '创建者';
COMMENT ON COLUMN "sys_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_role"."update_by" IS '更新者';
COMMENT ON COLUMN "sys_role"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_role"."remark" IS '备注';
COMMENT ON TABLE "sys_role" IS '角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "sys_role"
VALUES (1, '超级管理员', 'admin', 1, '1', '0', '0', 'admin', '2023-04-07 09:46:56', '', NULL, '超级管理员');
INSERT INTO "sys_role"
VALUES (2, '普通角色', 'common', 2, '2', '0', '0', 'admin', '2023-04-07 09:46:56', 'admin', '2023-04-20 11:42:21', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_dept";
CREATE TABLE "sys_role_dept"
(
    "role_id" int4 NOT NULL,
    "dept_id" int4 NOT NULL,
    primary key(role_id, dept_id)
);

COMMENT ON COLUMN "sys_role_dept"."role_id" IS '角色ID';
COMMENT ON COLUMN "sys_role_dept"."dept_id" IS '部门ID';
COMMENT ON TABLE "sys_role_dept" IS '角色和部门关联表';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO "sys_role_dept"
VALUES (2, 100);
INSERT INTO "sys_role_dept"
VALUES (2, 101);
INSERT INTO "sys_role_dept"
VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_menu";
CREATE TABLE "sys_role_menu"
(
    "role_id" int4 NOT NULL,
    "menu_id" int4 NOT NULL,
    primary key(role_id, menu_id)
)
;
COMMENT ON COLUMN "sys_role_menu"."role_id" IS '角色ID';
COMMENT ON COLUMN "sys_role_menu"."menu_id" IS '菜单ID';
COMMENT ON TABLE "sys_role_menu" IS '角色和菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO "sys_role_menu"
VALUES (2, 1);
INSERT INTO "sys_role_menu"
VALUES (2, 2);
INSERT INTO "sys_role_menu"
VALUES (2, 3);
INSERT INTO "sys_role_menu"
VALUES (2, 100);
INSERT INTO "sys_role_menu"
VALUES (2, 101);
INSERT INTO "sys_role_menu"
VALUES (2, 102);
INSERT INTO "sys_role_menu"
VALUES (2, 103);
INSERT INTO "sys_role_menu"
VALUES (2, 104);
INSERT INTO "sys_role_menu"
VALUES (2, 105);
INSERT INTO "sys_role_menu"
VALUES (2, 106);
INSERT INTO "sys_role_menu"
VALUES (2, 107);
INSERT INTO "sys_role_menu"
VALUES (2, 108);
INSERT INTO "sys_role_menu"
VALUES (2, 109);
INSERT INTO "sys_role_menu"
VALUES (2, 110);
INSERT INTO "sys_role_menu"
VALUES (2, 111);
INSERT INTO "sys_role_menu"
VALUES (2, 112);
INSERT INTO "sys_role_menu"
VALUES (2, 113);
INSERT INTO "sys_role_menu"
VALUES (2, 114);
INSERT INTO "sys_role_menu"
VALUES (2, 115);
INSERT INTO "sys_role_menu"
VALUES (2, 116);
INSERT INTO "sys_role_menu"
VALUES (2, 500);
INSERT INTO "sys_role_menu"
VALUES (2, 501);
INSERT INTO "sys_role_menu"
VALUES (2, 1000);
INSERT INTO "sys_role_menu"
VALUES (2, 1001);
INSERT INTO "sys_role_menu"
VALUES (2, 1002);
INSERT INTO "sys_role_menu"
VALUES (2, 1003);
INSERT INTO "sys_role_menu"
VALUES (2, 1004);
INSERT INTO "sys_role_menu"
VALUES (2, 1005);
INSERT INTO "sys_role_menu"
VALUES (2, 1006);
INSERT INTO "sys_role_menu"
VALUES (2, 1007);
INSERT INTO "sys_role_menu"
VALUES (2, 1008);
INSERT INTO "sys_role_menu"
VALUES (2, 1009);
INSERT INTO "sys_role_menu"
VALUES (2, 1010);
INSERT INTO "sys_role_menu"
VALUES (2, 1011);
INSERT INTO "sys_role_menu"
VALUES (2, 1012);
INSERT INTO "sys_role_menu"
VALUES (2, 1013);
INSERT INTO "sys_role_menu"
VALUES (2, 1014);
INSERT INTO "sys_role_menu"
VALUES (2, 1015);
INSERT INTO "sys_role_menu"
VALUES (2, 1016);
INSERT INTO "sys_role_menu"
VALUES (2, 1017);
INSERT INTO "sys_role_menu"
VALUES (2, 1018);
INSERT INTO "sys_role_menu"
VALUES (2, 1019);
INSERT INTO "sys_role_menu"
VALUES (2, 1020);
INSERT INTO "sys_role_menu"
VALUES (2, 1021);
INSERT INTO "sys_role_menu"
VALUES (2, 1022);
INSERT INTO "sys_role_menu"
VALUES (2, 1023);
INSERT INTO "sys_role_menu"
VALUES (2, 1024);
INSERT INTO "sys_role_menu"
VALUES (2, 1025);
INSERT INTO "sys_role_menu"
VALUES (2, 1026);
INSERT INTO "sys_role_menu"
VALUES (2, 1027);
INSERT INTO "sys_role_menu"
VALUES (2, 1028);
INSERT INTO "sys_role_menu"
VALUES (2, 1029);
INSERT INTO "sys_role_menu"
VALUES (2, 1030);
INSERT INTO "sys_role_menu"
VALUES (2, 1031);
INSERT INTO "sys_role_menu"
VALUES (2, 1032);
INSERT INTO "sys_role_menu"
VALUES (2, 1033);
INSERT INTO "sys_role_menu"
VALUES (2, 1034);
INSERT INTO "sys_role_menu"
VALUES (2, 1035);
INSERT INTO "sys_role_menu"
VALUES (2, 1036);
INSERT INTO "sys_role_menu"
VALUES (2, 1037);
INSERT INTO "sys_role_menu"
VALUES (2, 1038);
INSERT INTO "sys_role_menu"
VALUES (2, 1039);
INSERT INTO "sys_role_menu"
VALUES (2, 1040);
INSERT INTO "sys_role_menu"
VALUES (2, 1041);
INSERT INTO "sys_role_menu"
VALUES (2, 1042);
INSERT INTO "sys_role_menu"
VALUES (2, 1043);
INSERT INTO "sys_role_menu"
VALUES (2, 1044);
INSERT INTO "sys_role_menu"
VALUES (2, 1045);
INSERT INTO "sys_role_menu"
VALUES (2, 1046);
INSERT INTO "sys_role_menu"
VALUES (2, 1047);
INSERT INTO "sys_role_menu"
VALUES (2, 1048);
INSERT INTO "sys_role_menu"
VALUES (2, 1049);
INSERT INTO "sys_role_menu"
VALUES (2, 1050);
INSERT INTO "sys_role_menu"
VALUES (2, 1051);
INSERT INTO "sys_role_menu"
VALUES (2, 1052);
INSERT INTO "sys_role_menu"
VALUES (2, 1053);
INSERT INTO "sys_role_menu"
VALUES (2, 1054);
INSERT INTO "sys_role_menu"
VALUES (2, 1055);
INSERT INTO "sys_role_menu"
VALUES (2, 1056);
INSERT INTO "sys_role_menu"
VALUES (2, 1057);
INSERT INTO "sys_role_menu"
VALUES (2, 1058);
INSERT INTO "sys_role_menu"
VALUES (2, 1059);
INSERT INTO "sys_role_menu"
VALUES (2, 1060);
INSERT INTO "sys_role_menu"
VALUES (2, 1061);



-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "sys_user";
CREATE TABLE "sys_user"
(
    "user_id"         SERIAL      NOT NULL,
    "dept_id"       int4,
    "login_name"      varchar(30) NOT NULL,
    "user_name"       varchar(30),
    "user_type"       varchar(2) default '00',
    "email"           varchar(50),
    "phonenumber"     varchar(11),
    "sex"             char(1) default '0',
    "avatar"          varchar(100),
    "password"        varchar(50),
    "salt"            varchar(20),
    "status"          char(1) default '0',
    "del_flag"        char(1) default '0',
    "login_ip"        varchar(128),
    "login_date"      timestamp(6),
    "pwd_update_date" timestamp(6),
    "create_by"       varchar(64),
    "create_time"     timestamp(6),
    "update_by"       varchar(64),
    "update_time"     timestamp(6),
    "remark"          varchar(500),
    primary key (user_id),
    unique (login_name)
) ;
SELECT SETVAL(pg_get_serial_sequence('sys_user', 'user_id'), 1000, FALSE);

COMMENT ON COLUMN "sys_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "sys_user"."dept_id" IS '部门ID';
COMMENT ON COLUMN "sys_user"."login_name" IS '登录账号';
COMMENT ON COLUMN "sys_user"."user_name" IS '用户昵称';
COMMENT ON COLUMN "sys_user"."user_type" IS '用户类型（00系统用户 01注册用户）';
COMMENT ON COLUMN "sys_user"."email" IS '用户邮箱';
COMMENT ON COLUMN "sys_user"."phonenumber" IS '手机号码';
COMMENT ON COLUMN "sys_user"."sex" IS '用户性别（0男 1女 2未知）';
COMMENT ON COLUMN "sys_user"."avatar" IS '头像路径';
COMMENT ON COLUMN "sys_user"."password" IS '密码';
COMMENT ON COLUMN "sys_user"."salt" IS '盐加密';
COMMENT ON COLUMN "sys_user"."status" IS '帐号状态（0正常 1停用）';
COMMENT ON COLUMN "sys_user"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN "sys_user"."login_ip" IS '最后登录IP';
COMMENT ON COLUMN "sys_user"."login_date" IS '最后登录时间';
COMMENT ON COLUMN "sys_user"."pwd_update_date" IS '密码最后更新时间';
COMMENT ON COLUMN "sys_user"."create_by" IS '创建者';
COMMENT ON COLUMN "sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "sys_user"."update_by" IS '更新者';
COMMENT ON COLUMN "sys_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "sys_user"."remark" IS '备注';
COMMENT ON TABLE "sys_user" IS '用户信息表';



-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "sys_user"
VALUES (1, 103, 'admin', '超级管理员', '00', 'qt@qt300061.com', '15888888888', '1',
        '/profile/avatar/2023/04/28/blob_20230428151433A001.png', '29c67a30398638269fe600f73a054934', '111111', '0',
        '0', '0:0:0:0:0:0:0:1', '2023-05-24 15:29:08', '2023-04-07 09:46:54', 'admin', '2023-04-07 09:46:54', '',
        '2023-05-24 15:29:08', '管理员');

-- ----------------------------
-- Table structure for sys_user_online
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_online";
CREATE TABLE "sys_user_online"
(
    "sessionId"        varchar(50) NOT NULL,
    "login_name"       varchar(50),
    "dept_name"        varchar(50),
    "ipaddr"           varchar(128),
    "login_location"   varchar(255),
    "browser"          varchar(50),
    "os"               varchar(50),
    "status"           varchar(10),
    "start_timestamp"  timestamp(6),
    "last_access_time" timestamp(6),
    "expire_time" int4,
    primary key ("sessionId")
)
;
COMMENT ON COLUMN "sys_user_online"."sessionId" IS '用户会话id';
COMMENT ON COLUMN "sys_user_online"."login_name" IS '登录账号';
COMMENT ON COLUMN "sys_user_online"."dept_name" IS '部门名称';
COMMENT ON COLUMN "sys_user_online"."ipaddr" IS '登录IP地址';
COMMENT ON COLUMN "sys_user_online"."login_location" IS '登录地点';
COMMENT ON COLUMN "sys_user_online"."browser" IS '浏览器类型';
COMMENT ON COLUMN "sys_user_online"."os" IS '操作系统';
COMMENT ON COLUMN "sys_user_online"."status" IS '在线状态on_line在线off_line离线';
COMMENT ON COLUMN "sys_user_online"."start_timestamp" IS 'session创建时间';
COMMENT ON COLUMN "sys_user_online"."last_access_time" IS 'session最后访问时间';
COMMENT ON COLUMN "sys_user_online"."expire_time" IS '超时时间，单位为分钟';
COMMENT ON TABLE "sys_user_online" IS '在线用户记录';

-- ----------------------------
-- Records of sys_user_online
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_post";
CREATE TABLE "sys_user_post"
(
    "user_id" int4 NOT NULL,
    "post_id" int4 NOT NULL,
    primary key (user_id, post_id)
)
;
COMMENT ON COLUMN "sys_user_post"."user_id" IS '用户ID';
COMMENT ON COLUMN "sys_user_post"."post_id" IS '岗位ID';
COMMENT ON TABLE "sys_user_post" IS '用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO "sys_user_post"
VALUES (1, 1);
INSERT INTO "sys_user_post"
VALUES (2, 2);
INSERT INTO "sys_user_post"
VALUES (2, 3);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_role";
CREATE TABLE "sys_user_role"
(
    "user_id" int4 NOT NULL,
    "role_id" int4 NOT NULL,
    primary key(user_id, role_id)
)
;
COMMENT ON COLUMN "sys_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "sys_user_role"."role_id" IS '角色ID';
COMMENT ON TABLE "sys_user_role" IS '用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "sys_user_role"
VALUES (1, 1);
INSERT INTO "sys_user_role"
VALUES (2, 2);


SELECT SETVAL(pg_get_serial_sequence('sys_config', 'config_id'), (SELECT max( config_id)+1000  from sys_config), FALSE);
SELECT SETVAL(pg_get_serial_sequence('sys_dept', 'dept_id'), (SELECT max( dept_id)+1000  from sys_dept), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_dict_type', 'dict_id'), (SELECT max( dict_id)+1000  from sys_dict_type), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_job', 'job_id'), (SELECT max(job_id)+1000  from sys_job), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_job_log', 'job_log_id'), (SELECT max( job_log_id)+1000  from sys_job_log), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_logininfor', 'info_id'), (SELECT max( info_id)+1000  from sys_logininfor), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_menu', 'menu_id'), (SELECT max( menu_id)+1000  from sys_menu), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_notice', 'notice_id'), (SELECT max(notice_id)+1000  from sys_notice), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_oper_log', 'oper_id'), (SELECT max(oper_id)+1000  from sys_oper_log), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_post', 'post_id'), (SELECT max(post_id)+1000  from sys_post), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_role', 'role_id'), (SELECT max(role_id)+1000  from sys_role), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_user', 'user_id'), (SELECT max(user_id)+1000  from sys_user), FALSE);

SELECT SETVAL(pg_get_serial_sequence('sys_user', 'user_id'), (SELECT max(user_id)+1000  from sys_user), FALSE);