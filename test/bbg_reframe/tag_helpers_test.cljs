(ns bbg-reframe.tag-helpers-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [tubax.core :refer [xml->clj]]
            [bbg-reframe.model.tag-helpers :refer [has-tag? has-attr-with-value?]]
            ["sax" :as sax]
            [bbg-reframe.model.xmlapi :refer [create-votes-for-results list-results-of-votes-per-playernum]]))

; required for tubax to work and sax
(js/goog.exportSymbol "sax" sax)

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

(def poll-clj-cached (xml->clj poll-xml))

(comment
  poll-clj-cached

  {:content [poll-clj-cached]}

  (filter (has-tag? :results) (:content poll-clj-cached))
  (def tag-poll (filter (has-tag? :results) (:content poll-clj-cached)))
  (def tag-poll (->> poll-clj-cached
                     :content
                     (filter (has-tag? :results))
                     (filter (has-attr-with-value? :numplayers "1"))))
  tag-poll
  (get-in (first tag-poll) [:attrs :numplayers])
  (count tag-poll)

  (create-votes-for-results poll-clj-cached)


  (def results-data (list-results-of-votes-per-playernum {:content [poll-clj-cached]}))

  (first results-data)

  ;
  )

(deftest test-has-tag
  (testing "returns a list with all elements with tag")
  (let [list (->> poll-clj-cached
                  :content
                  (filter (has-tag? :results)))]
    (is (= 6 (count list)))))

(deftest test-has-attr-with-value
  (testing "returns a list with all elements with the attr set to the value")
  (let [list (->> poll-clj-cached
                  :content
                  (filter (has-tag? :results))
                  (filter (has-attr-with-value? :numplayers "1")))]
    (is (= 1 (count list)))
    (is (= "1" (get-in (first list) [:attrs :numplayers])))))


(deftest test-votes-best-rating-per-players
  (testing "returns processed vote results")
  (let [poll-item
        {:tag :results, :attrs {:numplayers "1"},
         :content
         [{:tag :result, :attrs {:value "Best", :numvotes "6"}, :content nil}
          {:tag :result, :attrs {:value "Recommended", :numvotes "61"}, :content nil}
          {:tag :result, :attrs {:value "Not Recommended", :numvotes "1327"}, :content nil}]}
        results (create-votes-for-results poll-item)]
    (is (= "1" (:players results)))
    (is (= 6 (:best-votes results)))
    (is (= 61 (:recommended-votes results)))
    (is (= 1327 (:not-recommended-votes results)))
    (is (= (double (/ 6 (+ 6 61 1327))) (:best-perc results)))
    (is (= (double (/ 61 (+ 6 61 1327))) (:recommended-perc results)))
    (is (= (double (/ 1327 (+ 6 61 1327))) (:not-recommended-perc results)))))

(deftest test-votes-best-rating-per-players-0
  (testing "returns processed vote results for 0 votes")
  (let [poll-results
        {:tag :results, :attrs {:numplayers "1"},
         :content
         [{:tag :result, :attrs {:value "Best", :numvotes "0"}, :content nil}
          {:tag :result, :attrs {:value "Recommended", :numvotes "0"}, :content nil}
          {:tag :result, :attrs {:value "Not Recommended", :numvotes "0"}, :content nil}]}
        results (create-votes-for-results poll-results)]
    (is (= "1" (:players results)))
    (is (= 0 (:best-votes results)))
    (is (= 0 (:recommended-votes results)))
    (is (= 0 (:not-recommended-votes results)))
    (is (= 0 (:best-perc results)))
    (is (= 0 (:recommended-perc results)))
    (is (= 0 (:not-recommended-perc results)))))

