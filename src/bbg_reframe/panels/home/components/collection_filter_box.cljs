(ns bbg-reframe.panels.home.components.collection-filter-box
  (:require
   [goog.string.format]
   [re-frame.core :as re-frame]
   [bbg-reframe.events :as events]))

(defn collection-filter-box
  [collection on-click add?]
  [:span.flex.gap-2.p-1.px-2.bg-slate-700.rounded.min-w-0
   [:button
    {:class (str "hover:text-slate-400 fa " (if add? "fa-plus-square" "fa-minus-square"))
     :on-click #(on-click)}]
   [:p.cursor-pointer.whitespace-nowrap.min-w-0.overflow-hidden.text-ellipsis
    {:class "hover:text-slate-400"
     :on-click #(re-frame/dispatch
                 [::events/navigate [:collection-view :id (:id collection)]])}
    (:name collection)]])
