-- Copyright (c) 2015 matsubara masakazu
-- Released under the MIT license
-- https://github.com/m-matsubara/sort/blob/master/LICENSE.txt

--*DataTitle 乱数データ(実行時間)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_TIME FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Random' and FST.KEY_TYPE = 'Integer') as "RATIO"

from
  VW_SORT_REPORT_TIME SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Random'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 文字列・乱数データ(実行時間)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_TIME FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Random' and FST.KEY_TYPE = 'String') as "RATIO"
from
  VW_SORT_REPORT_TIME SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Random'
  and SR.KEY_TYPE = 'String'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 重複なし乱数データ(実行時間)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_TIME FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Unique Random' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_TIME SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Unique Random'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 前半ソート済み・後半乱数(実行時間)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_TIME FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Half sorted' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_TIME SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Half sorted'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle ソート済み(実行時間)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_TIME FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Ascending ordered' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_TIME SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Ascending ordered'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 逆順ソート済み(実行時間)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_TIME FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Descending ordered' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_TIME SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Descending ordered'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 全て同値(実行時間)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_TIME FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Flat' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_TIME SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Flat'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 乱数データ(比較回数)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_COMPARE FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Random' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Random'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 文字列・乱数データ(比較回数)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_COMPARE FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Random' and FST.KEY_TYPE = 'String') as "RATIO"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Random'
  and SR.KEY_TYPE = 'String'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 重複なし乱数データ(比較回数)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_COMPARE FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Unique Random' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Unique Random'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 前半ソート済み・後半乱数(比較回数)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_COMPARE FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Half sorted' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Half sorted'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle ソート済み(比較回数)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_COMPARE FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Ascending ordered' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Ascending ordered'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 逆順ソート済み(比較回数)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_COMPARE FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Descending ordered' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Descending ordered'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;

--*DataTitle 全て同値(比較回数)
select
  SR.ALGORITHM as "アルゴリズム"
  , case when SR.STABLE = 'stable' then '安定' else ' ' end as "安定"
  , SR.N100 as "100"
  , SR.N1000 as "1000"
  , SR.N10000 as "10000"
  , SR.N100000 as "100000"
  , SR.N1000000 as "1000000"
  , SR.N10000000 as "10000000"
  /*, SR.N100000000 as "100000000"*/
  , SR.N10000000 / (select min(FST.N10000000) from VW_SORT_REPORT_COMPARE FST where FST.LANG = 'Java' and FST.ARRAY_TYPE = 'Flat' and FST.KEY_TYPE = 'Integer') as "RATIO"
from
  VW_SORT_REPORT_COMPARE SR
where
  SR.LANG = 'Java'
  and SR.ARRAY_TYPE = 'Flat'
  and SR.KEY_TYPE = 'Integer'
order by
  SR.N10000000
  , SR.N1000000
  , SR.N100000
  , SR.N10000
  , SR.N1000
  , SR.N100
;


