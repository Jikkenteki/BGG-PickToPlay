(ns bbg-reframe.panels.collections.collections-panel
  (:require
   [bbg-reframe.panels.collections.components.collections-comp :refer [collections-comp]]))

(defn collections-view-panel
  []
  [:div
   [:h1.m-3 "Collections"]
   [collections-comp]])