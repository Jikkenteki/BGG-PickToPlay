(ns bbg-reframe.panels.home.components.filter-collections-comp
  (:require [bbg-reframe.events :as events]
            [bbg-reframe.model.collection :refer [only-collections]]
            [bbg-reframe.panels.collections.collections-subs :as collections-subs]
            [bbg-reframe.subs :as subs]
            [clojure.set :refer [difference]]
            [re-frame.core :as re-frame]))

(defn filter-collections-comp
  []
  (let [only-collection-ids @(re-frame/subscribe [::subs/form :only-collection-ids])
        collections @(re-frame/subscribe [::collections-subs/collections])
        only (only-collections collections only-collection-ids)]
    (println "ONLY:" only)
    [:<>
     [:div.flex
      [:div "Restrict to collections: "
       (if (empty? only-collection-ids)
         "none"
         (map (fn [collection]
                ^{:key (:id collection)}
                [:span
                 [:button {:on-click
                           #(re-frame/dispatch
                             [::events/update-form :only-collection-ids
                              (remove (fn [id] (= (:id collection) id)) only-collection-ids)])
                           :class "fa fa-minus-square"}]
                 (:name collection)])
              only))]]
     [:div.flex
      (map (fn [collection]
             ^{:key (:id collection)}
             [:span [:button {:on-click
                              #(re-frame/dispatch
                                [::events/update-form :only-collection-ids (conj only-collection-ids (:id collection))])
                              :class "fa fa-plus-square"}]
              [:button {:on-click #(re-frame/dispatch
                                    [::events/navigate [:collection-view :id (:id collection)]])}
               (:name collection)]])
           (difference (into #{} collections) (into #{} only)))]]))