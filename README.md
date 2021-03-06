﻿Project Name: ソート（クイックソート・マージソートの改良アルゴリズムの提案）

松原正和(matsubara masakazu)

# ■概要
　クイックソート・マージソートの改良アルゴリズムを提案してみます。

<dl>
  <dt>mmSort</dt>
  <dd>
    Quicksortの比較回数削減を狙ったアルゴリズム。<br>
    Median of 5 Quicksort をベースとしている。<br>
    不安定ソート
  </dd>
  <dt>mmsSort</dt>
  <dd>
     Dual-pivot stable Quicksortとでもいうべきアルゴリズム。<br>
     Merge Sort のように作業領域を用いる。<br>
     3 way patition Quicksort アルゴリズムも併用する。<br>
     安定ソート
  </dd>
  <dt>MatMmsSort</dt>
  <dd>
     MatSortとmmsSort を組み合わせたアルゴリズム<br>
     作業領域の使用量は対象配列の1/10程度（任意に指定可能）<br>
     速度的にはmmsSortにやや劣る。<br>
     安定ソート
  </dd>
  <dt>mmsSort (Half memory)</dt>
  <dd>
     mmsSort をメモリ使用量を半分に削減したアルゴリズム。<br>
     MergeSortを併用する。<br>
     安定ソート
  </dd>
  <dt>Many Pivot Sort</dt>
  <dd>
     Quicksortのピボット値選択方法改善アルゴリズム（mmSortに少し劣ることが多い）<br>
     事前に多くのピボット値候補を決めておく。精度の高い（中央値に近い）<br>
     ピボット値を得ることができる。<br>
     不安定ソート
  </dd>
  <dt>MasSort</dt>
  <dd>
    3-Way Merge Sortアルゴリズムの一種<br>
    ヒープツリーやトーナメントツリーを使わず、サブツリーの先頭値の値で<br>
    サブツリーをソートし、１つの整数型変数のみでその状態を管理する。<br>
    4-Way 版もある。(Javaによる実装ではやや遅い)
    安定ソート
  </dd>
  <dt>MatSort</dt>
  <dd>
    作業領域のメモリ使用料量削減を狙ったMerge Sortの改善アルゴリズム<br>
    MasSort等、他のMerge Sort系アルゴリズムを内部的に併用する。<br>
    作業領域サイズを配列サイズの1/10程度に圧縮しても数％程度の性能低下<br>
    で済む。むしろ高速になるケースもある。<br>
    安定ソート
  </dd>
</dl>


# ■詳細
詳細は以下より参照のこと

[sort](http://www.mmatsubara.com/developer/sort/)


# ■このコンテンツの扱いについて（著作権・ライセンス・アルゴリズムの利用について）
　このコンテンツの目的は著作物の配布ではなく、アルゴリズムの提案です。ここで提案
するアルゴリズム自体は特許や著作権に左右されず改変も含めて自由に再実装（他の言語
への移植を含む）をしていただいて構いません。またJavaによる実装が提供されますが、
この実装(著作物)はMITライセンスに基づき商用を含め自由にご利用いただけます。

