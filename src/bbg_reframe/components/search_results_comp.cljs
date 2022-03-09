(ns bbg-reframe.components.search-results-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]))

(defn search-results-comp []
  (let [search-results @(re-frame/subscribe [::subs/search-results])]
    [:div.overflow-auto.grow.px-3
     [:ul
      (map
       (fn [game]
         ^{:key (:id game)}
         [:li
          [:span
          ;;  (:id game) " : "
           (:name game)]])
       search-results)]]))