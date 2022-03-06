(ns bbg-reframe.components.error-box-comp)

(defn error-box-comp [error-msg]
  [:div.error-box
   [:p error-msg]])