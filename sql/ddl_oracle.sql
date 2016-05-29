--drop table TB_SORT_RESULT;
create table TB_SORT_RESULT ( 
  NO NUMBER(5, 0) NOT NULL ENABLE
  , ALGORITHM VARCHAR2(50)
  , ARRAY_TYPE VARCHAR2(50)
  , ARRAY_SIZE NUMBER
  , TIME_SEC NUMBER(8, 3)
  , COMPARE_COUNT NUMBER
  , STABLE VARCHAR2(20)
  , PRIMARY KEY (NO, ALGORITHM, ARRAY_TYPE, ARRAY_SIZE)
); 

create or replace view VW_SORT_REPORT_TIME as 
select 
  SR.ARRAY_TYPE
  , SR.ALGORITHM
  , round(avg(case SR.ARRAY_SIZE when       100 then SR.TIME_SEC else null end), 3) as "N100"
  , round(avg(case SR.ARRAY_SIZE when      1000 then SR.TIME_SEC else null end), 3) as "N1000"
  , round(avg(case SR.ARRAY_SIZE when     10000 then SR.TIME_SEC else null end), 3) as "N10000"
  , round(avg(case SR.ARRAY_SIZE when    100000 then SR.TIME_SEC else null end), 3) as "N100000"
  , round(avg(case SR.ARRAY_SIZE when   1000000 then SR.TIME_SEC else null end), 3) as "N1000000"
  , round(avg(case SR.ARRAY_SIZE when  10000000 then SR.TIME_SEC else null end), 3) as "N10000000"
  , round(avg(case SR.ARRAY_SIZE when 100000000 then SR.TIME_SEC else null end), 3) as "N100000000"
  , min(SR.STABLE) as STABLE
from TB_SORT_RESULT SR
group by SR.ARRAY_TYPE, SR.ALGORITHM;

create or replace view VW_SORT_REPORT_COMPARE as 
select 
  SR.ARRAY_TYPE
  , SR.ALGORITHM
  , round(avg(case SR.ARRAY_SIZE when       100 then SR.COMPARE_COUNT else null end), 3) as "N100"
  , round(avg(case SR.ARRAY_SIZE when      1000 then SR.COMPARE_COUNT else null end), 3) as "N1000"
  , round(avg(case SR.ARRAY_SIZE when     10000 then SR.COMPARE_COUNT else null end), 3) as "N10000"
  , round(avg(case SR.ARRAY_SIZE when    100000 then SR.COMPARE_COUNT else null end), 3) as "N100000"
  , round(avg(case SR.ARRAY_SIZE when   1000000 then SR.COMPARE_COUNT else null end), 3) as "N1000000"
  , round(avg(case SR.ARRAY_SIZE when  10000000 then SR.COMPARE_COUNT else null end), 3) as "N10000000"
  , round(avg(case SR.ARRAY_SIZE when 100000000 then SR.COMPARE_COUNT else null end), 3) as "N100000000"
  , min(SR.STABLE) as STABLE
from TB_SORT_RESULT SR
group by SR.ARRAY_TYPE, SR.ALGORITHM;

