(ns bbg-reframe.components.search-results-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]))

(defn search-results-comp []
  (let [filtered @(re-frame/subscribe [::subs/filtered])]
    [:div.overflow-auto.grow.px-3
     [:ul
      (map
       (fn [name] [:li name])
       filtered)]]))