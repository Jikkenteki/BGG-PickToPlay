(ns bbg-reframe.panels.collections.collection-panel
  (:require [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]
            [bbg-reframe.forms.forms :refer [input-element]]
            [bbg-reframe.panels.collections.collections-events :as collections-events]
            [bbg-reframe.panels.collections.collections-subs :as collection-subs]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.forms.bind :refer [bind-form-to-value!]]
            [re-frame.core :as re-frame]))

(defn collection-view-panel
  []
  (let [route-params @(re-frame/subscribe [::subs/route-params 1])
        id (:id route-params)
        collection @(re-frame/subscribe [::collection-subs/collection id])]
    [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
     [naive-nav-bar]
     [:h1 "Collection with id: " id]
     [:div "Name: " (:name collection)]
     [:div
      [:button {:on-click
                #(re-frame/dispatch
                  [::collections-events/delete-collection (keyword id)])}
       "Delete"]]

     (let [form-path (bind-form-to-value! 
                      {:new-name (:name collection)} 
                      [:collection-form :edit-collection])]
       [:div
        [input-element {:class "input-box min-w-0"
                        :type :text
                        :placeholder "Collection name"
                        :path (into form-path [:new-name])}]
        [:button {:on-click #(re-frame/dispatch
                              [::collections-events/edit-collection-name [(keyword id) (into form-path [:new-name])]])} "Rename"]])]))