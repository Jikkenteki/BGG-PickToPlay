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
    [:div.my-3
     [:h3.font-bold  "Filter by collections"]
     [:div.flex
      [:div.flex.flex-1
       [:div
        (if (empty? only-collection-ids)
          "All"
          (map (fn [collection]
                 ^{:key (:id collection)}
                 [:span.flex.gap-2
                  [:button {:on-click
                            #(re-frame/dispatch
                              [::events/update-form :only-collection-ids
                               (remove (fn [id] (= (:id collection) id)) only-collection-ids)])
                            :class "hover:text-slate-400 fa fa-minus-square"}]
                  [:p.cursor-pointer
                   {:class "hover:text-slate-500"
                    :on-click #(re-frame/dispatch
                                [::events/navigate [:collection-view :id (:id collection)]])}
                   (:name collection)]])
               only))]]
      [:div.flex.flex-1.flex-col
       (map (fn [collection]
              ^{:key (:id collection)}
              [:span.flex.gap-2
               [:button
                {:class "hover:text-slate-400 fa fa-plus-square"
                 :on-click
                 #(re-frame/dispatch
                   [::events/update-form :only-collection-ids (conj only-collection-ids (:id collection))])}]
               [:p.cursor-pointer
                {:class "hover:text-slate-400"
                 :on-click #(re-frame/dispatch
                             [::events/navigate [:collection-view :id (:id collection)]])}
                (:name collection)]])
            (difference (into #{} collections) (into #{} only)))]]]))