# Erasable-Itemset-Mining-Using-Multiple-Maximum-Thresholds-Under-Different-Constraints

## 摘要  
  近年來隨著大數據量快速成長，資料探勘技術發展得更成熟也更成功地解決許多問題，並應用在多個領域。然而多數的資料探勘演算法，只使用單一的門檻來解決問題，其缺點是那些人們感興趣的項目，並不全然都會滿足單一門檻，因為可能有多個因子影響他們，因此使用統一的門檻來評估不同的項目有時是不公平的。在本論文中，我們引入多門檻的概念來解決可篩除項目集探勘問題，針對每一種物品給予其合適門檻值，使其挖掘出來的結果能更符合現實的應用。我們針對每個項目集的門檻計算考慮不同的限制方式提出了四種方法。首先，我們先設計了一個最小限制條件的演算法，這種方法採用一種較嚴謹的限制，並且有反單調特性，可以快速地縮小搜尋空間。第二個方法為平均限制條件，其不具有反單調特性，不能使用傳統的方式做處理，而採用了邊際策略和排序封閉特性來解決沒有反單調特性的困境。接著我們將第二個演算法擴展到第三個函數限制條件，使用者能依據需求自行設定項目集的門檻計算函數，讓整個探勘問題更有彈性。第四個方式為最大限制條件，其使用樹狀結構來儲存所需資料解決重複掃描資料庫的問題，因此大幅減少了演算法的執行時間。在實驗中，我們比較了多門檻每種限制條件下在不同參數中的差異，並衡量每種演算法的效能。從實驗結果可以觀察出每種限制條件和演算法設計策略有其優點和缺點，最小限制條件的候選項目集和可篩除項目集數量最少，只需要少量的記憶體與執行時間；平均和函數限制條件需經過兩階段驗證並保存上一層的資訊，因此會花費較多的執行時間和記憶體；而最大限制條件採用減少掃描資料庫次數的技術能有效提高效能，但額外儲存的資訊會增加記憶體使用量。  
  
關鍵字: 資料探勘、可篩除項目集探勘、單門檻探勘、多門檻探勘、探勘限制條件  

## Abstract  
  With the exponential growth of big data in recent years, data mining technology has become more mature and has successfully solved many problems and applied in many fields. However, most data mining algorithms use a single threshold to address issues. The drawback is that the items of interest to people do not all meet a single threshold, as multiple factors may influence them. Therefore, using a uniform threshold to evaluate different items is sometimes unfair. In this paper, we introduce the concept of multiple thresholds to address the issue of erasable itemset mining, assigning appropriate threshold values to each item so that the results can better match real-world applications. We propose four methods for threshold calculation of each itemset under different constraints. First, we design an algorithm with the minimum constraints, which adopts a tighter constraint and has an anti-monotonic property, allowing the search space to be quickly reduced. The second method is the average constraint, which does not have the anti-monotone property and cannot be processed using traditional methods. Instead, it employs a bound strategy and sorted closure property to overcome the lack of anti-monotone property. We then extend the second algorithm to the third function constraint, where users can set the threshold calculation function for the itemsets based on their needs, making the entire mining problem more flexible. The fourth method is the maximum constraint, which uses a tree structure to store the necessary information, solving the issue of repeated database scanning and significantly reducing the algorithm's execution time. In the experiments, we compared the differences under various constraints of multiple thresholds with different parameters and measured the performance of each algorithm. From the experimental results, it can be observed that each constraint and its corresponding algorithm design has its advantages and disadvantages. The minimum constraint generates the fewest candidate itemsets and erasable itemsets among them but requires less memory and execution time. The average and function constraints require a two-phase verification process and the preservation of the previous level's information, leading to higher execution time and memory usage. The maximum constraint adopts a technique of reducing the number of database scans, thus improving efficiency. However, its additional stored information increases memory usage.  
  
Keywords: data mining, erasable itemset mining, single-threshold mining, multiple-threshold mining, mining constraint.  

