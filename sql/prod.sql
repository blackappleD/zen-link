--2024-11-06 已发版
ALTER TABLE "business"."t_mer_req_log"
    ADD COLUMN "level" varchar(255);

COMMENT ON COLUMN "business"."t_mer_req_log"."level" IS '档次';

--2024-11-08 已发版
ALTER TABLE "business"."t_mer_report"
    ADD COLUMN "level1" "pg_catalog"."int4",
  ADD COLUMN "level2" "pg_catalog"."int4",
  ADD COLUMN "level3" "pg_catalog"."int4";

COMMENT ON COLUMN "business"."t_mer_report"."level1" IS '第一档次计数';

COMMENT ON COLUMN "business"."t_mer_report"."level2" IS '第二档次计数';

COMMENT ON COLUMN "business"."t_mer_report"."level3" IS '第三档次计数';

ALTER TABLE "business"."t_fx_req_record"
    ADD COLUMN "level" varchar(255);

COMMENT ON COLUMN "business"."t_fx_req_record"."level" IS '档次';


