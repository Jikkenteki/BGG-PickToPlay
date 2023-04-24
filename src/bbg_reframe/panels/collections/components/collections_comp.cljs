(ns bbg-reframe.panels.collections.components.collections-comp
  (:require [bbg-reframe.forms.bind :refer [bind-form-to-value!]]
            [bbg-reframe.forms.forms :refer [input-element]]
            [bbg-reframe.events :as events]
            [bbg-reframe.panels.collections.collections-events :as collections-events]
            [bbg-reframe.panels.collections.collections-subs :as collections-subs]
            [re-frame.core :as re-frame]))


(defn create-collection
  []
  (let [form-path (bind-form-to-value! {} [:form :create-collection])]
    [:div.flex.items-stretch.my-3
     [input-element {:class "input-box"
                     :type :text
                     :placeholder "Create collection"
                     :path (into form-path [:new-collection])}]
     [:div.bg-green-700.rounded-r.flex.items-center.p-2.cursor-pointer {:on-click #(re-frame/dispatch [::collections-events/new-collection form-path])}
      [:i.mx-auto.my-auto {:class "fa-solid fa-check fa-lg"}]]]))

(defn collections-comp
  []
  (let [collections @(re-frame/subscribe [::collections-subs/collections])]
    [:div.m-3
     [create-collection]
     [:div.mt-3
      (map (fn [collection]
             ^{:key (:id collection)}
             [:div.p-2.my-3.bg-slate-700.rounded.border-2.border-dashed.border-slate-300.cursor-pointer
              {:on-click #(re-frame/dispatch [::events/navigate [:collection-view :id (:id collection)]])}
              [:p
               (:name collection)]])
           collections)]]))