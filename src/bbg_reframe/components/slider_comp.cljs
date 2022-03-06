(ns bbg-reframe.components.slider-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]))

(defn slider-comp
  [id label min max step]
  (let [value @(re-frame/subscribe [::subs/form id])]
    [:div.flex.justify-between.mb-2.last:mb-1
     [:p.my-auto label]
     [:div.connector-line.grow.my-auto.ml-2]
     [:div.flex {:class "basis-1/2"}
      [:input.range.my-auto.mr-2.grow
       {:type "range"
        :min min
        :max max
        :step step
        :value value
        :id id
        :onChange #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])}]
      [:span.range-slider-ticket-value @value]]]))