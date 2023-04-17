(ns bbg-reframe.components.collections-comp
  (:require [bbg-reframe.forms.bind :refer [bind-form-to-value!]]
            [bbg-reframe.forms.forms :refer [input-element]]
            [bbg-reframe.views.collections-view.collections-events :as collections-events :refer [get-collection-names]]
            [bbg-reframe.views.collections-view.collections-subs :as collections-subs]
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
  (let [collections @(re-frame/subscribe [::collections-subs/collections-auth])]
    [:div.overflow-auto.grow.px-3
     [create-collection]
     [:ul
      (map (fn [li] [:li li]) (get-collection-names collections))]]))