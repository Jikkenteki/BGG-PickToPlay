(ns bbg-reframe.panels.collections.collection-panel
  (:require [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.panels.collections.collections-subs :as collection-subs]
            [re-frame.core :as re-frame]))

(defn collection-view-panel
  []
  (let [route-params @(re-frame/subscribe [::subs/route-params 1])
        id (:id route-params)
        collection @(re-frame/subscribe [::collection-subs/collection id])]
    [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
     [naive-nav-bar]
     [:h1 "Collection with id: " id]
     [:div "Name: "(:name collection)]]))