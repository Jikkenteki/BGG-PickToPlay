(ns bbg-reframe.components.checkbox-with-connector-comp)

(defn checkbox-with-connector-comp [{:keys [label on-click checked?]}]
  [:div.flex
   [:p label]
   [:div.connector-line.grow.my-auto.ml-2.bg-stone-600]
   [:div.w-7.h-7.border-2.rounded.border-stone-600.flex
    {:on-click on-click}
    (when checked?
      [:i.m-auto {:class "fa-solid fa-check"}])]])