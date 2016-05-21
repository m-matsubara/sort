--*DataTitle 乱数データ(実行時間)
select 
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR."100"
  , SR."1000"
  , SR."10000"
  , SR."100000"
  , SR."1000000"
  , SR."10000000"
  --, SR."100000000"
  
from
  VW_SORT_REPORT_TIME SR
where
  SR.ARRAY_TYPE = 'Random'
order by 
  SR."10000000"
;

--*DataTitle 前半ソート済み・後半乱数(実行時間)
select 
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR."100"
  , SR."1000"
  , SR."10000"
  , SR."100000"
  , SR."1000000"
  , SR."10000000"
  --, SR."100000000"
from
  VW_SORT_REPORT_TIME SR
where
  SR.ARRAY_TYPE = 'Half sorted'
order by 
  SR."10000000"
;

--*DataTitle ソート済み(実行時間)
select 
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR."100"
  , SR."1000"
  , SR."10000"
  , SR."100000"
  , SR."1000000"
  , SR."10000000"
  --, SR."100000000"
from
  VW_SORT_REPORT_TIME SR
where
  SR.ARRAY_TYPE = 'Ascending ordered'
order by 
  SR."10000000"
;

--*DataTitle 逆順ソート済み(実行時間)
select 
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR."100"
  , SR."1000"
  , SR."10000"
  , SR."100000"
  , SR."1000000"
  , SR."10000000"
  --, SR."100000000"
from
  VW_SORT_REPORT_TIME SR
where
  SR.ARRAY_TYPE = 'Descending ordered'
order by 
  SR."10000000"
;

--*DataTitle 全て同値(実行時間)
select 
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR."100"
  , SR."1000"
  , SR."10000"
  , SR."100000"
  , SR."1000000"
  , SR."10000000"
  --, SR."100000000"
from
  VW_SORT_REPORT_TIME SR
where
  SR.ARRAY_TYPE = 'Flat'
order by 
  SR."10000000"
;

--*DataTitle 乱数データ(比較回数)
select 
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR."100"
  , SR."1000"
  , SR."10000"
  , SR."100000"
  , SR."1000000"
  , SR."10000000"
  --, SR."100000000"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.ARRAY_TYPE = 'Random'
order by 
  SR."10000000"
;

--*DataTitle 前半ソート済み・後半乱数(比較回数)
select 
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR."100"
  , SR."1000"
  , SR."10000"
  , SR."100000"
  , SR."1000000"
  , SR."10000000"
  --, SR."100000000"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.ARRAY_TYPE = 'Half sorted'
order by 
  SR."10000000"
;

--*DataTitle ソート済み(比較回数)
select 
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR."100"
  , SR."1000"
  , SR."10000"
  , SR."100000"
  , SR."1000000"
  , SR."10000000"
  --, SR."100000000"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.ARRAY_TYPE = 'Ascending ordered'
order by 
  SR."10000000"
;

--*DataTitle 逆順ソート済み(比較回数)
select 
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR."100"
  , SR."1000"
  , SR."10000"
  , SR."100000"
  , SR."1000000"
  , SR."10000000"
  --, SR."100000000"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.ARRAY_TYPE = 'Descending ordered'
order by 
  SR."10000000"
;

--*DataTitle 全て同値(比較回数)
select 
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR."100"
  , SR."1000"
  , SR."10000"
  , SR."100000"
  , SR."1000000"
  , SR."10000000"
  --, SR."100000000"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.ARRAY_TYPE = 'Flat'
order by 
  SR."10000000"
;


