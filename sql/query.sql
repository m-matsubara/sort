select 
  SR.ARRAY_TYPE
  , SR.ALGORITHM as "ƒAƒ‹ƒSƒŠƒYƒ€"
  , round(avg(case SR.ARRAY_SIZE when       100 then SR.TIME_SEC else null end), 3) as "100"
  , round(avg(case SR.ARRAY_SIZE when      1000 then SR.TIME_SEC else null end), 3) as "1000"
  , round(avg(case SR.ARRAY_SIZE when     10000 then SR.TIME_SEC else null end), 3) as "10000"
  , round(avg(case SR.ARRAY_SIZE when    100000 then SR.TIME_SEC else null end), 3) as "100000"
  , round(avg(case SR.ARRAY_SIZE when   1000000 then SR.TIME_SEC else null end), 3) as "1000000"
  , round(avg(case SR.ARRAY_SIZE when  10000000 then SR.TIME_SEC else null end), 3) as "10000000"
  --, round(avg(case SR.ARRAY_SIZE when 100000000 then SR.TIME_SEC else null end), 3) as "100000000"
from TB_SORT_RESULT SR
where
  0=0
  and SR.ARRAY_TYPE = 'Random'
--  and SR.ARRAY_TYPE = 'Ascending ordered'
--  and SR.ARRAY_TYPE = 'Descending ordered'
--  and SR.ARRAY_TYPE = 'Flat'
group by SR.ARRAY_TYPE, SR.ALGORITHM
order by SR.ARRAY_TYPE, 8;

select 
  SR.ARRAY_TYPE
  , SR.ALGORITHM as "ƒAƒ‹ƒSƒŠƒYƒ€"
  , round(avg(case SR.ARRAY_SIZE when       100 then SR.COMPARE_COUNT else null end), 3) as "100"
  , round(avg(case SR.ARRAY_SIZE when      1000 then SR.COMPARE_COUNT else null end), 3) as "1000"
  , round(avg(case SR.ARRAY_SIZE when     10000 then SR.COMPARE_COUNT else null end), 3) as "10000"
  , round(avg(case SR.ARRAY_SIZE when    100000 then SR.COMPARE_COUNT else null end), 3) as "100000"
  , round(avg(case SR.ARRAY_SIZE when   1000000 then SR.COMPARE_COUNT else null end), 3) as "1000000"
  , round(avg(case SR.ARRAY_SIZE when  10000000 then SR.COMPARE_COUNT else null end), 3) as "10000000"
  --, round(avg(case SR.ARRAY_SIZE when 100000000 then SR.COMPARE_COUNT else null end), 3) as "100000000"
from TB_SORT_RESULT SR
where
  0=0
  and SR.ARRAY_TYPE = 'Random'
--  and SR.ARRAY_TYPE = 'Ascending ordered'
--  and SR.ARRAY_TYPE = 'Descending ordered'
--  and SR.ARRAY_TYPE = 'Flat'
group by SR.ARRAY_TYPE, SR.ALGORITHM
order by SR.ARRAY_TYPE, 8;

