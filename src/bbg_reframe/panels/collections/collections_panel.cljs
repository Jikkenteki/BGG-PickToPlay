(ns bbg-reframe.panels.collections.collections-panel
  (:require
   [bbg-reframe.panels.collections.components.collections-list :refer [collections-list]]
   [bbg-reframe.panels.collections.components.create-collection :refer [create-collection]]))

(defn collections-view-panel
  []
  [:div
   [:h1.m-3 "Collections"]
   [:div.m-3
    [create-collection]
    [collections-list]]])
