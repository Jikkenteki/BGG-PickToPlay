(ns bbg-reframe.panels.collections.collections-panel
  (:require [bbg-reframe.panels.collections.components.collections-comp :refer [collections-comp]]
            [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]))

(defn collections-view-panel
  []
  [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
   [naive-nav-bar]

   [:h1 "Collections"]
   [collections-comp]])