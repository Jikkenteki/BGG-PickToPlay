(ns bbg-reframe.tag-helpers-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [tubax.core :refer [xml->clj]]
            [bbg-reframe.model.tag-helpers :refer [has-tag? has-attr-with-value?]]))

(def poll-xml "
<poll name=\"suggested_numplayers\" title=\"User Suggested Number of Players\" totalvotes=\"2200\">
<results numplayers=\"1\">
<result value=\"Best\" numvotes=\"6\"/>
<result value=\"Recommended\" numvotes=\"61\"/>
<result value=\"Not Recommended\" numvotes=\"1327\"/>
</results>
<results numplayers=\"2\">
<result value=\"Best\" numvotes=\"1161\"/>
<result value=\"Recommended\" numvotes=\"791\"/>
<result value=\"Not Recommended\" numvotes=\"105\"/>
</results>
<results numplayers=\"3\">
<result value=\"Best\" numvotes=\"950\"/>
<result value=\"Recommended\" numvotes=\"978\"/>
<result value=\"Not Recommended\" numvotes=\"36\"/>
</results>
<results numplayers=\"4\">
<result value=\"Best\" numvotes=\"598\"/>
<result value=\"Recommended\" numvotes=\"1132\"/>
<result value=\"Not Recommended\" numvotes=\"150\"/>
</results>
<results numplayers=\"5\">
<result value=\"Best\" numvotes=\"164\"/>
<result value=\"Recommended\" numvotes=\"901\"/>
<result value=\"Not Recommended\" numvotes=\"631\"/>
</results>
<results numplayers=\"5+\">
<result value=\"Best\" numvotes=\"29\"/>
<result value=\"Recommended\" numvotes=\"220\"/>
<result value=\"Not Recommended\" numvotes=\"1105\"/>
</results>
</poll>")

;; (def poll-clj (xml->clj poll-xml))
(def poll-clj-cached
  {:tag :poll, :attrs
   {:name "suggested_numplayers", :title "User Suggested Number of Players", :totalvotes "2200"},
   :content [{:tag :results, :attrs {:numplayers "1"},
              :content [{:tag :result, :attrs {:value "Best", :numvotes "6"}, :content nil}
                        {:tag :result, :attrs {:value "Recommended", :numvotes "61"}, :content nil}
                        {:tag :result, :attrs {:value "Not Recommended", :numvotes "1327"},
                         :content nil}]}
             {:tag :results, :attrs {:numplayers "2"}, :content [{:tag :result, :attrs {:value "Best", :numvotes "1161"}, :content nil} {:tag :result, :attrs {:value "Recommended", :numvotes "791"}, :content nil} {:tag :result, :attrs {:value "Not Recommended", :numvotes "105"}, :content nil}]} {:tag :results, :attrs {:numplayers "3"}, :content [{:tag :result, :attrs {:value "Best", :numvotes "950"}, :content nil} {:tag :result, :attrs {:value "Recommended", :numvotes "978"}, :content nil} {:tag :result, :attrs {:value "Not Recommended", :numvotes "36"}, :content nil}]} {:tag :results, :attrs {:numplayers "4"}, :content [{:tag :result, :attrs {:value "Best", :numvotes "598"}, :content nil} {:tag :result, :attrs {:value "Recommended", :numvotes "1132"}, :content nil} {:tag :result, :attrs {:value "Not Recommended", :numvotes "150"}, :content nil}]} {:tag :results, :attrs {:numplayers "5"}, :content [{:tag :result, :attrs {:value "Best", :numvotes "164"}, :content nil} {:tag :result, :attrs {:value "Recommended", :numvotes "901"}, :content nil} {:tag :result, :attrs {:value "Not Recommended", :numvotes "631"}, :content nil}]} {:tag :results, :attrs {:numplayers "5+"}, :content [{:tag :result, :attrs {:value "Best", :numvotes "29"}, :content nil} {:tag :result, :attrs {:value "Recommended", :numvotes "220"}, :content nil} {:tag :result, :attrs {:value "Not Recommended", :numvotes "1105"}, :content nil}]}]})
(comment
  poll-clj-cached
  (def tag-poll (filter (has-tag? :results) (:content poll-clj-cached)))
  (def tag-poll (->> poll-clj-cached
                     :content
                     (filter (has-tag? :results))
                     (filter (has-attr-with-value? :numplayers "1"))))
  tag-poll
  (count tag-poll)
  ;
  )

(deftest test-has-tag
  (testing "returns a list with all elements with tag")
  (let [list (filter (has-tag? :results) (:content poll-clj-cached))]
    (is (= 6 (count list)))))

(deftest test-has-attr-with-value
  (testing "returns a list with all elements with the attr set to the value")
  (let [list (->> poll-clj-cached
                  :content
                  (filter (has-tag? :results))
                  (filter (has-attr-with-value? :numplayers "1")))]
    ;; (is (= 1 (count list)))
    ))