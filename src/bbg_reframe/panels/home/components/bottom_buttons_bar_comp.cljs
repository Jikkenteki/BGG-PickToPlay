(ns bbg-reframe.panels.home.components.bottom-buttons-bar-comp
  (:require [bbg-reframe.components.button-comp :refer [button-comp]]
            [bbg-reframe.events :as events]
            [bbg-reframe.panels.home.components.bottom-overlay-comp :refer [bottom-overlay-comp]]
            [bbg-reframe.subs :as subs]
            [re-frame.core :as re-frame]))

(defn bottom-buttons-bar-comp []
  (let [open-tab @(re-frame/subscribe [::subs/ui :open-tab])
        route-path @(re-frame/subscribe [::subs/route-path])]
    [:div.flex.flex-col.justify-end.bg-stone-800.border-t-2.border-slate-600
     (when open-tab [bottom-overlay-comp])
     [:div.bottom-buttons
      [button-comp {:active (= route-path :home)
                    :on-click #(re-frame/dispatch
                                [::events/navigate [:home]])
                    :children [:i.fa-solid.fa-dice.fa-xl]}]
      [button-comp {:active (= open-tab :sliders-tab)
                    :on-click #(re-frame/dispatch
                                [::events/set-open-tab :sliders-tab])
                    :children [:i.fa-solid.fa-sliders.fa-xl]}]
      [button-comp {:active (= route-path :collections-view)
                    :on-click #(re-frame/dispatch
                                [::events/navigate [:collections-view]])
                    :children [:i.fa-solid.fa-object-group.fa-xl]}]
      [button-comp {:active (= route-path :login-view)
                    :on-click #(re-frame/dispatch
                                [::events/navigate [:login-view]])
                    :children [:i.fa-solid.fa-user.fa-xl]}]]]))