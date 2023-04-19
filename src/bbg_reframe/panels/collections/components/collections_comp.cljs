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
    [:div
     [input-element {:class "input-box min-w-0"
                     :type :text
                     :placeholder "Collection name"
                     :path (into form-path [:new-collection])}]
     [:button {:on-click #(re-frame/dispatch
                           [::collections-events/new-collection form-path])} "Create"]]))

(defn collections-comp
  []
  (let [collections @(re-frame/subscribe [::collections-subs/collections])]
    [:div.overflow-auto.grow.px-3
     [create-collection]
     [:div
      (map (fn [collection]
             ^{:key (:id collection)}
             [:div [:a {:on-click #(re-frame/dispatch [::events/navigate [:collection-view :id (:id collection)]])}
                    (:name collection)]])
           collections)]]))