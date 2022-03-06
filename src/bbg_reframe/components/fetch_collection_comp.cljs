(ns bbg-reframe.components.fetch-collection-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.network-events :as network-events]
   [bbg-reframe.events :as events]))

(defn fetch-collection-comp []
  (let [user @(re-frame/subscribe [::subs/user])]
    [:div.flex.items-center.mb-1.h-full
     [:input.input-box.min-w-0.grow.h-full
      {:type "text"
       :id "name"
       :value user
       :placeholder "Insert BGG username"
       :on-change #(re-frame/dispatch [::events/update-user (-> % .-target .-value)])}]
     [:button.button.min-w-fit.px-2.ml-1
      {:disabled (empty? user)
       :on-click #(re-frame/dispatch [::network-events/fetch-collection])} "Fetch collection"]]))