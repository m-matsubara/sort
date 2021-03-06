-- Copyright (c) 2015 matsubara masakazu
-- Released under the MIT license
-- https://github.com/m-matsubara/sort/blob/master/LICENSE.txt

drop table if exists TB_SORT_RESULT;
create table TB_SORT_RESULT (
  LANG VARCHAR(20)
  , NO DECIMAL(5, 0) NOT NULL
  , ALGORITHM VARCHAR(50)
  , ARRAY_TYPE VARCHAR(50)
  , KEY_TYPE VARCHAR(30)
  , ARRAY_SIZE INTEGER
  , TIME_SEC DECIMAL(11, 6)
  , COMPARE_COUNT BIGINT
  , STABLE VARCHAR(20)
  , PRIMARY KEY (LANG, NO, ALGORITHM, ARRAY_TYPE, KEY_TYPE, ARRAY_SIZE)
);

create or replace view VW_SORT_REPORT_TIME as
select
  SR.LANG
  , SR.ARRAY_TYPE
  , SR.ALGORITHM
  , SR.KEY_TYPE
  , round(avg(case SR.ARRAY_SIZE when       100 then SR.TIME_SEC else null end), 6) as "N100"
  , round(avg(case SR.ARRAY_SIZE when      1000 then SR.TIME_SEC else null end), 6) as "N1000"
  , round(avg(case SR.ARRAY_SIZE when     10000 then SR.TIME_SEC else null end), 6) as "N10000"
  , round(avg(case SR.ARRAY_SIZE when    100000 then SR.TIME_SEC else null end), 6) as "N100000"
  , round(avg(case SR.ARRAY_SIZE when   1000000 then SR.TIME_SEC else null end), 6) as "N1000000"
  , round(avg(case SR.ARRAY_SIZE when  10000000 then SR.TIME_SEC else null end), 6) as "N10000000"
  , round(avg(case SR.ARRAY_SIZE when 100000000 then SR.TIME_SEC else null end), 6) as "N100000000"
  , min(SR.STABLE) as STABLE
from TB_SORT_RESULT SR
group by SR.LANG, SR.ARRAY_TYPE, SR.KEY_TYPE, SR.ALGORITHM;

create or replace view VW_SORT_REPORT_COMPARE as
select
  SR.LANG
  , SR.ARRAY_TYPE
  , SR.ALGORITHM
  , SR.KEY_TYPE
  , round(avg(case SR.ARRAY_SIZE when       100 then SR.COMPARE_COUNT else null end), 1) as "N100"
  , round(avg(case SR.ARRAY_SIZE when      1000 then SR.COMPARE_COUNT else null end), 1) as "N1000"
  , round(avg(case SR.ARRAY_SIZE when     10000 then SR.COMPARE_COUNT else null end), 1) as "N10000"
  , round(avg(case SR.ARRAY_SIZE when    100000 then SR.COMPARE_COUNT else null end), 1) as "N100000"
  , round(avg(case SR.ARRAY_SIZE when   1000000 then SR.COMPARE_COUNT else null end), 1) as "N1000000"
  , round(avg(case SR.ARRAY_SIZE when  10000000 then SR.COMPARE_COUNT else null end), 1) as "N10000000"
  , round(avg(case SR.ARRAY_SIZE when 100000000 then SR.COMPARE_COUNT else null end), 1) as "N100000000"
  , min(SR.STABLE) as STABLE
from TB_SORT_RESULT SR
group by SR.LANG, SR.ARRAY_TYPE, SR.KEY_TYPE, SR.ALGORITHM;

