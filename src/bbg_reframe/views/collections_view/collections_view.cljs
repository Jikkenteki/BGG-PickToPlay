(ns bbg-reframe.views.collections-view.collections-view
  (:require [bbg-reframe.components.collections-temp-comp :refer [collections-comp]]
            [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]))



(defn collections-view-panel
  []
  [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
   [naive-nav-bar]

   [:h1 "Collections"]
   [collections-comp]])