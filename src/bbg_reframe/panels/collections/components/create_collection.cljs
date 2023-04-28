(ns bbg-reframe.panels.collections.components.create-collection
  (:require [bbg-reframe.forms.bind :refer [bind-form-to-value!]]
            [bbg-reframe.forms.forms :refer [input-element]]
            [bbg-reframe.panels.collections.collections-events :as collections-events]
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
