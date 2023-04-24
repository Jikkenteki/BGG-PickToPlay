(ns bbg-reframe.panels.collections.collections-panel
  (:require
   [bbg-reframe.panels.collections.components.collections-comp :refer [collections-comp]]))

(defn collections-view-panel
  []
  [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
   [:h1 "Collections"]
   [collections-comp]])