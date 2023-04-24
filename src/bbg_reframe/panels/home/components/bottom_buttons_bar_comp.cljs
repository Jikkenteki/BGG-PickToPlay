(ns bbg-reframe.panels.home.components.bottom-buttons-bar-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]
   [bbg-reframe.components.button-comp :refer [button-comp]]
   [bbg-reframe.panels.home.components.bottom-overlay-comp :refer [bottom-overlay-comp]]))

(defn bottom-buttons-bar-comp []
  (let [open-tab @(re-frame/subscribe [::subs/ui :open-tab])]
    [:div.flex.flex-col.justify-end.bg-stone-800.border-t-2.border-slate-600
     (when open-tab [bottom-overlay-comp])
     [:div.bottom-buttons
      [button-comp {:active (= open-tab :sliders-tab)
                    :on-click #(re-frame/dispatch [::events/set-open-tab :sliders-tab])
                    :children [:i.mx-auto.my-auto {:class "fa-solid fa-sliders fa-xl"}]}]
      [button-comp {:on-click #(re-frame/dispatch [::events/navigate [:collections-view]])
                    :children [:i.mx-auto.my-auto {:class "fa-solid fa-object-group fa-xl"}]}]
      [button-comp {:active (= open-tab :user-name-tab)
                    :on-click #(re-frame/dispatch [::events/set-open-tab :user-name-tab])
                    :children [:i.mx-auto.my-auto {:class "fa-solid fa-user fa-xl"}]}]]]))