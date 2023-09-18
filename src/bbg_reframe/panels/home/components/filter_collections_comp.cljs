(ns bbg-reframe.panels.home.components.filter-collections-comp
  (:require [bbg-reframe.events :as events]
            [bbg-reframe.model.collection :refer [only-collections]]
            [bbg-reframe.panels.collections.collections-subs :as collections-subs]
            [bbg-reframe.panels.home.components.collection-filter-box :refer [collection-filter-box]]
            [bbg-reframe.subs :as subs]
            [clojure.set :refer [difference]]
            [re-frame.core :as re-frame]))

(defn filter-collections-comp
  []
  (let [only-collection-ids @(re-frame/subscribe [::subs/form :only-collection-ids])
        collections @(re-frame/subscribe [::collections-subs/collections])
        only (only-collections collections only-collection-ids)]
    [:div.my-3
     [:h3.font-bold.mb-2  "Filter by collections"]
     [:div.flex.gap-4
      [:div.flex.flex-1.flex-col.gap-1.min-w-0
       (if (empty? only-collection-ids)
         "All"
         (map (fn [collection]
                ^{:key (:id collection)}
                [collection-filter-box collection #(re-frame/dispatch
                                                    [::events/update-form :only-collection-ids
                                                     (remove (fn [id] (= (:id collection) id)) only-collection-ids)]) false])
              only))]
      [:div.flex.flex-1.flex-col.gap-1.min-w-0
       (map (fn [collection]
              ^{:key (:id collection)}
              [collection-filter-box collection #(re-frame/dispatch
                                                  [::events/update-form :only-collection-ids (conj only-collection-ids (:id collection))]) true])
            (difference (into #{} collections) (into #{} only)))]]]))