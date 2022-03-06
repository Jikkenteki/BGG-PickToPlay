(ns bbg-reframe.components.button-comp)

(defn button-comp [{:keys [children active on-click style]}]
  [:div.button.flex-1.flex.ml-1 {:style style
                                 :class (when active "active")
                                 :on-click on-click}
   children])