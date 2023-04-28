(ns bbg-reframe.panels.collections.components.collections-list
  (:require [bbg-reframe.events :as events]
            [bbg-reframe.panels.collections.collections-subs :as collections-subs]
            [re-frame.core :as re-frame]))

(defn collections-list
  []
  (let [collections @(re-frame/subscribe [::collections-subs/collections])]
    [:div.mt-3
     (map (fn [collection]
            ^{:key (:id collection)}
            [:div.p-2.my-3.bg-slate-700.rounded.border-2.border-dashed.border-slate-300.cursor-pointer
             {:on-click #(re-frame/dispatch [::events/navigate [:collection-view :id (:id collection)]])}
             [:p
              (:name collection)]])
          collections)]))